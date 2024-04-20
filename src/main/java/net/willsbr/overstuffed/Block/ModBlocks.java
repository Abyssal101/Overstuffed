package net.willsbr.overstuffed.Block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.overstuffed.Block.custom.ZirconLampBlock;
import net.willsbr.overstuffed.Item.ModCreativeModeTab;
import net.willsbr.overstuffed.Item.ModItems;
import net.willsbr.overstuffed.OverStuffed;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS= DeferredRegister.create(ForgeRegistries.BLOCKS, OverStuffed.MODID);


   public static final RegistryObject<Block> ZIRCON_LAMP = registerBlock("zircon_lamp",() -> new ZirconLampBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(6f).lightLevel(state ->state.getValue(ZirconLampBlock.LIT) ? 15 : 0)), ModCreativeModeTab.instance);
    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab)
    {
        RegistryObject<T> toReturn=BLOCKS.register(name,block);
        registerBlockItem(name,toReturn,tab);
        return toReturn;
    }
    public static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block, CreativeModeTab tab)
    {
       // return ModItems.ITEMS.register(name,() ->new BlockItem(block.get(),new Item.Properties().tab(tab)));
        //there is no tab anymore
        return ModItems.ITEMS.register(name,() ->new BlockItem(block.get(),new Item.Properties()));
    }


    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }

}
