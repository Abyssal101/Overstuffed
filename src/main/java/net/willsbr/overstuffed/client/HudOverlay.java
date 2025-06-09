package net.willsbr.overstuffed.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.willsbr.overstuffed.OverStuffed;
import net.willsbr.overstuffed.config.OverstuffedClientConfig;
import org.jline.utils.Colors;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

import static net.willsbr.overstuffed.client.AbstractClientMethods.AbstractDraw;


public class HudOverlay {
        private static final ResourceLocation STUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/hud/stuffedpoint.png");
       private static final ResourceLocation OVERSTUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/hud/overstuffedpoint.png");

    private static final ResourceLocation SUPERSTUFFED_POINT = new ResourceLocation(OverStuffed.MODID, "textures/hud/superstuffedpoint.png");

    private static final ResourceLocation WEIGHTSPRITE0 =new ResourceLocation(OverStuffed.MODID, "textures/hud/weightsprites/bellyicon1.png");

    private static final ResourceLocation WEIGHTSPRITE1 =new ResourceLocation(OverStuffed.MODID, "textures/hud/weightsprites/bellyicon2.png");
    private static final ResourceLocation WEIGHTSPRITE2 =new ResourceLocation(OverStuffed.MODID, "textures/hud/weightsprites/bellyicon3.png");
    private static final ResourceLocation WEIGHTSPRITE3 =new ResourceLocation(OverStuffed.MODID, "textures/hud/weightsprites/bellyicon4.png");
    private static final ResourceLocation WEIGHTSPRITE4 =new ResourceLocation(OverStuffed.MODID, "textures/hud/weightsprites/bellyicon5.png");


    private static final ResourceLocation STUFFED_BAR_BEG = new ResourceLocation(OverStuffed.MODID, "textures/hud/stuffed_bar_beg.png");
    private static final ResourceLocation STUFFED_BAR_MID = new ResourceLocation(OverStuffed.MODID, "textures/hud/stuffed_bar_mid.png");
    private static final ResourceLocation STUFFED_BAR_END = new ResourceLocation(OverStuffed.MODID, "textures/hud/stuffed_bar_end.png");


    private static final ResourceLocation STUFFED_BAR_ICON_BACKGROUND = new ResourceLocation(OverStuffed.MODID, "textures/hud/stuffedbariconbackground.png");

    private static final ResourceLocation STUFFED_PART = new ResourceLocation(OverStuffed.MODID, "textures/hud/stuffed_part.png");
    private static final ResourceLocation STUFFED_END = new ResourceLocation(OverStuffed.MODID, "textures/hud/stuffed_end.png");

    private static final ResourceLocation OVERSTUFFED_PART = new ResourceLocation(OverStuffed.MODID, "textures/hud/overstuffed_part.png");
    private static final ResourceLocation OVERSTUFFED_END = new ResourceLocation(OverStuffed.MODID, "textures/hud/overstuffed_end.png");

    private static final ResourceLocation SUPERSTUFFED_PART = new ResourceLocation(OverStuffed.MODID, "textures/hud/superstuffed_part.png");
    private static final ResourceLocation SUPERSTUFFED_END = new ResourceLocation(OverStuffed.MODID, "textures/hud/superstuffed_end.png");
    private static Font font;

    private static final ResourceLocation STOMACH_ICON_ONE = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomach4.png");
    private static final ResourceLocation STOMACH_ONE_MASK = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomach4mask.png");

    private static final ResourceLocation STOMACH_ICON_TWO = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomach5.png");
    private static final ResourceLocation STOMACH_ICON_THREE = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomach6.png");
    private static final ResourceLocation STOMACH_ICON_FOUR = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomach7.png");
    private static final ResourceLocation STOMACH_ICON_FIVE = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomach8.png");
    private static final ResourceLocation[] STOMACH_ICONS= new ResourceLocation[5];

