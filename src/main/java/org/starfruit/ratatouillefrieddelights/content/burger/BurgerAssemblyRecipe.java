package org.starfruit.ratatouillefrieddelights.content.burger;

import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BurgerAssemblyRecipe extends DeployerApplicationRecipe {
    public BurgerAssemblyRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(params);
    }

    @Override
    public ItemStack assemble(RecipeWrapper t, RegistryAccess registryAccess) {
        ItemStack burger = new ItemStack(RFDItems.BURGER.get());

        List<ItemStack> allItems = new ArrayList<>();

        if (t.getItem(0).is(RFDItems.BURGER.get())) {
            allItems.addAll(BurgerContents.get(t.getItem(0), BurgerContents.EMPTY).items);
        } else {
            allItems.add(t.getItem(0));
        }

        if (t.getItem(1).is(RFDItems.BURGER.get())) {
            allItems.addAll(BurgerContents.get(t.getItem(1), BurgerContents.EMPTY).items);
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
