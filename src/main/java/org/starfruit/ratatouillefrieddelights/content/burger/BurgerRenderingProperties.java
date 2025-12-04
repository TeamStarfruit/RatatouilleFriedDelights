package org.starfruit.ratatouillefrieddelights.content.burger;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.function.Supplier;

public interface BurgerRenderingProperties {
    int renderingPivot();
    int renderingHeight();
    Supplier<ItemStack> renderingItem();

    static BurgerRenderingProperties getBurgerRenderingProperties(ItemStack renderingItem) {
        return BURGER_RENDERING_PROPERTIES_MAP.getOrDefault(renderingItem.getItem(), SimpleBurgerRenderingProperties.of(4, 3, () ->
                renderingItem));
    }

    HashMap<Item, BurgerRenderingProperties> BURGER_RENDERING_PROPERTIES_MAP = new HashMap<>(); // Pixels, rendering starting from, height
}
