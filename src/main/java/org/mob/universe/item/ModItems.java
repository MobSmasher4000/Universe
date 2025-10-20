package org.mob.universe.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mob.universe.Universe;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Universe.MOD_ID);

    public static final RegistryObject<Item> SPECIFIER_WAND = ITEMS.register("drop_specifier",
            ()-> new Item(new Item.Properties().stacksTo(1).durability(6336)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
