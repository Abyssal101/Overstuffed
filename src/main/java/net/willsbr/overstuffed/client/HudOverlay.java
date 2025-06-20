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
import net.willsbr.overstuffed.config.OverstuffedWorldConfig;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

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

    private static final ResourceLocation[] WEIGHTSTAGESPRITES = {
            WEIGHTSPRITE0,
            WEIGHTSPRITE1,
            WEIGHTSPRITE2,
            WEIGHTSPRITE3,
            WEIGHTSPRITE4
    };

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
    private static final ResourceLocation STOMACH_ONE_MASK = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomachmask4.png");

    private static final ResourceLocation STOMACH_ICON_TWO = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomach5.png");
    private static final ResourceLocation STOMACH_TWO_MASK = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomachmask5.png");

    private static final ResourceLocation STOMACH_ICON_THREE = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomach6.png");
    private static final ResourceLocation STOMACH_THREE_MASK = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomachmask6.png");

    private static final ResourceLocation STOMACH_ICON_FOUR = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomach7.png");
    private static final ResourceLocation STOMACH_FOUR_MASK = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomachmask7.png");

    private static final ResourceLocation STOMACH_ICON_FIVE = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomach8.png");
    private static final ResourceLocation STOMACH_FIVE_MASK = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/stomachmask8.png");
    private static final ResourceLocation[] STOMACH_ICONS = {
            STOMACH_ICON_ONE,
            STOMACH_ICON_TWO,
            STOMACH_ICON_THREE,
            STOMACH_ICON_FOUR,
            STOMACH_ICON_FIVE
    };

    private static final ResourceLocation[] STOMACH_MASKS = {
            STOMACH_ONE_MASK,
            STOMACH_TWO_MASK,
            STOMACH_THREE_MASK,
            STOMACH_FOUR_MASK,
            STOMACH_FIVE_MASK
    };


    private static final ResourceLocation GREEN_ACID1 = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/acid_green1.png");
    private static final ResourceLocation GREEN_ACID2 = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/acid_green2.png");
    private static final ResourceLocation GREEN_ACID3 = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/acid_green3.png");
    private static final ResourceLocation GREEN_ACID4 = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/acid_green4.png");
    private static final ResourceLocation GREEN_ACID5 = new ResourceLocation(OverStuffed.MODID, "textures/hud/stomach/acid_green5.png");

    private static final ResourceLocation[] ACID_SPRITES = {
            GREEN_ACID1,
            GREEN_ACID2,
            GREEN_ACID3,
            GREEN_ACID4,
            GREEN_ACID5
    };


    private static final ResourceLocation NUMBER_ZERO = new ResourceLocation(OverStuffed.MODID, "textures/hud/numbers/number0.png");
    private static final ResourceLocation NUMBER_ONE = new ResourceLocation(OverStuffed.MODID, "textures/hud/numbers/number1.png");
    private static final ResourceLocation NUMBER_TWO = new ResourceLocation(OverStuffed.MODID, "textures/hud/numbers/number2.png");
    private static final ResourceLocation NUMBER_THREE = new ResourceLocation(OverStuffed.MODID, "textures/hud/numbers/number3.png");
    private static final ResourceLocation NUMBER_FOUR = new ResourceLocation(OverStuffed.MODID, "textures/hud/numbers/number4.png");
    private static final ResourceLocation NUMBER_FIVE = new ResourceLocation(OverStuffed.MODID, "textures/hud/numbers/number5.png");
    private static final ResourceLocation NUMBER_SIX = new ResourceLocation(OverStuffed.MODID, "textures/hud/numbers/number6.png");
    private static final ResourceLocation NUMBER_SEVEN = new ResourceLocation(OverStuffed.MODID, "textures/hud/numbers/number7.png");
    private static final ResourceLocation NUMBER_EIGHT = new ResourceLocation(OverStuffed.MODID, "textures/hud/numbers/number8.png");
    private static final ResourceLocation NUMBER_NINE = new ResourceLocation(OverStuffed.MODID, "textures/hud/numbers/number9.png");


    private static final ResourceLocation[] NUMBER_SPRITES = {
            NUMBER_ZERO,
            NUMBER_ONE,
            NUMBER_TWO,
            NUMBER_THREE,
            NUMBER_FOUR,
            NUMBER_FIVE,
            NUMBER_SIX,
            NUMBER_SEVEN,
            NUMBER_EIGHT,
            NUMBER_NINE
    };

    private static long acidTick=0;
    private static int currentAcid=0;

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

        font = Minecraft.getInstance().font;
        PoseStack poseStack=guiGraphics.pose();

        //need this to actually render
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        //New stuffed bar
        int barLeft=left+OverstuffedClientConfig.stuffedHudXOffset.get()+80;
        int barTop=top- OverstuffedClientConfig.stuffedHudYOffset.get();

        renderStomachIcon(gui,guiGraphics,poseStack,barLeft,barTop);
        renderWeightIcon(gui,guiGraphics,poseStack,screenWidth/2-12+ OverstuffedClientConfig.weightDisplayXOffset.get(),top-5+ OverstuffedClientConfig.weightDisplayYOffSet.get());

        if(OverstuffedClientConfig.debugView.get())
        {
            drawDebug(gui,guiGraphics,screenWidth/2,20);
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


    public static void renderWeightIcon(ForgeGui gui,GuiGraphics guiGraphics, PoseStack poseStack,int x, int y)
    {
        int outOf100=(int)((((double)ClientWeightBarData.getPlayerWeight()- OverstuffedClientConfig.getMinWeight())/(OverstuffedClientConfig.maxWeight.get()- OverstuffedClientConfig.getMinWeight()))*100);
        if(outOf100/20==5)
        {
            AbstractDraw(gui,guiGraphics,WEIGHTSTAGESPRITES[4],x,y,24,24);
        }
        else
        {
            AbstractDraw(gui,guiGraphics,WEIGHTSTAGESPRITES[outOf100/20],x,y,24,24);
        }

        //Number Logic;
        int lastStage=ClientWeightBarData.getLastWeightStage();
        Stack<Integer> order=new Stack<Integer>();
        while(lastStage/10!=0)
        {
            order.add(lastStage%10);
        }
        order.add(lastStage%10);
        poseStack.pushPose();
        poseStack.scale(0.33f,0.33f,0.33f);
        for(int i=0;i<order.size();i++)
        {
            AbstractDraw(gui,guiGraphics,NUMBER_SPRITES[order.pop()],(x+20+9*i)*3,(y+14)*3,24,24);

        }
        poseStack.popPose();


    }


    public static void renderStomachIcon(ForgeGui gui,GuiGraphics guiGraphics, PoseStack poseStack,int x, int y)
    {

        int totalIcons=5;

        //because commands can force the maximum higher
        int higherMax=Math.min(OverstuffedWorldConfig.absCalCap.get(),ClientCalorieMeter.getMax());

        int currentIconIndex=Math.min((int)((((double)ClientCalorieMeter.getMax())/higherMax)*5),totalIcons-1);
        AbstractDraw(gui,guiGraphics,STOMACH_ICONS[currentIconIndex],x,y,18,18);

        GL11.glEnable(GL11.GL_STENCIL_TEST); // Turn on da test
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT); // Flush old data

        GL11.glStencilMask(0xFF); // Writing = ON
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF); // Always "add" to frame
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE); // Replace on success
        RenderSystem.colorMask(false, false, false, false);

        AbstractDraw(gui,guiGraphics,STOMACH_MASKS[currentIconIndex],x,y,18,18);


        GL11.glStencilMask(0x00); // Writing = OFF
        GL11.glStencilFunc(GL11.GL_NOTEQUAL, 0, 0xFF); // Anything that wasn't defined above will not be rendered.
        RenderSystem.colorMask(true, true, true, true);

        //Anything rendered here will be cut if goes beyond frame defined before.
        drawAcid(gui,guiGraphics,x,y);

        GL11.glDisable(GL11.GL_STENCIL_TEST); // Turn this shit off!
        //drawContent(guiGraphics);

    }






    private static void drawAcid(ForgeGui gui,GuiGraphics guiGraphics,int x, int y) {

        int acidWidth=42;
        int acidHeight=17;

        //int currentCalories=100;

        int acidOffset=18-(int)((double)ClientCalorieMeter.getCurrentCalories()/ClientCalorieMeter.getMax()*18);


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

        AbstractDraw(gui, guiGraphics,ACID_SPRITES[currentAcid], x, y+acidOffset, acidWidth, acidHeight, acidWidth, acidHeight );

    }

    public static void drawDebug(ForgeGui gui,GuiGraphics guiGraphics,int x, int y)
    {

        ArrayList<Component> info= new ArrayList<Component>();
        info.add(Component.translatable("message.overstuffed.debugcurrentweight",""+ClientWeightBarData.getPlayerWeight()));
        info.add(Component.translatable("message.overstuffed.debugmaxweight",""+ OverstuffedClientConfig.getMaxWeight()));
        info.add(Component.translatable("message.overstuffed.debugminweight",""+ OverstuffedClientConfig.getMinWeight()));
        info.add(Component.translatable("message.overstuffed.debugcurrentcalories",""+ ClientCalorieMeter.getCurrentCalories()));
        info.add(Component.translatable("message.overstuffed.debugmaxcalories",""+
             (ClientCalorieMeter.getMax())));
        info.add(Component.translatable("message.overstuffed.debugnextmax",""+
                ClientCalorieMeter.getCurrentLost()+"/"+ ClientCalorieMeter.getInterval()));

        if(gui.getMinecraft().player!=null)
        {   long countdown=0;
            if(ClientCalorieMeter.getCurrentSavedTick()!=-1)
            {
                 countdown= ClientCalorieMeter.getCurrentDelay()-(gui.getMinecraft().player.tickCount-ClientCalorieMeter.getCurrentSavedTick());
            }
            info.add(Component.translatable("message.overstuffed.debugcalorieclear",""+
                    countdown));
        }

        info.add(Component.translatable("message.overstuffed.debugqueueweight",""+
                ClientWeightBarData.getQueuedWeight()));
        info.add(Component.translatable("message.overstuffed.debugtotalqueueweight",""+
                ClientWeightBarData.getTotalQueuedWeight()));


        for(int i=0;i<info.size();i++)
        {
            guiGraphics.drawCenteredString( font,info.get(i)
                    ,x,y+10*i, Color.RED.hashCode());
        }
    }


}
