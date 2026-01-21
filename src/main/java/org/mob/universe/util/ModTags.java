package org.mob.universe.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.mob.universe.Universe;

public class ModTags {
    public static class Blocks{

        public static final TagKey<Block> UNIVERSE = createTag("universe");

        private static TagKey<Block> createTag(String name){
            return BlockTags.create(new ResourceLocation(Universe.MOD_ID, name));
        }
    }

    public static class Items{

        public static final TagKey<Item> SPEED_UPGRADE = createTag("speed_upgrade");

        private static TagKey<Item> createTag(String name){
            return ItemTags.create(new ResourceLocation(Universe.MOD_ID, name));
        }

    }

}