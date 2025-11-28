package org.starfruit.ratatouillefrieddelights.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.data.loot.RFDAddTwoItemModifier;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

public class RFDGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public RFDGlobalLootModifierProvider(PackOutput output) {
        super(output, RatatouilleFriedDelights.MOD_ID);
    }

    @Override
    protected void start() {

            this.add("ice_cubes_to_ice",
                new RFDAddTwoItemModifier(new LootItemCondition[]{
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.ICE).build(),
                        LootItemRandomChanceCondition.randomChance(1f).build()},
                        RFDItems.ICE_CUBES.get()
                ));
    }
}
