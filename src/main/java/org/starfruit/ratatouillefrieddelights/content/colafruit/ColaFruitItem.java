package org.starfruit.ratatouillefrieddelights.content.colafruit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlocks;

public class ColaFruitItem extends Item {
    public ColaFruitItem(Properties props) { super(props); }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos clicked = ctx.getClickedPos();
        Direction face = ctx.getClickedFace();

        // 允许两种交互：
        // A) 对着“叶子方块的底面”（face == DOWN）使用
        // B) 对着“叶子下面的空气方块的侧/顶面”使用（容错）
        BlockPos targetPos = null;

        BlockState clickedState = level.getBlockState(clicked);
        boolean isLeaf = clickedState.is(RFDBlocks.COLA_LEAVES.get());

        if (isLeaf && face == Direction.DOWN) {
            targetPos = clicked.below();
        } else {
            // 如果点到的是叶子下面的空气，也允许放置
            BlockPos maybeLeafAbove = (level.getBlockState(clicked).isAir()) ? clicked.above() : null;
            if (maybeLeafAbove != null && level.getBlockState(maybeLeafAbove).is(RFDBlocks.COLA_LEAVES.get())) {
                targetPos = clicked;
            }
        }

        if (targetPos == null) return InteractionResult.PASS;

        // 目标格必须是空气，且果实能存活（上方是 cola_leaves）
        if (!level.getBlockState(targetPos).isAir()) return InteractionResult.FAIL;
        BlockState placeState = RFDBlocks.COLA_FRUIT_BLOCK.get().defaultBlockState()
                .setValue(ColaFruitBlock.AGE, 0);

        if (!placeState.canSurvive(level, targetPos)) return InteractionResult.FAIL;

        if (!level.isClientSide) {
            level.setBlock(targetPos, placeState, 3); // 更新+通知
            level.playSound(null, targetPos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 0.8f, 1.0f);
            if (!ctx.getPlayer().getAbilities().instabuild) {
                ctx.getItemInHand().shrink(1);
            }
            // 可选：触发游戏事件/粒子
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}