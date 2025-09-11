package org.starfruit.ratatouillefrieddelights.content.drum_processor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlockEntityTypes;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

public class DrumProcessorBlockEntity extends KineticBlockEntity {
    public DrumProcessorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.inputInv = new ItemStackHandler(2);
        this.outputInv = new ItemStackHandler(2);
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
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();

        if (getSpeed() == 0)
            return;
        if (inputInv.getStackInSlot(0)
                .isEmpty())
            return;

        float pitch = Mth.clamp((Math.abs(getSpeed()) / 256f) + .45f, .85f, 1f);
        SoundScapes.play(SoundScapes.AmbienceGroup.MILLING, worldPosition, pitch);
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;

        // 1) 没动力就不跑
        if (getSpeed() == 0.0F)
            return;

        // 输出槽满了 → 停机
        ItemStack out = outputInv.getStackInSlot(0);
        if (!out.isEmpty() && out.getCount() >= out.getMaxStackSize()) return;

        // 3) 正在计时：按处理速度递减；客户端只播粒子，服务端到0就执行 process()
        if (timer > 0) {
            timer -= getProcessingSpeed();

            if (level.isClientSide) {
                // spawnParticles();
                return;
            }
            if (timer <= 0) {
                process();  // 在 process() 里负责：消耗输入1个 + 把结果塞进 outputInv
            }
            return;
        }

        // 4) 空输入则不启动
        if (inputInv.getStackInSlot(0).isEmpty() || inputInv.getStackInSlot(1).isEmpty())
            return;

        // 5) 查配方（单输入）
        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);
        boolean matched = lastRecipe != null && lastRecipe.matches(inventoryIn, level);

        if (!matched) {
            Optional<RecipeHolder<CoatingRecipe>> recipe = RFDRecipeTypes.COATING.find(inventoryIn, level);
            if (recipe.isEmpty()) {
                // 未命中：给一个“空转冷却”（可选），并同步一次
                timer = 100;
                notifyUpdate();
                return; // 重要：别往下走
            } else {
                lastRecipe = recipe.get().value();
            }
        }

        // 6) 命中：设定处理时长并同步，然后等待下一tick进入“计时阶段”
        timer = Math.max(1, lastRecipe.getProcessingDuration());
        sendData();
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

    private void process() {
        if (level == null) return;
        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);

        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, this.level)) {
            Optional<RecipeHolder<CoatingRecipe>> recipe = RFDRecipeTypes.COATING.find(inventoryIn, this.level);
            if (recipe.isEmpty()) {
                return;
            }
        }

        ItemStack in0 = inputInv.getStackInSlot(0);
        ItemStack in1 = inputInv.getStackInSlot(1);
        in0.shrink(1);
        in1.shrink(1);
        inputInv.setStackInSlot(0, in0);
        inputInv.setStackInSlot(1, in1);
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

//    private boolean canProcess(ItemStack stack) {
//        if (level == null) return false;
//        SingleRecipeInput inventoryIn = new SingleRecipeInput(stack);
//
//        if (lastRecipe != null && lastRecipe.matches(inventoryIn, level))
//            return true;
//        return RFDRecipeTypes.COATING.find(inventoryIn, level)
//                .isPresent();}

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
            return super.isItemValid(slot, stack);
        }
    }


}
