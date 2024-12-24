package net.willsbr.overstuffed.Item;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.overstuffed.Block.ModBlocks;
import net.willsbr.overstuffed.OverStuffed;

public class ModCreativeModeTab extends CreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, OverStuffed.MODID);
    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("overstuffed_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.CAKE))
                    .title(Component.translatable("itemGroup.overstuffed"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.SCALE.get());
                    })
                    .build());


    public static void register(IEventBus eventBus)
    {

        CREATIVE_MODE_TABS.register(eventBus);
    }




    public ModCreativeModeTab(int place, String label) {
        super(CreativeModeTab.builder());
    }


    @Override
    public ItemStack getIconItem() {
        return new ItemStack(Items.CAKE);
    }

}


