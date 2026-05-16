package com.example.undeaddragonboss.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;

import com.example.undeaddragonboss.effect.ModEffects;

@Mod.EventBusSubscriber(modid = "undeaddragonboss", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PossessionEventHandler {
    
    @SubscribeEvent
    public static void onPlayerTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            if (player.hasEffect(ModEffects.DRAGON_POSSESSION.get())) {
                int remainingTicks = player.getEffect(ModEffects.DRAGON_POSSESSION.get()).getDuration();
                
                if (remainingTicks % 100 == 0) {
                    int secondsRemaining = remainingTicks / 20;
                    if (secondsRemaining > 0) {
                        player.displayClientMessage(
                                Component.literal(
                                        "§6[POSSESSION] §cResist the spirit! §e" + secondsRemaining + "s remaining"),
                                true);
                    }
                }
            }
        }
    }
}
