package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemHandlerFryerSegment implements IItemHandler {

    private final FryerInventory fryerInventory;
    int offset;

    public ItemHandlerFryerSegment(FryerInventory beltInventory, int offset) {
        this.fryerInventory = beltInventory;
        this.offset = offset;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        FryingItemStack stackAtOffset = this.fryerInventory.getStackAtOffset(offset);
        if (stackAtOffset == null)
            return ItemStack.EMPTY;
        return stackAtOffset.stack;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (this.fryerInventory.canInsertAt(offset)) {
            ItemStack remainder = ItemHelper.limitCountToMaxStackSize(stack, simulate);
            if (!simulate) {
                FryingItemStack newStack = new FryingItemStack(stack);
                newStack.insertedAt = offset;
                newStack.fryerPosition = offset + .5f + (fryerInventory.fryerMovementPositive ? -1 : 1) / 16f;
                newStack.prevFryerPosition = newStack.fryerPosition;
                this.fryerInventory.addItem(newStack);
                this.fryerInventory.fryer.setChanged();
                this.fryerInventory.fryer.sendData();
            }
            return remainder;
        }
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        FryingItemStack transported = this.fryerInventory.getStackAtOffset(offset);
        if (transported == null)
            return ItemStack.EMPTY;

        amount = Math.min(amount, transported.stack.getCount());
        ItemStack extracted = simulate ? transported.stack.copy()
                .split(amount) : transported.stack.split(amount);
        if (!simulate) {
            if (transported.stack.isEmpty())
                fryerInventory.toRemove.add(transported);
            else
                fryerInventory.fryer.notifyUpdate();
        }

        return extracted;
    }

    @Override
    public int getSlotLimit(int slot) {
        return Math.min(getStackInSlot(slot).getMaxStackSize(), 64);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

}
