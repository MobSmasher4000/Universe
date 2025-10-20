package org.mob.universe;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.mob.universe.blocks.ModBlocks;
import org.mob.universe.item.ModItems;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB,Universe.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("universetab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CREATIVE_UNIVERSE.get()))
                    .title(Component.translatable("creativetab.universetab"))
                    .displayItems(((itemDisplayParameters, output) ->{
                        output.accept(ModBlocks.INFERIUM_UNIVERSE.get());
                        output.accept(ModBlocks.PRUDENTIUM_UNIVERSE.get());
                        output.accept(ModBlocks.TERTIUM_UNIVERSE.get());
                        output.accept(ModBlocks.IMPERIUM_UNIVERSE.get());
                        output.accept(ModBlocks.SUPREMIUM_UNIVERSE.get());
                        output.accept(ModBlocks.INSANIUM_UNIVERSE.get());
                        output.accept(ModBlocks.CREATIVE_UNIVERSE.get());
                        output.accept(ModItems.SPECIFIER_WAND.get());
                    }))

                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
