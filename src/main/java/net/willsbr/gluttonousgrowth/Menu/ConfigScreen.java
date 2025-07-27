package net.willsbr.gluttonousgrowth.Menu;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModList;
import net.willsbr.gluttonousgrowth.CPMCompat.Capability.CPMData;
import net.willsbr.gluttonousgrowth.Menu.Buttons.*;
import net.willsbr.gluttonousgrowth.client.ClientCPMData;
import net.willsbr.gluttonousgrowth.config.GluttonousClientConfig;
import net.willsbr.gluttonousgrowth.config.GluttonousWorldConfig;
import net.willsbr.gluttonousgrowth.networking.ModMessages;
import net.willsbr.gluttonousgrowth.networking.packet.CPMDataC2SPacket;
import net.willsbr.gluttonousgrowth.networking.packet.SettingPackets.PlayerSyncMainSettingsC2S;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.ArrayList;

import static net.willsbr.gluttonousgrowth.client.ClientCPMData.getPlayersAPI;

public class ConfigScreen extends Screen {
    /** Distance from top of the screen to this GUI's title */
    private static final int TITLE_HEIGHT = 8;

    /** Distance from top of the screen to the options list's top */
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    /** Distance from bottom of the screen to the options list's bottom */
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    /** Height of each item in the options list */
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    /** Width of a button */
    private static final int BUTTON_WIDTH = 200;
    /** Height of a button */
    private static final int BUTTON_HEIGHT = 20;
    /** Distance from bottom of the screen to the "Done" button's top */
    public static final int DONE_BUTTON_TOP_OFFSET = 26;

    /** List of options shown on the screen */
    // Not a final field because this cannot be initialized in the constructor,
    // as explained below
    private StateButton stageBasedWeight;


    private EditBox weightLayerEditBox;
    private EditBox calorieLayerEditBox;

    private EditBox weightPreviewEditBox;
    private EditBox caloriePreviewEditBox;

    private EditBox totalStagesEditBox;


    private ArrayList<EditBox> allEditBoxes;

    private PortProofButton scaleUp;
    private PortProofButton scaleDown;

    private PortProofButton done;


    private int centerW;
    private int centerH;
    private int screenW;
    private int screenH;
    private int leftBackX;
    private int leftBackY;

    private int playerDisplayX;
    private int playerDisplayY;
    private int playerDisplayWidth;
    private int playerDisplayHeight;
    private int playerOutlineWidth;

    //draws from the bottom left corner
    //TODO add a Client config that modifies this width
    private int playerWidth;

    private int headingY;
    private int calorieHeadingX;
    private int weightHeadingX;

    private int loadedMaxCalories;


    private Window curWindow;
    private Font font;


    private ArrayList<Component> errors;
    private ArrayList<Component> warnings;


    private int lastGuiScale;

    public ConfigScreen() {
        super(ModMenus.mainTitle);
    }

