package org.starfruit.ratatouillefrieddelights.content.dipcup;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DipColor implements ItemColor {
    @Override
    public int getColor(@NotNull ItemStack itemStack, int i) {
        CompoundTag itemData = itemStack.getOrCreateTag();
        if (itemData.contains("dip_color")) {
            if (i == 0) return 0x00FFFFFF;
            if (i == 1) return 0xFFFFFFFF;
            return DipCupBlock.rgbaToArgb(itemData.getInt("dip_color"));
        } else {
            return i == 0 ? 0xFFFFFFFF : 0x00FFFFFF;
        }

    }
}
