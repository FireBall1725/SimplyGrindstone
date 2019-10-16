package ca.fireball1725.firelib2.common.recipes;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("firelib2")
@Mod.EventBusSubscriber(modid = "firelib2", bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeSubscriber {
  public static final GrindstoneRecipeSerializer<GrindstoneRecipe> grindstone = null;

  @SubscribeEvent
  public static void registerRecipe(RegistryEvent.Register<IRecipeSerializer<?>> event) {
    IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();
    registry.register(new GrindstoneRecipeSerializer<>(GrindstoneRecipe::new).setRegistryName("firelib2", "grindstone"));
  }
}
