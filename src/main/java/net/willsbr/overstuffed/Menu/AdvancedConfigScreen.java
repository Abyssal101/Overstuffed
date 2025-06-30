package net.willsbr.overstuffed.Menu;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.willsbr.overstuffed.Menu.Buttons.PortProofButton;
import net.willsbr.overstuffed.Menu.Buttons.SwapScreenButton;
import net.willsbr.overstuffed.Menu.Buttons.ToggleButton;
import net.willsbr.overstuffed.client.ClientWeightBarData;
import net.willsbr.overstuffed.config.GluttonousClientConfig;
import net.willsbr.overstuffed.networking.ModMessages;
import net.willsbr.overstuffed.networking.packet.WeightPackets.setMaxWeightDataSyncPacketC2S;
import net.willsbr.overstuffed.networking.packet.WeightPackets.setMinWeightDataSyncPacketC2S;
import net.willsbr.overstuffed.networking.packet.WeightPackets.setWeightC2SPacket;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;

public class AdvancedConfigScreen extends Screen {
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
    private static final int DONE_BUTTON_TOP_OFFSET = 26;
    private int centerW;
    private int centerH;
    private int screenW;
    private int screenH;
    private int leftBackX;
    private int leftBackY;

    private Window curWindow;
    private Font font;
    private boolean nonNumFlag;

    private ToggleButton weightEffect;
    private ToggleButton hitboxScaling;


    private EditBox maxWeight;
    private EditBox minWeight;
    private EditBox maxHitboxScaling;


    private ArrayList<Component> errors;
    private ArrayList<Component> warnings;

    public AdvancedConfigScreen() {
        super(ModMenus.advancedTitle);
    }

    // this should initialize most things, gets ercalled on screen change.
    @SuppressWarnings("resource")
    @Override
    protected void init() {
        super.init();

        curWindow = Minecraft.getInstance().getWindow();
        font = Minecraft.getInstance().font;
        screenW = curWindow.getGuiScaledWidth();
        screenH = curWindow.getGuiScaledHeight();
        centerW = screenW / 2;
        centerH = screenH / 2;

        nonNumFlag=false;

        errors=new ArrayList<Component>();
        warnings=new ArrayList<Component>();

        this.weightEffect= new ToggleButton(centerW-75,50,150,20,Component.translatable("menu.overstuffed.weighteffectsbutton"), GluttonousClientConfig.weightEffects.get(),true);
        this.weightEffect.setLocked(false);
        this.weightEffect.setTooltipText(Component.translatable("menu.overstuffed.weighteffects"));
        this.maxWeight= new EditBox(
                font,
                this.centerW+105-50,
                weightEffect.getY()+50,
                50,
                weightEffect.getHeight(),
                Component.translatable("menu.overstuffed.maxweight"));
        this.maxWeight.setValue(GluttonousClientConfig.maxWeight.get()+"");
        //So you can't go above 9999 because of this
        this.maxWeight.setMaxLength(4);

        this.minWeight= new EditBox(
                font,
                this.centerW-105,
                weightEffect.getY()+50,
                50,
                weightEffect.getHeight(),
                Component.translatable("menu.overstuffed.maxweight"));
        this.minWeight.setValue(GluttonousClientConfig.minWeight.get()+"");
        //So you can't go above 9999 because of this
        this.minWeight.setMaxLength(4);

        this.hitboxScaling=new ToggleButton(centerW-160,weightEffect.getY()+100,
                150,20,
                Component.translatable("menu.overstuffed.scalingenabled"),
                GluttonousClientConfig.hitBoxScalingEnabled.get(),false);
        this.hitboxScaling.setLocked(false);
        this.hitboxScaling.setTooltipText(Component.translatable("menu.overstuffed.scalingtooltip"));

        maxHitboxScaling=new EditBox(
                font,
                this.centerW+10,
                weightEffect.getY()+100,
                50,
                weightEffect.getHeight(),
                Component.translatable("menu.overstuffed.maxscaling"));
        this.maxHitboxScaling.setValue(GluttonousClientConfig.maxHitboxWidth.get()+"");



        this.addRenderableWidget(weightEffect);
        this.addRenderableWidget(maxWeight);
        this.addRenderableWidget(minWeight);

        this.addRenderableWidget(maxHitboxScaling);
        this.addRenderableWidget(hitboxScaling);




        ArrayList<SwapScreenButton> menuButtons= ModMenus.returnScreenButtons(centerW,25);
        for(SwapScreenButton button:menuButtons)
        {
            this.addRenderableWidget(button);
        }
        // Add the "Done" button
        this.addRenderableWidget(new PortProofButton(
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
        ));
    }

