package org.starfruit.ratatouillefrieddelights.entry;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.MapColor;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.cola_tree.RFDFlammableRotatedPillarBlock;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.ContinuousFryerBlock;
import org.starfruit.ratatouillefrieddelights.worldgen.tree.RFDTreeGrowers;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOnly;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class RFDBlocks {

    static {
        RatatouilleFriedDelights.REGISTRATE.setCreativeTab(RFDCreativeModeTabs.BASE_CREATIVE_TAB);
    }

     public static final BlockEntry<ContinuousFryerBlock> CONTINUOUS_FRYER = RatatouilleFriedDelights.REGISTRATE
            .block("continuous_fryer", ContinuousFryerBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.METAL).sound(SoundType.METAL))
            .transform(pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), AssetLookup.partialBaseModel(ctx, prov)))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), RatatouilleFriedDelights.asResource("block/continuous_fryer/continuous_fryer_single")))
            .build()
            .register();

    public static final BlockEntry<RFDFlammableRotatedPillarBlock> COLA_LOG =
            RatatouilleFriedDelights.REGISTRATE
                    .block("cola_log", RFDFlammableRotatedPillarBlock::new)
                    .initialProperties(() -> Blocks.OAK_LOG) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.WOOD)) // 可以额外改
                    .transform(axeOnly())
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<RFDFlammableRotatedPillarBlock> COLA_WOOD =
            RatatouilleFriedDelights.REGISTRATE
                    .block("cola_wood", RFDFlammableRotatedPillarBlock::new)
                    .initialProperties(() -> Blocks.OAK_WOOD) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.WOOD)) // 可以额外改
                    .transform(axeOnly())
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<RFDFlammableRotatedPillarBlock> STRIPPED_COLA_LOG =
            RatatouilleFriedDelights.REGISTRATE
                    .block("stripped_cola_log", RFDFlammableRotatedPillarBlock::new)
                    .initialProperties(() -> Blocks.OAK_LOG) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.WOOD)) // 可以额外改
                    .transform(axeOnly())
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<RFDFlammableRotatedPillarBlock> STRIPPED_COLA_WOOD =
            RatatouilleFriedDelights.REGISTRATE
                    .block("stripped_cola_wood", RFDFlammableRotatedPillarBlock::new)
                    .initialProperties(() -> Blocks.OAK_WOOD) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.WOOD)) // 可以额外改
                    .transform(axeOnly())
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<RFDFlammableRotatedPillarBlock> COLA_PLANKS =
            RatatouilleFriedDelights.REGISTRATE
                    .block("cola_planks", RFDFlammableRotatedPillarBlock::new)
                    .initialProperties(() -> Blocks.OAK_PLANKS) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.WOOD)) // 可以额外改
                    .transform(axeOnly())
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<LeavesBlock> COLA_LEAVES =
            RatatouilleFriedDelights.REGISTRATE
                    .block("cola_leaves", LeavesBlock::new)
                    .initialProperties(() -> Blocks.OAK_LEAVES) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.CHERRY_LEAVES))
                    .transform(axeOnly())
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<SaplingBlock> COLA_SAPLING =
            RatatouilleFriedDelights.REGISTRATE
                    .block("cola_sapling", props -> new SaplingBlock(RFDTreeGrowers.COLA_TREE, props))
                    .initialProperties(() -> Blocks.OAK_SAPLING) // 拷贝属性
                    .properties(p -> p.strength(2.0F).sound(SoundType.CHERRY_SAPLING))
                    .transform(axeOnly())
                    .item()
                    .build()
                    .register();

    public static void register() {
    }
}