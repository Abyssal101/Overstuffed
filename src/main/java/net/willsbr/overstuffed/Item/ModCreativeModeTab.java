package net.willsbr.overstuffed.Item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.willsbr.overstuffed.OverStuffed;

public class ModCreativeModeTab extends CreativeModeTab {
    public static final ModCreativeModeTab instance = new ModCreativeModeTab(CreativeModeTab.TABS.length, "overstuffed");



    public ModCreativeModeTab(int place, String label) {
        super(place, label);
    }


    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Items.CAKE);

    }
}

