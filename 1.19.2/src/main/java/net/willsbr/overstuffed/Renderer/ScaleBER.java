package net.willsbr.overstuffed.Renderer;

import com.ibm.icu.impl.IllegalIcuArgumentException;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.willsbr.overstuffed.Block.ModBlocks;
import net.willsbr.overstuffed.Entity.BlockEntity.ScaleBlockEntity;

import java.awt.*;

public class ScaleBER implements BlockEntityRenderer<ScaleBlockEntity> {

    private final BlockEntityRendererProvider.Context context;
    private int displayWeight;


    public ScaleBER(BlockEntityRendererProvider.Context context)
    {
        this.context=context;
    }
    @Override
    public void render(ScaleBlockEntity pBlockEntity, float pPartialTick, PoseStack poseStack,
                       MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay)
    {
        //THIS IS ON THE CLIENT!!!!!
        poseStack.pushPose();
        Font font=this.context.getFont();
        Level level= Minecraft.getInstance().level;
        BlockPos pos=pBlockEntity.getBlockPos().above();


        displayWeight= pBlockEntity.getDisplayWeight();

        int packedLight= LightTexture.pack(level.getBrightness(LightLayer.BLOCK,pos), level.getBrightness(LightLayer.SKY,pos));

        //So game doesn't crash when you breakthe block

            if(level.getBlockState(pBlockEntity.getBlockPos()).is(ModBlocks.SCALE.get()))
            {

                Direction FACING=level.getBlockState(pBlockEntity.getBlockPos()).getValue(BlockStateProperties.HORIZONTAL_FACING);
                poseStack.scale(0.008f,-0.01f,0.01f);
                if(FACING.equals(Direction.NORTH))
                {
                    poseStack.translate(52f,-122,8f);
                    poseStack.mulPose(Vector3f.XN.rotationDegrees(-45));
                }
                else if((FACING.equals(Direction.SOUTH)))
                {

                    poseStack.translate(75,-122,95);
                    poseStack.mulPose(Vector3f.XN.rotationDegrees(45));
                    poseStack.mulPose(Vector3f.YN.rotationDegrees(180));
                }
                else if(FACING.equals(Direction.EAST))
                {

                    poseStack.translate(118,-122,40);
                    poseStack.mulPose(Vector3f.YN.rotationDegrees(90));
                    poseStack.mulPose(Vector3f.XN.rotationDegrees(-47));
                }
                else {
                    poseStack.translate(8,-122,60);
                    poseStack.mulPose(Vector3f.YN.rotationDegrees(270));
                    poseStack.mulPose(Vector3f.XN.rotationDegrees(-47));
                }
            }


        if(displayWeight==0)
        {
            font.drawInBatch("0",0f,0f, Color.RED.hashCode(),false,poseStack.last().pose(),pBufferSource,
                    false,0,packedLight);
        }
        else if(displayWeight>0 && displayWeight<1000)
        {
            font.drawInBatch(""+displayWeight,0f,0f, Color.RED.hashCode(),false,poseStack.last().pose(),pBufferSource,
                    false,0,packedLight);
        }
        else
        {
            poseStack.translate(-4f,0,0);

            font.drawInBatch("ERROR",0f,0f, Color.RED.hashCode(),false,poseStack.last().pose(),pBufferSource,
                    false,0,packedLight);
        }


        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(ScaleBlockEntity pBlockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(pBlockEntity);
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(ScaleBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return BlockEntityRenderer.super.shouldRender(pBlockEntity, pCameraPos);
    }

    public void setWeight(int i)
    {
        displayWeight =i;
    }





}
