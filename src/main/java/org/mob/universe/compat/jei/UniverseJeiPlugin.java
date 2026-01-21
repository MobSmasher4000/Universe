package org.mob.universe.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.mob.universe.Universe;
import org.mob.universe.blocks.ModBlocks;
import org.mob.universe.recipe.UniverseRecipe;
import org.mob.universe.screen.screen.UniverseBlockScreen;

import java.util.List;

@JeiPlugin
public class UniverseJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Universe.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new UniverseRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<UniverseRecipe> recipes = recipeManager.getAllRecipesFor(UniverseRecipe.Type.INSTANCE);
        registration.addRecipes(UniverseRecipeCategory.UNIVERSE_RECIPE_TYPE, recipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(UniverseBlockScreen.class, 50, 42, 22, 15,
                UniverseRecipeCategory.UNIVERSE_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.UNIVERSE_BLOCK.get().asItem()), UniverseRecipeCategory.UNIVERSE_RECIPE_TYPE);
    }
}