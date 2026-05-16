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
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import java.util.UUID;

public class DragonPossessedEntity extends Monster {
    private int attackCooldown = 0;
    private static final int ATTACK_COOLDOWN = 120;
    private UUID originalOwner;
    private int lifespan = 12000;
    
    public DragonPossessedEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 250;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollingGoal(this, 1.2D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true) {
            @Override
            public boolean canUse() {
                if (originalOwner != null && target != null && target.getUUID().equals(originalOwner)) {
                    return false;
                }
                return super.canUse();
            }
        });
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.ARMOR, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        if (!this.level().isClientSide) {
            attackCooldown--;
            lifespan--;
            
            if (this.getRandom().nextInt(5) == 0) {
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                        this.getX() + (this.getRandom().nextDouble() - 0.5) * 2,
                        this.getY() + 1.5 + this.getRandom().nextDouble(),
                        this.getZ() + (this.getRandom().nextDouble() - 0.5) * 2,
                        0, 0.05, 0);
            }
            
            if (lifespan <= 0) {
                this.discard();
            }
            
            LivingEntity target = this.getTarget();
            if (target != null && this.distanceTo(target) < 25.0D) {
                if (attackCooldown <= 0 && this.distanceTo(target) < 5.0D) {
                    this.performDragonAttack(target);
                    attackCooldown = ATTACK_COOLDOWN;
                }
            }
        }
    }
    
    private void performDragonAttack(LivingEntity target) {
        this.playSound(SoundEvents.DRAGON_ATTACK, 1.0F, 0.9F);
        
        if (this.distanceTo(target) < 4.0D) {
            float damage = 16.0F;
            if (target.hurt(this.damageSources().mobAttack(this), damage)) {
                target.knockback(1.2D, this.getX() - target.getX(), this.getZ() - target.getZ());
                this.heal(damage * 0.2F);
                this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 80, 0));
            }
        }
    }
    
    public void setOriginalOwner(UUID uuid) {
        this.originalOwner = uuid;
    }
    
    public UUID getOriginalOwner() {
        return this.originalOwner;
    }
    
    @Override
    public void die(DamageSource damageSource) {
        if (!this.level().isClientSide && originalOwner != null) {
            Player owner = this.level().getPlayerByUUID(originalOwner);
            if (owner != null && !owner.isAlive()) {
                owner.revive();
                owner.setHealth(owner.getMaxHealth() * 0.5f);
                owner.teleportTo(this.getX(), this.getY(), this.getZ());
                owner.displayClientMessage(
                        net.minecraft.network.chat.Component.literal(
                                "§6[DRAGON CLAW] §eYou have reclaimed your body! You are the dragon now!"),
                        false);
                
                owner.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 2));
                owner.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 1));
                owner.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1));
            }
        }
        super.die(damageSource);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.originalOwner != null) {
            tag.putUUID("OriginalOwner", this.originalOwner);
        }
        tag.putInt("Lifespan", this.lifespan);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("OriginalOwner")) {
            this.originalOwner = tag.getUUID("OriginalOwner");
        }
        this.lifespan = tag.getInt("Lifespan");
    }
}
