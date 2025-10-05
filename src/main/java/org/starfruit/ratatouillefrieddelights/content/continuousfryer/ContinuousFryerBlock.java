package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.armor.DivingBootsItem;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.belt.transport.BeltTunnelInteractionHandler;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;

import com.simibubi.create.foundation.item.ItemHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.neoforged.neoforge.capabilities.Capabilities;

// TODO: 替换为你自己的 BE 类型注册类
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.starfruit.ratatouillefrieddelights.content.continuousfryer.*;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlocks;

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
    @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
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

        withBlockEntityDo(level, pos, ContinuousFryerBlockEntity::updateNeighbours);

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

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.updateEntityAfterFallOn(worldIn, entityIn);
        BlockPos entityPosition = entityIn.blockPosition();
        BlockPos fryerPos = null;

        if (RFDBlocks.CONTINUOUS_FRYER.has(worldIn.getBlockState(entityPosition)))
            fryerPos = entityPosition;
        else if (RFDBlocks.CONTINUOUS_FRYER.has(worldIn.getBlockState(entityPosition.below())))
            fryerPos = entityPosition.below();
        if (fryerPos == null)
            return;
        if (!(worldIn instanceof Level))
            return;

        entityInside(worldIn.getBlockState(fryerPos), (Level) worldIn, fryerPos, entityIn);
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof Player player) {
            if (player.isShiftKeyDown() && !AllItems.CARDBOARD_BOOTS.isIn(player.getItemBySlot(EquipmentSlot.FEET)))
                return;
            if (player.getAbilities().flying)
                return;
        }

        if (DivingBootsItem.isWornBy(entityIn))
            return;

        ContinuousFryerBlockEntity fryer = FryerHelper.getSegmentBE(worldIn, pos);
        if (fryer == null)
            return;
        ItemStack asItem = ItemHelper.fromItemEntity(entityIn);
        if (!asItem.isEmpty()) {
            if (worldIn.isClientSide)
                return;
            if (entityIn.getDeltaMovement().y > 0)
                return;
            Vec3 targetLocation = VecHelper.getCenterOf(pos)
                    .add(0, 5 / 16f, 0);
            if (!PackageEntity.centerPackage(entityIn, targetLocation))
                return;
            if (BeltTunnelInteractionHandler.getTunnelOnPosition(worldIn, pos) != null)
                return;
            withBlockEntityDo(worldIn, pos, be -> {
                IItemHandler handler = worldIn.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, be, null);
                if (handler == null)
                    return;
                ItemStack remainder = handler.insertItem(0, asItem, false);
                if (remainder.isEmpty())
                    entityIn.discard();
                else if (entityIn instanceof ItemEntity itemEntity && remainder.getCount() != itemEntity.getItem().getCount())
                    itemEntity.setItem(remainder);
            });
            return;
        }

        ContinuousFryerBlockEntity controller = FryerHelper.getControllerBE(worldIn, pos);
        if (controller == null || controller.passengers == null)
            return;
        if (controller.passengers.containsKey(entityIn)) {
            FryerMovementHandler.FriedEntityInfo info = controller.passengers.get(entityIn);
            if (info.getTicksSinceLastCollision() != 0 || pos.equals(entityIn.blockPosition()))
                info.refresh(pos, state);
        } else {
            controller.passengers.put(entityIn, new FryerMovementHandler.FriedEntityInfo(pos, state));
            entityIn.setOnGround(true);
        }
    }
}