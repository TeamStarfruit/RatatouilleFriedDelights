package org.starfruit.ratatouillefrieddelights.content.dipcup;

import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.starfruit.ratatouillefrieddelights.entry.RFDDataComponents;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DipableItem extends Item {
    public DipableItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();

        if (state.getBlock() instanceof DipCupBlock dipCup && state.getValue(DipCupBlock.OPENED)) {
            int remaining = state.getValue(DipCupBlock.REMAINING_DIP);
            if (remaining == 0) return InteractionResult.FAIL;

            // 取一个物品
            ItemStack dipped = stack.copy();
            dipped.setCount(1);

            int newColor = dipCup.dipColor;
            int finalColor = newColor;

            if (dipped.has(RFDDataComponents.DIP_COLOR)) {
                int oldColor = dipped.getOrDefault(RFDDataComponents.DIP_COLOR, 0xFFFFFFFF);
                finalColor = mixColorRGBA(oldColor, newColor);
            }

            dipped.set(RFDDataComponents.DIP_COLOR, finalColor);

            stack.shrink(1);
            if (context.getPlayer() != null && !context.getPlayer().addItem(dipped)) {
                context.getPlayer().drop(dipped, false);
            }

            level.setBlockAndUpdate(pos, state.setValue(DipCupBlock.REMAINING_DIP, remaining - 1));
            level.playSound(null, pos, SoundEvents.HONEY_DRINK,
                    SoundSource.BLOCKS,
                    0.5F + level.random.nextFloat() * 0.3F,
                    0.9F + level.random.nextFloat() * 0.2F);

            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    private static int mixColorRGBA(int c1, int c2) {
        int a1 = (c1 >> 24) & 0xFF;
        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = c1 & 0xFF;

        int a2 = (c2 >> 24) & 0xFF;
        int r2 = (c2 >> 16) & 0xFF;
        int g2 = (c2 >> 8) & 0xFF;
        int b2 = c2 & 0xFF;

        int a = (a1 + a2) / 2;
        int r = (r1 + r2) / 2;
        int g = (g1 + g2) / 2;
        int b = (b1 + b2) / 2;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

}
