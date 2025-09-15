package org.starfruit.ratatouillefrieddelights.data.recipe;

import com.google.common.base.Supplier;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.data.recipe.BaseRecipeProvider;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import org.jetbrains.annotations.NotNull;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.data.recipe.RFDRecipeProvider;
import org.starfruit.ratatouillefrieddelights.entry.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;



@SuppressWarnings("unused")
public final class RFDStandardRecipeGen extends BaseRecipeProvider {
    final List<GeneratedRecipe> all = new ArrayList<>();

    GeneratedRecipe
            TOP_BURGER_BUN = create(RFDItems.TOP_BURGER_BUN)
            .unlockedBy(RFDItems.BURGER_BUN::asItem) // mandatory
            .viaShapeless(b -> b
                    .requires(RFDItems.BOTTOM_BURGER_BUN.get())
            ),
            BOTTOM_BURGER_BUN = create(RFDItems.BOTTOM_BURGER_BUN)
                    .unlockedBy(RFDItems.BURGER_BUN::asItem) // mandatory
                    .viaShapeless(b -> b
                            .requires(RFDItems.TOP_BURGER_BUN.get())
            ),
            TALL_CUP = create(RFDItems.TALL_CUP).returns(3)
            .unlockedBy(Items.GLASS::asItem) // mandatory
            .viaShaped(b -> b
                    .pattern("Y Y")
                    .pattern("Y Y")
                    .pattern(" Y ")
                    .define('Y', Items.GLASS)
            ),
            CARDBOARD_STRAW = create(RFDItems.CARDBOARD_STRAW).returns(4)
                    .unlockedBy(Items.GLASS::asItem) // mandatory
                    .viaShaped(b -> b
                            .pattern(" Y ")
                            .pattern("Y Y")
                            .pattern(" Y ")
                            .define('Y', AllItems.CARDBOARD)
                    );
    /*            CHEF_HAT_WITH_GOGGLES = create(CRItems.CHEF_HAT_WITH_GOGGLES)
                        .unlockedBy(CRItems.CHEF_HAT::get)
                        .viaShapeless(b -> b
                                .requires(CRItems.CHEF_HAT.get())
                                .requires(AllItems.GOGGLES.get())
                        ),
                COMPOST_TEA_BOTTLE = create(CRItems.COMPOST_TEA_BOTTLE)
                        .unlockedBy(CRFluids.COMPOST_TEA.getBucket()::get)
                        .viaShapeless(b -> b
                                .requires(CRFluids.COMPOST_TEA.getBucket().get())
                                .requires(Items.GLASS_BOTTLE, 8)
                        ),
                COMPOST_TOWER = create(CRBlocks.COMPOST_TOWER_BLOCK)
                        .unlockedBy(Items.BARREL::asItem) // mandatory
                        .viaShaped(b -> b
                                .pattern(" Y ")
                                .pattern(" X ")
                                .pattern(" Y ")
                                .define('Y', AllItems.ZINC_INGOT.get())
                                .define('X', Items.BARREL)
                        ),
                EGG_SHELL_TO_BONE_MEAL = create(Items.BONE_MEAL::asItem)
                        .unlockedBy(Items.EGG::asItem)
                        .viaShapeless(b -> b
                                .requires(CRItems.EGG_SHELL.get())
                        ),
                FROZEN_BLOCK = create(CRBlocks.FROZEN_BLOCK)
                        .unlockedBy(Items.BLUE_ICE::asItem) // mandatory
                        .viaShaped(b -> b
                                .pattern(" Y ")
                                .pattern("YXY")
                                .pattern(" Y ")
                                .define('Y', Items.BLUE_ICE)
                                .define('X', Items.POWDER_SNOW_BUCKET)
                        ),
                IRRIGATION_TOWER = create(CRBlocks.IRRIGATION_TOWER_BLOCK)
                        .unlockedBy(AllBlocks.FLUID_TANK::asItem) // mandatory
                        .viaShaped(b -> b
                                .pattern(" Z ")
                                .pattern("YXY")
                                .define('X', AllBlocks.FLUID_PIPE)
                                .define('Y', AllItems.COPPER_SHEET)
                                .define('Z', AllBlocks.FLUID_TANK)
                        ),
                MECHANICAL_DEMOLDER = create(CRBlocks.MECHANICAL_DEMOLDER)
                        .unlockedBy(AllBlocks.ANDESITE_CASING::asItem) // mandatory
                        .viaShaped(b -> b
                                .pattern(" Z ")
                                .pattern(" Y ")
                                .pattern(" X ")
                                .define('Z', AllItems.ANDESITE_ALLOY)
                                .define('Y', AllBlocks.ANDESITE_CASING)
                                .define('X', Items.SLIME_BALL)
                        ),
                ORGANIC_COMPOST = create(ModItems.ORGANIC_COMPOST::get)
                        .unlockedBy(CRItems.COMPOST_RESIDUE::asItem) // mandatory
                        .viaShaped(b -> b
                                .pattern(" Y ")
                                .pattern("YXY")
                                .pattern(" Y ")
                                .define('Y', CRItems.COMPOST_RESIDUE)
                                .define('X', Items.DIRT)
                        ),
                OVEN = create(CRBlocks.OVEN)
                        .unlockedBy(AllItems.ANDESITE_ALLOY::asItem) // mandatory
                        .viaShaped(b -> b
                                .pattern(" Y ")
                                .pattern(" X ")
                                .pattern(" Y ")
                                .define('Y', AllItems.ANDESITE_ALLOY)
                                .define('X', Items.BARREL)
                        ),
                OVEN_FAN = create(CRBlocks.OVEN_FAN)
                        .unlockedBy(AllBlocks.ANDESITE_CASING::asItem) // mandatory
                        .viaShaped(b -> b
                                .pattern(" Y ")
                                .pattern(" X ")
                                .pattern(" Z ")
                                .define('Y', AllBlocks.COGWHEEL)
                                .define('X', AllBlocks.ANDESITE_CASING)
                                .define('Z', AllItems.PROPELLER)
                        ),
                SPREADER = create(CRBlocks.SPREADER_BLOCK)
                        .unlockedBy(AllItems.ANDESITE_ALLOY::asItem) // mandatory
                        .viaShaped(b -> b
                                .pattern(" X ")
                                .pattern("ZSZ")
                                .pattern(" Y ")
                                .define('Y', AllItems.PROPELLER)
                                .define('S', AllBlocks.ANDESITE_CASING)
                                .define('Z', AllItems.TREE_FERTILIZER)
                                .define('X', AllBlocks.COGWHEEL)
                        ),
                SQUEEZE_BASIN = create(CRBlocks.SQUEEZE_BASIN)
                        .unlockedBy(Items.COPPER_INGOT::asItem) // mandatory
                        .viaShaped(b -> b
                                .pattern(" Z ")
                                .pattern("Y Y")
                                .pattern("YYY")
                                .define('Y', Items.COPPER_INGOT)
                                .define('Z', AllItems.COPPER_SHEET)
                        ),
                THRESHER = create(CRBlocks.THRESHER)
                        .unlockedBy(AllItems.ANDESITE_ALLOY::asItem) // mandatory
                        .viaShaped(b -> b
                                .pattern(" S ")
                                .pattern("ZXZ")
                                .pattern(" Y ")
                                .define('Y', AllBlocks.ANDESITE_CASING)
                                .define('Z', AllBlocks.SHAFT)
                                .define('S', AllItems.ANDESITE_ALLOY)
                                .define('X', AllBlocks.MECHANICAL_HARVESTER)
                        );
*/
        // smoking
        GeneratedRecipe
            PANCAKE_MOLD_BAKED = create(RFDItems.PANCAKE_MOLD_BAKED::get)
                .viaCooking(RFDItems.PANCAKE_MOLD_FILLED::get)
                .forDuration(200)
                .inSmoker(),
            CONE_MOLD_BAKED = create(RFDItems.CONE_MOLD_BAKED::get)
                    .viaCooking(RFDItems.CONE_MOLD_FILLED::get)
                    .forDuration(200)
                    .inSmoker(),
            CHEESE_BAKED = create(RFDItems.CHEESE::get)
                    .viaCooking(RFDItems.RAW_CHEESE::get)
                    .forDuration(200)
                    .inSmoker(),
            BURGER_BUN_MOLD_BAKED = create(RFDItems.BURGER_BUN_MOLD_BAKED::get)
                .viaCooking(RFDItems.BURGER_BUN_MOLD_UNBAKED::get)
                .forDuration(200)
                .inSmoker();

