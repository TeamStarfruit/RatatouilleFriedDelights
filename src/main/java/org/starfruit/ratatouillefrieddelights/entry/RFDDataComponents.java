package org.starfruit.ratatouillefrieddelights.entry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class RFDDataComponents {
    private static final String DIP_COLOR_TAG = "DipColor";

    private RFDDataComponents() {
    }

    public static boolean hasDipColor(ItemStack stack) {
        return stack.hasTag() && stack.getTag() != null && stack.getTag().contains(DIP_COLOR_TAG);
    }

    public static int getDipColor(ItemStack stack, int defaultColor) {
        if (!hasDipColor(stack))
            return defaultColor;
        CompoundTag tag = stack.getTag();
        return tag != null ? tag.getInt(DIP_COLOR_TAG) : defaultColor;
    }

    public static void setDipColor(ItemStack stack, int color) {
        stack.getOrCreateTag().putInt(DIP_COLOR_TAG, color);
    }
}
