package org.mob.universe.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.mob.universe.blocks.ModBlocks;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.UNIVERSE_BLOCK.get());
        dropSelf(ModBlocks.INFERIUM_UNIVERSE.get());
        dropSelf(ModBlocks.PRUDENTIUM_UNIVERSE.get());
        dropSelf(ModBlocks.IMPERIUM_UNIVERSE.get());
        dropSelf(ModBlocks.SUPREMIUM_UNIVERSE.get());
        dropSelf(ModBlocks.TERTIUM_UNIVERSE.get());
        dropSelf(ModBlocks.INSANIUM_UNIVERSE.get());
        dropSelf(ModBlocks.CREATIVE_UNIVERSE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
