package net.willsbr.gluttonousgrowth.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.willsbr.gluttonousgrowth.GluttonousGrowth;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> GOLDEN_DIET_FOODS = tag("golden_diet_foods");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(GluttonousGrowth.MODID, name));
        }
    }
}