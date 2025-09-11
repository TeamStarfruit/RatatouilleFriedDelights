package org.starfruit.ratatouillefrieddelights.content.drum_processor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.forsteri.ratatouille.content.thresher.ThresherBlockEntity;
import org.forsteri.ratatouille.entry.CRBlockEntityTypes;
import org.forsteri.ratatouille.entry.CRRecipeTypes;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

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

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                RFDBlockEntityTypes.DRUM_PROCESSOR.get(),
                (be, context) -> be.capability
        );
    }

    @Override
    public void tick() {
        super.tick();

        if (getSpeed() == 0)
            return;
        for (int i = 0; i < outputInv.getSlots(); i++)
            if (outputInv.getStackInSlot(i)
                    .getCount() == outputInv.getSlotLimit(i))
                return;

        if (timer > 0) {
            timer -= getProcessingSpeed();

            if (level.isClientSide) {
                //spawnParticles();
                return;
            }
            if (timer <= 0)
                process();
            return;
        }

        if (inputInv.getStackInSlot(0)
                .isEmpty())
            return;

        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);
        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            Optional<RecipeHolder<CoatingRecipe>> recipe = RFDRecipeTypes.COATING.find(inventoryIn, level);
            if (!recipe.isPresent()) {
                timer = 100;
            } else {
                lastRecipe = recipe.get().value();
                timer = lastRecipe.getProcessingDuration();
            }
            notifyUpdate();
        }

        timer = lastRecipe.getProcessingDuration();
        sendData();
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

    private void process() {
        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);

        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            Optional<RecipeHolder<CoatingRecipe>> recipe = RFDRecipeTypes.COATING.find(inventoryIn, level);
            if (recipe.isEmpty()) {
                return;
            }
        }

        ItemStack stackInSlot = inputInv.getStackInSlot(0);
        stackInSlot.shrink(1);
        inputInv.setStackInSlot(0, stackInSlot);
        this.lastRecipe.rollResults().forEach((stack) -> {
            ItemHandlerHelper.insertItemStacked(this.outputInv, stack, false);
        });

        notifyUpdate();
    }

    @Override
    public float calculateStressApplied() {
        return 4.0f;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putInt("Timer", this.timer);
        compound.put("InputInventory", this.inputInv.serializeNBT(registries));
        compound.put("OutputInventory", this.outputInv.serializeNBT(registries));
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        this.timer = compound.getInt("Timer");
        this.inputInv.deserializeNBT(registries, compound.getCompound("InputInventory"));
        this.outputInv.deserializeNBT(registries, compound.getCompound("OutputInventory"));
        super.read(compound, registries, clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));
        super.addBehaviours(behaviours);
    }

    public void invalidate() {
        super.invalidate();
//        this.capability = null;
        invalidateCapabilities();
    }

    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(this.level, this.worldPosition, this.inputInv);
        ItemHelper.dropContents(this.level, this.worldPosition, this.outputInv);
    }

    private boolean canProcess(ItemStack stack) {
        if (level == null) return false;
        SingleRecipeInput inventoryIn = new SingleRecipeInput(stack);

        if (lastRecipe != null && lastRecipe.matches(inventoryIn, level))
            return true;
        return RFDRecipeTypes.COATING.find(inventoryIn, level)
                .isPresent();}

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
