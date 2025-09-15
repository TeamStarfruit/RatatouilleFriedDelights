package org.starfruit.ratatouillefrieddelights.content.continuous_fryer;

import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.logistics.box.PackageItem;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class FryingItemStack implements Comparable<FryingItemStack>{

    private static final Random R = new Random();

    public ItemStack stack;
    public float fryerPosition;
    public float sideOffset;
    public int angle;
    public int insertedAt;
    public Direction insertedFrom;
    public boolean locked;
    public boolean lockedExternally;

    public float prevFryerPosition;
    public float prevSideOffset;

    public FryingRecipe lastRecipe;
    public int processingTime;

    public FryingItemStack(ItemStack stack) {
        this.stack = stack;
        boolean centered = BeltHelper.isItemUpright(stack);
        angle = centered ? 180 : R.nextInt(360);
        if (PackageItem.isPackage(stack))
            angle = R.nextInt(4) * 90 + R.nextInt(20) - 10;
        sideOffset = prevSideOffset = getTargetSideOffset();
        insertedFrom = Direction.UP;
    }

    public float getTargetSideOffset() {
        return (angle - 180) / (360 * 3f);
    }

    @Override
    public int compareTo(FryingItemStack o) {
        return Float.compare(o.fryerPosition, fryerPosition);
    }

    public FryingItemStack getSimilar() {
        FryingItemStack copy = new FryingItemStack(stack.copy());
        copy.fryerPosition = fryerPosition;
        copy.insertedAt = insertedAt;
        copy.insertedFrom = insertedFrom;
        copy.prevFryerPosition = prevFryerPosition;
        copy.prevSideOffset = prevSideOffset;
//        copy.processedBy = processedBy;
        copy.processingTime = processingTime;
        return copy;
    }

    public FryingItemStack copy() {
        FryingItemStack copy = getSimilar();
        copy.angle = angle;
        copy.sideOffset = sideOffset;
        return copy;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("Item", stack.saveOptional(registries));
        nbt.putFloat("Pos", fryerPosition);
        nbt.putFloat("PrevPos", prevFryerPosition);
        nbt.putFloat("Offset", sideOffset);
        nbt.putFloat("PrevOffset", prevSideOffset);
        nbt.putInt("InSegment", insertedAt);
        nbt.putInt("Angle", angle);
        nbt.putInt("InDirection", insertedFrom.get3DDataValue());

        if (lastRecipe != null) {
            nbt.putString("LastRecipe", lastRecipe.toString());
            nbt.putInt("FryerProcessingTime", processingTime);
        }

        if (locked)
            nbt.putBoolean("Locked", locked);
        if (lockedExternally)
            nbt.putBoolean("LockedExternally", lockedExternally);
        return nbt;
    }

    public static FryingItemStack read(CompoundTag nbt, HolderLookup.Provider registries, Level level) {
        FryingItemStack stack = new FryingItemStack(ItemStack.parseOptional(registries, nbt.getCompound("Item")));
        stack.fryerPosition = nbt.getFloat("Pos");
        stack.prevFryerPosition = nbt.getFloat("PrevPos");
        stack.sideOffset = nbt.getFloat("Offset");
        stack.prevSideOffset = nbt.getFloat("PrevOffset");
        stack.insertedAt = nbt.getInt("InSegment");
        stack.angle = nbt.getInt("Angle");
        stack.insertedFrom = Direction.from3DDataValue(nbt.getInt("InDirection"));
        stack.locked = nbt.getBoolean("Locked");
        stack.lockedExternally = nbt.getBoolean("LockedExternally");

        if (nbt.contains("LastRecipe")) {
            stack.lastRecipe = (FryingRecipe) level.getRecipeManager().byKey(ResourceLocation.parse(nbt.getString("LastRecipe"))).orElse(null).value();
            stack.processingTime = nbt.getInt("FryerProcessingTime");
        }

        return stack;
    }

    public void clearFryerProcessingData() {
        lastRecipe = null;
        processingTime = 0;
    }
}
