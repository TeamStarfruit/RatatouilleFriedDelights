package org.starfruit.ratatouillefrieddelights.content.continuousfryer;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.starfruit.ratatouillefrieddelights.util.Lang;

public enum FryerPart implements StringRepresentable {
    END, MIDDLE, SINGLE;

    @Override
    public @NotNull String getSerializedName() {
        return Lang.asId(name());
    }
}