package ca.fireball1725.simplygrindstone;

import ca.fireball1725.mods.firelib2.FireMod;
import ca.fireball1725.simplygrindstone.client.render.RenderCrank;
import ca.fireball1725.simplygrindstone.common.blocks.Blocks;
import ca.fireball1725.simplygrindstone.common.items.Items;
import ca.fireball1725.simplygrindstone.common.tileentities.misc.TileEntityCrank;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;

@Mod("simplygrindstone")
public class SimplyGrindstone extends FireMod {

  public SimplyGrindstone() {
    super();
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
  }

  @Override
  public ArrayList<Block> getBlocks() {
    return Blocks.toList();
  }

  @Override
  public ArrayList<Item> getItems() {
    return Items.toList();
  }

  @Override
  public ArrayList<IRecipeSerializer<?>> getRecipeSerializers() {
    return null;
  }

  public void setup(final FMLCommonSetupEvent event) {
    DistExecutor.runWhenOn(Dist.CLIENT,
      () -> () -> ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrank.class, new RenderCrank()));
  }
}
