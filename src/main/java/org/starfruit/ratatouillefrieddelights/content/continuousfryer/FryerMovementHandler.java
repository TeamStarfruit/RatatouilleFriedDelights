package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.armor.CardboardArmorHandler;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltPart;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import com.simibubi.create.content.kinetics.belt.transport.BeltMovementHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.simibubi.create.content.kinetics.belt.transport.BeltMovementHandler.shouldIgnoreBlocking;
import static net.minecraft.core.Direction.AxisDirection.NEGATIVE;
import static net.minecraft.core.Direction.AxisDirection.POSITIVE;
import static net.minecraft.world.entity.MoverType.SELF;

public class FryerMovementHandler {
    public static class FriedEntityInfo {
        int ticksSinceLastCollision;
        BlockPos lastCollidedPos;
        BlockState lastCollidedState;

        public FriedEntityInfo(BlockPos collision, BlockState belt) {
            refresh(collision, belt);
        }

        public void refresh(BlockPos collision, BlockState belt) {
            ticksSinceLastCollision = 0;
            lastCollidedPos = new BlockPos(collision).immutable();
            lastCollidedState = belt;
        }

        public FryerMovementHandler.FriedEntityInfo tick() {
            ticksSinceLastCollision++;
            return this;
        }

        public int getTicksSinceLastCollision() {
            return ticksSinceLastCollision;
        }
    }

    public static boolean canBeTransported(Entity entity) {
        if (!entity.isAlive())
            return false;
        if (entity instanceof Player p && p.isShiftKeyDown()
                && !CardboardArmorHandler.testForStealth(entity))
            return false;
        return true;
    }

    public static void transportEntity(ContinuousFryerBlockEntity fryerBE, Entity entityIn, FriedEntityInfo info) {
        BlockPos pos = info.lastCollidedPos;
        Level world = fryerBE.getLevel();
        if (world == null)
            return;
        BlockEntity be = world.getBlockEntity(pos);
        BlockEntity blockEntityBelowPassenger = world.getBlockEntity(entityIn.blockPosition());
        BlockState blockState = info.lastCollidedState;
        Direction movementFacing =
                Direction.fromAxisAndDirection(blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)
                        .getAxis(), fryerBE.getSpeed() < 0 ? POSITIVE : NEGATIVE);

        boolean collidedWithBelt = be instanceof BeltBlockEntity;
        boolean betweenBelts = blockEntityBelowPassenger instanceof BeltBlockEntity && blockEntityBelowPassenger != be;

        // Don't fight other Belts
        if (!collidedWithBelt || betweenBelts) {
            return;
        }

        // Too slow
        boolean notHorizontal = fryerBE.getBlockState()
                .getValue(BeltBlock.SLOPE) != BeltSlope.HORIZONTAL;
        if (Math.abs(fryerBE.getSpeed()) < 1)
            return;

        // Not on top
        if (entityIn.getY() - .25f < pos.getY())
            return;

