package ca.fireball1725.simplygrindstone.common.tileentities.machines;

import ca.fireball1725.mods.firelib2.common.tileentities.TileEntityBase;
import ca.fireball1725.simplygrindstone.common.blocks.Blocks;
import ca.fireball1725.simplygrindstone.util.ICrankable;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityGrindstone extends TileEntityBase implements ICrankable {

  public TileEntityGrindstone() {
    super(Blocks.GRINDSTONE.getBlock().getTileEntityType());
  }

  @Override
  public void doCrank() {
    System.out.println(">>> Block Cranked");
  }

  @Override
  public boolean canCrank() {
    return false;
  }
}
