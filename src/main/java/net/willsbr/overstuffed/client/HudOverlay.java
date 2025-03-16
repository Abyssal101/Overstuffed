package net.willsbr.overstuffed.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.config.OverstuffedConfig;

import java.awt.*;

import static net.willsbr.overstuffed.client.AbstractClientMethods.AbstractDraw;


public class HudOverlay {
        private static final ResourceLocation STUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/stuffedpoint.png");
       private static final ResourceLocation OVERSTUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/overstuffedpoint.png");

    private static final ResourceLocation SUPERSTUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/superstuffedpoint.png");

    private static final ResourceLocation WEIGHTSPRITE0 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/bellyicon1.png");

    private static final ResourceLocation WEIGHTSPRITE1 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/bellyicon2.png");
    private static final ResourceLocation WEIGHTSPRITE2 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/bellyicon3.png");
    private static final ResourceLocation WEIGHTSPRITE3 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/bellyicon4.png");
    private static final ResourceLocation WEIGHTSPRITE4 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/bellyicon5.png");

    //DELETE THIS

    private static final ResourceLocation STUFFED_BAR_BEG = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/stuffed_bar_beg.png");
    private static final ResourceLocation STUFFED_BAR_MID = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/stuffed_bar_mid.png");
    private static final ResourceLocation STUFFED_BAR_END = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/stuffed_bar_end.png");


    private static final ResourceLocation STUFFED_BAR_ICON_BACKGROUND = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/stuffedbariconbackground.png");

    private static final ResourceLocation STUFFED_PART = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/stuffed_part.png");
    private static final ResourceLocation STUFFED_END = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/stuffed_end.png");

    private static final ResourceLocation OVERSTUFFED_PART = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/overstuffed_part.png");
    private static final ResourceLocation OVERSTUFFED_END = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/overstuffed_end.png");

    private static final ResourceLocation SUPERSTUFFED_PART = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/superstuffed_part.png");
    private static final ResourceLocation SUPERSTUFFED_END = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/superstuffed_end.png");
    private static Font font;


