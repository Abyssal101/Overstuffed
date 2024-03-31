package net.willsbr.overstuffed.Menu;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.willsbr.overstuffed.OverStuffed;

public class ConfigMenu extends Screen {

//screen resource textures
    private static final ResourceLocation OVERSTUFFED_BACKGROUND = new ResourceLocation(OverStuffed.MODID, "textures/gui/overstuffedbackground.png");

    private static EditBox editbox1;

    private static Button saveButton;

    private int leftBackX;
    private int leftBackY;
    private int centerW;
    private int centerH;

    private Window curWindow;
    private int screenW;
    private int screenH;




    public ConfigMenu(Component pTitle) {
        super(pTitle);
    }
    //this should initialize most things, gets ercalled on screen change.
    @Override
    protected void init() {
        super.init();
         curWindow= Minecraft.getInstance().getWindow();
         screenW=curWindow.getGuiScaledWidth();
         screenH=curWindow.getGuiScaledHeight();


        leftBackX=(int)(this.width/4*(1/curWindow.getGuiScale()));
        leftBackY=(int)(this.height/7*(1/curWindow.getGuiScale()));
        centerW=screenW/2;
        centerH=screenH/2;

        //make emR

        editbox1=new EditBox(Minecraft.getInstance().font, leftBackX+(width/60),leftBackY+(height/40),130,30,
                Component.literal("Beep boop").withStyle(ChatFormatting.GREEN));
//        saveButton= new Button(leftBackX+(int)(this.width/5),(int)(leftBackY+(this.height/2.2)),60,20,
//                Component.literal("Save").withStyle(ChatFormatting.WHITE),button ->{
//            onClose();
//        },null
//         );
//       saveButton= new Button.Builder(Component.literal("Save").withStyle(ChatFormatting.WHITE),button ->{
//           save();
       //});
       //saveButton.bounds(centerW+(screenW/18)+(int)(0.25* 25* curWindow.getGuiScale()),centerH+(int)(0.5* 22* curWindow.getGuiScale()),60,20);

        // Add widgets and precomputed values
        //addRenderableOnly too
        this.addRenderableWidget(editbox1);
        //this.addRenderableWidget(saveButton.build());
        //this.resize();
    }

    @Override
    public void tick() {
        super.tick();

        // Add ticking logic for EditBox in editBox
        //this.editBox.tick();
    }

    // mouseX and mouseY indicate the scaled coordinates of where the cursor is in on the screen
    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float partialTick) {
        //DELETE THIS YOUR DOING THIS SO THE VALUES RELOAD IN DEBUG
        this.init();
        // Background is typically rendered first
        this.renderBackground(pose);
        //this.renderDirtBackground(1);
        // Render things here before widgets (background textures)
        this.background(pose);
        // Then the widgets if this is a direct child of the Screen

        super.render(pose, mouseX, mouseY, partialTick);
//        pose.popPose();
        // Render things after widgets (tooltips)

    }

    public void background(PoseStack pose)
    {
        RenderSystem.setShaderTexture(0, OVERSTUFFED_BACKGROUND);
            //System.out.println(""+curWindow.getGuiScale());
        int backgroundWidth=screenW/6+(int)(50* curWindow.getGuiScale());
        int backgroundHeight=screenH/4+(int)(45* curWindow.getGuiScale());

        this.blit(pose,centerW-(screenW/12)-(int)(0.5*50* curWindow.getGuiScale()),centerH-(screenH/4)-(int)(0.5*25* curWindow.getGuiScale()),0,0,0,
               backgroundWidth,backgroundHeight,backgroundWidth,backgroundHeight);
        //the mappings are wrong the two second ones are swapped
        this.blit(pose,curWindow.getGuiScaledWidth()/2,0,0,0,4, curWindow.getGuiScaledHeight(),4, curWindow.getGuiScaledHeight());

    }



    public void save()
    {


        this.onClose();
    }
    //this saves any data
    @Override
    public void onClose() {
        // Stop any handlers here
        System.out.println(editbox1.getValue()+"tests");



        // Call last in case it interferes with the override
        super.onClose();
    }

    //this resets all the junk
    @Override
    public void removed() {
        // Reset initial states here

        // Call last in case it interferes with the override
        super.removed()
        ;}
}
