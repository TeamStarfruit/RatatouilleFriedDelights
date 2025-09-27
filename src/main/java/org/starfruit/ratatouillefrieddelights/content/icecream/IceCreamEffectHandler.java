package org.starfruit.ratatouillefrieddelights.content.icecream;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import org.starfruit.ratatouillefrieddelights.RatatouilleFriedDelights;
import org.starfruit.ratatouillefrieddelights.entry.RFDTags;

@EventBusSubscriber(modid = RatatouilleFriedDelights.MOD_ID)
public class IceCreamEffectHandler {
    @SubscribeEvent
    public static void onEatIceCream(LivingEntityUseItemEvent.Finish event) {
        LivingEntity entity = event.getEntity();
        ItemStack stack = event.getItem();
        Level level = entity.level();

        // 检查是不是带有 ice_cream 标签的物品
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            if (stack.is(RFDTags.AllItemTags.RATATOUILLE_ICE_CREAM.tag)) {
                applySoftFreezeEffect(player, level);
            }
        }
    }

    private static void applySoftFreezeEffect(Player entity, Level level) {
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 5, 1)); // 5秒 减速 II
        entity.isFreezing();
        entity.setIsInPowderSnow(true);
    }
}
