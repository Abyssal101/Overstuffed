package net.willsbr.overstuffed.Menu;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.willsbr.overstuffed.Menu.Buttons.PortProofButton;
import net.willsbr.overstuffed.Menu.Buttons.ToggleButton;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.config.OverstuffedConfig;

import javax.annotation.Nonnull;
import java.awt.*;

public class GraphicsConfigScreen extends Screen {
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

    /** List of options shown on the screen */
    // Not a final field because this cannot be initialized in the constructor,
    // as explained below
    private EditBox weightDisplayX;
    private EditBox weightDisplayY;

    private EditBox stuffedHudXOffset;
    private EditBox stuffedHudYOffset;

    private ToggleButton debugView;

    private PortProofButton toConfigScreen;

    private int centerW;
    private int centerH;
    private int screenW;
    private int screenH;
    private int leftBackX;
    private int leftBackY;

    private Window curWindow;
    private Font font;

    private int lastGuiScale;

    private boolean nonNumFlag;
    private static final ResourceLocation WEIGHTPLACEHOLDER = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/placeholder.png");


    public GraphicsConfigScreen() {
        super(Component
                .literal("Overstuffed Graphics Config Menu")
                .withStyle(ChatFormatting.BLUE));
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

        nonNumFlag=false;





        // Create the options list
        // It must be created in this method instead of in the constructor,
        // or it will not be displayed properly


        //buttons


        //ALL editbox sizes are based off this first editbox.
        this.weightDisplayX = new EditBox(
                font,
                centerW - 40-35,
                50,
                35,
                25,
                Component.literal("Weight Display X Coord"));
        this.weightDisplayX.setValue(OverstuffedConfig.weightDisplayX.get()+"");

        this.weightDisplayY = new EditBox(
                font,
                centerW +40,
                50,
                35,
                25,
                Component.literal("Weight Display Y Coord"));
        this.weightDisplayY.setValue(OverstuffedConfig.weightDisplayY.get()+"");

        this.stuffedHudXOffset=new EditBox(
                font,
                centerW - 40-35,
                110,
                35,
                25,
                Component.literal("Stuffed Hud X Offset"));
        this.stuffedHudXOffset.setValue(OverstuffedConfig.stuffedHudXOffset.get()+"");

        this.stuffedHudYOffset = new EditBox(
                font,
                centerW +40,
                110,
                35,
                25,
                Component.literal("Stuffed Hud Y Offset"));
        this.stuffedHudYOffset.setValue(OverstuffedConfig.stuffedHudYOffset.get()+"");

        this.debugView= new ToggleButton(screenW/2-50,150,100,20,
                Component.translatable("message.overstuffed.debugbutton"),OverstuffedConfig.debugView.get());
        this.debugView.setLocked(false);

        this.toConfigScreen= new PortProofButton(screenW - 120, 8, 100, 20,
                Component.literal("Gameplay Config"), new Runnable() {
            @Override
            public void run() {
                swapScreen("gameplay");
            }
        });

        this.addRenderableWidget(this.weightDisplayX);
        this.addRenderableWidget(this.weightDisplayY);

        this.addRenderableWidget(this.stuffedHudXOffset);
        this.addRenderableWidget(this.stuffedHudYOffset);

        this.addRenderableWidget(debugView);

        this.addRenderableWidget(toConfigScreen);


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
    public void tick() {
        super.tick();
        // Add ticking logic for EditBox in editBox
        this.weightDisplayX.tick();
        this.weightDisplayY.tick();

        this.stuffedHudXOffset.tick();
        this.stuffedHudYOffset.tick();
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
        //drawing the edit box's title

        pose.pushPose();
        pose.scale(.8f,0.8f,1);
        guiGraphics.drawCenteredString(  font, "Weight Display X Coordinate",(int)(this.weightDisplayX.getX()*1.2+20*1.2),50,Color.WHITE.hashCode());

        guiGraphics.drawCenteredString(  font, "Weight Display Y Coordinate",(int)(this.weightDisplayY.getX()*1.2+30*1.2),50,Color.WHITE.hashCode());

        guiGraphics.drawCenteredString(  font, "Stuffed Hud X Offset",(int)(this.stuffedHudXOffset.getX()*1.2+20*1.2),120,Color.WHITE.hashCode());

        guiGraphics.drawCenteredString(  font, "Stuffed Hud Y Offset",(int)(this.stuffedHudYOffset.getX()*1.2+20*1.2),120,Color.WHITE.hashCode());


        pose.popPose();

        if(weightDisplayY.isFocused() || weightDisplayX.isFocused())
        {
            //RenderSystem.setShaderTexture(0,WEIGHTPLACEHOLDER);

            guiGraphics.blit(WEIGHTPLACEHOLDER,this.getEditBoxInt("weight",true),
                    this.getEditBoxInt("weight",false),0,0,34,34,20,20);
        }

        if(stuffedHudYOffset.isFocused() || stuffedHudXOffset.isFocused())
        {
            //RenderSystem.setShaderTexture(0,WEIGHTPLACEHOLDER);

            guiGraphics.blit(WEIGHTPLACEHOLDER,screenW/2+10+this.getEditBoxInt("stuffed",true),
                    screenH-45-this.getEditBoxInt("stuffed",false),0,0,70,6,34,34);
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
        nonNumFlag=false;


        //TODO MAKE THE GRAPHICS CONFIG ERROR ALSO APPEAR AS A RED MESSAGE
        if(nonNumFlag==true)
        {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Error, non-letter value inputted for a coordinate, which" +
                    "failed to save"));
            nonNumFlag=false;
        }
        else{
            OverstuffedConfig.weightDisplayX.set(this.getEditBoxInt("weight", true));
            OverstuffedConfig.weightDisplayY.set(this.getEditBoxInt("weight", false));

            OverstuffedConfig.stuffedHudXOffset.set(this.getEditBoxInt("stuffed",true));
            OverstuffedConfig.stuffedHudYOffset.set(this.getEditBoxInt("stuffed",false));
        }


        OverstuffedConfig.debugView.set(debugView.getSetting());



        OverstuffedConfig.saveConfig();

        // Call last in case it interferes with the override
        super.onClose();
    }

    @Override
    public void removed() {
        // Reset initial states here

        // Call last in case it interferes with the override
        super.removed();
    }

    public int getEditBoxInt(String name, boolean xOrY)
    {
        try {


            if (name.contentEquals("weight")) {

                if (xOrY)
                {
                    int newX = Integer.parseInt(weightDisplayX.getValue().trim());
                    return newX;
                }
                else
                {
                    int newY = Integer.parseInt(weightDisplayY.getValue().trim());
                    return newY;
                }
            }
            else if (name.contentEquals("stuffed")) {

                if (xOrY)
                {
                    int newX = Integer.parseInt(stuffedHudXOffset.getValue().trim());
                    return newX;
                }
                else
                {
                    int newY = Integer.parseInt(stuffedHudYOffset.getValue().trim());
                    return newY;
                }
            }

        }
        catch(NumberFormatException e)
        {
            nonNumFlag=true;
            //Minecraft.getInstance().player.sendSystemMessage(Component.literal("Error, non-letter value inputted for coordinates"));
        }
        return 0;
    }

    public void swapScreen(String screenName)
    {
        this.onClose();
        if(screenName.contentEquals("gameplay"))
        {
            Minecraft.getInstance().setScreen(new ConfigScreen());
        }
    }

}
