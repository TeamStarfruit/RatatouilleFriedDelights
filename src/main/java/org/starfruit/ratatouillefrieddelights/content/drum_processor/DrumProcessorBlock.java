package org.starfruit.ratatouillefrieddelights.content.drum_processor;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;


public class DrumProcessorBlock extends HorizontalKineticBlock implements IBE<DrumProcessorBlockEntity> {
    public DrumProcessorBlock(Properties properties) {super(properties);}

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return getRotationAxis(state) == face.getAxis();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getClockWise()
                .getAxis();
    }

    @Override
    public Class<DrumProcessorBlockEntity> getBlockEntityClass() {
        return DrumProcessorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DrumProcessorBlockEntity> getBlockEntityType() {
        return RFDBlockEntityTypes.DRUM_PROCESSOR.get();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        boolean isZ = pState.getValue(HORIZONTAL_FACING).getAxis() == Direction.Axis.Z;
        return Shapes.or(
                Shapes.create(0, 0, 0, 1, 2 / 16f, 1),
                Shapes.create(isZ ? 0 : 1 / 16f, 2 / 16f, isZ ? 1 / 16f : 0, isZ ? 1 : 15 / 16f, 15 / 16f, isZ ? 15 / 16f : 1)
        );
    }
}
