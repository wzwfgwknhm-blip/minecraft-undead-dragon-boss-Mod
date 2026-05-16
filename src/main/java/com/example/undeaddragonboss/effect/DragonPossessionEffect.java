package com.example.undeaddragonboss.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class DragonPossessionEffect extends MobEffect {
    public DragonPossessionEffect() {
        super(MobEffectCategory.HARMFUL, 0x8b0000);
    }
    
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            if (player.level().isClientSide) {
                for (int i = 0; i < 3; i++) {
                    player.level().addParticle(
                            net.minecraft.core.particles.ParticleTypes.SOUL,
                            player.getX() + (Math.random() - 0.5) * 2,
                            player.getY() + 1 + Math.random(),
                            player.getZ() + (Math.random() - 0.5) * 2,
                            (Math.random() - 0.5) * 0.1,
                            Math.random() * 0.1,
                            (Math.random() - 0.5) * 0.1
                    );
                }
            }
        }
    }
    
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
