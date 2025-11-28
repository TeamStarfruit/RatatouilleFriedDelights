package org.starfruit.ratatouillefrieddelights.mixin;

import com.simibubi.create.content.fluids.spout.FillingBySpout;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.starfruit.ratatouillefrieddelights.content.burger.BurgerSaucingRecipe;

import java.util.List;

@Mixin(value = FillingBySpout.class, remap = false)
public abstract class BurgerSaucingMixin {
    @Unique
    private static ItemStack ratafry$injectingStack;

    @Unique
    private static Level ratafry$level;

    @Inject(method = "fillItem", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;rollResults()Ljava/util/List;"))
    private static void fillItem(Level world, int requiredAmount, ItemStack stack, FluidStack availableFluid, CallbackInfoReturnable<ItemStack> cir) {
        ratafry$injectingStack = stack;
        ratafry$level = world;
    }

    @Redirect(method = "fillItem", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/fluids/transfer/FillingRecipe;rollResults()Ljava/util/List;"))
    private static List<ItemStack> injected(FillingRecipe instance) {
        if (!(instance instanceof BurgerSaucingRecipe recipe))
            return instance.rollResults();
        ItemStackHandler handler = new ItemStackHandler(1);
        handler.setStackInSlot(0, ratafry$injectingStack.copy());
        return List.of(recipe.assemble(new RecipeWrapper(handler), ratafry$level.registryAccess()));
    }
}
