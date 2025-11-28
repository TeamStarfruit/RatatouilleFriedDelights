package org.starfruit.ratatouillefrieddelights.content.food;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Properties;
import java.util.function.Supplier;

public class ConvertableEdibleItem extends Item {
    private final Supplier<Item> convertsTo;

    public ConvertableEdibleItem(Properties properties, Supplier<Item> convertsTo) {
        super(properties);
        this.convertsTo = convertsTo;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        var result = super.finishUsingItem(stack, level, entity);
        return convertsTo == null ? result : new ItemStack(convertsTo.get());
    }
}