    @Override
    public void tick()
    {
        super.tick();

    }

    // mouseX and mouseY indicate the scaled coordinates of where the cursor is in
    // on the screen
    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Background is typically rendered first

        PoseStack pose=guiGraphics.pose();
        this.renderBackground(guiGraphics);
        // Then the widgets if this is a direct child of the Screen
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // Draw the title
        guiGraphics.drawCenteredString( font, this.getTitle().getString(),
                this.width / 2, TITLE_HEIGHT, Color.WHITE.hashCode());

        guiGraphics.drawCenteredString(font, Component.translatable("menu.overstuffed.maxweight"), centerW+80,maxWeight.getY()-20,Color.WHITE.hashCode());
        guiGraphics.drawCenteredString(font, Component.translatable("menu.overstuffed.minmaxrange"), centerW+80,maxWeight.getY()-10,Color.GRAY.hashCode());

        guiGraphics.drawCenteredString(font, Component.translatable("menu.overstuffed.minweight"), centerW-80,minWeight.getY()+-20,Color.WHITE.hashCode());
        guiGraphics.drawCenteredString(font, Component.translatable("menu.overstuffed.minmaxrange"), centerW-80,minWeight.getY()-10,Color.GRAY.hashCode());

        guiGraphics.drawCenteredString(font, Component.translatable("menu.overstuffed.maxscaling"), maxHitboxScaling.getX()+20,maxHitboxScaling.getY()-10,Color.WHITE.hashCode());



        int max=1;
        int min=0;
        try{
            max=Integer.parseInt(maxWeight.getValue());
            min=Integer.parseInt(minWeight.getValue());
        }
        catch (NumberFormatException e)
        {
            errors.add(Component.translatable("error.overstuffed.nonnumber"));
        }

        if(max<min)
        {
            errors.add(Component.translatable("error.overstuffed.badinterval"));
        }
        else if(Math.abs(max-min)<100)
        {
            errors.add(Component.translatable("menu.overstuffed.rangesmall"));
        }

        ModMenus.drawIssues(guiGraphics,font,centerW,weightEffect.getY()+10,errors,warnings);

        pose.pushPose();

    }

    public void save() {
        this.onClose();
    }

    @Override
    public void onClose() {
        // Save mod configuration
        nonNumFlag=false;

        int max;
        int min;
        try{
            max=Integer.parseInt(maxWeight.getValue());
            min=Integer.parseInt(minWeight.getValue());
            if(minWeight.getValue()!=maxWeight.getValue() && max>min)
            {
                if(max-min>100)
                {
                    if(Minecraft.getInstance().player != null)
                    {
                        //Normalizing old weight to new min and max
                        double weightRatio=((double) ClientWeightBarData.getPlayerWeight()- GluttonousClientConfig.minWeight.get());
                        weightRatio=weightRatio/(GluttonousClientConfig.maxWeight.get()- GluttonousClientConfig.minWeight.get());
                        //System.out.println("Ratio"+weightRatio);
                        int newRange=max-min;

                        int relativeWeight=(int)Math.round(weightRatio*newRange)+min;
                        ClientWeightBarData.setCurrentWeight(relativeWeight);
                        //System.out.println(ClientWeightBarData.getPlayerWeight());
                        ModMessages.sendToServer(new setWeightC2SPacket(ClientWeightBarData.getPlayerWeight()));

                        GluttonousClientConfig.maxWeight.set(max);
                        GluttonousClientConfig.minWeight.set(min);
                        ModMessages.sendToServer(new setMinWeightDataSyncPacketC2S(min));
                        ModMessages.sendToServer(new setMaxWeightDataSyncPacketC2S(max));
                    }

                    //System.out.println("CLient Weight:"+ClientWeightBarData.getPlayerWeight());
                    //System.out.println("CLient Min Weight:"+min);


                }

            }
        }
        catch (Exception e)
        {
            //Minecraft.getInstance().player.sendSystemMessage(Component.literal("Error: Non-Number Character contained in the weight box"));
        }

        GluttonousClientConfig.weightEffects.set(weightEffect.getSetting());
        GluttonousClientConfig.hitBoxScalingEnabled.set(hitboxScaling.getSetting());
        try{
            float converted=Float.parseFloat(maxHitboxScaling.getValue());
            GluttonousClientConfig.maxHitboxWidth.set(converted);
        }
        catch(NumberFormatException e)
        {
        //I AM LOSING MY MIND
        }
        GluttonousClientConfig.saveConfig();
        // Call last in case it interferes with the override
        super.onClose();
    }

    @Override
    public void removed()
    {
        // Reset initial states here
        // Call last in case it interferes with the override
        super.removed();
    }

}
