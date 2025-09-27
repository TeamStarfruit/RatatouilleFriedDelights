package org.starfruit.ratatouillefrieddelights.worldgen.tree.deco;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.starfruit.ratatouillefrieddelights.content.colafruit.ColaFruitBlock;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlocks;

public class ColaFruitDecorator extends TreeDecorator {

    // 1) 现在用 MapCodec（不是 Codec）
    public static final MapCodec<ColaFruitDecorator> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            com.mojang.serialization.Codec.FLOAT.fieldOf("chance").forGetter(d -> d.chance),
            com.mojang.serialization.Codec.INT.fieldOf("max_per_tree").forGetter(d -> d.maxPerTree)
    ).apply(inst, ColaFruitDecorator::new));

    private final float chance;
    private final int maxPerTree;

    public ColaFruitDecorator(float chance, int maxPerTree) {
        this.chance = chance;
        this.maxPerTree = maxPerTree;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        // 2) 这里返回我们注册的类型（见下一个类）
        return RFDTreeDecoratorTypes.COLA_FRUIT.get();
    }

    @Override
    public void place(Context ctx) {
        RandomSource rand = ctx.random();
        int placed = 0;

        for (BlockPos leafPos : ctx.leaves()) {
            if (placed >= maxPerTree) break;
            if (rand.nextFloat() > chance) continue;

            BlockPos below = leafPos.below();

            // 3) 用 ctx.isAir / ctx.setBlock（不是 level.isAir / level.setBlock）
            if (!ctx.isAir(below)) continue;

            // 上方就是叶子（ctx.leaves() 已保证 leafPos 是叶子），因此能“存活”的条件已满足
            BlockState fruit = RFDBlocks.COLA_FRUIT_BLOCK.get()
                    .defaultBlockState()
                    .setValue(ColaFruitBlock.AGE, 0);

            ctx.setBlock(below, fruit);
            placed++;
        }
    }
}
