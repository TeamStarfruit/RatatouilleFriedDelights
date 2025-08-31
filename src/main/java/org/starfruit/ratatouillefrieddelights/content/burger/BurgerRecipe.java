package org.starfruit.ratatouillefrieddelights.content.burger;

import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipeParams;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.starfruit.ratatouillefrieddelights.entry.RFDDataComponents;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDTags;

import java.util.List;

public class BurgerRecipe extends DeployerApplicationRecipe {
    public BurgerRecipe(ItemApplicationRecipeParams params) {
        super(params);
    }

    @Override
    public boolean matches(RecipeWrapper craftingInput, Level level) {
        return RFDTags.AllItemTags.BURGER_BASE.matches(craftingInput.getItem(0))
                && RFDTags.AllItemTags.BURGER_TOPPINGS.matches(craftingInput.getItem(1));
    }

    @Override
    public ItemStack assemble(RecipeWrapper t, HolderLookup.Provider provider) {
        ItemStack burger = new ItemStack(RFDItems.BURGER.get());
        burger.set(RFDDataComponents.BURGER_CONTENTS, new BurgerContents(List.of(
                new ItemStack(RFDItems.BOTTOM_BURGER_BUN.get()),
                new ItemStack(RFDItems.HAMBURGER_PATTY.get()),
                new ItemStack(RFDItems.CHEESE_SLICE.get()),
                new ItemStack(RFDItems.SHREDDED_LETTUCE.get()),
                new ItemStack(RFDItems.TOMATO_SLICES.get()),
                new ItemStack(RFDItems.TOP_BURGER_BUN.get())
        )));

        return burger;
    }

    @Override
    public List<ProcessingOutput> getRollableResults() {
        return List.of(new ProcessingOutput(new ItemStack(Items.APPLE), 1));
    } // TODO: For debug, please mixin into BeltDeployerCallbacks#activate to really apply
}
