package org.mob.universe.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.mob.universe.Universe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class UniverseRecipe implements Recipe<SimpleContainer> {
    private final Ingredient catalyst;
    private final List<UniverseDrop> drops;
    private final ResourceLocation id;

    public UniverseRecipe(ResourceLocation id, Ingredient catalyst, List<UniverseDrop> drops) {
        this.id = id;
        this.catalyst = catalyst;
        this.drops = drops;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }
        return catalyst.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY; // Not used for multi-drop results
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.catalyst); // This ensures the list is not empty
        return ingredients;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return drops.isEmpty() ? ItemStack.EMPTY : drops.get(0).stack().copy();
    }

    public List<UniverseDrop> getDrops() {
        return drops;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    // Helper Record for Drops
    public record UniverseDrop(ItemStack stack, double chance, int minRolls, int maxRolls) {}

    public static class Type implements RecipeType<UniverseRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "universe";
    }

    public static class Serializer implements RecipeSerializer<UniverseRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Universe.MOD_ID, "universe");

        @Override
        public UniverseRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            Ingredient catalyst = Ingredient.fromJson(pSerializedRecipe.get("catalyst"));

            JsonArray dropsArray = GsonHelper.getAsJsonArray(pSerializedRecipe, "drops");
            List<UniverseDrop> drops = new ArrayList<>();

            for (JsonElement element : dropsArray) {
                JsonObject obj = element.getAsJsonObject();
                ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(obj, "output"));
                double chance = GsonHelper.getAsDouble(obj, "chance");
                int min = GsonHelper.getAsInt(obj, "minRolls", 1);
                int max = GsonHelper.getAsInt(obj, "maxRolls", 1);
                drops.add(new UniverseDrop(output, chance, min, max));
            }

            return new UniverseRecipe(pRecipeId, catalyst, drops);
        }

        @Override
        public @Nullable UniverseRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient catalyst = Ingredient.fromNetwork(pBuffer);
            int dropCount = pBuffer.readInt();
            List<UniverseDrop> drops = new ArrayList<>();

            for (int i = 0; i < dropCount; i++) {
                ItemStack stack = pBuffer.readItem();
                double chance = pBuffer.readDouble();
                int min = pBuffer.readInt();
                int max = pBuffer.readInt();
                drops.add(new UniverseDrop(stack, chance, min, max));
            }

            return new UniverseRecipe(pRecipeId, catalyst, drops);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, UniverseRecipe pRecipe) {
            pRecipe.catalyst.toNetwork(pBuffer);
            pBuffer.writeInt(pRecipe.drops.size());

            for (UniverseDrop drop : pRecipe.drops) {
                pBuffer.writeItemStack(drop.stack(), false);
                pBuffer.writeDouble(drop.chance());
                pBuffer.writeInt(drop.minRolls());
                pBuffer.writeInt(drop.maxRolls());
            }
        }
    }
}