        // Lock entities in place
        boolean isPlayer = entityIn instanceof Player;
        if (entityIn instanceof LivingEntity && !isPlayer)
            ((LivingEntity) entityIn).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 1, false, false));

        final Direction beltFacing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        final BeltSlope slope = blockState.getValue(BeltBlock.SLOPE);
        final Direction.Axis axis = beltFacing.getAxis();
        float movementSpeed = fryerBE.getFryerMovementSpeed();
        final Direction movementDirection = Direction.get(axis == Direction.Axis.X ? NEGATIVE : POSITIVE, axis);

        Vec3i centeringDirection = Direction.get(POSITIVE, beltFacing.getClockWise()
                        .getAxis())
                .getNormal();
        Vec3 movement = Vec3.atLowerCornerOf(movementDirection.getNormal())
                .scale(movementSpeed);

        double diffCenter =
                axis == Direction.Axis.Z ? (pos.getX() + .5f - entityIn.getX()) : (pos.getZ() + .5f - entityIn.getZ());
        if (Math.abs(diffCenter) > 48 / 64f)
            return;

        BeltPart part = blockState.getValue(BeltBlock.PART);
        float top = 13 / 16f;
        boolean onSlope = notHorizontal && (part == BeltPart.MIDDLE || part == BeltPart.PULLEY
                || part == (slope == BeltSlope.UPWARD ? BeltPart.END : BeltPart.START) && entityIn.getY() - pos.getY() < top
                || part == (slope == BeltSlope.UPWARD ? BeltPart.START : BeltPart.END)
                && entityIn.getY() - pos.getY() > top);

        boolean movingDown = onSlope && slope == (movementFacing == beltFacing ? BeltSlope.DOWNWARD : BeltSlope.UPWARD);
        boolean movingUp = onSlope && slope == (movementFacing == beltFacing ? BeltSlope.UPWARD : BeltSlope.DOWNWARD);

        if (beltFacing.getAxis() == Direction.Axis.Z) {
            boolean b = movingDown;
            movingDown = movingUp;
            movingUp = b;
        }

        if (movingUp)
            movement = movement.add(0, Math.abs(axis.choose(movement.x, movement.y, movement.z)), 0);
        if (movingDown)
            movement = movement.add(0, -Math.abs(axis.choose(movement.x, movement.y, movement.z)), 0);

        Vec3 centering = Vec3.atLowerCornerOf(centeringDirection).scale(diffCenter * Math.min(Math.abs(movementSpeed), .1f) * 4);

        if (!(entityIn instanceof LivingEntity)
                || ((LivingEntity) entityIn).zza == 0 && ((LivingEntity) entityIn).xxa == 0)
            movement = movement.add(centering);

        float step = entityIn.maxUpStep();
        if (!isPlayer && entityIn instanceof LivingEntity livingEntity) {
            step = (float) livingEntity.getAttributeBaseValue(Attributes.STEP_HEIGHT);
            //noinspection DataFlowIssue
            livingEntity.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(1.0f);
        }

        // Entity Collisions
        if (Math.abs(movementSpeed) < .5f) {
            Vec3 checkDistance = movement.normalize()
                    .scale(0.5);
            AABB bb = entityIn.getBoundingBox();
            AABB checkBB = new AABB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            checkBB = checkBB.move(checkDistance)
                    .inflate(-Math.abs(checkDistance.x), -Math.abs(checkDistance.y), -Math.abs(checkDistance.z));
            List<Entity> list = world.getEntities(entityIn, checkBB);
            list.removeIf(e -> shouldIgnoreBlocking(entityIn, e));
            if (!list.isEmpty()) {
                entityIn.setDeltaMovement(0, 0, 0);
                info.ticksSinceLastCollision--;
                return;
            }
        }

        entityIn.fallDistance = 0;

        if (movingUp) {
            float minVelocity = .13f;
            float yMovement = (float) -(Math.max(Math.abs(movement.y), minVelocity));
            entityIn.move(SELF, new Vec3(0, yMovement, 0));
            entityIn.move(SELF, movement.multiply(1, 0, 1));
        } else if (movingDown) {
            entityIn.move(SELF, movement.multiply(1, 0, 1));
            entityIn.move(SELF, movement.multiply(0, 1, 0));
        } else {
            entityIn.move(SELF, movement);
        }

        entityIn.setOnGround(true);

        if (!isPlayer && entityIn instanceof LivingEntity livingEntity) {
            livingEntity.getAttribute(Attributes.STEP_HEIGHT).setBaseValue(step);
        }

        boolean movedPastEndingSlope = onSlope && (AllBlocks.BELT.has(world.getBlockState(entityIn.blockPosition()))
                || AllBlocks.BELT.has(world.getBlockState(entityIn.blockPosition()
                .below())));

        if (movedPastEndingSlope && !movingDown && Math.abs(movementSpeed) > 0)
            entityIn.setPos(entityIn.getX(), entityIn.getY() + movement.y, entityIn.getZ());
        if (movedPastEndingSlope) {
            entityIn.setDeltaMovement(movement);
            entityIn.hurtMarked = true;
        }

    }
}
