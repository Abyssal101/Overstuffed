package net.willsbr.gluttonousgrowth.Item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.gluttonousgrowth.GluttonousGrowth;
import net.willsbr.gluttonousgrowth.Item.custom.FatbitItem;
import net.willsbr.gluttonousgrowth.fluid.ModFluids;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS, GluttonousGrowth.MODID);

    // Calorium bucket, Not ready to be utilized, but needs to be registered for textures
    public static final RegistryObject<Item> CALORIUM_BUCKET = ITEMS.register("calorium_bucket",
            () -> new BucketItem(ModFluids.CALORIUM,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    // Drawable-surface item rendered by a custom BEWLR (see FatbitItem / FatbitItemRenderer).
    public static final RegistryObject<Item> FATBIT = ITEMS.register("fatbit",
            () -> new FatbitItem(new Item.Properties()));

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
