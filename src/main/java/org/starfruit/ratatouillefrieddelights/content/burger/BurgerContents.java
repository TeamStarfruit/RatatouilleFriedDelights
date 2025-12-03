package org.starfruit.ratatouillefrieddelights.content.burger;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.starfruit.ratatouillefrieddelights.entry.RFDItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class BurgerContents implements TooltipComponent {
    private static final String TAG_KEY = "BurgerContents";
    public static final BurgerContents EMPTY = new BurgerContents(List.of());
    private static final Fraction BURGER_IN_BURGER_WEIGHT = Fraction.getFraction(1, 16);
    private static final int NO_STACK_INDEX = -1;
    final List<ItemStack> items;

    public BurgerContents(List<ItemStack> items) {
        this.items = items;
    }

    public static BurgerContents get(ItemStack stack) {
        return get(stack, EMPTY);
    }

    public static BurgerContents get(ItemStack stack, BurgerContents fallback) {
        if (!stack.hasTag())
            return fallback;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TAG_KEY, Tag.TAG_LIST))
            return fallback;
        ListTag list = tag.getList(TAG_KEY, Tag.TAG_COMPOUND);
        List<ItemStack> contents = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            contents.add(ItemStack.of(list.getCompound(i)));
        }
        return new BurgerContents(contents);
    }

    public static boolean has(ItemStack stack) {
        if (stack.is(RFDItems.BURGER.get()))
            return true;

        return stack.hasTag() && stack.getTag() != null && stack.getTag().contains(TAG_KEY, Tag.TAG_LIST);
    }

    public ItemStack getItemUnsafe(int index) {
        return this.items.get(index);
    }

    public Stream<ItemStack> itemCopyStream() {
        return this.items.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> items() {
        return this.items;
    }

    public Iterable<ItemStack> itemsCopy() {
        return Lists.transform(this.items, ItemStack::copy);
    }

    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof BurgerContents burgerContents))
            return false;
        if (this.items.size() != burgerContents.items.size())
            return false;
        for (int i = 0; i < this.items.size(); i++) {
            if (!ItemStack.isSameItemSameTags(this.items.get(i), burgerContents.items.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (ItemStack stack : items) {
            int itemHash = stack.isEmpty() ? 0 : stack.getItem().hashCode();
            int tagHash = stack.hasTag() ? stack.getTag().hashCode() : 0;
            result = 31 * result + itemHash;
            result = 31 * result + stack.getCount();
            result = 31 * result + tagHash;
        }
        return result;
    }

    @Override
    public String toString() {
        return "BurgerContents" + this.items;
    }

    public static class Mutable {
        private final List<ItemStack> items;

        public Mutable(BurgerContents contents) {
            this.items = new ArrayList<>(contents.items);
        }

        public BurgerContents.Mutable clearItems() {
            this.items.clear();
            return this;
        }

        private int findStackIndex(ItemStack stack) {
            if (!stack.isStackable()) {
                return NO_STACK_INDEX;
            } else {
                for (int i = 0; i < this.items.size(); i++) {
                    if (ItemStack.isSameItemSameTags(this.items.get(i), stack)) {
                        return i;
                    }
                }

                return NO_STACK_INDEX;
            }
        }

        private int getMaxAmountToAdd(ItemStack stack) {
            return Integer.MAX_VALUE;
        }

        public int tryInsert(ItemStack stack) {
            if (!stack.isEmpty()) {
                int i = Math.min(stack.getCount(), this.getMaxAmountToAdd(stack));
                if (i == 0) {
                    return 0;
                } else {
                    int j = this.findStackIndex(stack);
                    if (j != NO_STACK_INDEX) {
                        ItemStack existing = this.items.remove(j);
                        ItemStack merged = existing.copyWithCount(existing.getCount() + i);
                        stack.shrink(i);
                        this.items.add(0, merged);
                    } else {
                        this.items.add(0, stack.split(i));
                    }

                    return i;
                }
            } else {
                return 0;
            }
        }

        public int tryTransfer(Slot slot, Player player) {
            ItemStack itemstack = slot.getItem();
            int i = this.getMaxAmountToAdd(itemstack);
            return this.tryInsert(slot.safeTake(itemstack.getCount(), i, player));
        }

        @Nullable
        public ItemStack removeOne() {
            if (this.items.isEmpty()) {
                return null;
            } else {
                ItemStack itemstack = this.items.remove(0).copy();
                return itemstack;
            }
        }

        public BurgerContents toImmutable() {
            return new BurgerContents(List.copyOf(this.items));
        }
    }

    public static void setBurger(ItemStack stack, List<ItemStack> contents) {
        ListTag listTag = new ListTag();
        for (ItemStack content : contents) {
            CompoundTag itemTag = new CompoundTag();
            content.copy().save(itemTag);
            listTag.add(itemTag);
        }
        stack.getOrCreateTag().put(TAG_KEY, listTag);
    }
}
