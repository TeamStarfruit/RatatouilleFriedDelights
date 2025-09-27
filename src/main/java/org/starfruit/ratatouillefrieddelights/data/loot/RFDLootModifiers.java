package org.starfruit.ratatouillefrieddelights.data.loot;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

import java.util.function.Supplier;

public class RFDLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RatatouilleFriedDelights.MOD_ID);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_TWO_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_two_item", () -> RFDAddTwoItemModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
