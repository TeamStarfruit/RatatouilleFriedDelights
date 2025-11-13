package org.starfruit.ratatouillefrieddelights.config;

import com.simibubi.create.infrastructure.config.CFluids;
import net.createmod.catnip.config.ConfigBase;

public class RFDFluids extends ConfigBase {
    public final ConfigInt fryerTankCapacity = i(1, 1, "fryerTankCapacity", RFDFluids.Comments.buckets, RFDFluids.Comments.fryerTankCapacity);

    @Override
    public String getName() {
        return "fluids";
    }

    private static class Comments {
        static String buckets = "[in Buckets]";

        static String fryerTankCapacity = "The amount of liquid a fryer can hold per block.";
    }
}
