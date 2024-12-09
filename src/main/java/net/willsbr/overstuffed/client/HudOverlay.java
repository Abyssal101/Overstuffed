package net.willsbr.overstuffed.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.StuffedBar.PlayerStuffedBar;
import net.willsbr.overstuffed.config.OverstuffedConfig;

import java.awt.*;

import static net.willsbr.overstuffed.client.AbstractClientMethods.AbstractDraw;


public class HudOverlay {
        private static final ResourceLocation STUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/stuffedpoint.png");
       private static final ResourceLocation OVERSTUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/overstuffedpoint.png");

    private static final ResourceLocation SUPERSTUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/superstuffedpoint.png");

    private static final ResourceLocation WEIGHTSPRITE0 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/belly1.png");

    private static final ResourceLocation WEIGHTSPRITE1 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/belly2.png");
    private static final ResourceLocation WEIGHTSPRITE2 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/belly3.png");
    private static final ResourceLocation WEIGHTSPRITE3 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/belly4.png");
    private static final ResourceLocation WEIGHTSPRITE4 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/belly5.png");


    private static Font font;


    private static final ResourceLocation[] WEIGHTSTAGESPRITES= new ResourceLocation[5];
    private static final ResourceLocation WEIGHTSPRITEBACKGROUND= new ResourceLocation(OverStuffed.MODID,"textures/stuffedbar/weightbackground.png");
    public static final IGuiOverlay HUD_STUFFEDBAR=((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        //anything in here gets rendered
        Window curWindow= Minecraft.getInstance().getWindow();

        screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        screenHeight =  Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int rightHeight = 45;
        int leftHeight = 39;

        int left = screenWidth / 2 + 60+OverstuffedConfig.stuffedHudXOffset.get();
        int top = screenHeight - rightHeight-OverstuffedConfig.stuffedHudYOffset.get();
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
        //1.19.2

        for(int i = 0; i < ClientStuffedBarData.getSoftLimit(); i++) {
            int idx = i * 2 + 1;
            int x = left - 9 - (9-i)*8 + 32;
            int y = top;
            if(ClientStuffedBarData.getPlayerStuffedBar()>i)
            {
                //Changes to guiGraphics
                AbstractDraw(gui,guiGraphics,STUFFED_POINT, x, y,5,5);
            }
            else {
                break;
            }
        }
        //1.19.2

        for(int i = ClientStuffedBarData.getSoftLimit(); i < (ClientStuffedBarData.getCurrentFirmLimit()+ ClientStuffedBarData.getSoftLimit());
            i++) {
            int idx = i * 2 + 1;
            int x = left - 9 - (9-i)*8 + 32;
            int y = top;
            if(ClientStuffedBarData.getPlayerStuffedBar()>i)
            {
                AbstractDraw(gui,guiGraphics,OVERSTUFFED_POINT, x, y,5,5);
                //  12,6,6);
            }
            else {
                break;
            }
        }
        //hard limit swap
        //1.19.2


        for(int i = (ClientStuffedBarData.getSoftLimit()+ClientStuffedBarData.getCurrentFirmLimit()); i < (ClientStuffedBarData.getHardLimit()+
                ClientStuffedBarData.getSoftLimit()+ClientStuffedBarData.getCurrentFirmLimit());
            i++) {
            int idx = i * 2 + 1;
            int x = left - 9 - (9-i)*8 + 32;
            int y = top;
            if(ClientStuffedBarData.getPlayerStuffedBar()>i)
            {

                AbstractDraw(gui,guiGraphics,SUPERSTUFFED_POINT, x, y,5,5);
            }
            else {
                break;
            }
        }
        poseStack.pushPose();
        poseStack.scale(2,2,2);
        poseStack.popPose();

        //1.19.2

        AbstractDraw(gui,guiGraphics,WEIGHTSPRITEBACKGROUND, OverstuffedConfig.weightDisplayX.get(), OverstuffedConfig.weightDisplayY.get(),33,33);


        int outOf100=(int)((((double)ClientWeightBarData.getPlayerWeight())/OverstuffedConfig.maxWeight.get())*100);
        //for checking which sprite is currently being displayed.
        //System.out.println(outOf100/20-1+"/5");
        //1.19.2

        AbstractDraw(gui,guiGraphics,WEIGHTSTAGESPRITES[outOf100/20-1], OverstuffedConfig.weightDisplayX.get(),OverstuffedConfig.weightDisplayY.get(),32,32);



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
                            (ClientStuffedBarData.getSoftLimit()+ ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getCurrentFirmLimit()))
                    ,screenWidth/2,60, Color.RED.hashCode());
            guiGraphics.drawCenteredString( font, Component.translatable("message.overstuffed.debugnextmax",""+
                            ClientStuffedBarData.getCurrentLost()+"/"+ClientStuffedBarData.getInterval())
                    ,screenWidth/2,70, Color.RED.hashCode());

        }

    });


}
