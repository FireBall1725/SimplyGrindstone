package ca.fireball1725.simplygrindstone;

import ca.fireball1725.mods.firelib2.FireMod;
import ca.fireball1725.simplygrindstone.common.blocks.Blocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod("simplygrindstone")
public class SimplyGrindstone extends FireMod {

  public SimplyGrindstone() {
    super();
  }

  @Override
  public ArrayList<Block> getBlocks() {
    return Blocks.toList();
  }

  @Override
  public ArrayList<Item> getItems() {
    return null;
  }

  @Override
  public ArrayList<IRecipeSerializer<?>> getRecipeSerializers() {
    return null;
  }
}
