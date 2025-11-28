package org.starfruit.ratatouillefrieddelights.content.burger;

import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BurgerItem extends Item {
    private final Supplier<List<ItemStack>> defaultContents;

    public BurgerItem(Properties properties, Supplier<List<ItemStack>> defaultContents) {
        super(properties);
        this.defaultContents = defaultContents;
    }

    @SuppressWarnings("removal")
    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new BurgerRenderer()));
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        return BurgerItem.foodProperties(stack, entity);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        List<ItemStack> contents = defaultContents.get().stream().map(ItemStack::copy).collect(Collectors.toList());
        BurgerContents.setBurger(stack, contents);
        return stack;
    }

    public static FoodProperties foodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        List<ItemStack> items = BurgerContents.get(stack, new BurgerContents(List.of())).items;
        return BurgerItem.foodPropertiesFromComponents(items, entity);
    }

    public static FoodProperties foodPropertiesFromComponents(List<ItemStack> items, @Nullable LivingEntity entity) {
        List<FoodProperties> componentProperties = items.stream()
                .map(itemStack -> itemStack.getFoodProperties(entity))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        int nutrition = componentProperties.stream()
                .map(FoodProperties::getNutrition)
                .reduce(Integer::sum)
                .orElse(0);
        float saturation = componentProperties.stream()
                .map(FoodProperties::getSaturationModifier)
                .reduce(Float::sum)
                .orElse(0F);
        boolean alwaysEat = componentProperties.stream()
                .anyMatch(FoodProperties::canAlwaysEat);
        boolean fastFood = componentProperties.stream()
                .anyMatch(FoodProperties::isFastFood);
        boolean meat = componentProperties.stream()
                .anyMatch(FoodProperties::isMeat);

        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationMod(saturation);

        if (alwaysEat)
            builder.alwaysEat();
        if (fastFood)
            builder.fast();
        if (meat)
            builder.meat();

        componentProperties.stream()
                .map(FoodProperties::getEffects)
                .flatMap(Collection::stream)
                .forEach(pair -> builder.effect(() -> new MobEffectInstance(pair.getFirst()), pair.getSecond()));

        return builder.build();
    }
}
