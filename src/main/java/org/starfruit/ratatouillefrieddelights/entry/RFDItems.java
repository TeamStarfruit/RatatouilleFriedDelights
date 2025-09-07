package org.starfruit.ratatouillefrieddelights.entry;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import org.forsteri.ratatouille.Ratatouille;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import net.minecraft.world.item.Item;
import org.starfruit.ratatouillefrieddelights.content.burger.BurgerItem;
import org.starfruit.ratatouillefrieddelights.content.cola_fruit.ColaFruitItem;

public class RFDItems {
    static {
        Ratatouille.REGISTRATE.setCreativeTab(RFDCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final ItemEntry<ColaFruitItem> COLA_FRUITS = RatatouilleFriedDelights.REGISTRATE.item("cola_fruits", ColaFruitItem::new)
            .properties(p -> p.food(new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.75f) // 200t=10s, amplifier 0=I, 概率75%
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0), 1.0f) // 15秒 速度I
            .alwaysEdible()                  // 饱腹时也能吃
            .fast()                       // “快速吃掉”
            .saturationModifier(1F)
            .build())).register();
    public static final ItemEntry<Item> FRENCH_FRIES = RatatouilleFriedDelights.REGISTRATE.item("french_fries", Item::new).register();
    public static final ItemEntry<Item> RAW_POTATO_STICK = RatatouilleFriedDelights.REGISTRATE.item("raw_potato_stick", Item::new).register();
    public static final ItemEntry<Item> ORIGINAL_RECIPE_CHICKEN_LEG = RatatouilleFriedDelights.REGISTRATE.item("original_recipe_chicken_leg", Item::new).register();
    public static final ItemEntry<Item> ORIGINAL_RECIPE_CHICKEN_BREAST = RatatouilleFriedDelights.REGISTRATE.item("original_recipe_chicken_breast", Item::new).register();
    public static final ItemEntry<Item> CHICKEN_NUGGETS = RatatouilleFriedDelights.REGISTRATE.item("chicken_nuggets", Item::new).register();
    public static final ItemEntry<Item> TOP_BURGER_BUN = RatatouilleFriedDelights.REGISTRATE.item("top_burger_bun", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> BOTTOM_BURGER_BUN = RatatouilleFriedDelights.REGISTRATE.item("bottom_burger_bun", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> BURGER_BUN = RatatouilleFriedDelights.REGISTRATE.item("burger_bun", Item::new).register();
    public static final ItemEntry<Item> HAMBURGER_PATTY = RatatouilleFriedDelights.REGISTRATE.item("hamburger_patty", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> SHREDDED_LETTUCE = RatatouilleFriedDelights.REGISTRATE.item("shredded_lettuce", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> TOMATO_SLICES = RatatouilleFriedDelights.REGISTRATE.item("tomato_slices", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> FISH_FILLET = RatatouilleFriedDelights.REGISTRATE.item("fish_fillet", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> CHEESE_SLICE = RatatouilleFriedDelights.REGISTRATE.item("cheese_slice", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> FRIED_APPLE_PIE = RatatouilleFriedDelights.REGISTRATE.item("fried_apple_pie", Item::new).register();
    public static final ItemEntry<BurgerItem> BURGER = RatatouilleFriedDelights.REGISTRATE.item("burger", BurgerItem::new)
            .tag(RFDTags.AllItemTags.BURGER_BASE.tag, RFDTags.AllItemTags.BURGER_TOPPINGS.tag) // Yes, this is how I implemented it, don't question
            .model((ctx, prov) -> prov.getBuilder("item/" + ctx.getName()))
            .register();


    public static void register() {}
}
