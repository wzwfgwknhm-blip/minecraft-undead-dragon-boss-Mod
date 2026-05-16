package com.example.undeaddragonboss.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;

import com.example.undeaddragonboss.entity.ModEntities;
import com.example.undeaddragonboss.effect.ModEffects;

public class DragonClawItem extends SwordItem {
    private static final int LUNGE_COOLDOWN = 80;
    private static final int POSSESSION_TRIGGER_CHANCE = 15;
    
    public DragonClawItem(Item.Properties properties) {
        super(Tiers.DIAMOND, 8, -1.0f, properties
                .setNoRepair()
                .durability(512));
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide && player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(itemStack);
        }
        
        if (!level.isClientSide) {
            performLunge(player, level);
            player.getCooldowns().addCooldown(this, LUNGE_COOLDOWN);
        }
        
        return InteractionResultHolder.success(itemStack);
    }
    
    private void performLunge(Player player, Level level) {
        Vec3 lookDirection = player.getLookAngle();
        double lungePower = 1.5D;
        
        Vec3 velocity = lookDirection.scale(lungePower);
        player.setDeltaMovement(player.getDeltaMovement().add(velocity));
        player.hurtMarked = true;
        
        player.playSound(SoundEvents.DRAGON_BREATH, 1.0F, 1.0F);
        
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class,
                player.getBoundingBox().inflate(2.0D, 1.0D, 2.0D))) {
            if (entity != player && !entity.isInvulnerable()) {
                entity.hurt(level.damageSources().playerAttack(player), 12.0F);
                entity.knockback(0.8D, lookDirection.x, lookDirection.z);
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1));
            }
        }
    }
    
    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(itemStack, target, attacker);
        
        if (attacker instanceof Player player && !player.level().isClientSide) {
            CompoundTag tag = itemStack.getOrCreateTag();
            int hitCount = tag.getInt("HitCount");
            hitCount++;
            tag.putInt("HitCount", hitCount);
            
            if (hitCount >= 5 && Math.random() < (0.15 * (hitCount / 5.0))) {
                initiateSpirit(player, itemStack);
            }
        }
        
        return result;
    }
    
    private void initiateSpirit(Player player, ItemStack claw) {
        if (!player.hasEffect(ModEffects.DRAGON_POSSESSION.get())) {
            player.addEffect(new MobEffectInstance(ModEffects.DRAGON_POSSESSION.get(), 600, 0));
            CompoundTag tag = claw.getOrCreateTag();
            tag.putInt("PossessedPlayer", player.getId());
            tag.putLong("PossessionStartTime", player.level().getGameTime());
            player.playSound(SoundEvents.WITHER_SPAWN, 1.0F, 0.3F);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 1));
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal(
                            "§4[DRAGON CLAW] §cThe dragon spirit attempts to possess you! Resist or transform!"),
                    false);
        }
    }
}
