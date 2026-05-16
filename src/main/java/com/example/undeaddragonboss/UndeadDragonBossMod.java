package com.example.undeaddragonboss;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafxmod.FXModLoadingContext;

import com.example.undeaddragonboss.biome.ModBiomes;
import com.example.undeaddragonboss.entity.ModEntities;
import com.example.undeaddragonboss.item.ModItems;
import com.example.undeaddragonboss.recipe.ModRecipes;
import com.example.undeaddragonboss.world.structure.ModStructures;
import com.example.undeaddragonboss.effect.ModEffects;

@Mod("undeaddragonboss")
public class UndeadDragonBossMod {
    public UndeadDragonBossMod() {
        IEventBus modEventBus = FXModLoadingContext.getInstance().getModEventBus();
        
        ModEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModBiomes.register(modEventBus);
        ModStructures.register(modEventBus);
        ModEffects.register(modEventBus);
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        // Common setup
    }
    
    @Mod.EventBusSubscriber(modid = "undeaddragonboss", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSetup {
        @net.minecraftforge.api.distmarker.OnlyIn(Dist.CLIENT)
        public static void clientSetup(FMLClientSetupEvent event) {
            // Client setup
        }
    }
}
