package com.example.undeaddragonboss.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.ParticleTypes;

import com.example.undeaddragonboss.item.ModItems;

public class UndeadDragonBossEntity extends Monster {
    private int miasmaAttackCooldown = 0;
    private int biteAttackCooldown = 0;
    private static final int MIASMA_COOLDOWN = 200;
    private static final int BITE_COOLDOWN = 150;
    private boolean isResurrected = false;
    
    public UndeadDragonBossEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 500;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 250.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.ARMOR, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide) {
            miasmaAttackCooldown--;
            biteAttackCooldown--;
            
            LivingEntity target = this.getTarget();
            if (target != null && this.distanceTo(target) < 30.0D) {
                if (miasmaAttackCooldown <= 0) {
                    this.performMiasmaAttack();
                    miasmaAttackCooldown = MIASMA_COOLDOWN;
                }
                
                if (biteAttackCooldown <= 0 && this.distanceTo(target) < 6.0D) {
                    this.performBiteAttack(target);
                    biteAttackCooldown = BITE_COOLDOWN;
                }
            }
        }
    }
    
    private void performMiasmaAttack() {
        this.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 0.5F);
        
        for (int i = 0; i < 20; i++) {
            double offsetX = (this.getRandom().nextDouble() - 0.5D) * 8.0D;
            double offsetY = (this.getRandom().nextDouble() - 0.5D) * 4.0D;
            double offsetZ = (this.getRandom().nextDouble() - 0.5D) * 8.0D;
            
            this.level().addParticle(ParticleTypes.SQUID_INK,
                    this.getX() + offsetX, this.getY() + 1.5D + offsetY, this.getZ() + offsetZ,
                    0.0D, 0.0D, 0.0D);
        }
        
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(16.0D))) {
            if (entity != this && !(entity instanceof UndeadDragonBossEntity)) {
                entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
                
                if (!entity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    entity.hurt(this.damageSources().indirectMagic(this, this), 5.0F);
                }
            }
        }
    }
    
    private void performBiteAttack(LivingEntity target) {
        this.playSound(SoundEvents.DRAGON_ATTACK, 1.0F, 0.8F);
        
        if (this.distanceTo(target) < 4.0D) {
            float damage = 20.0F;
            if (target.hurt(this.damageSources().mobAttack(this), damage)) {
                float healAmount = damage * 0.25F;
                this.heal(healAmount);
                
                this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1));
                
                target.knockback(1.5D, this.getX() - target.getX(), this.getZ() - target.getZ());
            }
        }
    }
    
    @Override
    public void die(DamageSource damageSource) {
        if (!this.level().isClientSide) {
            if (!isResurrected) {
                for (int i = 0; i < 3; i++) {
                    ItemStack dragonClaw = new ItemStack(ModItems.DRAGON_CLAW.get());
                    this.spawnAtLocation(dragonClaw);
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    ItemStack drop = new ItemStack(ModItems.DRAGON_CLAW.get());
                    this.spawnAtLocation(drop);
                }
            }
        }
        super.die(damageSource);
    }
    
    public void setResurrected(boolean resurrected) {
        this.isResurrected = resurrected;
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("MiasmaAttackCooldown", this.miasmaAttackCooldown);
        tag.putInt("BiteAttackCooldown", this.biteAttackCooldown);
        tag.putBoolean("IsResurrected", this.isResurrected);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.miasmaAttackCooldown = tag.getInt("MiasmaAttackCooldown");
        this.biteAttackCooldown = tag.getInt("BiteAttackCooldown");
        this.isResurrected = tag.getBoolean("IsResurrected");
    }
}
