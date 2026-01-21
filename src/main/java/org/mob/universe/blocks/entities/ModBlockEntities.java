package org.mob.universe.blocks.entities;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mob.universe.Universe;
import org.mob.universe.blocks.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Universe.MOD_ID);

    public static final RegistryObject<BlockEntityType<UniverseBlockEntity>> UNIVERSE_BE =
            BLOCK_ENTITIES.register("universe_be", () ->
                    BlockEntityType.Builder.of(UniverseBlockEntity::new,
                            ModBlocks.UNIVERSE_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
