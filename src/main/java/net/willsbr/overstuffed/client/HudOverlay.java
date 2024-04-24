package net.willsbr.overstuffed.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.willsbr.overstuffed.OverStuffed;

public class HudOverlay {
        private static final ResourceLocation STUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/stuffedpoint.png");
       private static final ResourceLocation OVERSTUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/overstuffedpoint.png");

    private static final ResourceLocation SUPERSTUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/superstuffedpoint.png");

    public static final IGuiOverlay HUD_STUFFEDBAR=((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        //anything in here gets rendered
        Window curWindow= Minecraft.getInstance().getWindow();
      //  int x=screenWidth/2+(int)((screenWidth/100));
       // int y=(int)(screenHeight/2+screenHeight/4+(1/curWindow.getGuiScale()*screenHeight/4));

        //aa fuck it
        screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        screenHeight =  Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int rightHeight = 45;
        int leftHeight = 39;

        int left = screenWidth / 2 + 60;
        int top = screenHeight - rightHeight;
        rightHeight += 10;



        //need this to actually render
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, STUFFED_POINT);
        for(int i = 0; i < ClientStuffedBarData.getSoftLimit(); i++) {
            int idx = i * 2 + 1;
            int x = left - 9 - (9-i)*8 + 32;
            int y = top;



            if(ClientStuffedBarData.getPlayerStuffedBar()>i)
            {
               // GuiComponent.blit(poseStack,x - 94 + (i * 9), y - 54,0,0,12,
                      //  12,6,6);
                GuiComponent.blit(poseStack,x, y,0,0,0,5,5,5,5);
                        //  12,6,6);
            }
            else {
                break;
            }

        }

        RenderSystem.setShaderTexture(0, OVERSTUFFED_POINT);
        for(int i = ClientStuffedBarData.getSoftLimit(); i < (ClientStuffedBarData.getCurrentFirmLimit()+ ClientStuffedBarData.getSoftLimit());
            i++) {
            int idx = i * 2 + 1;
            int x = left - 9 - (9-i)*8 + 32;
            int y = top;
            if(ClientStuffedBarData.getPlayerStuffedBar()>i)
            {
                GuiComponent.blit(poseStack,x, y,0,0,0,5,5,5,5);
                //  12,6,6);
            }
            else {
                break;
            }
        }
        //hard limit swap
        RenderSystem.setShaderTexture(0, SUPERSTUFFED_POINT);
        for(int i = (ClientStuffedBarData.getSoftLimit()+ClientStuffedBarData.getCurrentFirmLimit()); i < (ClientStuffedBarData.getHardLimit()+
                ClientStuffedBarData.getSoftLimit()+ClientStuffedBarData.getCurrentFirmLimit());
            i++) {
            int idx = i * 2 + 1;
            int x = left - 9 - (9-i)*8 + 32;
            int y = top;
            if(ClientStuffedBarData.getPlayerStuffedBar()>i)
            {
                GuiComponent.blit(poseStack,x, y,0,0,0,5,5,5,5);
                //  12,6,6);
            }
            else {
                break;
            }
        }
        poseStack.pushPose();
        poseStack.scale(2,2,2);
        poseStack.translate( 0,0,0);

        GuiComponent.drawString(poseStack, gui.getFont(), "Weight:  "+(ClientWeightBarData.getPlayerWeight()+ ClientWeightBarData.getMinWeight())+" / "+(ClientWeightBarData.getMaxWeight()+ClientWeightBarData.getMinWeight()), 20,20,255);
        poseStack.popPose();
      //  RenderSystem.setShaderTexture(0, STUFFED_POINT);





    });

}
