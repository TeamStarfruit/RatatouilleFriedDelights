package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;

public class ContinuousFryerBlockEntity extends BlockEntity {
    public ContinuousFryerBlockEntity(BlockPos pos, BlockState state) {
        this(RFDBlockEntityTypes.CONTINUOUS_FRYER.get(), pos, state);
    }

    public ContinuousFryerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
