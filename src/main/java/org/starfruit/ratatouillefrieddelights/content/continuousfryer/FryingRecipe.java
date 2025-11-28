package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

public class FryingRecipe extends ProcessingRecipe<RecipeWrapper> {
    public FryingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(RFDRecipeTypes.FRYING, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected boolean canRequireHeat() {
        return true;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper inv, @NotNull Level worldIn) {
        if (inv.isEmpty())
            return false;
        return !ingredients.isEmpty() && ingredients.get(0)
                .test(inv.getItem(0));
    }

    public boolean matches(ItemStack item, FluidStack fluid, BlazeBurnerBlock.HeatLevel heatLevel) {
        if (ingredients.isEmpty())
            return false;

        if (fluidIngredients.isEmpty()) {
            return ingredients.get(0)
                    .test(item)
                    && getRequiredHeat().testBlazeBurner(heatLevel);
        } else {
            FluidIngredient fluidIngredient = fluidIngredients.get(0);

            boolean sameFluid = fluidIngredient.test(fluid);

            return ingredients.get(0)
                    .test(item)
                    && sameFluid
                    && fluid.getAmount() >= fluidIngredient.getRequiredAmount()
                    && getRequiredHeat().testBlazeBurner(heatLevel);
        }
    }
}
