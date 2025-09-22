package org.starfruit.ratatouillefrieddelights.content.burger;

import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.starfruit.ratatouillefrieddelights.entry.RFDDataComponents;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BurgerSaucingRecipe extends FillingRecipe {
    public BurgerSaucingRecipe(ProcessingRecipeParams params) {
        super(params);
    }

    @Override
    public ItemStack assemble(SingleRecipeInput t, HolderLookup.Provider provider) {
        ItemStack burger = new ItemStack(RFDItems.BURGER.get());

        List<ItemStack> allItems = new ArrayList<>();

        if (t.getItem(0).is(RFDItems.BURGER)) {
            allItems.addAll(t.getItem(0).getOrDefault(RFDDataComponents.BURGER_CONTENTS, BurgerContents.EMPTY).items);
        } else {
            allItems.add(t.getItem(0));
        }

        allItems.addAll(this.getRollableResults().stream().map(ProcessingOutput::rollOutput).toList());

        allItems = allItems.stream().map(ItemStack::copy).collect(Collectors.toList());
        allItems.forEach(allItem -> allItem.setCount(1));
        allItems.removeIf(ItemStack::isEmpty);

        BurgerContents.setBurger(burger, allItems);

        return burger;
    }
}
