package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.belt.BeltPart;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.neoforged.neoforge.capabilities.Capabilities;

// TODO: 替换为你自己的 BE 类型注册类
import org.forsteri.ratatouille.content.oven.OvenBlockEntity;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;

import javax.annotation.ParametersAreNonnullByDefault;

public class ContinuousFryerBlock extends HorizontalKineticBlock implements IBE<ContinuousFryerBlockEntity> {
    public static final Property<FryerPart> PART = EnumProperty.create("part", FryerPart.class);

    public ContinuousFryerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(PART, FryerPart.SINGLE));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return state.getValue(PART) != FryerPart.MIDDLE && face.getAxis() == getRotationAxis(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public PathType getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, Mob entity) {
        return PathType.RAIL;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);

        if (oldState.getBlock() == state.getBlock())
            return;
        if (isMoving)
            return;

        withBlockEntityDo(worldIn, pos, ContinuousFryerBlockEntity::updateConnectivity);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult result = super.onWrenched(state, context);
        withBlockEntityDo(context.getLevel(), context.getClickedPos(),
                be -> {be.updateConnectivity();be.updateNeighbours();});
        return result;
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter world, Entity entity) {
        super.updateEntityAfterFallOn(world, entity);
        if (!(entity instanceof ItemEntity itemEntity))
            return;
        if (!entity.isAlive())
            return;
        if (entity.level().isClientSide)
            return;

        DirectBeltInputBehaviour input = BlockEntityBehaviour.get(world, entity.blockPosition(), DirectBeltInputBehaviour.TYPE);
        if (input == null)
            return;

        Vec3 delta = entity.getDeltaMovement().multiply(1, 0, 1).normalize();
        Direction nearest = Direction.getNearest(delta.x, delta.y, delta.z);
        ItemStack remainder = input.handleInsertion(itemEntity.getItem(), nearest, false);
        itemEntity.setItem(remainder);
        if (remainder.isEmpty())
            itemEntity.discard();
    }

    /**
     * 只处理“向 BE 内灌油”的交互。
     * 不做 GenericItemEmptying（炸锅不从物品里倒液体）。
     */
    protected ItemInteractionResult tryFillOil(Level level, Player player, InteractionHand hand,
                                               ItemStack heldItem, ContinuousFryerBlockEntity be) {
        // 允许插入 → 尝试把玩家手上的流体物品“倒入”锅内油槽
        be.internalTank.allowInsertion();
        ItemInteractionResult res = FluidHelper.tryEmptyItemIntoBE(level, player, hand, heldItem, be)
                ? ItemInteractionResult.SUCCESS
                : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        be.internalTank.forbidInsertion(); // 还原为默认策略（如你在 BE 里设置了只读/只写，可以按需调整）
        return res;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return AllShapes.CASING_13PX.get(Direction.UP);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level.isClientSide)
            return;
        if (state.getBlock() == newState.getBlock())
            return;
        if (isMoving)
            return;
        if (!state.hasBlockEntity())
            return;

        withBlockEntityDo(level, pos, be -> {
            ItemStack held = be.getHeldItemStack();
            if (!held.isEmpty())
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), held);

            be.updateNeighbours();
        });

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public Class<ContinuousFryerBlockEntity> getBlockEntityClass() {
        return ContinuousFryerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ContinuousFryerBlockEntity> getBlockEntityType() {
        return RFDBlockEntityTypes.CONTINUOUS_FRYER.get();
    }

    // 放置时打点成就（与 Drain 一致）
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        AdvancementBehaviour.setPlacedBy(level, pos, placer);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getClockWise()
                .getAxis();
    }
}