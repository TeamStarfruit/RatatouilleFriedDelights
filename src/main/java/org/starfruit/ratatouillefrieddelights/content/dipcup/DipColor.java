package org.starfruit.ratatouillefrieddelights.content.dipcup;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.starfruit.ratatouillefrieddelights.entry.RFDDataComponents;

public class DipColor implements ItemColor {
    @Override
    public int getColor(@NotNull ItemStack itemStack, int i) {
        if (itemStack.has(RFDDataComponents.DIP_COLOR)) {
            if (i == 0) return 0x00FFFFFF;
            if (i == 1) return 0xFFFFFFFF;
            return DipCupBlock.rgbaToArgb(itemStack.getOrDefault(RFDDataComponents.DIP_COLOR, 0xFFFFFF00));
        } else {
            return i == 0 ? 0xFFFFFFFF : 0x00FFFFFF;
        }

    }
}