    private static final ResourceLocation GREEN_ACID1 = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/acid_green1.png");
    private static final ResourceLocation GREEN_ACID2 = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/acid_green2.png");
    private static final ResourceLocation GREEN_ACID3 = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/acid_green3.png");
    private static final ResourceLocation GREEN_ACID4 = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/acid_green4.png");
    private static final ResourceLocation GREEN_ACID5 = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/acid_green5.png");
    private static final ResourceLocation[] ACID_SPRITES= new ResourceLocation[5];


    private static long acidTick=0;
    private static int currentAcid=0;



    private static final ResourceLocation[] WEIGHTSTAGESPRITES= new ResourceLocation[5];
    private static final ResourceLocation WEIGHTSPRITEBACKGROUND= new ResourceLocation(OverStuffed.MODID,"textures/hud/weightbackground.png");
    public static final IGuiOverlay HUD_STUFFEDBAR=((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        //anything in here gets rendered
        Window curWindow= Minecraft.getInstance().getWindow();

        screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        screenHeight =  Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int rightHeight = 49;
        int leftHeight = 10;

        int left = screenWidth / 2 + leftHeight ;
        int top = screenHeight - rightHeight ;
        rightHeight += 10;

        WEIGHTSTAGESPRITES[0]=WEIGHTSPRITE0;
        WEIGHTSTAGESPRITES[1]=WEIGHTSPRITE1;
        WEIGHTSTAGESPRITES[2]=WEIGHTSPRITE2;
        WEIGHTSTAGESPRITES[3]=WEIGHTSPRITE3;
        WEIGHTSTAGESPRITES[4]=WEIGHTSPRITE4;

        STOMACH_ICONS[0]=STOMACH_ICON_ONE;
        STOMACH_ICONS[1]=STOMACH_ICON_TWO;
        STOMACH_ICONS[2]=STOMACH_ICON_THREE;
        STOMACH_ICONS[3]=STOMACH_ICON_FOUR;
        STOMACH_ICONS[4]=STOMACH_ICON_FIVE;

        ACID_SPRITES[0]=GREEN_ACID1;
        ACID_SPRITES[1]=GREEN_ACID2;
        ACID_SPRITES[2]=GREEN_ACID3;
        ACID_SPRITES[3]=GREEN_ACID4;
        ACID_SPRITES[4]=GREEN_ACID5;


        font = Minecraft.getInstance().font;
        PoseStack poseStack=guiGraphics.pose();


        //need this to actually render
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);


        //New stuffed bar

       // AbstractDraw(gui,guiGraphics,STUFFED_BAR, (int)(left-10),(int)(top),84,8);
        int barLeft=left+ OverstuffedClientConfig.stuffedHudXOffset.get();
        int barTop=top- OverstuffedClientConfig.stuffedHudYOffset.get();

        AbstractDraw(gui,guiGraphics,STUFFED_BAR_BEG, (int)(barLeft),(int)(barTop),10,8);
        AbstractDraw(gui,guiGraphics,STUFFED_BAR_END, (int)(barLeft+(ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-2)*8+10),(int)(barTop),10,8);

        for(int i=1;i<(ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-1);i++)
        {
            AbstractDraw(gui,guiGraphics,STUFFED_BAR_MID, (int)(barLeft+8*(i-1)+10),(int)(barTop),8,8);
        }


        for(int i=0;i<ClientStuffedBarData.getPlayerStuffedBar();i++)
        {
            if(i<ClientStuffedBarData.getSoftLimit()-1)
            {
                AbstractDraw(gui,guiGraphics,STUFFED_PART, (int)(barLeft+2+8*i),(int)(barTop+2),8,4);
            }
            else if(i==ClientStuffedBarData.getSoftLimit()-1)
            {
                AbstractDraw(gui,guiGraphics,STUFFED_END, (int)(barLeft+2+8*i),(int)(barTop+2),8,4);
            }
            else if(i<(ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-1))
            {
                AbstractDraw(gui,guiGraphics,OVERSTUFFED_PART, (int)(barLeft+2+8*i),(int)(barTop+2),8,4);
            }
            else if(i==(ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-1))
            {
                AbstractDraw(gui,guiGraphics,OVERSTUFFED_END, (int)(barLeft+2+8*i),(int)(barTop+2),8,4);
            }
            else if(i<(ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-1))
            {
                AbstractDraw(gui,guiGraphics,SUPERSTUFFED_PART, (int)(barLeft+2+8*i),(int)(barTop+2),8,4);
            }
            else if(i==(ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getFirmLimit()+ClientStuffedBarData.getSoftLimit()-1))
            {
                AbstractDraw(gui,guiGraphics,SUPERSTUFFED_END, (int)(barLeft+2+8*i),(int)(barTop+2),8,4);
            }
        }





        poseStack.pushPose();
        poseStack.scale(2,2,2);
        poseStack.popPose();


        //1.19.2

        //AbstractDraw(gui,guiGraphics,WEIGHTSPRITEBACKGROUND, OverstuffedConfig.weightDisplayX.get(), OverstuffedConfig.weightDisplayY.get(),33,33);


        int outOf100=(int)((((double)ClientWeightBarData.getPlayerWeight()- OverstuffedClientConfig.getMinWeight())/(OverstuffedClientConfig.maxWeight.get()- OverstuffedClientConfig.getMinWeight()))*100);
        //for checking which sprite is currently being displayed.
        //System.out.println(outOf100/20-1+"/5");
        //1.19.2
        if(outOf100/20==5)
        {
            AbstractDraw(gui,guiGraphics,WEIGHTSTAGESPRITES[4],screenWidth/2-12+ OverstuffedClientConfig.weightDisplayXOffset.get(),top-5+ OverstuffedClientConfig.weightDisplayYOffSet.get(),24,24);
        }
        else {
            AbstractDraw(gui,guiGraphics,WEIGHTSTAGESPRITES[outOf100/20],screenWidth/2-12+ OverstuffedClientConfig.weightDisplayXOffset.get(),top-5+ OverstuffedClientConfig.weightDisplayYOffSet.get(),24,24);
        }

        renderStomachIcon(gui,guiGraphics,poseStack);

        if(OverstuffedClientConfig.debugView.get())
        {
            //Current Weight
            //Max Weight
            //Min Weight

            //Stuffed current valaue
            //# of food to get to next stuffed
            ArrayList<Component> info= new ArrayList<Component>();
            info.add(Component.translatable("message.overstuffed.debugcurrentweight",""+ClientWeightBarData.getPlayerWeight()));
            info.add(Component.translatable("message.overstuffed.debugmaxweight",""+ OverstuffedClientConfig.getMaxWeight()));
            info.add(Component.translatable("message.overstuffed.debugminweight",""+ OverstuffedClientConfig.getMinWeight()));
            info.add(Component.translatable("message.overstuffed.debugcurrentstuffed",""+ ClientStuffedBarData.getPlayerStuffedBar()));
            info.add(Component.translatable("message.overstuffed.debugmaxstuffed",""+
                    (ClientStuffedBarData.getSoftLimit()+ ClientStuffedBarData.getHardLimit()+ClientStuffedBarData.getFirmLimit())));
            info.add(Component.translatable("message.overstuffed.debugnextmax",""+
                    ClientStuffedBarData.getCurrentLost()+"/"+ClientStuffedBarData.getInterval()));
            info.add(Component.translatable("message.overstuffed.debugqueueweight",""+
                    ClientWeightBarData.getQueuedWeight()));
            info.add(Component.translatable("message.overstuffed.debugtotalqueueweight",""+
                    ClientWeightBarData.getTotalQueuedWeight()));


            for(int i=0;i<info.size();i++)
            {
                guiGraphics.drawCenteredString( font,info.get(i)
                        ,screenWidth/2,20+10*i, Color.RED.hashCode());
            }

        }

    });


    static private void renderPlayerModel(PoseStack poseStack) {
//        int x = width / 2;
//        int y = height / 2;

        // Player rendering area size
        int size = 30;

        drawEntity(poseStack, 100, 100, size,
                10, 10,
                Minecraft.getInstance().player);
    }

    /**
     * This method handles drawing the player entity
     */
    public static void drawEntity(PoseStack poseStack, int x, int y, int size,
                                  float mouseX, float mouseY, LivingEntity entity) {
        float f = (float)Math.atan(mouseX / 40.0F);
        float f1 = (float)Math.atan(mouseY / 40.0F);

        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(x, y, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);

        RenderSystem.applyModelViewMatrix();

        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale((float)size, (float)size, (float)size);

        Axis Vector3f = null;
        Quaternionf quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternionf quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        posestack1.mulPose(quaternion);

        float f2 = entity.yBodyRot;
        float f3 = entity.getYRot();
        float f4 = entity.getXRot();
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;

        entity.yBodyRot = 180.0F + f * 20.0F;
        entity.setYRot(180.0F + f * 40.0F);
        entity.setXRot(-f1 * 20.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();

        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        //quaternion1.conj();
        dispatcher.overrideCameraOrientation(quaternion1);
        dispatcher.setRenderShadow(false);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance()
                .renderBuffers().bufferSource();

        RenderSystem.runAsFancy(() -> {
            dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F,
                    posestack1, bufferSource, 15728880);
        });

        bufferSource.endBatch();
        dispatcher.setRenderShadow(true);

        entity.yBodyRot = f2;
        entity.setYRot(f3);
        entity.setXRot(f4);
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;

        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
    }


    public static void renderStomachIcon(ForgeGui gui,GuiGraphics guiGraphics, PoseStack poseStack) {

        int x=10;
        int y=10;
        AbstractDraw(gui,guiGraphics,STOMACH_ICON_ONE,x,y,18,18);

        GL11.glEnable(GL11.GL_STENCIL_TEST); // Turn on da test
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT); // Flush old data

        GL11.glStencilMask(0xFF); // Writing = ON
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF); // Always "add" to frame
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE); // Replace on success
        RenderSystem.colorMask(false, false, false, false);

        drawMaskShape(guiGraphics);

        GL11.glStencilMask(0x00); // Writing = OFF
        GL11.glStencilFunc(GL11.GL_NOTEQUAL, 0, 0xFF); // Anything that wasn't defined above will not be rendered.
        RenderSystem.colorMask(true, true, true, true);

        //Anything rendered here will be cut if goes beyond frame defined before.
        drawContent(guiGraphics);

        GL11.glDisable(GL11.GL_STENCIL_TEST); // Turn this shit off!
        //drawContent(guiGraphics);

    }




    private static void drawMaskShape(GuiGraphics guiGraphics) {
        // Draw the mask shape - this defines the area where content will be visible
        // The mask texture should have transparent areas (where content won't show)
        // and opaque areas (where content will show through)

        // Set up rendering for the mask
        //RenderSystem.setShaderTexture(0, STOMACH_ONE_MASK);
        guiGraphics.blit(STOMACH_ONE_MASK, 10, 10, 0, 0, 18, 18, 18, 18);
    }

    private static void drawContent(GuiGraphics guiGraphics) {

        int acidWidth=42;
        int acidHeight=17;
        // Draw the content that will be masked
        // This content will only be visible where the mask is opaque

        // Set up rendering for the content
        //RenderSystem.setShaderTexture(0, masked);

        // Draw the content at the same position and size as the mask
        // This ensures proper alignment between mask and content

        acidTick++;
        if(acidTick/20>0)
        {
            acidTick=0;
            currentAcid++;
            if(currentAcid==5)
            {
                currentAcid=0;
            }
        }

        guiGraphics.blit(ACID_SPRITES[currentAcid], 10, 15, 0, 0, acidWidth, acidHeight, acidWidth, acidHeight);

    }

}
