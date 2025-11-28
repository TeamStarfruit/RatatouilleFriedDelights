package org.starfruit.ratatouillefrieddelights.data.recipe.ratatouille;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import org.forsteri.ratatouille.content.frozen_block.FreezingRecipe;
import org.forsteri.ratatouille.data.recipe.api.FreezingRecipeGen;
import org.forsteri.ratatouille.entry.CRRecipeTypes;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

@SuppressWarnings("unused")
public class RFDFreezingRecipeGen extends FreezingRecipeGen {
/*GeneratedRecipe
            CHOCOLATE_MOLD_SOLID = this.create(
            CRItems.CHOCOLATE_MOLD_FILLED::get,
            b -> b.output(CRItems.CHOCOLATE_MOLD_SOLID.get())),

            BLUE_ICE = this.create(
            Items.PACKED_ICE::asItem,
            b -> b.output(Items.BLUE_ICE)
            ),

            MELON_POPSICLE_MOLD_SOLID = this.create(
            CRItems.MELON_POPSICLE_MOLD_FILLED::get,
            b -> b.output(CRItems.MELON_POPSICLE_MOLD_SOLID.get())
            );
*/
    public RFDFreezingRecipeGen(PackOutput output) {
        super(output, RatatouilleFriedDelights.MOD_ID);
    }
}
