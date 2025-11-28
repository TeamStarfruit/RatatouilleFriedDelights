package org.starfruit.ratatouillefrieddelights.compat.jei;

import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.compat.jei.category.*;
import org.starfruit.ratatouillefrieddelights.content.continuousfryer.FryingRecipe;
import org.starfruit.ratatouillefrieddelights.content.drumprocessor.CoatingRecipe;
import org.starfruit.ratatouillefrieddelights.content.drumprocessor.TumblingRecipe;
import org.starfruit.ratatouillefrieddelights.entry.RFDBlocks;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;
import org.starfruit.ratatouillefrieddelights.entry.RFDRecipeTypes;
import org.starfruit.ratatouillefrieddelights.util.Lang;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@JeiPlugin
@ParametersAreNonnullByDefault
public class RFDJei implements IModPlugin {
    private static final ResourceLocation ID = RatatouilleFriedDelights.asResource("jei_plugin");
    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();
    private IIngredientManager ingredientManager;

    public RFDJei() {}

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration){
        allCategories.clear();
        final CreateRecipeCategory<?> coating = builder(CoatingRecipe.class)
                .addTypedRecipes(RFDRecipeTypes.COATING::getType)
                .catalyst(RFDBlocks.DRUM_PROCESSOR::get)
                .itemIcon(RFDBlocks.DRUM_PROCESSOR.get())
                .emptyBackground(177, 53)
                .build("coating", CoatingCategory::new);

        final CreateRecipeCategory<?> tumbling = builder(TumblingRecipe.class)
                .addTypedRecipes(RFDRecipeTypes.TUMBLING::getType)
                .catalyst(RFDBlocks.DRUM_PROCESSOR::get)
                .itemIcon(RFDBlocks.DRUM_PROCESSOR.get())
                .emptyBackground(177, 53)
                .build("tumbling", TumblingCategory::new);

        final CreateRecipeCategory<?> frying = builder(FryingRecipe.class)
                .addTypedRecipes(RFDRecipeTypes.FRYING::getType)
                .catalyst(RFDBlocks.CONTINUOUS_FRYER::get)
                .itemIcon(RFDBlocks.CONTINUOUS_FRYER.get())
                .emptyBackground(160, 53)
                .build("frying", FryingCategory::new);
        allCategories.forEach(registration::addRecipeCategories);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(new ItemStack(RFDItems.COLA_FRUITS.get()), VanillaTypes.ITEM_STACK, Lang.translateDirect("jei.info.cola_fruits"));

        ingredientManager = registration.getIngredientManager();
        allCategories.forEach(c -> c.registerRecipes(registration));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
    }

    private <T extends Recipe<?>> RFDJei.CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
        return new RFDJei.CategoryBuilder<>(recipeClass);
    }

    private class CategoryBuilder<T extends Recipe<?>> {
        private final Class<? extends T> recipeClass;
        private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList<>();
        private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();
        private Predicate<CRecipes> predicate = cRecipes -> true;
        private IDrawable background;
        private IDrawable icon;

        public CategoryBuilder(Class<? extends T> recipeClass) {
            this.recipeClass = recipeClass;
        }

        @SuppressWarnings("unchecked")
        public RFDJei.CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipe -> {
                if (recipeClass.isInstance(recipe))
                    recipes.add((T) recipe);
            }, recipeType.get()));
        }

        public RFDJei.CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
            recipeListConsumers.add(consumer);
            return this;
        }

        public RFDJei.CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
            return catalystStack(() -> new ItemStack(supplier.get()
                    .asItem()));
        }

        public RFDJei.CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
            catalysts.add(supplier);
            return this;
        }

        public RFDJei.CategoryBuilder<T> itemIcon(ItemLike item) {
            icon(new ItemIcon(() -> new ItemStack(item)));
            return this;
        }

        public RFDJei.CategoryBuilder<T> icon(IDrawable icon) {
            this.icon = icon;
            return this;
        }

        public RFDJei.CategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
            icon(new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2)));
            return this;
        }

        public RFDJei.CategoryBuilder<T> emptyBackground(int width, int height) {
            background(new EmptyBackground(width, height));
            return this;
        }

        public RFDJei.CategoryBuilder<T> background(IDrawable background) {
            this.background = background;
            return this;
        }

        public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
            Supplier<List<T>> recipesSupplier;
            if (predicate.test(AllConfigs.server().recipes)) {
                recipesSupplier = () -> {
                    List<T> recipes = new ArrayList<>();
                    for (Consumer<List<T>> consumer : recipeListConsumers)
                        consumer.accept(recipes);
                    return recipes;
                };
            } else {
                recipesSupplier = Collections::emptyList;
            }

            CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info(new mezz.jei.api.recipe.RecipeType(RatatouilleFriedDelights.asResource(name), this.recipeClass), Lang.translateDirect("recipe." + name, new Object[0]), this.background, this.icon, recipesSupplier, this.catalysts);
            CreateRecipeCategory<T> category = factory.create(info);
            allCategories.add(category);
            return category;
        }
    }
}