    // this should initialize most things, gets ercalled on screen change.
    @SuppressWarnings("resource")
    @Override
    protected void init() {
        super.init();

        curWindow = Minecraft.getInstance().getWindow();
        font = Minecraft.getInstance().font;
        lastGuiScale=(int)curWindow.getGuiScale();
        screenW = curWindow.getGuiScaledWidth();
        screenH = curWindow.getGuiScaledHeight();
        centerW = screenW / 2;
        centerH = screenH / 2;
        leftBackX = (int) (this.width / 4 * (1 / curWindow.getGuiScale()));
        leftBackY = (int) (this.height / 7 * (1 / curWindow.getGuiScale()));


        playerDisplayWidth=100;
        playerDisplayHeight=150;
        playerOutlineWidth=2;
        playerDisplayX=centerW-playerDisplayWidth/2;
        playerDisplayY=50;
        playerWidth= GluttonousClientConfig.playerDisplayScale.get();

        headingY=playerDisplayY;
        calorieHeadingX=centerW-100;
        weightHeadingX=centerW+100;

        errors=new ArrayList<Component>();
        warnings=new ArrayList<Component>();

        // Create the options list
        // It must be created in this method instead of in the constructor,
        // or it will not be displayed properly

        //buttons
        this.stageBasedWeight= new StateButton(weightHeadingX+12,headingY+20*3-5,50,20,Component.translatable("menu.overstuffed.stage"),Component.translatable("menu.overstuffed.granular"), GluttonousClientConfig.stageGain.get());
        this.stageBasedWeight.setTooltipText(Component.translatable("menu.overstuffed.gaintypetooltip"));


        //ALL editbox sizes are based off this first editbox.
        this.allEditBoxes=new ArrayList<EditBox>();
        this.weightLayerEditBox = new EditBox(
                font,
                weightHeadingX+25,
                headingY+18,
                70,
                15,
                Component.translatable("menu.overstuffed.weightlayerbox"));
        this.weightLayerEditBox.setValue(GluttonousClientConfig.weightLayerConfigEntry.get());
        this.allEditBoxes.add(this.weightLayerEditBox);

        this.calorieLayerEditBox = new EditBox(
                font,
                calorieHeadingX-34,
                headingY+18,
                weightLayerEditBox.getWidth(),
                weightLayerEditBox.getHeight(),
                Component.translatable("menu.overstuffed.callayerbox"));
        this.calorieLayerEditBox.setValue(GluttonousClientConfig.stuffedLayerConfigEntry.get());
        this.allEditBoxes.add(this.calorieLayerEditBox);

        //ALL editbox sizes are based off this first editbox.
        this.caloriePreviewEditBox = new EditBox(
                font,
                calorieHeadingX-55,
                headingY+19*2,
                50,
                15,
                Component.translatable("menu.overstuffed.weightlayerbox"));
        //todo make it so that you send the abs cal cap to the client to make this more reliable.
        this.caloriePreviewEditBox.setTooltip(Tooltip.create(Component.translatable("menu.gluttonousgrowth.previewtooltip")));


        this.caloriePreviewEditBox.setHint(Component.translatable("menu.overstuffed.calorierange",""+ GluttonousWorldConfig.absCalCap.get()).withStyle(ChatFormatting.GRAY));
        this.allEditBoxes.add(this.caloriePreviewEditBox);

        this.weightPreviewEditBox = new EditBox(font,
                weightHeadingX+2,
                headingY+19*2,
                50,
                15,
                Component.translatable("menu.overstuffed.weightlayerbox"));
        this.weightPreviewEditBox.setTooltip(Tooltip.create(Component.translatable("menu.gluttonousgrowth.previewtooltip")));
        this.weightPreviewEditBox.setHint(Component.translatable("menu.overstuffed.weightrange", GluttonousClientConfig.getMinWeight(), GluttonousClientConfig.getMaxWeight()).withStyle(ChatFormatting.GRAY));
        this.allEditBoxes.add(this.weightPreviewEditBox);


        this.totalStagesEditBox = new EditBox(
                font,
                weightHeadingX+25,
                headingY+20*4-3,
                20,
                15,
                Component.translatable("menu.overstuffed.stages"));
        this.totalStagesEditBox.setValue(GluttonousClientConfig.totalStages.get()+"");
        this.allEditBoxes.add(this.totalStagesEditBox);


        ArrayList<SwapScreenButton> menuButtons= ModMenus.returnScreenButtons(centerW,25);
        for(SwapScreenButton button:menuButtons)
        {
            this.addRenderableWidget(button);
        }
        this.addRenderableWidget(stageBasedWeight);

        for(EditBox editBox:allEditBoxes)
        {
            this.addRenderableWidget(editBox);
        }

        this.scaleUp=new PortProofButton(
                playerDisplayX+playerDisplayWidth-20,
                playerDisplayY+playerDisplayHeight-20,
                20, 20,
                Component.literal("+"),
                new Runnable() {
                    @Override
                    public void run() {
                        playerWidth=playerWidth+10;
                    }
                }
        );
        this.addRenderableWidget(scaleUp);

        this.scaleDown=new PortProofButton(
                playerDisplayX,
                playerDisplayY+playerDisplayHeight-20,
                20, 20,
                Component.literal("-"),
                new Runnable() {
                    @Override
                    public void run() {
                        if(playerWidth>=10)
                        {
                            playerWidth=playerWidth-10;
                        }
                    }
                }
        );
        this.addRenderableWidget(scaleDown);


        // Add the "Done" button
        this.done= new PortProofButton(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                Component.literal("Save"),
                new Runnable() {
                    @Override
                    public void run() {
                         onClose();
                    }
                }
        );

        this.addRenderableWidget(done);

    }

    @Override
    public void tick() {
        super.tick();
        // Add ticking logic for EditBox in editBox
        for(EditBox editBox:allEditBoxes)
        {
            editBox.tick();
        }
        if(weightPreviewEditBox!=null)
        {
            if(!weightPreviewEditBox.getValue().isEmpty())
            {
                try
                {
                    int previewWeight=Integer.parseInt(weightPreviewEditBox.getValue());
                    previewWeight=Math.max(previewWeight, GluttonousClientConfig.getMinWeight());
                    previewWeight=Math.min(previewWeight, GluttonousClientConfig.getMaxWeight());
                    ClientCPMData.previewWeight(previewWeight);
                }
                catch(Exception e)
                {

                }
            }
            if(!caloriePreviewEditBox.getValue().isEmpty())
            {
                try
                {
                    int previewCal=Integer.parseInt(caloriePreviewEditBox.getValue());
                    previewCal=Math.max(previewCal,0);
                    previewCal=Math.min(previewCal, GluttonousWorldConfig.absCalCap.get());
                    ClientCPMData.previewStuffed(previewCal);
                }
                catch(Exception e)
                {

                }
            }
        }


    }

