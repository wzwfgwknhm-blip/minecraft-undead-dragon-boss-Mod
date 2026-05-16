package com.example.undeaddragonboss.effect;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.effect.MobEffect;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = 
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "undeaddragonboss");
    
    public static final RegistryObject<MobEffect> UNDYING_CURSE = MOB_EFFECTS.register("undying_curse",
            () -> new UndyingCurseEffect());
    
    public static final RegistryObject<MobEffect> DRAGON_POSSESSION = MOB_EFFECTS.register("dragon_possession",
            () -> new DragonPossessionEffect());
    
    public static void register(IEventBus modEventBus) {
        MOB_EFFECTS.register(modEventBus);
    }
}
