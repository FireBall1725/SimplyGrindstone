package ca.fireball1725.firelib2.common.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GrindstoneRecipeSerializer<T extends GrindstoneRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {
  private final GrindstoneRecipeSerializer.IFactory<T> factory;

  GrindstoneRecipeSerializer(GrindstoneRecipeSerializer.IFactory<T> factory) {
    this.factory = factory;
  }

  @Override
  @Nonnull
  public T read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
    String group = JSONUtils.getString(json, "group", "");
    JsonElement ingredientElement = JSONUtils.isJsonArray(json, "ingredient") ?
      JSONUtils.getJsonArray(json, "ingredient") :
      JSONUtils.getJsonObject(json, "ingredient");
    Ingredient ingredient = Ingredient.deserialize(ingredientElement);
    ItemStack result;

    if (!json.has("result")) {
      throw new JsonSyntaxException("Missing result, expected to find a string or object");
    }

    if (json.get("result").isJsonObject()) {
      result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
    } else {
      String resultString = JSONUtils.getString(json, "result");
      ResourceLocation resourceLocation = new ResourceLocation(resultString);
      result = new ItemStack(Registry.ITEM.getValue(resourceLocation).orElseThrow(() -> new IllegalStateException("Item: " + resultString + " does not exist")));
    }

    float experience = JSONUtils.getFloat(json, "experience", 0.0f);
    int grindingTime = JSONUtils.getInt(json, "grindingTime", 200);

    return this.factory.create(recipeId, group, ingredient, result, experience, grindingTime);
  }

  @Nullable
  @Override
  public T read(ResourceLocation recipeId, PacketBuffer buffer) {
    String group = buffer.readString(32767);
    Ingredient ingredient = Ingredient.read(buffer);
    ItemStack result = buffer.readItemStack();
    float experience = buffer.readFloat();
    int grindingTime = buffer.readInt();

    return this.factory.create(recipeId, group, ingredient, result, experience, grindingTime);
  }

  @Override
  public void write(PacketBuffer buffer, T recipe) {
    buffer.writeString(recipe.group);
    recipe.ingredient.write(buffer);
    buffer.writeItemStack(recipe.result);
    buffer.writeFloat(recipe.experience);
    buffer.writeInt(recipe.grindTime);
  }

  interface IFactory<T extends GrindstoneRecipe> {
    T create(ResourceLocation resourceLocation, String group, Ingredient ingredient, ItemStack result, float experience, int grindingTime);
  }
}
