package org.mob.universe.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import org.mob.universe.Universe;
import org.mob.universe.blocks.ModBlocks;
import org.mob.universe.datagen.builder.UniverseRecipeBuilder;
import org.mob.universe.item.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

//      Universe crafting recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.UNIVERSE_BLOCK.get())
                .pattern("NMN")
                .pattern("DED")
                .pattern("CTC")
                .define('N', Items.NETHERITE_BLOCK)
                .define('M', Items.NETHER_STAR)
                .define('D', Items.DRAGON_BREATH)
                .define('E', Items.DRAGON_HEAD)
                .define('C', Items.CRYING_OBSIDIAN)
                .define('T', Items.TOTEM_OF_UNDYING)
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(consumer);

//      Drop specifier recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SPECIFIER_WAND.get())
                .pattern("BNB")
                .pattern("NTN")
                .pattern("BNB")
                .define('B', Items.NETHERITE_BLOCK)
                .define('N', Items.NETHER_STAR)
                .define('T', Items.TOTEM_OF_UNDYING)
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(consumer);

        UniverseRecipeBuilder.universeRecipe()
                .catalyst(Ingredient.of(Items.HOPPER))
                .addDrop(new ItemStack(Items.DIRT), 1.0, 2, 4)
                .addDrop(new ItemStack(Items.COBBLESTONE), 1.0, 2, 4)
                .addDrop(new ItemStack(Items.COBBLED_DEEPSLATE), 1.0, 2, 4)
                .addDrop(new ItemStack(Items.COAL_ORE), 0.2, 2, 4)
                .addDrop(new ItemStack(Items.IRON_ORE), 0.2, 2, 4)
                .addDrop(new ItemStack(Items.GOLD_ORE), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.DIAMOND_ORE), 0.01, 3, 6)
                .addDrop(new ItemStack(Items.EMERALD_ORE), 0.01, 2, 4)
                .addDrop(new ItemStack(Items.LAPIS_ORE), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.COPPER_ORE), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.REDSTONE_ORE), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.PORKCHOP), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.BEEF), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.HONEYCOMB), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.HONEY_BOTTLE), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.COD), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.ROTTEN_FLESH), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.BONE), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.STRING), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.SPIDER_EYE), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.GUNPOWDER), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.SLIME_BALL), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.PHANTOM_MEMBRANE), 0.001, 2, 4)
                .addDrop(new ItemStack(Items.PRISMARINE_SHARD), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.PRISMARINE_CRYSTALS), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.SPONGE), 0.001, 2, 4)
                .addDrop(new ItemStack(Items.SNOWBALL), 0.1, 2, 4)
                .addDrop(new ItemStack(Items.SCULK), 0.0001, 2, 4)
                .addDrop(new ItemStack(Items.ECHO_SHARD), 0.0001, 2, 4)
                .addDrop(new ItemStack(Items.SCULK_CATALYST), 0.0001, 2, 4)
                .addDrop(new ItemStack(Items.SCULK_SHRIEKER), 0.0001, 2, 4)
                .addDrop(new ItemStack(Items.TOTEM_OF_UNDYING), 0.0001, 2, 4)
                .unlockedBy("has_hopper", has(Items.HOPPER))
                .save(consumer, new ResourceLocation(Universe.MOD_ID, "overworld_universe"));

        UniverseRecipeBuilder.universeRecipe()
                .catalyst(Ingredient.of(Items.BEACON))
                .addDrop(new ItemStack(Items.NETHER_GOLD_ORE), 0.3, 2, 4)
                .addDrop(new ItemStack(Items.NETHER_QUARTZ_ORE), 0.5, 3, 6)
                .addDrop(new ItemStack(Items.GILDED_BLACKSTONE), 0.1, 3, 6)
                .addDrop(new ItemStack(Items.GHAST_TEAR), 0.1, 1, 2)
                .addDrop(new ItemStack(Items.MAGMA_CREAM), 0.1, 1, 2)
                .addDrop(new ItemStack(Items.WITHER_SKELETON_SKULL), 0.01, 1, 3)
                .addDrop(new ItemStack(Items.COAL), 0.5, 2, 5)
                .addDrop(new ItemStack(Items.BLAZE_ROD), 0.2, 1, 2)
                .addDrop(new ItemStack(Items.PORKCHOP), 0.2, 1, 2)
                .addDrop(new ItemStack(Items.STRING), 0.2, 1, 2)
                .addDrop(new ItemStack(Items.ENDER_PEARL), 0.4, 1, 2)
                .unlockedBy("has_beacon", has(Items.BEACON))
                .save(consumer, new ResourceLocation(Universe.MOD_ID, "nether_universe"));

        UniverseRecipeBuilder.universeRecipe()
                .catalyst(Ingredient.of(Items.DRAGON_EGG))
                .addDrop(new ItemStack(Items.DRAGON_EGG), 0.001, 1, 2)
                .addDrop(new ItemStack(Items.ENDER_PEARL), 0.7, 2, 5)
                .addDrop(new ItemStack(Items.DRAGON_HEAD), 0.001, 1, 2)
                .addDrop(new ItemStack(Items.DRAGON_BREATH), 0.01, 1, 2)
                .addDrop(new ItemStack(Items.SHULKER_SHELL), 0.1, 1, 2)
                .addDrop(new ItemStack(Items.END_STONE), 1.0, 2, 4)
                .addDrop(new ItemStack(Items.CHORUS_FRUIT), 0.1, 1, 4)
                .unlockedBy("has_dragon_egg", has(Items.DRAGON_EGG))
                .save(consumer, new ResourceLocation(Universe.MOD_ID, "end_universe"));


    }

}
