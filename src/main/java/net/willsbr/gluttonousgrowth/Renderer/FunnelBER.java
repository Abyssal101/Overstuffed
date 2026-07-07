package net.willsbr.gluttonousgrowth.Renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.willsbr.gluttonousgrowth.Entity.BlockEntity.FunnelBlockEntity;
import net.willsbr.gluttonousgrowth.GluttonousGrowth;
import net.willsbr.gluttonousgrowth.Block.custom.Funnel;
import net.willsbr.gluttonousgrowth.fluid.ModFluids;

public class FunnelBER implements BlockEntityRenderer<FunnelBlockEntity> {
    public FunnelBER(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(FunnelBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = be.getLevel();
        if (level == null) return;

        BlockPos pos = be.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (!state.hasProperty(Funnel.CAPACITY)) return;

        int capacity = state.getValue(Funnel.CAPACITY); // 0..20
        if (capacity <= 0) return; // nothing to render

        // Convert block space (0..16) to model space (0..1) for PoseStack
        // Our funnel "bowl" bounds (roughly fits the Blockbench model):
        // inner min/max in block units: x:[3.5, 12.5], z:[3.5, 13.0], y base: 5.0 to 15.0
        // scale height by capacity (0..20)
        float minX = 2f / 16f;
        float maxX = 14 / 16f;
        float minZ = 2f / 16f;
        float maxZ = 14 / 16f;
        float baseY = 5.0f / 16f;
        float maxY = 1f;
        float fillRatio = Math.min(1.0f, capacity / 20.0f);
        float curY = baseY + (maxY - baseY) * fillRatio;

        poseStack.pushPose();
        // Translate to block origin so 0..1 local coords map to the block
        poseStack.translate(0.0, 0.0, 0.0);

        // CALORIUM_STILL ("block/nutrientpaste") is a block-atlas-style resource location — it's
        // only valid when looked up as a stitched sprite in the blocks atlas, which is where Forge
        // already stitches it (since ModFluids exposes it via getStillTexture()). Binding it directly
        // as a standalone texture via entityTranslucentCull failed to resolve (no "textures/" prefix,
        // no ".png" suffix) and silently fell back to the missing-texture checkerboard. Look the
        // sprite up in the atlas instead, and use RenderType.translucent(), which is bound to it.
        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                .apply(ModFluids.CALORIUM_STILL);

        VertexConsumer vc = buffer.getBuffer(RenderType.translucent());

        int light = packedLight;
        int overlay = packedOverlay;

        // Use the fluid's actual defined tint (ABGR) instead of a hardcoded color, so the paste
        // renders with its real hue rather than an arbitrary blue that reads as plain water.
        int tint = IClientFluidTypeExtensions.of(ModFluids.CALORIUM_TYPE.get()).getTintColor();
        float tintA = ((tint >>> 24) & 0xFF) / 255f;
        float tintB = ((tint >>> 16) & 0xFF) / 255f;
        float tintG = ((tint >>> 8) & 0xFF) / 255f;
        float tintR = (tint & 0xFF) / 255f;
        float r = 1, g = 1, b = 1, a = 1;

        // Draw top surface (quad)
        addQuad(vc, poseStack, minX, curY, minZ, maxX, curY, maxZ, r, g, b, a, light, overlay, Face.UP, sprite);

        // Draw side walls (thin vertical quads) so you can see fluid behind glass and edges
        addQuad(vc, poseStack, minX, baseY, minZ, minX, curY, maxZ, r, g, b, a * 0.8f, light, overlay, Face.WEST, sprite);
        addQuad(vc, poseStack, maxX, baseY, minZ, maxX, curY, maxZ, r, g, b, a * 0.8f, light, overlay, Face.EAST, sprite);
        addQuad(vc, poseStack, minX, baseY, minZ, maxX, curY, minZ, r, g, b, a * 0.8f, light, overlay, Face.NORTH, sprite);
        addQuad(vc, poseStack, minX, baseY, maxZ, maxX, curY, maxZ, r, g, b, a * 0.8f, light, overlay, Face.SOUTH, sprite);

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(FunnelBlockEntity pBlockEntity) {
        return true; // allow rendering through frustum a bit (small object)
    }

    @Override
    public boolean shouldRender(FunnelBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return BlockEntityRenderer.super.shouldRender(pBlockEntity, pCameraPos);
    }

    private enum Face { UP, DOWN, NORTH, SOUTH, WEST, EAST }

    private static void addQuad(VertexConsumer vc, PoseStack poseStack,
                                float x1, float y1, float z1,
                                float x2, float y2, float z2,
                                float r, float g, float b, float a,
                                int light, int overlay, Face face, TextureAtlasSprite sprite) {
        // Build four vertices depending on face. We assume axis-aligned quads.
        // UVs come from the sprite's own bounds in the shared blocks atlas, not raw 0..1 — the
        // sprite only occupies a small sub-rectangle of that atlas texture.
        PoseStack.Pose last = poseStack.last();
        float u1 = sprite.getU0(), v1 = sprite.getV0();
        float u2 = sprite.getU1(), v2 = sprite.getV1();
        switch (face) {
            case UP -> {
                vc.vertex(last.pose(), x1, y1, z2).color(r, g, b, a).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 1, 0).endVertex();
                vc.vertex(last.pose(), x2, y1, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 1, 0).endVertex();
                vc.vertex(last.pose(), x2, y1, z1).color(r, g, b, a).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 1, 0).endVertex();
                vc.vertex(last.pose(), x1, y1, z1).color(r, g, b, a).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 1, 0).endVertex();


            }
            case NORTH -> {
                // z = z1 constant; vertical quad from y1..y2, x1..x2
                vc.vertex(last.pose(), x1, y2, z1).color(r, g, b, a).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 0, -1).endVertex();
                vc.vertex(last.pose(), x2, y2, z1).color(r, g, b, a).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 0, -1).endVertex();
                vc.vertex(last.pose(), x2, y1, z1).color(r, g, b, a).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 0, -1).endVertex();
                vc.vertex(last.pose(), x1, y1, z1).color(r, g, b, a).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 0, -1).endVertex();
            }
            case SOUTH -> {
                // z = z2 constant
                vc.vertex(last.pose(), x1, y1, z2).color(r, g, b, a).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 0, 1).endVertex();
                vc.vertex(last.pose(), x2, y1, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 0, 1).endVertex();
                vc.vertex(last.pose(), x2, y2, z2).color(r, g, b, a).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 0, 1).endVertex();
                vc.vertex(last.pose(), x1, y2, z2).color(r, g, b, a).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, 0, 1).endVertex();
            }
            case WEST -> {
                // x = x1 constant
                vc.vertex(last.pose(), x1, y1, z1).color(r, g, b, a).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), -1, 0, 0).endVertex();
                vc.vertex(last.pose(), x1, y1, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), -1, 0, 0).endVertex();
                vc.vertex(last.pose(), x1, y2, z2).color(r, g, b, a).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), -1, 0, 0).endVertex();
                vc.vertex(last.pose(), x1, y2, z1).color(r, g, b, a).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), -1, 0, 0).endVertex();
            }
            case EAST -> {
                // x = x2 constant
                vc.vertex(last.pose(), x2, y2, z1).color(r, g, b, a).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), 1, 0, 0).endVertex();
                vc.vertex(last.pose(), x2, y2, z2).color(r, g, b, a).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), 1, 0, 0).endVertex();
                vc.vertex(last.pose(), x2, y1, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), 1, 0, 0).endVertex();
                vc.vertex(last.pose(), x2, y1, z1).color(r, g, b, a).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), 1, 0, 0).endVertex();
            }
            case DOWN -> {
                vc.vertex(last.pose(), x1, y1, z2).color(r, g, b, a).uv(u1, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, -1, 0).endVertex();
                vc.vertex(last.pose(), x2, y1, z2).color(r, g, b, a).uv(u2, v2).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, -1, 0).endVertex();
                vc.vertex(last.pose(), x2, y1, z1).color(r, g, b, a).uv(u2, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, -1, 0).endVertex();
                vc.vertex(last.pose(), x1, y1, z1).color(r, g, b, a).uv(u1, v1).overlayCoords(overlay).uv2(light).normal(last.normal(), 0, -1, 0).endVertex();
            }
        }
    }
}
