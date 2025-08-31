package org.starfruit.ratatouillefrieddelights.content.burger;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class BurgerContents implements TooltipComponent
{
    public static final BurgerContents EMPTY = new BurgerContents(List.of());
    public static final Codec<BurgerContents> CODEC = ItemStack.CODEC.listOf().xmap(BurgerContents::new, p_331551_ -> p_331551_.items);
    public static final StreamCodec<RegistryFriendlyByteBuf, BurgerContents> STREAM_CODEC = ItemStack.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(BurgerContents::new, p_331649_ -> p_331649_.items);
    private static final Fraction BURGER_IN_BURGER_WEIGHT = Fraction.getFraction(1, 16);
    private static final int NO_STACK_INDEX = -1;
    final List<ItemStack> items;

    BurgerContents(List<ItemStack> items) {
        this.items = items;
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
        if (this == other) {
            return true;
        } else {
            return other instanceof BurgerContents burgercontents && ItemStack.listMatches(this.items, burgercontents.items);
        }
    }

    @Override
    public int hashCode() {
        return ItemStack.hashStackList(this.items);
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
                return -1;
            } else {
                for (int i = 0; i < this.items.size(); i++) {
                    if (ItemStack.isSameItemSameComponents(this.items.get(i), stack)) {
                        return i;
                    }
                }

                return -1;
            }
        }

        private int getMaxAmountToAdd(ItemStack stack) {
            return Integer.MAX_VALUE;
        }

        public int tryInsert(ItemStack stack) {
            if (!stack.isEmpty() && stack.canFitInsideContainerItems()) { // Neo: stack-aware placeability check
                int i = Math.min(stack.getCount(), this.getMaxAmountToAdd(stack));
                if (i == 0) {
                    return 0;
                } else {
                    int j = this.findStackIndex(stack);
                    if (j != -1) {
                        ItemStack itemstack = this.items.remove(j);
                        ItemStack itemstack1 = itemstack.copyWithCount(itemstack.getCount() + i);
                        stack.shrink(i);
                        this.items.add(0, itemstack1);
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
}
