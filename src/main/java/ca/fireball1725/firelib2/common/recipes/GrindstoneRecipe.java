package ca.fireball1725.firelib2.common.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class GrindstoneRecipe implements IRecipe<IInventory> {
  public static final IRecipeType<GrindstoneRecipe> grindstone = IRecipeType.register("grindstone");

  private final IRecipeType<?> type;
  private final ResourceLocation id;

  final String group;
  final Ingredient ingredient;
  final ItemStack result;
  final float experience;
  final int grindTime;

  GrindstoneRecipe(ResourceLocation resourceLocation, String group, Ingredient ingredient, ItemStack result, float experience, int grindTime) {
    type = grindstone;
    id = resourceLocation;
    this.group = group;
    this.ingredient = ingredient;
    this.result = result;
    this.experience = experience;
    this.grindTime = grindTime;
  }

  @Override
  public boolean matches(IInventory inv, World worldIn) {
    return this.ingredient.test(inv.getStackInSlot(0));
  }

  @Override
  @Nonnull
  public ItemStack getCraftingResult(IInventory inv) {
    return this.result.copy();
  }

  @Override
  public boolean canFit(int width, int height) {
    return true;
  }

  @Override
  @Nonnull
  public ItemStack getRecipeOutput() {
    return result;
  }

  @Override
  @Nonnull
  public ResourceLocation getId() {
    return id;
  }

  @Override
  @Nonnull
  public IRecipeSerializer<?> getSerializer() {
    return RecipeSubscriber.grindstone;
  }

  @Override
  @Nonnull
  public IRecipeType<?> getType() {
    return type;
  }

  @Override
  @Nonnull
  public NonNullList<Ingredient> getIngredients() {
    NonNullList<Ingredient> nonNullList = NonNullList.create();
    nonNullList.add(this.ingredient);
    return nonNullList;
  }

  @Override
  @Nonnull
  public ItemStack getIcon() {
    return new ItemStack(null);
  }

  public int getGrindTime() {
    return grindTime;
  }
}
