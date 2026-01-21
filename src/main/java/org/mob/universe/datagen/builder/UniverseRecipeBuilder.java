package org.mob.universe.datagen.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.mob.universe.recipe.UniverseRecipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class UniverseRecipeBuilder implements RecipeBuilder {
    private Ingredient catalyst;
    private final List<DropData> drops = new ArrayList<>();
    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private UniverseRecipeBuilder() {}

    public static UniverseRecipeBuilder universeRecipe() {
        return new UniverseRecipeBuilder();
    }

    /**
     * Defines the item required in the input slot.
     */
    public UniverseRecipeBuilder catalyst(Ingredient catalyst) {
        this.catalyst = catalyst;
        return this;
    }

    /**
     * Adds a drop to the recipe with specific chance and roll ranges.
     */
    public UniverseRecipeBuilder addDrop(ItemStack item, double chance, int minRolls, int maxRolls) {
        this.drops.add(new DropData(item, chance, minRolls, maxRolls));
        return this;
    }

    /**
     * Simplified helper for a guaranteed 100% single drop.
     */
    public UniverseRecipeBuilder addDrop(ItemStack item) {
        return this.addDrop(item, 1.0, 1, 1);
    }

    @Override
    public UniverseRecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public UniverseRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return drops.isEmpty() ? Items.AIR : drops.get(0).stack().getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        if (this.catalyst == null) {
            throw new IllegalStateException("Universe recipe " + id + " has no catalyst!");
        }
        if (this.drops.isEmpty()) {
            throw new IllegalStateException("Universe recipe " + id + " has no drops!");
        }

        Advancement.Builder advancement = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);

        this.criteria.forEach(advancement::addCriterion);

        ResourceLocation recipeId = new ResourceLocation(
                id.getNamespace(),
                "universe/" + id.getPath()
        );

        ResourceLocation advancementId = new ResourceLocation(
                id.getNamespace(),
                "recipes/universe/" + id.getPath()
        );

        consumer.accept(new Result(
                recipeId,
                this.catalyst,
                this.drops,
                this.group == null ? "" : this.group,
                advancement,
                advancementId));
    }

    // Helper internal record to hold drop data
    private record DropData(ItemStack stack, double chance, int min, int max) {}

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient catalyst;
        private final List<DropData> drops;
        private final String group;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, Ingredient catalyst, List<DropData> drops, String group,
                      Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.catalyst = catalyst;
            this.drops = drops;
            this.group = group;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!group.isEmpty()) {
                json.addProperty("group", group);
            }

            // Matches "catalyst": { "item": "..." }
            json.add("catalyst", catalyst.toJson());

            // Matches "drops": [ ... ]
            JsonArray dropsArray = new JsonArray();
            for (DropData drop : drops) {
                JsonObject dropObj = new JsonObject();

                dropObj.addProperty("chance", drop.chance);

                JsonObject outputObj = new JsonObject();
                outputObj.addProperty("item", ForgeRegistries.ITEMS.getKey(drop.stack.getItem()).toString());
                if (drop.stack.getCount() > 1) {
                    outputObj.addProperty("count", drop.stack.getCount());
                }
                dropObj.add("output", outputObj);

                dropObj.addProperty("minRolls", drop.min);
                dropObj.addProperty("maxRolls", drop.max);

                dropsArray.add(dropObj);
            }
            json.add("drops", dropsArray);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            // Ensure this points to your registered Serializer instance
            return UniverseRecipe.Serializer.INSTANCE;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}