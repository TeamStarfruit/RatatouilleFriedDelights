package org.starfruit.ratatouillefrieddelights.entry;

import net.createmod.catnip.config.ConfigBase;

public class RFDKinetics extends ConfigBase {
    public final RFDStress stressValues;
    public RFDKinetics() {
        this.stressValues =  (RFDStress)this.nested(1, RFDStress::new, new String[]{RFDKinetics.Comments.stress});;
    }

    public String getName() {
        return "kinetics";
    }

    private static class Comments {
        static String stress = "Fine tune the kinetic stats of individual components";
    }
}