package com.example.undeaddragonboss.world.structure;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class ModStructures {
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = 
            DeferredRegister.create(ForgeRegistries.STRUCTURE_PIECE_TYPES, "undeaddragonboss");
    
    public static void register(IEventBus modEventBus) {
        STRUCTURE_PIECES.register(modEventBus);
    }
}
