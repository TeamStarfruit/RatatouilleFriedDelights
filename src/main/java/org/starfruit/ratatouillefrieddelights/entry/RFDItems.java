package org.starfruit.ratatouillefrieddelights.entry;

import com.tterrag.registrate.util.entry.ItemEntry;
import org.forsteri.ratatouille.Ratatouille;
import org.forsteri.ratatouille.entry.CRCreativeModeTabs;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import net.minecraft.world.item.Item;

public class RFDItems {
    static {
        Ratatouille.REGISTRATE.setCreativeTab(RFDCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final ItemEntry<Item> FRENCH_FRIES = RatatouilleFriedDelights.REGISTRATE.item("french_fries", Item::new).register();
    public static final ItemEntry<Item> RAW_POTATO_STICK = RatatouilleFriedDelights.REGISTRATE.item("raw_potato_stick", Item::new).register();
    public static final ItemEntry<Item> ORIGINAL_RECIPE_CHICKEN_LEG = RatatouilleFriedDelights.REGISTRATE.item("original_recipe_chicken_leg", Item::new).register();
    public static final ItemEntry<Item> ORIGINAL_RECIPE_CHICKEN_BREAST = RatatouilleFriedDelights.REGISTRATE.item("original_recipe_chicken_breast", Item::new).register();
    public static final ItemEntry<Item> CHICKEN_NUGGETS = RatatouilleFriedDelights.REGISTRATE.item("chicken_nuggets", Item::new).register();
    public static final ItemEntry<Item> TOP_BURGER_BUN = RatatouilleFriedDelights.REGISTRATE.item("top_burger_bun", Item::new).register();
    public static final ItemEntry<Item> BOTTOM_BURGER_BUN = RatatouilleFriedDelights.REGISTRATE.item("bottom_burger_bun", Item::new).register();
    public static final ItemEntry<Item> FISH_FILLET = RatatouilleFriedDelights.REGISTRATE.item("fish_fillet", Item::new).register();
    public static final ItemEntry<Item> CHEESE_SLICE = RatatouilleFriedDelights.REGISTRATE.item("cheese_slice", Item::new).register();
    public static final ItemEntry<Item> FRIED_APPLE_PIE = RatatouilleFriedDelights.REGISTRATE.item("fried_apple_pie", Item::new).register();


    public static void register() {}
}
