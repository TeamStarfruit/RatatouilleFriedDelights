package org.starfruit.ratatouillefrieddelights.entry;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
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

    public static final ItemEntry<ColaFruitItem> COLA_FRUITS = RatatouilleFriedDelights.REGISTRATE.item("cola_fruits", ColaFruitItem::new).register();
    public static final ItemEntry<Item> COLA_NUTS = RatatouilleFriedDelights.REGISTRATE.item("cola_nuts", Item::new).properties(p -> p.food(new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.75f) // 200t=10s, amplifier 0=I, 概率75%
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0), 1.0f) // 15秒 速度I
            .alwaysEdible()                  // 饱腹时也能吃
            .fast()                       // “快速吃掉”
            .saturationModifier(1F)
            .build())).register();

    public static final ItemEntry<Item> FRENCH_FRIES = RatatouilleFriedDelights.REGISTRATE.item("french_fries", Item::new).register();
    public static final ItemEntry<Item> RAW_POTATO_STICK = RatatouilleFriedDelights.REGISTRATE.item("raw_potato_stick", Item::new).register();

    public static final ItemEntry<Item> ORIGINAL_CHICKEN_DRUMSTICK = RatatouilleFriedDelights.REGISTRATE.item("original_chicken_drumstick", Item::new).register();
    public static final ItemEntry<Item> BREADED_ORIGINAL_DRUMSTICK = RatatouilleFriedDelights.REGISTRATE.item("breaded_original_drumstick", Item::new).register();
    public static final ItemEntry<Item> BATTERED_ORIGINAL_DRUMSTICK = RatatouilleFriedDelights.REGISTRATE.item("battered_original_drumstick", Item::new).register();
    public static final ItemEntry<Item> ORIGINAL_CHICKEN_KEEL = RatatouilleFriedDelights.REGISTRATE.item("original_chicken_keel", Item::new).register();
    public static final ItemEntry<Item> BREADED_ORIGINAL_KEEL = RatatouilleFriedDelights.REGISTRATE.item("breaded_original_keel", Item::new).register();
    public static final ItemEntry<Item> BATTERED_ORIGINAL_KEEL = RatatouilleFriedDelights.REGISTRATE.item("battered_original_keel", Item::new).register();
    public static final ItemEntry<Item> SECRET_SEASONING_POWDER = RatatouilleFriedDelights.REGISTRATE.item("secret_seasoning_powder", Item::new).register();

    public static final ItemEntry<Item> BUTTER = RatatouilleFriedDelights.REGISTRATE.item("butter", Item::new).register();
    public static final ItemEntry<Item> BREADCRUMB = RatatouilleFriedDelights.REGISTRATE.item("breadcrumb", Item::new).register();

    public static final ItemEntry<Item> BOX_OF_CHICKEN_NUGGETS = RatatouilleFriedDelights.REGISTRATE.item("box_of_chicken_nuggets", Item::new).register();
    public static final ItemEntry<Item> CHICKEN_NUGGETS = RatatouilleFriedDelights.REGISTRATE.item("chicken_nuggets", Item::new).register();
    public static final ItemEntry<Item> RAW_CHICKEN_NUGGETS = RatatouilleFriedDelights.REGISTRATE.item("raw_chicken_nuggets", Item::new).register();

    public static final ItemEntry<Item> CHEESE_SLICE = RatatouilleFriedDelights.REGISTRATE.item("cheese_slice", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> CHEESE = RatatouilleFriedDelights.REGISTRATE.item("cheese", Item::new).register();
    public static final ItemEntry<Item> RAW_CHEESE = RatatouilleFriedDelights.REGISTRATE.item("raw_cheese", Item::new).register();

    public static final ItemEntry<Item> FILLET_O_FISH = RatatouilleFriedDelights.REGISTRATE.item("fillet_o_fish", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> RAW_FILLET_O_FISH = RatatouilleFriedDelights.REGISTRATE.item("raw_fillet_o_fish", Item::new).register();
    public static final ItemEntry<Item> RAW_FISH_FILLET = RatatouilleFriedDelights.REGISTRATE.item("raw_fish_fillet", Item::new).register();

    public static final ItemEntry<Item> SUNFLOWER_SEED_OIL_BOTTLE = RatatouilleFriedDelights.REGISTRATE.item("sunflower_seed_oil_bottle", Item::new).register();
    public static final ItemEntry<Item> SUNFLOWER_SEEDS = RatatouilleFriedDelights.REGISTRATE.item("sunflower_seeds", Item::new).register();

    public static final ItemEntry<Item> TOP_BURGER_BUN = RatatouilleFriedDelights.REGISTRATE.item("top_burger_bun", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> BOTTOM_BURGER_BUN = RatatouilleFriedDelights.REGISTRATE.item("bottom_burger_bun", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> BURGER_BUN = RatatouilleFriedDelights.REGISTRATE.item("burger_bun", Item::new).register();
    public static final ItemEntry<Item> BURGER_BUN_MOLD_BAKED = RatatouilleFriedDelights.REGISTRATE.item("burger_bun_mold_baked", Item::new).register();
    public static final ItemEntry<Item> BURGER_BUN_MOLD_UNBAKED = RatatouilleFriedDelights.REGISTRATE.item("burger_bun_mold_unbaked", Item::new).register();
    public static final ItemEntry<Item> BURGER_BUN_MOLD = RatatouilleFriedDelights.REGISTRATE.item("burger_bun_mold", Item::new).register();

    public static final ItemEntry<Item> HAMBURGER_PATTY = RatatouilleFriedDelights.REGISTRATE.item("hamburger_patty", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> SHREDDED_LETTUCE = RatatouilleFriedDelights.REGISTRATE.item("shredded_lettuce", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();
    public static final ItemEntry<Item> TOMATO_SLICES = RatatouilleFriedDelights.REGISTRATE.item("tomato_slices", Item::new)
            .tag(RFDTags.AllItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).register();


    public static final ItemEntry<Item> FRIED_APPLE_PIE = RatatouilleFriedDelights.REGISTRATE.item("fried_apple_pie", Item::new).register();
    public static final ItemEntry<Item> RAW_APPLE_PIE = RatatouilleFriedDelights.REGISTRATE.item("raw_apple_pie", Item::new).register();
    public static final ItemEntry<Item> PUFF_PASTRY = RatatouilleFriedDelights.REGISTRATE.item("puff_pastry", Item::new).register();
    public static final ItemEntry<Item> APPLE_SLICES = RatatouilleFriedDelights.REGISTRATE.item("apple_slices", Item::new).register();
    public static final ItemEntry<SequencedAssemblyItem> UNPROCESSED_RAW_APPLE_PIE = RatatouilleFriedDelights.REGISTRATE.item("unprocessed_raw_apple_pie", SequencedAssemblyItem::new).register();

    public static final ItemEntry<Item> HOTCAKE_MEAL = RatatouilleFriedDelights.REGISTRATE.item("hotcake_meal", Item::new).register();
    public static final ItemEntry<SequencedAssemblyItem> UNPROCESSED_HOTCAKE_MEAL = RatatouilleFriedDelights.REGISTRATE.item("unprocessed_hotcake_meal", SequencedAssemblyItem::new).register();
    public static final ItemEntry<Item> PANCAKE = RatatouilleFriedDelights.REGISTRATE.item("pancake", Item::new).register();
    public static final ItemEntry<Item> PANCAKE_MOLD_BAKED = RatatouilleFriedDelights.REGISTRATE.item("pancake_mold_baked", Item::new).register();
    public static final ItemEntry<Item> PANCAKE_MOLD_FILLED = RatatouilleFriedDelights.REGISTRATE.item("pancake_mold_filled", Item::new).register();
    public static final ItemEntry<Item> PANCAKE_MOLD = RatatouilleFriedDelights.REGISTRATE.item("pancake_mold", Item::new).register();

    public static final ItemEntry<Item> ICE_CUBES = RatatouilleFriedDelights.REGISTRATE.item("ice_cubes", Item::new).register();
    public static final ItemEntry<Item> BOBA_CUP = RatatouilleFriedDelights.REGISTRATE.item("boba_cup", Item::new).register();
    public static final ItemEntry<Item> TALL_CUP = RatatouilleFriedDelights.REGISTRATE.item("tall_cup", Item::new).register();
    public static final ItemEntry<Item> SHORT_CUP = RatatouilleFriedDelights.REGISTRATE.item("short_cup", Item::new).register();
    public static final ItemEntry<Item> COLA = RatatouilleFriedDelights.REGISTRATE.item("cola", Item::new).register();

    public static final ItemEntry<Item> VANILLA_CONE = RatatouilleFriedDelights.REGISTRATE.item("vanilla_cone", Item::new).register();
    public static final ItemEntry<Item> CONE = RatatouilleFriedDelights.REGISTRATE.item("cone", Item::new).register();
    public static final ItemEntry<Item> ICE_CREAM = RatatouilleFriedDelights.REGISTRATE.item("ice_cream", Item::new).register();
    public static final ItemEntry<Item> CHOCOLATE_SUNDAE = RatatouilleFriedDelights.REGISTRATE.item("chocolate_sundae", Item::new).register();

    public static final ItemEntry<Item> TARTAR_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("tartar_topping", Item::new).register();
    public static final ItemEntry<Item> TOMATO_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("tomato_topping", Item::new).register();

    public static final ItemEntry<BurgerItem> BURGER = RatatouilleFriedDelights.REGISTRATE.item("burger", BurgerItem::new)
            .tag(RFDTags.AllItemTags.BURGER_BASE.tag, RFDTags.AllItemTags.BURGER_TOPPINGS.tag) // Yes, this is how I implemented it, don't question
            .model((ctx, prov) -> prov.getBuilder("item/" + ctx.getName()))
            .register();


    public static void register() {}
}
