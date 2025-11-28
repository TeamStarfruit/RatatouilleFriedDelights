package org.starfruit.ratatouillefrieddelights.data.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

import java.util.function.Supplier;

public class RFDLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, RatatouilleFriedDelights.MOD_ID);

    public static final Supplier<Codec<? extends IGlobalLootModifier>> ADD_TWO_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_two_item", RFDAddTwoItemModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
