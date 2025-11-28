package org.starfruit.ratatouillefrieddelights.content.dipcup;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.starfruit.ratatouillefrieddelights.entry.RFDDataComponents;

public class DipColor implements ItemColor {
    @Override
    public int getColor(@NotNull ItemStack itemStack, int i) {
        if (RFDDataComponents.hasDipColor(itemStack)) {
            if (i == 0) return 0x00FFFFFF;
            if (i == 1) return 0xFFFFFFFF;
            return DipCupBlock.rgbaToArgb(RFDDataComponents.getDipColor(itemStack, 0xFFFFFF00));
        } else {
            return i == 0 ? 0xFFFFFFFF : 0x00FFFFFF;
        }

    }
}
