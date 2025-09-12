package org.starfruit.ratatouillefrieddelights.data.recipe.create;

import com.simibubi.create.AllFluids;
import com.simibubi.create.api.data.recipe.DeployingRecipeGen;
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.forsteri.ratatouille.Ratatouille;
import org.forsteri.ratatouille.entry.CRFluids;
import org.forsteri.ratatouille.entry.CRItems;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDFluids;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class RFDSequencedAssemblyRecipeGen extends SequencedAssemblyRecipeGen {
    GeneratedRecipe
            HOTCAKE_MEAL = create("hotcake_meal", b -> b
            .require(RFDItems.PANCAKE.get())
            .transitionTo(RFDItems.UNPROCESSED_HOTCAKE_MEAL.get())
            .addOutput(RFDItems.HOTCAKE_MEAL.get(), 1)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new, rb -> rb
                    .require(RFDItems.PANCAKE.get())
            )
            .addStep(DeployerApplicationRecipe::new, rb -> rb
                    .require(RFDItems.PANCAKE.get())
            )
            .addStep(DeployerApplicationRecipe::new, rb -> rb
                    .require(RFDItems.BUTTER.get())
            )
            .addStep(FillingRecipe::new, rb -> rb
                    .require(AllFluids.HONEY.get(), 100)
            )
    ),

    RAW_APPLE_PIE = create("raw_apple_pie", b -> b
            .require(RFDItems.PUFF_PASTRY.get())
            .transitionTo(RFDItems.UNPROCESSED_RAW_APPLE_PIE.get())
            .addOutput(RFDItems.RAW_APPLE_PIE.get(), 1)
            .loops(1)
            .addStep(PressingRecipe::new, rb -> rb)
            .addStep(DeployerApplicationRecipe::new, rb -> rb
                    .require(RFDItems.APPLE_SLICES.get())
            )
            .addStep(PressingRecipe::new, rb -> rb)
    );

    public RFDSequencedAssemblyRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }
}
