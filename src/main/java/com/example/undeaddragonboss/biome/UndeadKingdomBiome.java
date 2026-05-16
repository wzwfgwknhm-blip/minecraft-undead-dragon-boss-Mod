package com.example.undeaddragonboss.biome;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.entity.MobCategory;

public class UndeadKingdomBiome {
    public static Biome createBiome() {
        MobSpawnSettings.Builder mobSpawnSettings = new MobSpawnSettings.Builder();
        
        mobSpawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
                net.minecraft.world.entity.monster.Skeleton.class, 40, 1, 3));
        mobSpawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
                net.minecraft.world.entity.monster.Zombie.class, 40, 2, 4));
        mobSpawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
                net.minecraft.world.entity.monster.WitherSkeleton.class, 30, 1, 2));
        
        BiomeGenerationSettings.Builder biomeGenerationSettings = new BiomeGenerationSettings.Builder();
        
        return new Biome.BiomeBuilder()
                .precipitation(false)
                .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                .temperature(0.5F)
                .downfall(0.0F)
                .specialEffects(new net.minecraft.world.level.biome.BiomeSpecialEffects.Builder()
                        .waterColor(0x3f76e4)
                        .waterFogColor(0x050533)
                        .fogColor(0x808080)
                        .skyColor(0x3d2817)
                        .build())
                .mobSpawnSettings(mobSpawnSettings.build())
                .generationSettings(biomeGenerationSettings.build())
                .build();
    }
}
