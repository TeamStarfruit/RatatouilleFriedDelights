package org.starfruit.ratatouillefrieddelights.worldgen.tree;

import net.minecraft.world.level.block.grower.TreeGrower;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;

import java.util.Optional;

public class RFDTreeGrowers {
    public static final TreeGrower COLA_TREE = new TreeGrower(RatatouilleFriedDelights.MOD_ID + ":cola_tree",
            Optional.empty(), Optional.of(RFDConfiguredFeatures.COLA_TREE_KEY), Optional.empty());
}
