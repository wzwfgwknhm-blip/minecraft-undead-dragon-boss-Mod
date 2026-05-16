package com.example.undeaddragonboss.biome;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, "undeaddragonboss");
    
    public static final RegistryObject<Biome> UNDEAD_KINGDOM = BIOMES.register("undead_kingdom",
            UndeadKingdomBiome::createBiome);
    
    public static void register(IEventBus modEventBus) {
        BIOMES.register(modEventBus);
    }
}
