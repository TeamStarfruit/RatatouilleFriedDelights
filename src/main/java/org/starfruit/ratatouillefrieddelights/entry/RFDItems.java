package org.starfruit.ratatouillefrieddelights.entry;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.item.ItemDescription;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.forsteri.ratatouille.Ratatouille;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import net.minecraft.world.item.Item;
import org.starfruit.ratatouillefrieddelights.content.dipcup.*;
import org.starfruit.ratatouillefrieddelights.content.food.ConvertableEdibleItem;
import org.starfruit.ratatouillefrieddelights.content.food.ConvertableDrinkableItem;
import org.starfruit.ratatouillefrieddelights.content.burger.BurgerItem;
import org.starfruit.ratatouillefrieddelights.content.colafruit.ColaFruitItem;
import vectorwing.farmersdelight.common.registry.ModEffects;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.List;
import java.util.function.Supplier;

public class RFDItems {
    static {
        Ratatouille.REGISTRATE.setCreativeTab(RFDCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final ItemEntry<ColaFruitItem> COLA_FRUITS = RatatouilleFriedDelights.REGISTRATE.item("cola_fruits", ColaFruitItem::new).register();
    public static final ItemEntry<Item> COLA_NUTS = RatatouilleFriedDelights.REGISTRATE.item("cola_nuts", Item::new).properties(p -> p.food(new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.75f) // 200t=10s, amplifier 0=I, 75%
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0), 1.0f) // 15?I
            .alwaysEat()                  // 
            .fast()                       // ?
            .saturationMod(1F)
            .build())).register();

    public static final ItemEntry<Item> SUNFLOWER_SEEDS = RatatouilleFriedDelights.REGISTRATE.item("sunflower_seeds", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.2f)
                    .alwaysEat().fast()
                    .build()))
            .register();

    public static final ItemEntry<DipableItem> A_FRY = RatatouilleFriedDelights.REGISTRATE
            .item("a_fry", DipableItem::new)
            .properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.2f)
                    .alwaysEat()
                    .build()))
            .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), "item/generated")
                    .texture("layer0", prov.modLoc("item/" + ctx.getName()))
                    .texture("layer1", prov.modLoc("item/" + ctx.getName() + "_partial"))
                    .texture("layer2", prov.modLoc("item/" + ctx.getName() + "_dip")))
            .color(() -> DipColor::new)
            .register();
    public static final ItemEntry<Item> FRENCH_FRIES = RatatouilleFriedDelights.REGISTRATE.item("french_fries", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(6).saturationMod(0.6f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> RAW_POTATO_STICKS = RatatouilleFriedDelights.REGISTRATE.item("raw_potato_sticks", Item::new).register();
    public static final ItemEntry<Item> PEELED_POTATO = RatatouilleFriedDelights.REGISTRATE.item("peeled_potato", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.3f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<SequencedAssemblyItem> UNPROCESSED_DUO_CHICKEN_BUCKET = RatatouilleFriedDelights.REGISTRATE.item("unprocessed_duo_chicken_bucket", SequencedAssemblyItem::new).register();
    public static final ItemEntry<ConvertableEdibleItem> ORIGINAL_CHICKEN_DRUMSTICK = RatatouilleFriedDelights.REGISTRATE.item("original_chicken_drumstick", p -> new ConvertableEdibleItem(p, () -> Items.BONE)).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(7).saturationMod(0.8f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> BREADED_ORIGINAL_DRUMSTICK = RatatouilleFriedDelights.REGISTRATE.item("breaded_original_drumstick", Item::new).register();
    public static final ItemEntry<Item> BATTERED_ORIGINAL_DRUMSTICK = RatatouilleFriedDelights.REGISTRATE.item("battered_original_drumstick", Item::new).register();
    public static final ItemEntry<ConvertableEdibleItem> ORIGINAL_CHICKEN_KEEL = RatatouilleFriedDelights.REGISTRATE.item("original_chicken_keel", p -> new ConvertableEdibleItem(p, () -> Items.BONE)).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(7).saturationMod(0.8f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> BREADED_ORIGINAL_KEEL = RatatouilleFriedDelights.REGISTRATE.item("breaded_original_keel", Item::new).register();
    public static final ItemEntry<Item> BATTERED_ORIGINAL_KEEL = RatatouilleFriedDelights.REGISTRATE.item("battered_original_keel", Item::new).register();
    public static final ItemEntry<Item> SECRET_SEASONING_POWDER = RatatouilleFriedDelights.REGISTRATE.item("secret_seasoning_powder", Item::new).register();

    public static final ItemEntry<Item> BUTTER = RatatouilleFriedDelights.REGISTRATE.item("butter", Item::new).register();
    public static final ItemEntry<Item> BREADCRUMB = RatatouilleFriedDelights.REGISTRATE.item("breadcrumb", Item::new).register();

    public static final ItemEntry<DipableItem> A_CHICKEN_NUGGET = RatatouilleFriedDelights.REGISTRATE.item("a_chicken_nugget",
                    DipableItem::new)
            .properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.2f)
                    .alwaysEat()
                    .build()))
            .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), "item/generated")
                    .texture("layer0", prov.modLoc("item/" + ctx.getName()))
                    .texture("layer1", prov.modLoc("item/" + ctx.getName() + "_partial"))
                    .texture("layer2", prov.modLoc("item/" + ctx.getName() + "_dip")))
            .color(() -> DipColor::new)
            .register();
    public static final ItemEntry<Item> CHICKEN_NUGGETS = RatatouilleFriedDelights.REGISTRATE.item("chicken_nuggets", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(5).saturationMod(0.6f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> RAW_CHICKEN_NUGGETS = RatatouilleFriedDelights.REGISTRATE.item("raw_chicken_nuggets", Item::new).register();

    public static final ItemEntry<Item> CHEESE_SLICE = RatatouilleFriedDelights.REGISTRATE.item("cheese_slice", Item::new)
            .tag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(3).saturationMod(0.4f)
                    .alwaysEat()
                    .build()))
            .tag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag)
            .register();
    public static final ItemEntry<Item> CHEESE = RatatouilleFriedDelights.REGISTRATE.item("cheese", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(5).saturationMod(0.6f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> RAW_CHEESE = RatatouilleFriedDelights.REGISTRATE.item("raw_cheese", Item::new).register();

    public static final ItemEntry<Item> FILLET_O_FISH = RatatouilleFriedDelights.REGISTRATE.item("fillet_o_fish", Item::new)
            .properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(7).saturationMod(0.7f)
                    .alwaysEat()
                    .build()))
            .tag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag)
            .register();
    public static final ItemEntry<Item> BREADED_FISH_FILLET = RatatouilleFriedDelights.REGISTRATE.item("breaded_fish_fillet", Item::new).register();
    public static final ItemEntry<Item> RAW_FISH_FILLET = RatatouilleFriedDelights.REGISTRATE.item("raw_fish_fillet", Item::new).register();

    public static final ItemEntry<Item> TOP_BURGER_BUN = RatatouilleFriedDelights.REGISTRATE.item("top_burger_bun", Item::new)
            .properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(3).saturationMod(0.4f)
                    .alwaysEat()
                    .build()))
            .tag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag)
            .register();
    public static final ItemEntry<Item> BOTTOM_BURGER_BUN = RatatouilleFriedDelights.REGISTRATE.item("bottom_burger_bun", Item::new)
            .properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(3).saturationMod(0.4f)
                    .alwaysEat()
                    .build()))
            .tag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag)
            .register();
    public static final ItemEntry<Item> BURGER_BUN = RatatouilleFriedDelights.REGISTRATE.item("burger_bun", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(5).saturationMod(0.6f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> BURGER_BUN_MOLD_BAKED = RatatouilleFriedDelights.REGISTRATE.item("burger_bun_mold_baked", Item::new).register();
    public static final ItemEntry<Item> BURGER_BUN_MOLD_UNBAKED = RatatouilleFriedDelights.REGISTRATE.item("burger_bun_mold_unbaked", Item::new).register();
    public static final ItemEntry<Item> BURGER_BUN_MOLD = RatatouilleFriedDelights.REGISTRATE.item("burger_bun_mold", Item::new).register();

    public static final ItemEntry<Item> HAMBURGER_PATTY = RatatouilleFriedDelights.REGISTRATE.item("hamburger_patty", Item::new)
            .tag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENT_RENDERING_HELPER.tag) //tag
            .register();
    public static final ItemEntry<Item> SHREDDED_LETTUCE = RatatouilleFriedDelights.REGISTRATE.item("shredded_lettuce", Item::new)
            .tag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENT_RENDERING_HELPER.tag)
            .register();
    public static final ItemEntry<Item> TOMATO_SLICES = RatatouilleFriedDelights.REGISTRATE.item("tomato_slices", Item::new)
            .properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.3f)
                    .alwaysEat().fast()
                    .build()))
            .tag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENTS.tag)
            .register();
    public static final ItemEntry<Item> TOMATO_INGREDIENT = RatatouilleFriedDelights.REGISTRATE.item("tomato_ingredient", Item::new)
            .tag(RFDTags.RFDItemTags.RATATOUILLE_BURGER_INGREDIENT_RENDERING_HELPER.tag).register();


    public static final ItemEntry<Item> FRIED_APPLE_PIE = RatatouilleFriedDelights.REGISTRATE.item("fried_apple_pie", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(8).saturationMod(0.6f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> RAW_APPLE_PIE = RatatouilleFriedDelights.REGISTRATE.item("raw_apple_pie", Item::new).register();
    public static final ItemEntry<Item> PUFF_PASTRY = RatatouilleFriedDelights.REGISTRATE.item("puff_pastry", Item::new).register();
    public static final ItemEntry<Item> APPLE_SLICES = RatatouilleFriedDelights.REGISTRATE.item("apple_slices", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.3f)
                    .alwaysEat().fast()
                    .build()))
            .register();
    public static final ItemEntry<SequencedAssemblyItem> UNPROCESSED_RAW_APPLE_PIE = RatatouilleFriedDelights.REGISTRATE.item("unprocessed_raw_apple_pie", SequencedAssemblyItem::new).register();

    public static final ItemEntry<Item> ONION_RINGS = RatatouilleFriedDelights.REGISTRATE.item("onion_rings", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(8).saturationMod(0.6f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> BREADED_ONION_RINGS = RatatouilleFriedDelights.REGISTRATE.item("breaded_onion_rings", Item::new).register();

    public static final ItemEntry<Item> HOT_DOG = RatatouilleFriedDelights.REGISTRATE.item("hot_dog", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(6).saturationMod(0.4f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> CHOCOLATE_DONUT = RatatouilleFriedDelights.REGISTRATE.item("chocolate_donut", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(9).saturationMod(0.9f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> CREAMY_DONUT = RatatouilleFriedDelights.REGISTRATE.item("creamy_donut", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(9).saturationMod(0.9f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> FRIED_DONUT = RatatouilleFriedDelights.REGISTRATE.item("fried_donut", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(7).saturationMod(0.7f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> DOUGH_RING = RatatouilleFriedDelights.REGISTRATE.item("dough_ring", Item::new).register();


    public static final ItemEntry<Item> HOTCAKE_MEAL = RatatouilleFriedDelights.REGISTRATE.item("hotcake_meal", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(10).saturationMod(0.8f)
                    .effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 2000, 1),1.0f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<SequencedAssemblyItem> UNPROCESSED_HOTCAKE_MEAL = RatatouilleFriedDelights.REGISTRATE.item("unprocessed_hotcake_meal", SequencedAssemblyItem::new).register();
    public static final ItemEntry<Item> PANCAKE = RatatouilleFriedDelights.REGISTRATE.item("pancake", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(3).saturationMod(0.5f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<Item> PANCAKE_MOLD_BAKED = RatatouilleFriedDelights.REGISTRATE.item("pancake_mold_baked", Item::new).register();
    public static final ItemEntry<Item> PANCAKE_MOLD_FILLED = RatatouilleFriedDelights.REGISTRATE.item("pancake_mold_filled", Item::new).register();
    public static final ItemEntry<Item> PANCAKE_MOLD = RatatouilleFriedDelights.REGISTRATE.item("pancake_mold", Item::new).register();

    public static final ItemEntry<Item> VANILLA_CONE = RatatouilleFriedDelights.REGISTRATE.item("vanilla_cone", Item::new)
            .properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.2f)
            .alwaysEat()                 // ?
            .saturationMod(1F)
            .build())).register();
    public static final ItemEntry<Item> CONE = RatatouilleFriedDelights.REGISTRATE.item("cone", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(1).saturationMod(0.1f)
                    .alwaysEat().fast()
                    .build()))
            .register();
    public static final ItemEntry<Item> CONE_MOLD_BAKED = RatatouilleFriedDelights.REGISTRATE.item("cone_mold_baked", Item::new).register();
    public static final ItemEntry<Item> CONE_MOLD_FILLED = RatatouilleFriedDelights.REGISTRATE.item("cone_mold_filled", Item::new).register();
    public static final ItemEntry<Item> CONE_MOLD = RatatouilleFriedDelights.REGISTRATE.item("cone_mold", Item::new).register();

    public static final ItemEntry<Item> ICE_CUBES = RatatouilleFriedDelights.REGISTRATE.item("ice_cubes", Item::new).register();
    public static final ItemEntry<Item> CARDBOARD_STRAW = RatatouilleFriedDelights.REGISTRATE.item("cardboard_straw", Item::new).register();
    public static final ItemEntry<Item> BOBA_CUP = RatatouilleFriedDelights.REGISTRATE.item("boba_cup", Item::new)
            .onRegisterAfter(Registries.ITEM, item -> ItemDescription.useKey(item, "item.ratatouille_fried_delights.boba_cup"))
            .register();
    public static final ItemEntry<Item> TALL_CUP = RatatouilleFriedDelights.REGISTRATE.item("tall_cup", Item::new).register();
    public static final ItemEntry<Item> SHORT_CUP = RatatouilleFriedDelights.REGISTRATE.item("short_cup", Item::new).register();
    public static final ItemEntry<ConvertableDrinkableItem> COLA = RatatouilleFriedDelights.REGISTRATE.item("cola", p -> new ConvertableDrinkableItem(p, TALL_CUP)).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(3).saturationMod(0.2f)
                    .alwaysEat()
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 20, 1), 1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 20, 1), 1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.JUMP, 20 * 20, 1), 1.0f)
                    .build()))
            .register();
    public static final ItemEntry<SequencedAssemblyItem> UNPROCESSED_COLA = RatatouilleFriedDelights.REGISTRATE.item("unprocessed_cola", SequencedAssemblyItem::new).register();

    public static final ItemEntry<ConvertableEdibleItem> ICE_CREAM = RatatouilleFriedDelights.REGISTRATE.item("ice_cream", p -> new ConvertableEdibleItem(p, SHORT_CUP)).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(4).saturationMod(0.6f)
                    .alwaysEat()
                    .build()))
            .register();
    public static final ItemEntry<ConvertableEdibleItem> CHOCOLATE_SUNDAE = RatatouilleFriedDelights.REGISTRATE.item("chocolate_sundae", p -> new ConvertableEdibleItem(p, SHORT_CUP)).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(6).saturationMod(0.8f)
                    .alwaysEat()
                    .build()))
            .register();

    public static final ItemEntry<Item> SUNFLOWER_SEED_OIL_BOTTLE = RatatouilleFriedDelights.REGISTRATE.item("sunflower_seed_oil_bottle", Item::new).register();

    public static final ItemEntry<Item> NO42_CONCRETE_MIXING_PASTA = RatatouilleFriedDelights.REGISTRATE.item("no.42_concrete_mixing_pasta", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(4).saturationMod(0.2f)
                    .alwaysEat()
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 20 * 10, 1), 1.0f)
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 10, 3), 1.0f)
                    .build()))
            .onRegisterAfter(Registries.ITEM, item -> ItemDescription.useKey(item, "item.ratatouille_fried_delights.no.42_concrete_mixing_pasta"))
            .register();
    public static final ItemEntry<Item> FRIED_RESIDUE = RatatouilleFriedDelights.REGISTRATE.item("fried_residue", Item::new).properties(p -> p.food(new FoodProperties.Builder()
                    .nutrition(2).saturationMod(0.1f)
                    .alwaysEat()
                    .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 20 * 5, 1), 0.5f)
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 5, 1), 0.5f)
                    .build()))
            .register();

    public static final ItemEntry<Item> TARTAR_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("tartar_topping", Item::new).register();
    public static final ItemEntry<Item> KETCHUP_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("ketchup_topping", Item::new).register();
    public static final ItemEntry<Item> MAYONNAISE_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("mayonnaise_topping", Item::new).register();

    public static final ItemEntry<Item> BOBA_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("boba_topping", Item::new).register();
    public static final ItemEntry<Item> GLOW_BERRIES_BALL_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("glow_berries_ball_topping", Item::new).register();
    public static final ItemEntry<Item> CHORUS_BOBA_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("chorus_boba_topping", Item::new).register();
    public static final ItemEntry<Item> SLIME_JELLY_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("slime_jelly_topping", Item::new).register();
    public static final ItemEntry<Item> COOKIES_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("cookies_topping", Item::new).register();
    public static final ItemEntry<Item> TEA_BOTTOM_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("tea_bottom_topping", Item::new).register();
    public static final ItemEntry<Item> TEA_TOP_TOPPING = RatatouilleFriedDelights.REGISTRATE.item("tea_top_topping", Item::new).register();

    private static final Supplier<List<ItemStack>> defaultBurger = () -> List.of(
            new ItemStack(RFDItems.BOTTOM_BURGER_BUN.get()),
            new ItemStack(ModItems.BEEF_PATTY.get()),
            new ItemStack(RFDItems.CHEESE_SLICE.get()),
            new ItemStack(ModItems.CABBAGE_LEAF.get()),
            new ItemStack(RFDItems.TOMATO_SLICES.get()),
            new ItemStack(RFDItems.TOP_BURGER_BUN.get())
    );

    public static final ItemEntry<BurgerItem> BURGER = RatatouilleFriedDelights.REGISTRATE.item("burger", properties -> new BurgerItem(properties, defaultBurger))
            .properties(
                    properties -> properties.food(BurgerItem.foodPropertiesFromComponents(defaultBurger.get(), null))
            )
            .tag(RFDTags.RFDItemTags.BURGER_BASE.tag, RFDTags.RFDItemTags.BURGER_TOPPINGS.tag) // Yes, this is how I implemented it, don't question
            .model((ctx, prov) -> prov.getBuilder("item/" + ctx.getName()).texture("particle", RatatouilleFriedDelights.asResource("item/empty")))
            .register();
    public static void register() {}
}

