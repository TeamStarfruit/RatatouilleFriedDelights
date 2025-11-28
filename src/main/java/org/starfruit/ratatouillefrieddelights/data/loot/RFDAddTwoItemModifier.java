package org.starfruit.ratatouillefrieddelights.data.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import java.util.function.Supplier;

public class RFDAddTwoItemModifier extends LootModifier {
    public static final Supplier<Codec<RFDAddTwoItemModifier>> CODEC =
            Suppliers.memoize(() -> RecordCodecBuilder.create(inst ->
                    LootModifier.codecStart(inst).and(
                            BuiltInRegistries.ITEM.byNameCodec()
                                    .fieldOf("item")
                                    .forGetter(m -> m.item)
                    ).apply(inst, RFDAddTwoItemModifier::new)
            ));

    private final Item item;
    //https://docs.neoforged.net/docs/resources/server/loottables/glm
    public RFDAddTwoItemModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
        for (LootItemCondition condition : this.conditions) {
            if(!condition.test(lootContext)) {
                return generatedLoot;
            }
        }
        ItemStack stack = new ItemStack(this.item);
        stack.setCount(2);
        generatedLoot.add(stack);
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
