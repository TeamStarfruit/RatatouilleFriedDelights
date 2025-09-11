package org.starfruit.ratatouillefrieddelights.content.drum_processor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.forsteri.ratatouille.content.thresher.ThresherBlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;

public class DrumProcessorBlockEntity extends KineticBlockEntity {
    public DrumProcessorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.inputInv = new ItemStackHandler(16);
        this.outputInv = new ItemStackHandler(16);
        this.capability = new DrumProcessorInventoryHandler();
    }

    public ItemStackHandler inputInv;
    public ItemStackHandler outputInv;
    public IItemHandler capability;
    public int timer;
    private CoatingRecipe lastRecipe;

    private boolean canProcess(ItemStack stack) {return false;}

    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    private class DrumProcessorInventoryHandler extends CombinedInvWrapper {
        public DrumProcessorInventoryHandler() {
            super(new IItemHandlerModifiable[]{DrumProcessorBlockEntity.this.inputInv, DrumProcessorBlockEntity.this.outputInv});
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (DrumProcessorBlockEntity.this.outputInv == this.getHandlerFromIndex(this.getIndexForSlot(slot)))
                return stack;
            if (!this.isItemValid(slot, stack))
                return stack;
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (DrumProcessorBlockEntity.this.inputInv == this.getHandlerFromIndex(getIndexForSlot(slot)))
                return ItemStack.EMPTY;
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (DrumProcessorBlockEntity.this.outputInv == this.getHandlerFromIndex(this.getIndexForSlot(slot)))
                return false;
            return DrumProcessorBlockEntity.this.canProcess(stack) && super.isItemValid(slot, stack);
        }
    }


}
