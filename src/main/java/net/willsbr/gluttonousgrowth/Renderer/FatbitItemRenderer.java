package net.willsbr.gluttonousgrowth.Renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.willsbr.gluttonousgrowth.GluttonousGrowth;
import net.willsbr.gluttonousgrowth.Item.FatbitData;

import java.util.List;

/**
 * Custom item renderer (BEWLR) for the fatbit. It always draws the placeholder base model, and only
 * while the item is actually held in a hand does it draw the "screen" overlay on top of the model.
 */
public class FatbitItemRenderer extends BlockEntityWithoutLevelRenderer {


    public static final ModelResourceLocation FATBIT_BASE_MODEL =
            new ModelResourceLocation(GluttonousGrowth.MODID, "fatbitbase", "inventory");
    // → models/item/gizmo_base.json

    public FatbitItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ctx, PoseStack pose,
                             MultiBufferSource buffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();


        BakedModel baseModel = mc.getModelManager().getModel(FATBIT_BASE_MODEL);
        RenderType renderType = ItemBlockRenderTypes.getRenderType(stack, true);
        VertexConsumer baseBuffer = ItemRenderer.getFoilBufferDirect(buffer, renderType, true, stack.hasFoil());
        mc.getItemRenderer().renderModelLists(baseModel, stack, light, overlay, pose, baseBuffer);

        // The drawable overlay only appears while the item is genuinely held in a hand.
        if (isHeldInHand(ctx)) {
            renderOverlay(mc, stack, pose, buffer, light);
        }
    }

    private static boolean isHeldInHand(ItemDisplayContext ctx) {
        return ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || ctx == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || ctx == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
    }

    /**
     * Draws the fatbit's content flat onto the front face of the item. For this version that is just
     * centered text lines; because the content comes from {@link FatbitData}, varying text (and later
     * shapes/sizes) can be added without touching this render path.
     */
    private static void renderOverlay(Minecraft mc, ItemStack stack, PoseStack pose,
                                      MultiBufferSource buffer, int light) {
        List<String> lines = FatbitData.getLines(stack);
        if (lines.isEmpty()) {
            return;
        }
        Font font = mc.font;

        pose.pushPose();

        pose.translate(0.4, 0.26, 0.5);
        // Vanilla fonts are authored large (a line is ~9 units tall); this scale shrinks them onto
        // the item face, and the negative Y flips the font's top-down axis into model space.
        float scale = 0.004F;
        pose.scale(scale, -scale, scale);

        int totalHeight = lines.size() * 9;
        int y = -totalHeight / 2;
        for (String line : lines) {
            font.drawInBatch(line, 0, (float) y, 0xFFFFFFFF, false, pose.last().pose(), buffer,
                    Font.DisplayMode.POLYGON_OFFSET, 0, light);
            y += 9;
        }
        pose.popPose();
    }
}