    private static final ResourceLocation[] WEIGHTSTAGESPRITES= new ResourceLocation[5];
    private static final ResourceLocation WEIGHTSPRITEBACKGROUND= new ResourceLocation(OverStuffed.MODID,"textures/stuffedbar/weightbackground.png");
    public static final IGuiOverlay HUD_STUFFEDBAR=((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        //anything in here gets rendered
        Window curWindow= Minecraft.getInstance().getWindow();

        screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        screenHeight =  Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int rightHeight = 49;
        int leftHeight = 10;

        int left = screenWidth / 2 + leftHeight + OverstuffedConfig.stuffedHudXOffset.get();
        int top = screenHeight - rightHeight -OverstuffedConfig.stuffedHudYOffset.get();
        rightHeight += 10;

        WEIGHTSTAGESPRITES[0]=WEIGHTSPRITE0;
        WEIGHTSTAGESPRITES[1]=WEIGHTSPRITE1;
        WEIGHTSTAGESPRITES[2]=WEIGHTSPRITE2;
        WEIGHTSTAGESPRITES[3]=WEIGHTSPRITE3;
        WEIGHTSTAGESPRITES[4]=WEIGHTSPRITE4;
        //TODO Figure out how to make it draw ONTOP of chat

        font = Minecraft.getInstance().font;
        PoseStack poseStack=guiGraphics.pose();


        //need this to actually render
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


        //New stuffed bar

       // AbstractDraw(gui,guiGraphics,STUFFED_BAR, (int)(left-10),(int)(top),84,8);


        AbstractDraw(gui,guiGraphics,STUFFED_BAR_BEG, (int)(left),(int)(top),10,8);
        AbstractDraw(gui,guiGraphics,STUFFED_BAR_END, (int)(left+(ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-2)*8+10),(int)(top),10,8);

        for(int i=1;i<(ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-1);i++)
        {
            AbstractDraw(gui,guiGraphics,STUFFED_BAR_MID, (int)(left+8*(i-1)+10),(int)(top),8,8);
        }


        for(int i=0;i<ClientStuffedBarData.getPlayerStuffedBar();i++)
        {
            if(i<ClientStuffedBarData.getSoftLimit()-1)
            {
                AbstractDraw(gui,guiGraphics,STUFFED_PART, (int)(left+2+8*i),(int)(top+2),8,4);
            }
            else if(i==ClientStuffedBarData.getSoftLimit()-1)
            {
                AbstractDraw(gui,guiGraphics,STUFFED_END, (int)(left+2+8*i),(int)(top+2),8,4);
            }
            else if(i<(ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-1))
            {
                AbstractDraw(gui,guiGraphics,OVERSTUFFED_PART, (int)(left+2+8*i),(int)(top+2),8,4);
            }
            else if(i==(ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-1))
            {
                AbstractDraw(gui,guiGraphics,OVERSTUFFED_END, (int)(left+2+8*i),(int)(top+2),8,4);
            }
            else if(i<(ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-1))
            {
                AbstractDraw(gui,guiGraphics,SUPERSTUFFED_PART, (int)(left+2+8*i),(int)(top+2),8,4);
            }
            else if(i==(ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-1))
            {
                AbstractDraw(gui,guiGraphics,SUPERSTUFFED_END, (int)(left+2+8*i),(int)(top+2),8,4);
            }
        }




        poseStack.pushPose();
        poseStack.scale(2,2,2);
        poseStack.popPose();

        //1.19.2

        //AbstractDraw(gui,guiGraphics,WEIGHTSPRITEBACKGROUND, OverstuffedConfig.weightDisplayX.get(), OverstuffedConfig.weightDisplayY.get(),33,33);


        int outOf100=(int)((((double)ClientWeightBarData.getPlayerWeight()-OverstuffedConfig.getMinWeight())/(OverstuffedConfig.maxWeight.get()-OverstuffedConfig.getMinWeight()))*100);
        //for checking which sprite is currently being displayed.
        //System.out.println(outOf100/20-1+"/5");
        //1.19.2
        if(outOf100/20==5)
        {
            AbstractDraw(gui,guiGraphics,WEIGHTSTAGESPRITES[4],screenWidth/2-12+OverstuffedConfig.weightDisplayX.get(),top-5+OverstuffedConfig.weightDisplayY.get(),24,24);
        }
        else {
            AbstractDraw(gui,guiGraphics,WEIGHTSTAGESPRITES[outOf100/20],screenWidth/2-12+OverstuffedConfig.weightDisplayX.get(),top-5+OverstuffedConfig.weightDisplayY.get(),24,24);
        }



        if(OverstuffedConfig.debugView.get())
        {
            //Current Weight
            //Max Weight
            //Min Weight

            //Stuffed current valaue
            //# of food to get to next stuffed

           guiGraphics.drawCenteredString( font, Component.translatable("message.overstuffed.debugcurrentweight",""+ClientWeightBarData.getPlayerWeight())
                   ,screenWidth/2,20, Color.RED.hashCode());

            guiGraphics.drawCenteredString( font, Component.translatable("message.overstuffed.debugmaxweight",""+OverstuffedConfig.getMaxWeight())
                    ,screenWidth/2,30, Color.RED.hashCode());
            guiGraphics.drawCenteredString( font, Component.translatable("message.overstuffed.debugminweight",""+OverstuffedConfig.getMinWeight())
                    ,screenWidth/2,40, Color.RED.hashCode());


            guiGraphics.drawCenteredString( font, Component.translatable("message.overstuffed.debugcurrentstuffed",""+ ClientStuffedBarData.getPlayerStuffedBar())
                    ,screenWidth/2,50, Color.RED.hashCode());
            guiGraphics.drawCenteredString( font, Component.translatable("message.overstuffed.debugmaxstuffed",""+
                            (ClientStuffedBarData.getSoftLimit()+ ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getFirmLimit()))
                    ,screenWidth/2,60, Color.RED.hashCode());
            guiGraphics.drawCenteredString( font, Component.translatable("message.overstuffed.debugnextmax",""+
                            ClientStuffedBarData.getCurrentLost()+"/"+ClientStuffedBarData.getInterval())
                    ,screenWidth/2,70, Color.RED.hashCode());
            guiGraphics.drawCenteredString( font, Component.translatable("message.overstuffed.debugqueueweight",""+
                            ClientWeightBarData.getQueuedWeight())
                    ,screenWidth/2,80, Color.RED.hashCode());

        }

    });

}
