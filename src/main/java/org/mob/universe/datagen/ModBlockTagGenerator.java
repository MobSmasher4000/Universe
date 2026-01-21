package org.mob.universe.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.mob.universe.Universe;
import org.mob.universe.blocks.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Universe.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.UNIVERSE_BLOCK.get())
                .add(ModBlocks.INFERIUM_UNIVERSE.get())
                .add(ModBlocks.PRUDENTIUM_UNIVERSE.get())
                .add(ModBlocks.TERTIUM_UNIVERSE.get())
                .add(ModBlocks.IMPERIUM_UNIVERSE.get())
                .add(ModBlocks.SUPREMIUM_UNIVERSE.get())
                .add(ModBlocks.INSANIUM_UNIVERSE.get())
                .add(ModBlocks.CREATIVE_UNIVERSE.get())
        ;
    }
}
