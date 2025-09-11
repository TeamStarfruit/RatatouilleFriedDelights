package org.starfruit.ratatouillefrieddelights.content.drum_processor;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;


public class DrumProcessorBlock extends HorizontalKineticBlock implements IBE<DrumProcessorBlockEntity> {
    public DrumProcessorBlock(Properties properties) {super(properties);}

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!stack.isEmpty())
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (level.isClientSide)
            return ItemInteractionResult.SUCCESS;

        withBlockEntityDo(level, pos, millstone -> {
            boolean emptyOutput = true;
            IItemHandlerModifiable inv = millstone.outputInv;
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getStackInSlot(slot);
                if (!stackInSlot.isEmpty())
                    emptyOutput = false;
                player.getInventory()
                        .placeItemBackInInventory(stackInSlot);
                inv.setStackInSlot(slot, ItemStack.EMPTY);
            }

            if (emptyOutput) {
                inv = millstone.inputInv;
                for (int slot = 0; slot < inv.getSlots(); slot++) {
                    player.getInventory()
                            .placeItemBackInInventory(inv.getStackInSlot(slot));
                    inv.setStackInSlot(slot, ItemStack.EMPTY);
                }
            }

            millstone.setChanged();
            millstone.sendData();
        });

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.updateEntityAfterFallOn(worldIn, entityIn);

        if (entityIn.level().isClientSide)
            return;
        if (!(entityIn instanceof ItemEntity itemEntity))
            return;
        if (!entityIn.isAlive())
            return;

        DrumProcessorBlockEntity drumprocessor = null;
        for (BlockPos pos : Iterate.hereAndBelow(entityIn.blockPosition()))
            if (drumprocessor == null)
                drumprocessor = getBlockEntity(worldIn, pos);

        if (drumprocessor == null)
            return;

        IItemHandler capability = drumprocessor.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, drumprocessor.getBlockPos(), null);
        if (capability == null)
            return;

        ItemStack remainder = capability
                .insertItem(0, itemEntity.getItem(), false);
        if (remainder.isEmpty())
            itemEntity.discard();
        if (remainder.getCount() < itemEntity.getItem()
                .getCount())
            itemEntity.setItem(remainder);
    }
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
