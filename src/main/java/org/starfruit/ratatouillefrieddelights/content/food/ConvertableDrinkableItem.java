package org.starfruit.ratatouillefrieddelights.content.food;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

import java.util.function.Supplier;

public class ConvertableDrinkableItem extends ConvertableEdibleItem {
    public ConvertableDrinkableItem(Properties properties) {
        super(properties, null);
    }

    public ConvertableDrinkableItem(Properties properties, Supplier<Item> convertsTo) {
        super(properties, convertsTo);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}
