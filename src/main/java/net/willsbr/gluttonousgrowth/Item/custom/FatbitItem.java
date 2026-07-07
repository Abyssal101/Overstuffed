package net.willsbr.gluttonousgrowth.Item.custom;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.willsbr.gluttonousgrowth.Renderer.FatbitItemRenderer;

import java.util.function.Consumer;

/**
 * The fatbit item. Its geometry and overlay are drawn by a custom BEWLR ({@link FatbitItemRenderer}),
 * supplied here through the client-only IClientItemExtensions hook. The lambda body is only ever
 * loaded/run on the client dist, so referencing the client renderer class here is dedicated-server safe.
 */
public class FatbitItem extends Item {

    public FatbitItem(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                // Built lazily so the Minecraft-instance lookups in the renderer run at render time.
                if (renderer == null) {
                    renderer = new FatbitItemRenderer();
                }
                return renderer;
            }
        });
    }
}
