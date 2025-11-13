package org.starfruit.ratatouillefrieddelights.config;

import net.createmod.catnip.config.ConfigBase;

public class RFDServer extends ConfigBase {
//    public final ConfigBase.ConfigGroup infrastructure;
//    public final ConfigBase.ConfigInt tickrateSyncTimer;
//    public final CRecipes recipes;
    public final RFDKinetics kinetics;
    public final RFDFluids fluids;
//    public final CLogistics logistics;
//    public final CSchematics schematics;
//    public final CEquipment equipment;
//    public final CTrains trains;

    public RFDServer() {
//        this.infrastructure = this.group(0, "infrastructure", new String[]{CServer.Comments.infrastructure});
//        this.tickrateSyncTimer = this.i(20, 5, "tickrateSyncTimer", new String[]{"[in Ticks]", CServer.Comments.tickrateSyncTimer, CServer.Comments.tickrateSyncTimer2});
//        this.recipes = (CRecipes)this.nested(0, CRecipes::new, new String[]{CServer.Comments.recipes});
        this.kinetics = this.nested(0, RFDKinetics::new, new String[]{RFDServer.Comments.kinetics});
        this.fluids = this.nested(0, RFDFluids::new, new String[]{RFDServer.Comments.fluids});
//        this.logistics = (CLogistics)this.nested(0, CLogistics::new, new String[]{CServer.Comments.logistics});
//        this.schematics = (CSchematics)this.nested(0, CSchematics::new, new String[]{CServer.Comments.schematics});
//        this.equipment = (CEquipment)this.nested(0, CEquipment::new, new String[]{CServer.Comments.equipment});
//        this.trains = (CTrains)this.nested(0, CTrains::new, new String[]{CServer.Comments.trains});
    }

    public String getName() {
        return "server";
    }

    private static class Comments {
//        static String recipes = "Packmakers' control panel for internal recipe compat";
//        static String schematics = "Everything related to Schematic tools";
        static String kinetics = "Ratatouille Fried Delights' Kinetic components and their properties";
        static String fluids = "Ratatouille Fried Delights' liquid manipulation tools";
//        static String logistics = "Tweaks for logistical components";
//        static String equipment = "Equipment and gadgets added by Create";
//        static String trains = "Create's builtin Railway systems";
//        static String infrastructure = "The Backbone of Create";
//        static String tickrateSyncTimer = "The amount of time a server waits before sending out tickrate synchronization packets.";
//        static String tickrateSyncTimer2 = "These packets help animations to be more accurate when tps is below 20.";
    }
}
