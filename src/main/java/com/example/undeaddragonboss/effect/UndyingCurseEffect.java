package com.example.undeaddragonboss.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;

public class UndyingCurseEffect extends MobEffect {
    public UndyingCurseEffect() {
        super(MobEffectCategory.HARMFUL, 0x2a2a2a);
    }
    
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Monster monster) {
            monster.setSpeed(monster.getSpeed() * 0.7f);
        }
    }
    
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
