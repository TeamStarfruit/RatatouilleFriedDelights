package org.starfruit.ratatouillefrieddelights.content.burger;

import net.minecraft.world.item.ItemStack;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Supplier;

public record SimpleBurgerRenderingProperties(int renderingPivot, int renderingHeight, Supplier<ItemStack> renderingItem) implements BurgerRenderingProperties {
    public static SimpleBurgerRenderingProperties of(int renderingPivot, int renderingHeight, Supplier<ItemStack> renderingItem) {
        return new SimpleBurgerRenderingProperties(renderingPivot, renderingHeight, renderingItem);
    }
    
    static {
        BURGER_RENDERING_PROPERTIES_MAP.put(
                RFDItems.BOTTOM_BURGER_BUN.get(),
                of(2, 3, RFDItems.BOTTOM_BURGER_BUN::asStack)
        );

        BURGER_RENDERING_PROPERTIES_MAP.put(
                ModItems.BEEF_PATTY.get(),
                of(2, 2, RFDItems.HAMBURGER_PATTY::asStack)
        );

        BURGER_RENDERING_PROPERTIES_MAP.put(
                RFDItems.FILLET_O_FISH.get(),
                of(3, 4, RFDItems.FILLET_O_FISH::asStack)
        );

        BURGER_RENDERING_PROPERTIES_MAP.put(
                RFDItems.CHEESE_SLICE.get(),
                of(5, 0, RFDItems.CHEESE_SLICE::asStack)
        );

        BURGER_RENDERING_PROPERTIES_MAP.put(
                ModItems.CABBAGE_LEAF.get(),
                of(1, 2, RFDItems.SHREDDED_LETTUCE::asStack)
        );

        BURGER_RENDERING_PROPERTIES_MAP.put(
                RFDItems.TOMATO_SLICES.get(),
                of(3, 1, RFDItems.TOMATO_INGREDIENT::asStack)
        );

        BURGER_RENDERING_PROPERTIES_MAP.put(
                RFDItems.TOP_BURGER_BUN.get(),
                of(3, 2, RFDItems.TOP_BURGER_BUN::asStack)
        );

        BURGER_RENDERING_PROPERTIES_MAP.put(
                RFDItems.TARTAR_TOPPING.get(),
                of(6, 1, RFDItems.TARTAR_TOPPING::asStack)
        );

        BURGER_RENDERING_PROPERTIES_MAP.put(
                RFDItems.KETCHUP_TOPPING.get(),
                of(6, 1, RFDItems.KETCHUP_TOPPING::asStack)
        );
    }
}
