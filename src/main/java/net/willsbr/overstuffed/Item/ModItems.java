package net.willsbr.overstuffed.Item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.overstuffed.Item.custom.EightBallItem;
import net.willsbr.overstuffed.OverStuffed;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS=DeferredRegister.create(ForgeRegistries.ITEMS, OverStuffed.MODID);


    public static final RegistryObject<Item> ZIRCON=ITEMS.register("zircon",() -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_ZIRCON=ITEMS.register("raw_zircon",() -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EIGHT_BALL=ITEMS.register("eight_ball",() -> new EightBallItem(new Item.Properties().stacksTo(1)));

    //personal testing
    //public static final RegistryObject<Item> EXPLOSIVE_BOW=ITEMS.register("explosive_bow",() -> new ExplosiveBowItem(new Item.Properties().tab(ModCreativeModeTab.TUTORIAL_TAB).defaultDurability(10).stacksTo(1)));

    public static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
