package com.example.undeaddragonboss.entity;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "undeaddragonboss");
    
    public static final RegistryObject<EntityType<UndeadDragonBossEntity>> UNDEAD_DRAGON_BOSS = ENTITIES.register("undead_dragon_boss",
            () -> EntityType.Builder.of(UndeadDragonBossEntity::new, MobCategory.MONSTER)
                    .sized(3.0f, 4.0f)
                    .clientTrackingRange(10)
                    .updateInterval(3)
                    .build("undead_dragon_boss"));
    
    public static final RegistryObject<EntityType<DragonPossessedEntity>> DRAGON_POSSESSED = ENTITIES.register("dragon_possessed",
            () -> EntityType.Builder.of(DragonPossessedEntity::new, MobCategory.MONSTER)
                    .sized(2.5f, 3.5f)
                    .clientTrackingRange(10)
                    .updateInterval(3)
                    .build("dragon_possessed"));
    
    public static void register(IEventBus modEventBus) {
        ENTITIES.register(modEventBus);
    }
}
