package org.starfruit.ratatouillefrieddelights.mixin;

import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.deployer.BeltDeployerCallbacks;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.starfruit.ratatouillefrieddelights.content.burger.BurgerAssemblyRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(BeltDeployerCallbacks.class)
public class BurgerAssemblyMixin {
    @Shadow
    private static void awardAdvancements(DeployerBlockEntity blockEntity, ItemStack created) {}

    @Inject(method = "activate", at = @At("HEAD"), cancellable = true)
    private static void activate(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler, DeployerBlockEntity blockEntity, Recipe<?> recipe, CallbackInfo ci) {
        if (blockEntity.getLevel() == null) return;

        if (!(recipe instanceof BurgerAssemblyRecipe pr)) return;

        List<ItemStack> stacks;

        stacks = new ArrayList<>();
        for (int i = 0; i < transported.stack.copyWithCount(1).getCount(); i++) {
            ItemStackHandler recipeInv = new ItemStackHandler(2);

            recipeInv.setStackInSlot(0, transported.stack.copyWithCount(1));
            recipeInv.setStackInSlot(1, blockEntity.getPlayer().getMainHandItem());

            ItemStack stack = pr.assemble(new RecipeWrapper(recipeInv), blockEntity.getLevel().registryAccess());

            // I manually created the inv same as blockEntity.recipeInv every time, it should be more efficient if we just call that

            for (ItemStack previouslyRolled : stacks) {
                if (stack.isEmpty())
                    continue;
                if (!ItemStack.isSameItemSameComponents(stack, previouslyRolled))
                    continue;
                int amount = Math.min(previouslyRolled.getOrDefault(DataComponents.MAX_STACK_SIZE, 64) - previouslyRolled.getCount(),
                        stack.getCount());
                previouslyRolled.grow(amount);
                stack.shrink(amount);
            }

            if (stack.isEmpty())
                continue;

            stacks.add(stack);
        }

        List<TransportedItemStack> collect =
                stacks
                        .stream()
                        .map(stack -> {
                            TransportedItemStack copy = transported.copy();
                            boolean centered = BeltHelper.isItemUpright(stack);
                            copy.stack = stack;
                            copy.locked = true;
                            copy.angle = centered ? 180 : Create.RANDOM.nextInt(360);
                            return copy;
                        })
                        .peek(t -> t.locked = false)
                        .collect(Collectors.toList());

        blockEntity.award(AllAdvancements.DEPLOYER);

        transported.clearFanProcessingData();

        TransportedItemStack left = transported.copy();
//            blockEntity.getPlayer().spawnedItemEffects = transported.stack.copy();
        left.stack.shrink(1);
        ItemStack resultItem;

        if (collect.isEmpty()) {
            resultItem = left.stack.copy();
            handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertTo(left));
        } else {
            resultItem = collect.getFirst().stack.copy();
            handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertToAndLeaveHeld(collect, left));
        }

        ItemStack heldItem = blockEntity.getPlayer().getMainHandItem();
        boolean keepHeld =
                ((ItemApplicationRecipe) recipe).shouldKeepHeldItem();

        if (!keepHeld) {
            if (heldItem.isDamageableItem())
                heldItem.hurtAndBreak(1, blockEntity.getPlayer(),
                        LivingEntity.getSlotForHand(InteractionHand.MAIN_HAND));
            else
                heldItem.shrink(1);
        }

        if (!resultItem.isEmpty())
            awardAdvancements(blockEntity, resultItem);

        BlockPos pos = blockEntity.getBlockPos();
        Level world = blockEntity.getLevel();
        if (world == null) {
            ci.cancel();
            return;
        }
        if (heldItem.isEmpty())
            world.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, .25f, 1);
        world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, .25f, .75f);

        blockEntity.notifyUpdate();
        ci.cancel();
    }
}
