package org.starfruit.ratatouillefrieddelights.entry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.belt.BeltModel;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import org.forsteri.ratatouille.entry.CRStress;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.cola_fruit.ColaFruitBlock;
import org.starfruit.ratatouillefrieddelights.content.cola_tree.RFDFlammableRotatedPillarBlock;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.ContinuousFryerBlock;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.FryerGenerator;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.FryerModel;
import org.starfruit.ratatouillefrieddelights.content.drum_processor.DrumProcessorBlock;
import org.starfruit.ratatouillefrieddelights.worldgen.tree.RFDTreeGrowers;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class RFDBlocks {

    static {
        RatatouilleFriedDelights.REGISTRATE.setCreativeTab(RFDCreativeModeTabs.BASE_CREATIVE_TAB);
    }

     public static final BlockEntry<ContinuousFryerBlock> CONTINUOUS_FRYER = RatatouilleFriedDelights.REGISTRATE
            .block("continuous_fryer", ContinuousFryerBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.METAL).sound(SoundType.METAL))
            .transform(pickaxeOnly())
            .blockstate(new FryerGenerator()::generate)
            .transform(RFDStress.setImpact(4F))
             .onRegister(CreateRegistrate.blockModel(() -> FryerModel::new))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), RatatouilleFriedDelights.asResource("block/continuous_fryer/item")))
            .build()
            .register();

    public static final BlockEntry<DrumProcessorBlock> DRUM_PROCESSOR = RatatouilleFriedDelights.REGISTRATE
            .block("drum_processor", DrumProcessorBlock::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.mapColor(MapColor.METAL).sound(SoundType.METAL))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p), 270))
            .transform(RFDStress.setImpact(4F))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), RatatouilleFriedDelights.asResource("block/drum_processor/item")))
            .build()
            .register();

    public static final BlockEntry<RFDFlammableRotatedPillarBlock> COLA_LOG =
            RatatouilleFriedDelights.REGISTRATE
                    .block("cola_log", RFDFlammableRotatedPillarBlock::new)
                    .tag(BlockTags.LOGS)
                    .tag(BlockTags.LOGS_THAT_BURN)
                    .tag(BlockTags.OVERWORLD_NATURAL_LOGS)
                    .initialProperties(() -> Blocks.OAK_LOG) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.WOOD)) // 可以额外改
                    .blockstate((c, p) -> p.axisBlock(c.getEntry())) //原木state注册
                    .transform(axeOnly())
                    .item()
                    .tag(ItemTags.LOGS)
                    .tag(ItemTags.LOGS_THAT_BURN)
                    .build()
                    .register();

    public static final BlockEntry<RFDFlammableRotatedPillarBlock> COLA_WOOD =
            RatatouilleFriedDelights.REGISTRATE
                    .block("cola_wood", RFDFlammableRotatedPillarBlock::new)
                    .tag(BlockTags.LOGS)
                    .tag(BlockTags.LOGS_THAT_BURN)
                    .tag(BlockTags.OVERWORLD_NATURAL_LOGS)
                    .initialProperties(() -> Blocks.OAK_WOOD) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.WOOD)) // 可以额外改
                    .transform(axeOnly())
                    .item()
                    .tag(ItemTags.LOGS)
                    .tag(ItemTags.LOGS_THAT_BURN)
                    .build()
                    .register();

    public static final BlockEntry<RFDFlammableRotatedPillarBlock> STRIPPED_COLA_LOG =
            RatatouilleFriedDelights.REGISTRATE
                    .block("stripped_cola_log", RFDFlammableRotatedPillarBlock::new)
                    .tag(BlockTags.LOGS)
                    .tag(BlockTags.LOGS_THAT_BURN)
                    .tag(BlockTags.OVERWORLD_NATURAL_LOGS)
                    .initialProperties(() -> Blocks.OAK_LOG) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.WOOD)) // 可以额外改
                    .blockstate((c, p) -> p.axisBlock(c.getEntry())) //原木state注册
                    .transform(axeOnly())
                    .item()
                    .tag(ItemTags.LOGS)
                    .tag(ItemTags.LOGS_THAT_BURN)
                    .build()
                    .register();

    public static final BlockEntry<RFDFlammableRotatedPillarBlock> STRIPPED_COLA_WOOD =
            RatatouilleFriedDelights.REGISTRATE
                    .block("stripped_cola_wood", RFDFlammableRotatedPillarBlock::new)
                    .tag(BlockTags.LOGS)
                    .tag(BlockTags.LOGS_THAT_BURN)
                    .tag(BlockTags.OVERWORLD_NATURAL_LOGS)
                    .initialProperties(() -> Blocks.OAK_WOOD) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.WOOD)) // 可以额外改
                    .transform(axeOnly())
                    .item()
                    .tag(ItemTags.LOGS)
                    .tag(ItemTags.LOGS_THAT_BURN)
                    .build()
                    .register();

    public static final BlockEntry<RFDFlammableRotatedPillarBlock> COLA_PLANKS =
            RatatouilleFriedDelights.REGISTRATE
                    .block("cola_planks", RFDFlammableRotatedPillarBlock::new)
                    .tag(BlockTags.PLANKS)
                    .initialProperties(() -> Blocks.OAK_PLANKS) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.WOOD)) // 可以额外改
                    .transform(axeOnly())
                    .item()
                    .tag(ItemTags.PLANKS)
                    .build()
                    .register();

    public static final BlockEntry<LeavesBlock> COLA_LEAVES =
            RatatouilleFriedDelights.REGISTRATE
                    .block("cola_leaves", LeavesBlock::new)
                    .initialProperties(() -> Blocks.OAK_LEAVES)
                    .properties(p -> p.strength(0.2F).randomTicks().noOcclusion().sound(SoundType.GRASS))
                    .tag(BlockTags.LEAVES)
                    .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models()
                            .cubeAll(ctx.getName(), prov.blockTexture(ctx.getEntry()))))
                    .loot((lt, block) -> lt.add(RFDBlocks.COLA_LEAVES.get(),
                            lt.createLeavesDrops(
                                    RFDBlocks.COLA_LEAVES.get(),   // 树叶
                                    RFDBlocks.COLA_SAPLING.get(),  // 树苗
                                    0.05F, 0.0625F, 0.0833F, 0.10F // Fortune 掉率
                            )
                    ))
                    .item()
                    .tag(ItemTags.LEAVES)
                    .build()
                    .register();

    public static final BlockEntry<SaplingBlock> COLA_SAPLING =
            RatatouilleFriedDelights.REGISTRATE
                    .block("cola_sapling", props -> new SaplingBlock(RFDTreeGrowers.COLA_TREE, props))
                    .tag(BlockTags.SAPLINGS)
                    .initialProperties(() -> Blocks.OAK_SAPLING) // 拷贝属性
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.standardModel(c, p)))
                    .properties(p -> p.strength(2.0F).sound(SoundType.CHERRY_SAPLING))
                    .item()
                    .model(
                            (c, p) ->
                                    p.generated(c, p.modLoc("block/" + p.name(c))))
                    .build()
                    .register();

    public static final BlockEntry<ColaFruitBlock> COLA_FRUIT_BLOCK =
            RatatouilleFriedDelights.REGISTRATE
                    .block("cola_fruit_block", ColaFruitBlock::new)
                    .initialProperties(() -> Blocks.COCOA)
                    .properties(p -> p.noCollission().randomTicks())
                    .blockstate((ctx, prov) -> {
                        prov.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
                            int age = state.getValue(ColaFruitBlock.AGE);
                            return ConfiguredModel.builder()
                                    .modelFile(prov.models().getExistingFile(prov.modLoc("block/cola_fruit_block/cola_fruit_block_stage" + age)))
                                    .build();
                        });
                    })
                    .loot((lt, block) -> {
                        LootItemCondition.Builder ripe = LootItemBlockStatePropertyCondition
                                .hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(ColaFruitBlock.AGE, 2)); // 成熟：AGE=2

                        lt.add(block,
                                LootTable.lootTable()
                                        // 成熟：掉 2~4 个
                                        .withPool(
                                                LootPool.lootPool()
                                                        .when(ripe)
                                                        .add(LootItem.lootTableItem(RFDItems.COLA_FRUITS.get())
                                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                                                        )
                                        )
                                        // 未成熟：固定掉 1 个
                                        .withPool(
                                                LootPool.lootPool()
                                                        .when(InvertedLootItemCondition.invert(ripe))
                                                        .add(LootItem.lootTableItem(RFDItems.COLA_FRUITS.get())
                                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                                        )
                                        )
                        );
                    })

                    .item()
                    .model((c, p) -> p.withExistingParent(c.getName(), RatatouilleFriedDelights.asResource("block/cola_fruit_block/item")))
                    .build()
                    .register();


    public static void register() {
    }
}