    // mouseX and mouseY indicate the scaled coordinates of where the cursor is in
    // on the screen
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Background is typically rendered first
        this.renderBackground(guiGraphics);
        // Then the widgets if this is a direct child of the Screen
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // Draw the title

       guiGraphics.drawCenteredString(font, this.getTitle().getString(), this.width / 2, TITLE_HEIGHT, Color.WHITE.hashCode());
        //drawing the edit box's title

        drawWeightSection(guiGraphics,weightHeadingX,headingY);
        drawCalorieSection(guiGraphics,calorieHeadingX,headingY);

       //Error Handling
        if(!GluttonousClientConfig.usingFigura.get())
        {
            if(!ModList.get().isLoaded("cpm"))
            {
                warnings.add(Component.translatable("error.overstuffed.nocpm"));
            }
            else if(ModList.get().isLoaded("cpm") && ClientCPMData.CPMUpdated()==-1)
            {
                warnings.add(Component.translatable("message.overstuffed.cpmversion",Component.literal(ClientCPMData.minCPMVersion)));
            }
            else{
                if( getPlayersAPI()!=null && getPlayersAPI().getAnimationPlaying(this.weightLayerEditBox.getValue())==-1
                        &&  Minecraft.getInstance().player!=null)
                {
                    warnings.add(Component.translatable("error.overstuffed.foundweightlayer"));
                }
                if( getPlayersAPI()!=null && getPlayersAPI().getAnimationPlaying(this.calorieLayerEditBox.getValue())==-1
                        && Minecraft.getInstance().player!=null)
                {
                    warnings.add(Component.translatable("error.overstuffed.foundcalorielayer"));
                }

                if(calorieLayerEditBox.getValue().contentEquals(weightLayerEditBox.getValue()))
                {
                    errors.add(Component.translatable("error.overstuffed.samelayer"));
                }
            }
        }


