package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.processing.burner.ScrollInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.starfruit.ratatouillefrieddelights.entry.RFDPartialModels;
import org.starfruit.ratatouillefrieddelights.entry.RFDSpriteShifts;

import java.util.function.Consumer;

public class ContinuousFryerVisual extends KineticBlockEntityVisual<ContinuousFryerBlockEntity> {
    public static final float MAGIC_SCROLL_MULTIPLIER = 1f / (31.5f * 16f);
    public static final float SCROLL_FACTOR_OTHERWISE = 0.5f;
    public static final float SCROLL_OFFSET_OTHERWISE = 0f;

    protected final @Nullable RotatingInstance axis;
    final Direction direction;
    protected final ScrollInstance belt;

    public ContinuousFryerVisual(VisualizationContext context, ContinuousFryerBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(BlockStateProperties.HORIZONTAL_FACING).getClockWise();

        Instancer<ScrollInstance> beltModel = instancerProvider()
                .instancer(AllInstanceTypes.SCROLLING, Models.partial(RFDPartialModels.FRYER_BELT));

        belt = setup(beltModel.createInstance());

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
        consumer.accept(belt);
    }

    @Override
    public void update(float partialTick) {
        if (axis != null) this.axis.setup(this.blockEntity, this.blockEntity.getSpeed()).setChanged();
        setup(belt);
    }

    @Override
    public void updateLight(float partialTick) {
        BlockPos inFront = pos.relative(direction);
        if (axis != null) relight(inFront, axis);
        relight(belt);
    }

    @Override
    protected void _delete() {
        if (axis != null) axis.delete();
        belt.delete();
    }

    private ScrollInstance setup(ScrollInstance key) {
        Direction facing = blockState.getValue(ContinuousFryerBlock.HORIZONTAL_FACING).getAxis()
                == Direction.Axis.X
                ? Direction.EAST
                : Direction.SOUTH;
        boolean alongX = facing.getAxis() == Direction.Axis.X;

        float speed = blockEntity.getSpeed();
        if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE ^ (alongX)) {
            speed = -speed;
        }
        speed = -speed;
        float rotX = 0;
        float rotY = facing.toYRot() + (alongX ? 180 : 0);
        float rotZ = 0;

        Quaternionf q = new Quaternionf().rotationXYZ(rotX * Mth.DEG_TO_RAD, rotY * Mth.DEG_TO_RAD, rotZ * Mth.DEG_TO_RAD);

        key.setSpriteShift(RFDSpriteShifts.FRYER_BELT, 1f, SCROLL_FACTOR_OTHERWISE)
                .position(getVisualPosition())
                .rotation(q)
                .speed(0, speed * MAGIC_SCROLL_MULTIPLIER)
                .offset(0, SCROLL_OFFSET_OTHERWISE)
                .colorRgb(RotatingInstance.colorFromBE(blockEntity))
                .setChanged();

        return key;
    }
}
