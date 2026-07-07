package net.willsbr.gluttonousgrowth.Item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Backing store for whatever the fatbit's "screen" draws. Deliberately minimal for this version:
 * a list of text lines stored in item NBT, so the drawable surface is genuinely data-driven rather
 * than hardcoded in the renderer. The eventual goal (shapes and per-element sizing) can be layered
 * on by extending this data model without changing the renderer's contract.
 */
public class FatbitData {
    private static final String ROOT = "gluttonousgrowth_fatbit";
    private static final String LINES = "lines";

    // Shown when the item has no drawn content yet, so a freshly crafted fatbit still displays something.
    private static final List<String> DEFAULT = List.of("Fatbit V1.0");

    public static List<String> getLines(ItemStack stack) {
        CompoundTag tag = stack.getTagElement(ROOT);
        if (tag == null || !tag.contains(LINES, Tag.TAG_LIST)) {
            return DEFAULT;
        }
        ListTag list = tag.getList(LINES, Tag.TAG_STRING);
        if (list.isEmpty()) {
            return DEFAULT;
        }
        List<String> out = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            out.add(list.getString(i));
        }
        return out;
    }

    public static void setLines(ItemStack stack, List<String> lines) {
        CompoundTag root = stack.getOrCreateTagElement(ROOT);
        ListTag list = new ListTag();
        for (String line : lines) {
            list.add(StringTag.valueOf(line));
        }
        root.put(LINES, list);
    }
}
