package org.starfruit.ratatouillefrieddelights.content.burger;

import net.createmod.catnip.data.Couple;
import net.minecraft.world.item.Item;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import java.util.HashMap;

public class BurgerRenderingProperties {
    public static final HashMap<Item, Couple<Integer>> BURGER_RENDERING_PROPERTIES = new HashMap<>(); // Pixels, rendering starting from, height

    static {
        BURGER_RENDERING_PROPERTIES.put(
                RFDItems.BOTTOM_BURGER_BUN.get(),
                Couple.create(2, 2)
        );

        BURGER_RENDERING_PROPERTIES.put(
                RFDItems.HAMBURGER_PATTY.get(),
                Couple.create(1, 2)
        );

        BURGER_RENDERING_PROPERTIES.put(
                RFDItems.CHEESE_SLICE.get(),
                Couple.create(3, 3)
        );

        BURGER_RENDERING_PROPERTIES.put(
                RFDItems.SHREDDED_LETTUCE.get(),
                Couple.create(2, 1)
        );

        BURGER_RENDERING_PROPERTIES.put(
                RFDItems.TOMATO_SLICES.get(),
                Couple.create(3, 1)
        );

        BURGER_RENDERING_PROPERTIES.put(
                RFDItems.TOP_BURGER_BUN.get(),
                Couple.create(3, 2)
        );
    }
}
