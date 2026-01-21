package org.mob.universe.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.mob.universe.Universe;
import org.mob.universe.blocks.ModBlocks;
import org.mob.universe.recipe.UniverseRecipe;

import java.util.List;

public class UniverseRecipeCategory implements IRecipeCategory<UniverseRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Universe.MOD_ID, "universe");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Universe.MOD_ID, "textures/gui/universe_block/universe_block_gui.png");
    public static final RecipeType<UniverseRecipe> UNIVERSE_RECIPE_TYPE = new RecipeType<>(UID, UniverseRecipe.class);

    private final IGuiHelper helper;
    private final IDrawable icon;
    private final IDrawable slotDrawable;

    public UniverseRecipeCategory(IGuiHelper helper) {
        this.helper = helper;
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.UNIVERSE_BLOCK.get()));
        this.slotDrawable = helper.getSlotDrawable();
    }

    @Override
    public RecipeType<UniverseRecipe> getRecipeType() {
        return UNIVERSE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.universe.universe_block");
    }

    @Override
    public IDrawable getBackground() {
        return helper.createBlankDrawable(175, 170);
    }

    @Override
    public void draw(UniverseRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.drawString(net.minecraft.client.Minecraft.getInstance().font, "Catalyst", 22, 10, 0xFF404040, false);
        guiGraphics.drawString(net.minecraft.client.Minecraft.getInstance().font, "Possible Drops", 80, 10, 0xFF404040, false);
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, UniverseRecipe recipe, IFocusGroup focuses) {
        // 1. THE INPUT SLOT
        builder.addSlot(RecipeIngredientRole.INPUT, 32, 25)
                .addIngredients(recipe.getIngredients().get(0))
                .setBackground(slotDrawable, -1, -1);

        // 2. THE OUTPUT GRID
        List<UniverseRecipe.UniverseDrop> drops = recipe.getDrops();
        int xStart = 80;
        int yStart = 25;
        int columns = 5;

        for (int i = 0; i < drops.size(); i++) {
            UniverseRecipe.UniverseDrop drop = drops.get(i);
            int row = i / columns;
            int col = i % columns;

            builder.addSlot(RecipeIngredientRole.OUTPUT, xStart + (col * 18), yStart + (row * 18))
                    .addItemStack(drop.stack())
                    .setBackground(slotDrawable, -1, -1)
                    .addTooltipCallback((recipeSlotView, tooltip) -> {
                        double chance = drop.chance();
                        int minRoll = drop.minRolls();
                        int maxRoll = drop.maxRolls();
                        // "#.######" means: show up to 6 places, but only if they aren't zero
                        java.text.DecimalFormat smartFormat = new java.text.DecimalFormat("0.######");
                        String formattedPercent = smartFormat.format(chance * 100);

                        if (chance < 0.001) {
                            long oneInX = Math.round(1.0 / chance);
                            tooltip.add(Component.literal("§d§lULTRA RARE"));
                            tooltip.add(Component.literal("§7Chance: §b" + formattedPercent + "%"));
                            tooltip.add(Component.literal("§8(1 in " + String.format("%,d", oneInX) + ")"));
                            tooltip.add(Component.translatable("tooltip.universe.drop_rolls",minRoll,maxRoll));
                        } else if (chance < 0.1) {
                            tooltip.add(Component.literal("§7Chance: §e" + formattedPercent + "%"));
                            tooltip.add(Component.translatable("tooltip.universe.drop_rolls",minRoll,maxRoll));
                        } else {
                            tooltip.add(Component.literal("§7Chance: §6" + formattedPercent + "%"));
                            tooltip.add(Component.translatable("tooltip.universe.drop_rolls",minRoll,maxRoll));
                        }
                    });
        }
    }
}