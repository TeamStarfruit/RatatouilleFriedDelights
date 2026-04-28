package org.starfruit.ratatouillefrieddelights.content.drumprocessor;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.createmod.catnip.data.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public abstract class DrumProcessingRecipe extends StandardProcessingRecipe<RecipeInput> {
    public DrumProcessingRecipe(IRecipeTypeInfo typeInfo, ProcessingRecipeParams params) {
        super(typeInfo, params);
    }

    protected int getMaxInputCount() {
        return 16;
    }

    protected int getMaxOutputCount() {
        return 16;
    }

    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    public boolean matches(RecipeInput inv, Level worldIn) {
        if (inv.isEmpty())
            return false;

        List<Pair<Ingredient, MutableInt>> condensedIngredients =
                ItemHelper.condenseIngredients(ingredients);

        int[] extractedFromSlot = new int[inv.size()];

        for (Pair<Ingredient, MutableInt> pair : condensedIngredients) {
            Ingredient ingredient = pair.getFirst();
            int requiredCount = pair.getSecond().getValue();

            for (int slot = 0; slot < inv.size() && requiredCount > 0; slot++) {
                ItemStack stack = inv.getItem(slot);
                if (stack.isEmpty() || stack.getCount() <= extractedFromSlot[slot])
                    continue;

                if (ingredient.test(stack)) {
                    int available = stack.getCount() - extractedFromSlot[slot];
                    int extracted = Math.min(requiredCount, available);
                    extractedFromSlot[slot] += extracted;
                    requiredCount -= extracted;
                }
            }

            if (requiredCount > 0)
                return false;
        }

        return true;
    }
}
