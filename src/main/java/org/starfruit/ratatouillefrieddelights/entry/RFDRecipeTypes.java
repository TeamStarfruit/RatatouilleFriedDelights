package org.starfruit.ratatouillefrieddelights.entry;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.burger.BurgerRecipe;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public enum RFDRecipeTypes implements IRecipeTypeInfo {
    BURGER(() -> new ItemApplicationRecipe.Serializer<>(BurgerRecipe::new), AllRecipeTypes.DEPLOYING::getType, false),;
    private final ResourceLocation id;
    private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> serializerObject;
    private final @Nullable DeferredHolder<RecipeType<?>, RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;
    public final Supplier<RecipeSerializer<?>> serializerSupplier;

    RFDRecipeTypes(StandardProcessingRecipe.Factory<?> processingFactory) {
        this(() -> new StandardProcessingRecipe.Serializer<>(processingFactory));
    }

    RFDRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(this.name());
        this.id = RatatouilleFriedDelights.asResource(name);
        this.serializerSupplier = serializerSupplier;
        this.serializerObject = RFDRecipeTypes.Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        this.typeObject = RFDRecipeTypes.Registers.TYPE_REGISTER.register(name, () -> {
            return RecipeType.simple(this.id);
        });
        this.type = this.typeObject;
    }

    RFDRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
        String name = Lang.asId(name());
        id = RatatouilleFriedDelights.asResource(name);
        this.serializerSupplier = serializerSupplier;
        serializerObject = RFDRecipeTypes.Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        if (registerType) {
            typeObject = RFDRecipeTypes.Registers.TYPE_REGISTER.register(name, typeSupplier);
            type = typeObject;
        } else {
            typeObject = null;
            type = typeSupplier;
        }
    }

    public static void register(IEventBus modEventBus) {
        ShapedRecipePattern.setCraftingSize(9, 9);
        RFDRecipeTypes.Registers.SERIALIZER_REGISTER.register(modEventBus);
        RFDRecipeTypes.Registers.TYPE_REGISTER.register(modEventBus);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
        return (RecipeType<R>) type.get();
    }

    public <I extends RecipeInput, R extends Recipe<I>> Optional<RecipeHolder<R>> find(I inv, Level world) {
        return world.getRecipeManager()
                .getRecipeFor(getType(), inv, world);
    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, RatatouilleFriedDelights.MOD_ID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, RatatouilleFriedDelights.MOD_ID);
    }
}
