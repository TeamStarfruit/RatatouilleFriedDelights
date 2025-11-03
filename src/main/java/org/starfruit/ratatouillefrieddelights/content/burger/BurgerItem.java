package org.starfruit.ratatouillefrieddelights.content.burger;

import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import org.starfruit.ratatouillefrieddelights.entry.RFDDataComponents;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BurgerItem extends Item {
    public BurgerItem(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("removal")
    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new BurgerRenderer()));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return super.getUseAnimation(stack);
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        return BurgerItem.foodProperties(stack, entity);
    }

    public static FoodProperties foodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        List<ItemStack> items = stack.getOrDefault(RFDDataComponents.BURGER_CONTENTS, new BurgerContents(List.of())).items;
        return BurgerItem.foodPropertiesFromComponents(items, entity);
    }

    public static FoodProperties foodPropertiesFromComponents(List<ItemStack> items, @Nullable LivingEntity entity) {
        Supplier<Stream<FoodProperties>> properties = () -> items.stream().map(
                itemStack -> itemStack.getFoodProperties(entity)
        ).filter(Objects::nonNull);
        return new FoodProperties(
                properties.get().map(FoodProperties::nutrition).reduce(Integer::sum).orElse(0),
                properties.get().map(FoodProperties::saturation).reduce(Float::sum).orElse(0F),
                properties.get().map(FoodProperties::canAlwaysEat).reduce(Boolean::logicalOr).orElse(false),
                properties.get().map(FoodProperties::eatSeconds).reduce(Float::sum).orElse(1.6F),
                Optional.empty(),
                List.of()
        );
    }

    // Copied from create
}