        ModMenus.drawIssues(guiGraphics,font,centerW,playerDisplayY+playerDisplayHeight+10,errors,warnings);
        guiGraphics.fill(playerDisplayX-playerOutlineWidth,playerDisplayY-playerOutlineWidth,playerDisplayX+playerDisplayWidth+playerOutlineWidth,playerDisplayY+playerDisplayHeight+playerOutlineWidth,Color.GRAY.hashCode());
        guiGraphics.fill(playerDisplayX,playerDisplayY,playerDisplayX+playerDisplayWidth,playerDisplayY+playerDisplayHeight,Color.BLACK.hashCode());
        if(Minecraft.getInstance().player!=null)
        {
            renderEntityInInventoryFollowsMouse(guiGraphics, playerDisplayX+playerDisplayWidth/2, playerDisplayY+playerDisplayHeight-10, playerWidth, (float)(playerDisplayX+51)-mouseX,(float)(playerDisplayY+ 75 - 50)-mouseY, Minecraft.getInstance().player);
        }
        // pose.popPose();
        // Render things after widgets (tooltips)
    }

    public void save() {
        this.onClose();
    }

    @Override
    public void onClose() {
        // Save mod configuration
        if(!GluttonousClientConfig.weightLayerConfigEntry.get().contentEquals(this.weightLayerEditBox.getValue()))
        {
            GluttonousClientConfig.setWeightLayer(this.weightLayerEditBox.getValue());
        }
        if(!GluttonousClientConfig.stuffedLayerConfigEntry.get().contentEquals(this.calorieLayerEditBox.getValue()))
        {
            GluttonousClientConfig.setStuffedLayer((this.calorieLayerEditBox.getValue()));
        }
        if(ModList.get().isLoaded("cpm") && Minecraft.getInstance().player!=null && ClientCPMData.getPlayersAPI()!=null)
        {

            ModMessages.sendToServer(new CPMDataC2SPacket(GluttonousClientConfig.stuffedLayerConfigEntry.get(), GluttonousClientConfig.weightLayerConfigEntry.get(),
                    ClientCPMData.getTotalStuffedFrames(),ClientCPMData.getTotalWeightFrames()));
        }
        GluttonousClientConfig.stageGain.set(stageBasedWeight.getSetting());
        GluttonousClientConfig.totalStages.set(Integer.parseInt(totalStagesEditBox.getValue()));
        GluttonousClientConfig.playerDisplayScale.set(playerWidth);

        //OverstuffedConfig.setSetting(1, momentum.getSetting());
        GluttonousClientConfig.saveConfig();

        if(Minecraft.getInstance().player!=null)
        {
            ModMessages.sendToServer(new PlayerSyncMainSettingsC2S(GluttonousClientConfig.stageGain.get(),
                    GluttonousClientConfig.weightEffects.get()));
        }

        CPMData.checkIfUpdateCPM("weight");
        CPMData.checkIfUpdateCPM("stuffed");

        // Call last in case it interferes with the override
        super.onClose();
    }



    @Override
    public void removed() {
        // Reset initial states here
        // Call last in case it interferes with the override
        super.removed();
    }

    public static void renderEntityInInventoryFollowsMouse(GuiGraphics pGuiGraphics, int pX, int pY, int pScale, float pMouseX, float pMouseY, LivingEntity pEntity) {
        float f = (float)Math.atan((double)(pMouseX / 40.0F));
        float f1 = (float)Math.atan((double)(pMouseY / 40.0F));
        // Forge: Allow passing in direct angle components instead of mouse position
        renderEntityInInventoryFollowsAngle(pGuiGraphics, pX, pY, pScale, f, f1, pEntity);
    }

    public static void renderEntityInInventoryFollowsAngle(GuiGraphics pGuiGraphics, int pX, int pY, int pScale, float angleXComponent, float angleYComponent, LivingEntity pEntity) {
        float f = angleXComponent;
        float f1 = angleYComponent;
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float)Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(f1 * 20.0F * ((float)Math.PI / 180F));
        quaternionf.mul(quaternionf1);
        float f2 = pEntity.yBodyRot;
        float f3 = pEntity.getYRot();
        float f4 = pEntity.getXRot();
        float f5 = pEntity.yHeadRotO;
        float f6 = pEntity.yHeadRot;
        pEntity.yBodyRot = 180.0F + f * 20.0F;
        pEntity.setYRot(180.0F + f * 40.0F);
        pEntity.setXRot(-f1 * 20.0F);
        pEntity.yHeadRot = pEntity.getYRot();
        pEntity.yHeadRotO = pEntity.getYRot();
        renderEntityInInventory(pGuiGraphics, pX, pY, pScale, quaternionf, quaternionf1, pEntity);
        pEntity.yBodyRot = f2;
        pEntity.setYRot(f3);
        pEntity.setXRot(f4);
        pEntity.yHeadRotO = f5;
        pEntity.yHeadRot = f6;
    }

    public static void renderEntityInInventory(GuiGraphics pGuiGraphics, int pX, int pY, int pScale, Quaternionf pPose, @Nullable Quaternionf pCameraOrientation, LivingEntity pEntity) {
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate((double)pX, (double)pY, 50.0D);
        pGuiGraphics.pose().mulPoseMatrix((new Matrix4f()).scaling((float)pScale, (float)pScale, (float)(-pScale)));
        pGuiGraphics.pose().mulPose(pPose);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (pCameraOrientation != null) {
            pCameraOrientation.conjugate();
            entityrenderdispatcher.overrideCameraOrientation(pCameraOrientation);
        }

        entityrenderdispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(pEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, pGuiGraphics.pose(), pGuiGraphics.bufferSource(), 15728880);
        });
        pGuiGraphics.flush();
        entityrenderdispatcher.setRenderShadow(true);
        pGuiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    public void drawWeightSection(GuiGraphics guiGraphics,int x, int y)
    {
        guiGraphics.drawCenteredString(font, Component.translatable("menu.overstuffed.weightheading"),x,y,Color.white.hashCode());
        x=x-40;
        y=y+20;
        guiGraphics.drawString(font, Component.translatable("menu.overstuffed.layname"),x,y,Color.white.hashCode());
        y=y+20;
        guiGraphics.drawString(font, Component.translatable("menu.overstuffed.preview"),x,y,Color.white.hashCode());
        //This should also show up when stage based gainig is selected
        //guiGraphics.drawString(font, Component.translatable("menu.overstuffed.stage"),x+100,y,Color.white.hashCode());


        y=y+20;
        guiGraphics.drawString(font, Component.translatable("menu.overstuffed.gaintype"),x,y,Color.white.hashCode());

        //TODO Make these show up only when stage based gaining is selected

        if(stageBasedWeight.getSetting())
        {
            this.totalStagesEditBox.visible=true;
            y=y+20;
            guiGraphics.drawString(font, Component.translatable("menu.overstuffed.stages"),x,y,Color.white.hashCode());
        }
        else{
            this.totalStagesEditBox.visible=false;

        }




    }
    public void drawCalorieSection(GuiGraphics guiGraphics,int x, int y)
    {
        guiGraphics.drawCenteredString(font, Component.translatable("menu.overstuffed.calorieheading"), x,y,Color.white.hashCode());
        x=x-100;
        y=y+20;
        guiGraphics.drawString(font, Component.translatable("menu.overstuffed.layname"),x,y,Color.white.hashCode());
        y=y+20;
        guiGraphics.drawString(font, Component.translatable("menu.overstuffed.preview"),x,y,Color.white.hashCode());
    }









}
