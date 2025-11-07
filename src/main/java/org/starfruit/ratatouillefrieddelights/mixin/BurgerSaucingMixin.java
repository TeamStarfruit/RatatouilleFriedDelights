package org.starfruit.ratatouillefrieddelights.mixin;

import com.simibubi.create.content.fluids.spout.FillingBySpout;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.starfruit.ratatouillefrieddelights.content.burger.BurgerSaucingRecipe;

import java.util.List;

@Mixin(FillingBySpout.class)
public abstract class BurgerSaucingMixin {
    @Unique
    private static ItemStack ratafry$injectingStack;

    @Unique
    private static Level ratafry$level;

    @Inject(method = "fillItem", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;rollResults(Lnet/minecraft/util/RandomSource;)Ljava/util/List;"))
    private static void fillItem(Level world, int requiredAmount, ItemStack stack, FluidStack availableFluid, CallbackInfoReturnable<ItemStack> cir) {
        ratafry$injectingStack = stack;
        ratafry$level = world;
    }

    @Redirect(method = "fillItem", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;rollResults(Lnet/minecraft/util/RandomSource;)Ljava/util/List;"))
    private static List<ItemStack> injected(FillingRecipe instance, RandomSource random) {
        if (!(instance instanceof BurgerSaucingRecipe recipe)) return instance.rollResults(random);
        return List.of(recipe.assemble(new SingleRecipeInput(ratafry$injectingStack), ratafry$level.registryAccess()));
    }
}
