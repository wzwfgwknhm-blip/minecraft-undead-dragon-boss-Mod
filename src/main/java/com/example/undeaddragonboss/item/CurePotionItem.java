package com.example.undeaddragonboss.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffects;

public class CurePotionItem extends Item {
    public CurePotionItem(Item.Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            if (player.hasEffect(MobEffects.POISON)) {
                player.removeEffect(MobEffects.POISON);
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }
                return InteractionResultHolder.success(itemStack);
            }
        }
        
        return InteractionResultHolder.pass(itemStack);
    }
}
