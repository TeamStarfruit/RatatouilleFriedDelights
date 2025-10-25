package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;

public class FryingRecipe extends StandardProcessingRecipe<RecipeInput> {
    public FryingRecipe(ProcessingRecipeParams params) {
        super(RFDRecipeTypes.FRYING, params);
    }

    protected int getMaxInputCount() {
        return 1;
    }

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

    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    public boolean matches(RecipeInput inv, @NotNull Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.getFirst()
                .test(inv.getItem(0));
    }

    public boolean matches(ItemStack item, FluidStack fluid, BlazeBurnerBlock.HeatLevel heatLevel) {
        if (fluidIngredients.isEmpty()) {
            return ingredients.getFirst().test(item)
                    && getRequiredHeat().testBlazeBurner(heatLevel);
        } else {
            FluidIngredient fluidIngredient = fluidIngredients.getFirst();

            boolean sameFluid = fluidIngredient.getMatchingFluidStacks().stream()
                    .anyMatch(fs -> fs.getFluid().getFluidType() == fluid.getFluid().getFluidType());

            return ingredients.getFirst().test(item)
                    && sameFluid
                    && fluid.getAmount() >= fluidIngredients.getFirst().getRequiredAmount()
                    && getRequiredHeat().testBlazeBurner(heatLevel);
        }
    }

}
