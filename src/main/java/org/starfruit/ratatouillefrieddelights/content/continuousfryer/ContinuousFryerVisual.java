package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import org.starfruit.ratatouillefrieddelights.entry.RFDPartialModels;

import java.util.function.Consumer;

public class ContinuousFryerVisual extends KineticBlockEntityVisual<ContinuousFryerBlockEntity> {

    protected final @Nullable RotatingInstance axis;
    final Direction direction;

    public ContinuousFryerVisual(VisualizationContext context, ContinuousFryerBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise();
        this.direction = (Direction)this.blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        if (blockEntity.shouldRenderAxis()) {
            this.axis  = (RotatingInstance)instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(RFDPartialModels.FRYER_AXIS))
                    .createInstance();
            this.axis.setup(blockEntity,facing.getAxis(), this.blockEntity.getSpeed())
                    .setPosition(getVisualPosition())
                    .rotateToFace(Direction.SOUTH, this.direction)
                    .setChanged();
        } else {
            axis = null;
        }
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        if (axis != null) consumer.accept(axis);
    }

    @Override
    public void update(float partialTick) {
        if (axis != null) this.axis.setup(this.blockEntity, this.blockEntity.getSpeed()).setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        BlockPos inFront = pos.relative(direction);
        if (axis != null) relight(inFront, axis);
    }

    @Override
    protected void _delete() {
        if (axis != null) axis.delete();
    }
}
