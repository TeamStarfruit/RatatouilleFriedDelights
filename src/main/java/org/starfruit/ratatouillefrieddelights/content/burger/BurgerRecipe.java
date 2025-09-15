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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        List<ItemStack> allItems = new ArrayList<>();

        if (t.getItem(0).is(RFDItems.BURGER)) {
            allItems.addAll(t.getItem(0).getOrDefault(RFDDataComponents.BURGER_CONTENTS, BurgerContents.EMPTY).items);
        } else {
            allItems.add(t.getItem(0));
        }

        if (t.getItem(1).is(RFDItems.BURGER)) {
            allItems.addAll(t.getItem(1).getOrDefault(RFDDataComponents.BURGER_CONTENTS, BurgerContents.EMPTY).items);
        } else {
            allItems.add(t.getItem(1));
        }

        allItems = allItems.stream().map(ItemStack::copy).collect(Collectors.toList());
        allItems.forEach(allItem -> allItem.setCount(1));
        allItems.removeIf(ItemStack::isEmpty);

        burger.set(RFDDataComponents.BURGER_CONTENTS, new BurgerContents(allItems));

        return burger;
    }

    @Override
    public List<ProcessingOutput> getRollableResults() {
        return List.of(new ProcessingOutput(new ItemStack(Items.APPLE), 1));
    } // TODO: For debug, please mixin into BeltDeployerCallbacks#activate to really apply
}
