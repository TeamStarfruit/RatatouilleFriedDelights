package org.starfruit.ratatouillefrieddelights.content.drum_processor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
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
    public DrumProcessingRecipe lastRecipe;

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

    public void spawnParticles() {
        if (level == null) return;

        var axis = getEjectDirection().getAxis();

        for (int i = 0; i < inputInv.getSlots(); i++) {
            ItemStack stackInSlot = inputInv.getStackInSlot(i);
            if (stackInSlot.isEmpty()) return;

            ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);

            float angle = level.random.nextFloat() * 360;
            Vec3 offset = new Vec3(0, 0.5f, 0);
            offset = VecHelper.rotate(offset, angle, axis);

            Vec3 target = VecHelper.rotate(offset, getSpeed() > 0 ? 25 : -25, axis);

            Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));
            target = VecHelper.offsetRandomly(target.subtract(offset), level.random, 1 / 128f);

            level.addParticle(data, center.x, center.y, center.z, target.x, target.y, target.z);
        }
    }


    @Override
    public void tick() {
        super.tick();
        if (level == null) return;

        // 1) 没动力就不跑
        if (getSpeed() == 0.0F)
            return;
        // copy from ThresherBlockEntity::tick
        if (canOutput()) {
            Direction direction = getEjectDirection();
            for (int slot = 0; slot < this.outputInv.getSlots(); slot++) {
                ItemStack stack = this.outputInv.getStackInSlot(slot);
                if (!stack.isEmpty()) {
                    BlockEntity be = this.level.getBlockEntity(this.worldPosition.below().relative(direction));
                    InvManipulationBehaviour inserter =
                            be == null ? null : BlockEntityBehaviour.get(level, be.getBlockPos(), InvManipulationBehaviour.TYPE);
                    IItemHandler targetInv = be == null ? null
                            : Optional.ofNullable(level.getCapability(Capabilities.ItemHandler.BLOCK, be.getBlockPos(), direction.getOpposite()))
                            .orElse(inserter == null ? null : inserter.getInventory());
                    if (targetInv != null) {
                        if (ItemHandlerHelper.insertItemStacked(targetInv, stack, true).isEmpty()) {
                            ItemHandlerHelper.insertItemStacked(targetInv, stack.copy(), false);
                            this.outputInv.setStackInSlot(slot, ItemStack.EMPTY);
                            notifyUpdate();
                        }
                    }
                }
            }
        } else if (level.getBlockState(getBlockPos().relative(getEjectDirection())).getBlock() instanceof AirBlock) {
            for (int slot = 0; slot < this.outputInv.getSlots(); slot++) {
                ItemStack stack = this.outputInv.getStackInSlot(slot);
                if (!stack.isEmpty()) {
                    Vec3 neighbour = VecHelper.getCenterOf(getBlockPos().relative(getEjectDirection()));
                    ItemEntity itementity = new ItemEntity(level, neighbour.x, Mth.floor(neighbour.y) + 1 / 16F, neighbour.z, stack.split(level.random.nextInt(21) + 10));
                    itementity.setDeltaMovement(Vec3.ZERO);
                    level.addFreshEntity(itementity);
                    this.outputInv.setStackInSlot(slot, ItemStack.EMPTY);
                    notifyUpdate();
                }
            }
        }

        // 输出槽满了 → 停机
        ItemStack out = outputInv.getStackInSlot(0);
        if (!out.isEmpty() && out.getCount() >= out.getMaxStackSize()) return;

        // 3) 正在计时：按处理速度递减；客户端只播粒子，服务端到0就执行 process()
        if (timer > 0) {
            timer -= getProcessingSpeed();

            if (level.isClientSide) {
                spawnParticles();
                return;
            }
            if (timer <= 0) {
                process();  // 在 process() 里负责：消耗输入1个 + 把结果塞进 outputInv
            }
            return;
        }

        // 4) 空输入则不启动
        if (inputInv.getStackInSlot(0).isEmpty() || inputInv.getStackInSlot(1).isEmpty()) {
            notifyUpdate();
            return;
        }

        // 5) 查配方（单输入）
        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);
        boolean matched = lastRecipe != null && lastRecipe.matches(inventoryIn, level);

        if (!matched) {
            Optional<RecipeHolder<DrumProcessingRecipe>> recipe = RFDRecipeTypes.COATING.find(inventoryIn, level);
            if (recipe.isEmpty())  recipe = RFDRecipeTypes.TUMBLING.find(inventoryIn, level);

            if (recipe.isEmpty()) {
                timer = 100;
            } else {
                lastRecipe = recipe.get().value();
                timer = lastRecipe.getProcessingDuration();
            }
        } else {
            timer = lastRecipe.getProcessingDuration();
        }
        notifyUpdate();
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

    private void process() {
        if (level == null) return;
        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);

        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, this.level)) {
            Optional<RecipeHolder<DrumProcessingRecipe>> recipe = RFDRecipeTypes.COATING.find(inventoryIn, level);
            if (recipe.isEmpty())  recipe = RFDRecipeTypes.TUMBLING.find(inventoryIn, level);

            if (recipe.isEmpty()) {
                return;
            }

            this.lastRecipe = recipe.get().value();
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

    public Direction getEjectDirection() {
        return getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getCounterClockWise();
    }

    private boolean canOutput() {
        if (level == null) return false;
        Direction direction = getEjectDirection();
        BlockPos neighbour = getBlockPos().relative(direction);
        BlockPos output = neighbour.below();
        BlockState blockState = level.getBlockState(neighbour);
        if (FunnelBlock.isFunnel(blockState)) {
            if (FunnelBlock.getFunnelFacing(blockState) == direction) {
                return false;
            }
        } else {
            if (!blockState.getCollisionShape(level, neighbour).isEmpty()) {
                return false;
            }

            BlockEntity blockEntity = level.getBlockEntity(output);
            if (blockEntity instanceof BeltBlockEntity belt) {
                return belt.getSpeed() == 0.0F || belt.getMovementFacing() != direction.getOpposite();
            }
        }

        DirectBeltInputBehaviour directBeltInputBehaviour = (DirectBeltInputBehaviour) BlockEntityBehaviour.get(level, output, DirectBeltInputBehaviour.TYPE);
        return directBeltInputBehaviour != null && directBeltInputBehaviour.canInsertFromSide(direction);
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
