package org.starfruit.ratatouillefrieddelights.data.recipe;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille.*;
import org.starfruit.ratatouillefrieddelights.data.recipe.create.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class RFDRecipeProvider extends RecipeProvider {
    static final List<ProcessingRecipeGen<?, ?, ?>> GENERATORS = new ArrayList<>();
    static final int BUCKET = 1000;
    static final int BOTTLE = 250;

    public RFDRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    public static void registerAllProcessing(DataGenerator gen, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        GENERATORS.add(new ThreshingRecipeGen(output, registries));
        GENERATORS.add(new SqueezingRecipeGen(output, registries));
        GENERATORS.add(new DemoldingRecipeGen(output, registries));
        GENERATORS.add(new FreezingRecipeGen(output, registries));
        GENERATORS.add(new BakingRecipeGen(output, registries));
        GENERATORS.add(new RFDCompactingRecipeGen(output, registries));
        GENERATORS.add(new RFDCuttingRecipeGen(output, registries));
        GENERATORS.add(new RFDEmptyingRecipeGen(output, registries));
        GENERATORS.add(new RFDFillingRecipeGen(output, registries));
        GENERATORS.add(new RFDMillingRecipeGen(output, registries));
        GENERATORS.add(new RFDMixingRecipeGen(output, registries));

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
}
