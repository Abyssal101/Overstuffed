package net.willsbr.overstuffed.Block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.overstuffed.Block.custom.Scale;
import net.willsbr.overstuffed.Item.ModCreativeModeTabs;
import net.willsbr.overstuffed.Item.ModItems;
import net.willsbr.overstuffed.OverStuffed;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS= DeferredRegister.create(ForgeRegistries.BLOCKS, OverStuffed.MODID);
    
   public static final RegistryObject<Block> SCALE=registerBlock("scale",
           () ->new Scale(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3f,3f).dynamicShape().noOcclusion()));

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block)
    {


        RegistryObject<T> toReturn=BLOCKS.register(name,block);
        registerBlockItem(name,toReturn);
        return toReturn;
    }
    public static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block)
    {
        return ModItems.ITEMS.register(name,() ->new BlockItem(block.get(),new Item.Properties()));
    }


    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }

}
