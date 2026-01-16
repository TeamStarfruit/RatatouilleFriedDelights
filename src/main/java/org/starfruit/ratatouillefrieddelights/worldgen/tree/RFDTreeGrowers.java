package org.starfruit.ratatouillefrieddelights.worldgen.tree;

import net.minecraft.world.level.block.grower.TreeGrower;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.worldgen.RFDConfiguredFeatures;

import java.util.Optional;

public class RFDTreeGrowers {
    public static final TreeGrower COLA_TREE = new TreeGrower(RatatouilleFriedDelights.MOD_ID + ":cola_tree",
            Optional.empty(), Optional.of(RFDConfiguredFeatures.COLA), Optional.empty());
}
