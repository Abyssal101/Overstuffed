package net.willsbr.overstuffed.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.config.OverstuffedConfig;

public class HudOverlay {
        private static final ResourceLocation STUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/stuffedpoint.png");
       private static final ResourceLocation OVERSTUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/overstuffedpoint.png");

    private static final ResourceLocation SUPERSTUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/superstuffedpoint.png");

    //TODO MAKE AN ACTUAL WEIGHT SPRITE 0 CURRENTLY RESUSES 1
    private static final ResourceLocation WEIGHTSPRITE0 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/belly2.png");


    private static final ResourceLocation WEIGHTSPRITE1 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/belly2.png");
    private static final ResourceLocation WEIGHTSPRITE2 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/belly3.png");
    private static final ResourceLocation WEIGHTSPRITE3 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/belly4.png");
    private static final ResourceLocation WEIGHTSPRITE4 =new ResourceLocation(OverStuffed.MODID, "textures/stuffedbar/weightsprites/belly5.png");



    private static  ResourceLocation[] WEIGHTSTAGESPRITES= new ResourceLocation[5];

    private static final ResourceLocation WEIGHTSPRITEBACKGROUND= new ResourceLocation(OverStuffed.MODID,"textures/stuffedbar/weightbackground.png");
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

        WEIGHTSTAGESPRITES[0]=WEIGHTSPRITE0;
        WEIGHTSTAGESPRITES[1]=WEIGHTSPRITE1;
        WEIGHTSTAGESPRITES[2]=WEIGHTSPRITE2;
        WEIGHTSTAGESPRITES[3]=WEIGHTSPRITE3;
        WEIGHTSTAGESPRITES[4]=WEIGHTSPRITE4;







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


        GuiComponent.drawString(poseStack, gui.getFont(), "Weight:  "+ClientWeightBarData.getPlayerWeight()+" / "+OverstuffedConfig.maxWeight.get(), 20,20,255);

        poseStack.popPose();
        //TODO-MAKE OVERSTUFFED POINTS AND WEIGHT STAGE SPRITE HAVE ADJUSTABLE LOCATION(HUD PANEL IN SETTINGS AND SAVE IN CONFIG)
        RenderSystem.setShaderTexture(0,WEIGHTSPRITEBACKGROUND);
        GuiComponent.blit(poseStack,40,10,0,0,20,20,20,20);

        int outOf100=(int)((((double)ClientWeightBarData.getPlayerWeight())/OverstuffedConfig.maxWeight.get())*100);
        System.out.println(outOf100/20+"/5");
        RenderSystem.setShaderTexture(0,WEIGHTSTAGESPRITES[outOf100/20]);
        GuiComponent.blit(poseStack,41,11,0,0,19,19,19,19);




    });

}
