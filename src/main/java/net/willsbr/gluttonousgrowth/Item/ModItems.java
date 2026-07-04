package net.willsbr.gluttonousgrowth.Item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.gluttonousgrowth.GluttonousGrowth;
import net.willsbr.gluttonousgrowth.fluid.ModFluids;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS, GluttonousGrowth.MODID);

    // Calorium bucket — intentionally NOT added to any creative tab (your custom tab doesn't list it)
    public static final RegistryObject<Item> CALORIUM_BUCKET = ITEMS.register("calorium_bucket",
            () -> new BucketItem(ModFluids.CALORIUM,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
