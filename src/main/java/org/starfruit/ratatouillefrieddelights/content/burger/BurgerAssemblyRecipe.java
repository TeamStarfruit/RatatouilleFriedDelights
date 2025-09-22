package org.starfruit.ratatouillefrieddelights.content.burger;

import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipeParams;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.starfruit.ratatouillefrieddelights.entry.RFDDataComponents;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BurgerAssemblyRecipe extends DeployerApplicationRecipe {
    public BurgerAssemblyRecipe(ItemApplicationRecipeParams params) {
        super(params);
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

        BurgerContents.setBurger(burger, allItems);

        return burger;
    }
}
