package com.example.undeaddragonboss.item;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "undeaddragonboss");
    
    public static final RegistryObject<Item> DRAGON_CLAW = ITEMS.register("dragon_claw",
            () -> new DragonClawItem(new Item.Properties()));
    
    public static final RegistryObject<Item> SUMMONING_CATALYST = ITEMS.register("summoning_catalyst",
            () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> CURE_POTION = ITEMS.register("cure_potion",
            () -> new CurePotionItem(new Item.Properties().stacksTo(64)));
    
    public static final RegistryObject<Item> CURSED_BONES = ITEMS.register("cursed_bones",
            () -> new Item(new Item.Properties().stacksTo(64)));
    
    public static final RegistryObject<Item> SOUL_ESSENCE = ITEMS.register("soul_essence",
            () -> new Item(new Item.Properties().stacksTo(64)));
    
    public static final RegistryObject<Item> DARK_AMETHYST = ITEMS.register("dark_amethyst",
            () -> new Item(new Item.Properties().stacksTo(64)));
    
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
