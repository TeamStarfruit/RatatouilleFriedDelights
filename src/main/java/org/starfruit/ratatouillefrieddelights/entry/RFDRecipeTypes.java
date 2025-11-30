package org.starfruit.ratatouillefrieddelights.entry;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.burger.BurgerAssemblyRecipe;
import org.starfruit.ratatouillefrieddelights.content.burger.BurgerSaucingRecipe;
import org.starfruit.ratatouillefrieddelights.content.continuousfryer.FryingRecipe;
import org.starfruit.ratatouillefrieddelights.content.drumprocessor.CoatingRecipe;
import org.starfruit.ratatouillefrieddelights.content.drumprocessor.TumblingRecipe;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public enum RFDRecipeTypes implements IRecipeTypeInfo {

    COATING(CoatingRecipe::new),
    TUMBLING(TumblingRecipe::new),
    FRYING(FryingRecipe::new),


//    BURGER_ASSEMBLY(() -> new ItemApplicationRecipeS.Serializer<>(BurgerAssemblyRecipe::new), AllRecipeTypes.DEPLOYING::getType, false),
//    BURGER_SAUCING(() -> new StandardProcessingRecipe.Serializer<>(BurgerSaucingRecipe::new), AllRecipeTypes.FILLING::getType, false),
    BURGER_ASSEMBLY(() -> new ProcessingRecipeSerializer<>(BurgerAssemblyRecipe::new), AllRecipeTypes.DEPLOYING::getType, false),
    BURGER_SAUCING(() -> new ProcessingRecipeSerializer<>(BurgerSaucingRecipe::new), AllRecipeTypes.FILLING::getType, false),
    ;
    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    private final @Nullable RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;
    public final Supplier<RecipeSerializer<?>> serializerSupplier;


    RFDRecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory processingFactory) {
        this(() -> new ProcessingRecipeSerializer<>(processingFactory));
    }

    RFDRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(this.name());
        this.id = RatatouilleFriedDelights.asResource(name);
        this.serializerSupplier = serializerSupplier;
        this.serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        this.typeObject = Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(this.id));
        this.type = this.typeObject;
    }

    RFDRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
        String name = Lang.asId(name());
        id = RatatouilleFriedDelights.asResource(name);
        this.serializerSupplier = serializerSupplier;
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        if (registerType) {
            typeObject = Registers.TYPE_REGISTER.register(name, typeSupplier);
            type = typeObject;
        } else {
            typeObject = null;
            type = typeSupplier;
        }
    }

    public static void register(IEventBus modEventBus) {
        ShapedRecipe.setCraftingSize(3, 3);
        Registers.SERIALIZER_REGISTER.register(modEventBus);
        Registers.TYPE_REGISTER.register(modEventBus);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @Override
    public <T extends RecipeType<?>> T getType() {
        return (T) this.type.get();
    }

    public <C extends Container, R extends Recipe<C>> Optional<R> find(C inv, Level world) {
        return world.getRecipeManager()
                .getRecipeFor(getType(), inv, world);
    }

    public static @Nullable FryingRecipe findFryingRecipe(Level level, ItemStack item, FluidStack fluid, BlazeBurnerBlock.HeatLevel heatLevel) {
        if (level == null || item.isEmpty() || fluid.isEmpty())
            return null;

        return level.getRecipeManager()
                .getAllRecipesFor(RFDRecipeTypes.FRYING.getType())
                .stream()
                .map(recipe -> (FryingRecipe) ((Object) recipe))
                .filter(recipe -> recipe.matches(item, fluid, heatLevel))
                .findFirst()
                .orElse(null);
    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, RatatouilleFriedDelights.MOD_ID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, RatatouilleFriedDelights.MOD_ID);
    }
}
