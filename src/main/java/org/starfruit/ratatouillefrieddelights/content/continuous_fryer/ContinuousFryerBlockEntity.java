package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;

import java.util.List;

public class ContinuousFryerBlockEntity extends SmartBlockEntity {
    public ContinuousFryerBlockEntity(BlockPos pos, BlockState state) {
        this(RFDBlockEntityTypes.CONTINUOUS_FRYER.get(), pos, state);
    }

    public ContinuousFryerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
