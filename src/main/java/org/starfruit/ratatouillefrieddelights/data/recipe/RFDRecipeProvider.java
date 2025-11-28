package org.starfruit.ratatouillefrieddelights.data.recipe;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.starfruit.ratatouillefrieddelights.data.recipe.farmersdelights.FDCuttingRecipeGen;
import org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille.*;
import org.starfruit.ratatouillefrieddelights.data.recipe.create.*;
import org.jetbrains.annotations.NotNull;
import org.starfruit.ratatouillefrieddelights.data.recipe.ratatouillefrydelights.RFDCoatingRecipeGen;
import org.starfruit.ratatouillefrieddelights.data.recipe.ratatouillefrydelights.RFDFryingRecipeGen;
import org.starfruit.ratatouillefrieddelights.data.recipe.ratatouillefrydelights.RFDTumblingRecipeGen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class RFDRecipeProvider extends RecipeProvider {
    static final List<ProcessingRecipeGen> GENERATORS = new ArrayList<>();
    static final int BUCKET = 1000;
    static final int BOTTLE = 250;

    public RFDRecipeProvider(PackOutput output) {
        super(output);
    }

    public static void registerAllProcessing(DataGenerator gen, PackOutput output) {
        GENERATORS.add(new RFDThreshingRecipeGen(output));
        GENERATORS.add(new RFDSqueezingRecipeGen(output));
        GENERATORS.add(new RFDDemoldingRecipeGen(output));
        GENERATORS.add(new RFDFreezingRecipeGen(output));
        GENERATORS.add(new RFDBakingRecipeGen(output));

        GENERATORS.add(new RFDCompactingRecipeGen(output));
        GENERATORS.add(new RFDCuttingRecipeGen(output));
        GENERATORS.add(new RFDEmptyingRecipeGen(output));
        GENERATORS.add(new RFDFillingRecipeGen(output));
        GENERATORS.add(new RFDMillingRecipeGen(output));
        GENERATORS.add(new RFDMixingRecipeGen(output));
        GENERATORS.add(new RFDDeployingRecipeGen(output));

        GENERATORS.add(new RFDCoatingRecipeGen(output));
        GENERATORS.add(new RFDTumblingRecipeGen(output));
        GENERATORS.add(new RFDFryingRecipeGen(output));

        gen.addProvider(true, new DataProvider() {
            @Override
            public @NotNull CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf(GENERATORS.stream()
                        .map(gen -> gen.run(dc))
                        .toArray(CompletableFuture[]::new));
            }

            @Override
            public @NotNull String getName() {
                return "Create: RatatouilleFriedDelights's Processing Recipes";
            }
        });
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        FDCuttingRecipeGen.register(pWriter);
    }
}
