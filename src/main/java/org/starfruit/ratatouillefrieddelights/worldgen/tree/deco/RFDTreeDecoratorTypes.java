package org.starfruit.ratatouillefrieddelights.worldgen.tree.deco;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

public class RFDTreeDecoratorTypes {
    public static final DeferredRegister<TreeDecoratorType<?>> DECORATORS =
            DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, RatatouilleFriedDelights.MOD_ID);

    // 关键：TreeDecoratorType 现在接收 MapCodec
    public static final DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<ColaFruitDecorator>> COLA_FRUIT =
            DECORATORS.register("cola_fruit", () -> new TreeDecoratorType<>(ColaFruitDecorator.CODEC));

    public static void register(IEventBus bus) {
        DECORATORS.register(bus);
    }
}
