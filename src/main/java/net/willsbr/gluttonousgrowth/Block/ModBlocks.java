package net.willsbr.gluttonousgrowth.Block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.willsbr.gluttonousgrowth.Block.custom.Scale;
import net.willsbr.gluttonousgrowth.Item.ModItems;
import net.willsbr.gluttonousgrowth.GluttonousGrowth;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS= DeferredRegister.create(ForgeRegistries.BLOCKS, GluttonousGrowth.MODID);

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
