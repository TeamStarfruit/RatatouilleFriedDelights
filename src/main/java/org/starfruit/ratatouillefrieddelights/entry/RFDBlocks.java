package org.starfruit.ratatouillefrieddelights.entry;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.content.continuous_fryer.ContinuousFryerBlock;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
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
            .model((c, p) -> p.withExistingParent(c.getName(), RatatouilleFriedDelights.asResource("block/continuous_fryer/item")))
            .build()
            .register();

    public static void register() {
    }
}