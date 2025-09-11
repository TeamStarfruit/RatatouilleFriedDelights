package org.starfruit.ratatouillefrieddelights.content.drum_processor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
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
import org.forsteri.ratatouille.entry.CRPartialModels;
import org.jetbrains.annotations.Nullable;
import org.starfruit.ratatouillefrieddelights.entry.RFDPartialModels;

import java.util.function.Consumer;

public class DrumProcessorVisual extends KineticBlockEntityVisual<DrumProcessorBlockEntity> {

    protected final RotatingInstance DrumProcessor;
    final Direction direction;

    public DrumProcessorVisual(VisualizationContext context, DrumProcessorBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise();
        this.direction = (Direction)this.blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        this.DrumProcessor  = (RotatingInstance)instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(RFDPartialModels.DRUM))
                .createInstance();

        this.DrumProcessor.setup(blockEntity,facing.getAxis(), this.getSpeed())
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, this.direction)
                .setChanged();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(DrumProcessor);
    }

    public void update(float partialTick) {
        this.DrumProcessor.setup((KineticBlockEntity)this.blockEntity, this.getSpeed()).setChanged();
    }

    private float getSpeed() {
        return ((DrumProcessorBlockEntity)this.blockEntity).getSpeed();
    }

    @Override
    public void updateLight(float partialTick) {
        BlockPos inFront = pos.relative(direction);
        relight(inFront, DrumProcessor);
    }

    @Override
    protected void _delete() {
        DrumProcessor.delete();
    }
}