    public RFDStandardRecipeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, RatatouilleFriedDelights.MOD_ID);
    }

    GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike, ? extends ItemLike> result) {
        return create(result::get);
    }

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(result);
    }

    //@Override
    //public @NotNull String getName() {
    //    return "Create: RatatouilleFriedDelights's Processing Recipes";
    //}

    class GeneratedRecipeBuilder {

        List<ICondition> recipeConditions;
        private String suffix;
        private Supplier<? extends ItemLike> result;
        private ResourceLocation compatDatagenOutput;
        private Supplier<ItemPredicate> unlockedBy;
        private int amount;

        public GeneratedRecipeBuilder(Supplier<? extends ItemLike> result) {
            this();
            this.result = result;
        }

        private GeneratedRecipeBuilder() {
            this.recipeConditions = new ArrayList<>();
            this.suffix = "";
            this.amount = 1;
        }

        public GeneratedRecipeBuilder(ResourceLocation result) {
            this();
            this.compatDatagenOutput = result;
        }

        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        GeneratedRecipeBuilder whenModLoaded(String modid) {
            return withCondition(new ModLoadedCondition(modid));
        }

        GeneratedRecipeBuilder withCondition(ICondition condition) {
            recipeConditions.add(condition);
            return this;
        }

        GeneratedRecipeBuilder whenModMissing(String modid) {
            return withCondition(new NotCondition(new ModLoadedCondition(modid)));
        }

        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
            return register(consumer -> {
                ShapedRecipeBuilder b =
                        builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        private ResourceLocation createLocation(String recipeType) {
            return RatatouilleFriedDelights.asResource(recipeType + "/" + getRegistryName().getPath() + suffix);
        }

        private ResourceLocation getRegistryName() {
            return compatDatagenOutput == null ? RegisteredObjectsHelper.getKeyOrThrow(result.get()
                    .asItem()) : compatDatagenOutput;
        }

        GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
            return register(recipeOutput -> {
                ShapelessRecipeBuilder b =
                        builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));

                RecipeOutput conditionalOutput = recipeOutput.withConditions(recipeConditions.toArray(new ICondition[0]));

                b.save(conditionalOutput, createLocation("crafting"));
            });
        }

        GeneratedRecipe viaNetheriteSmithing(Supplier<? extends Item> base, Supplier<Ingredient> upgradeMaterial) {
            return register(consumer -> {
                SmithingTransformRecipeBuilder b =
                        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.of(base.get()), upgradeMaterial.get(), RecipeCategory.COMBAT, result.get()
                                        .asItem());
                b.unlocks("has_item", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(base.get())
                        .build()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        private ResourceLocation createSimpleLocation(String recipeType) {
            return RatatouilleFriedDelights.asResource(recipeType + "/" + getRegistryName().getPath() + suffix);
        }

        GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
            return unlockedBy(item).viaCookingIngredient(() -> Ingredient.of(item.get()));
        }

        GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
            return new GeneratedCookingRecipeBuilder(ingredient);
        }

        GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(item.get())
                    .build();
            return this;
        }

        GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
            return unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of(tag.get()));
        }

        GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(tag.get())
                    .build();
            return this;
        }

        class GeneratedCookingRecipeBuilder {

            private Supplier<Ingredient> ingredient;
            private float exp;
            private int cookingTime;

            GeneratedCookingRecipeBuilder(Supplier<Ingredient> ingredient) {
                this.ingredient = ingredient;
                cookingTime = 200;
                exp = 0;
            }

            GeneratedCookingRecipeBuilder forDuration(int duration) {
                cookingTime = duration;
                return this;
            }

            GeneratedCookingRecipeBuilder rewardXP(float xp) {
                exp = xp;
                return this;
            }

            GeneratedRecipe inFurnace() {
                return inFurnace(b -> b);
            }

            GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                return create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
            }

            private <T extends AbstractCookingRecipe> GeneratedRecipe create(RecipeSerializer<T> serializer,
                                                                             UnaryOperator<SimpleCookingRecipeBuilder> builder, AbstractCookingRecipe.Factory<T> factory, float cookingTimeModifier) {
                return register(recipeOutput -> {
                    boolean isOtherMod = compatDatagenOutput != null;

                    SimpleCookingRecipeBuilder b = builder.apply(SimpleCookingRecipeBuilder.generic(ingredient.get(),
                            RecipeCategory.MISC, isOtherMod ? Items.DIRT : result.get(), exp,
                            (int) (cookingTime * cookingTimeModifier), serializer, factory));
                    if (unlockedBy != null)
                        b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));

                    RecipeOutput conditionalOutput = recipeOutput.withConditions(recipeConditions.toArray(new ICondition[0]));

                    b.save(
//                            isOtherMod ? new ModdedCookingRecipeOutput(conditionalOutput, compatDatagenOutput) : conditionalOutput,
                            conditionalOutput,
                            createSimpleLocation(RegisteredObjectsHelper.getKeyOrThrow(serializer).getPath())
                    );
                });
            }

            GeneratedRecipe inSmoker() {
                return inSmoker(b -> b);
            }

            GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
                create(RecipeSerializer.CAMPFIRE_COOKING_RECIPE, builder, CampfireCookingRecipe::new, 3);
                return create(RecipeSerializer.SMOKING_RECIPE, builder, SmokingRecipe::new, .5f);
            }

            GeneratedRecipe inBlastFurnace() {
                return inBlastFurnace(b -> b);
            }

            GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(RecipeSerializer.SMELTING_RECIPE, builder, SmeltingRecipe::new, 1);
                return create(RecipeSerializer.BLASTING_RECIPE, builder, BlastingRecipe::new, .5f);
            }
        }
    }
}
