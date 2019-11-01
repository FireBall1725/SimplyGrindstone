package ca.fireball1725.simplygrindstone.common.blocks.machines;

import ca.fireball1725.mods.firelib2.common.blocks.BlockBase;
import ca.fireball1725.mods.firelib2.util.RotationType;
import ca.fireball1725.simplygrindstone.common.container.ContainerGrindstone;
import ca.fireball1725.simplygrindstone.common.tileentities.machines.TileEntityGrindstone;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;

public class BlockGrindstone extends BlockBase {
  public BlockGrindstone() {
    super(Properties.create(Material.ROCK)
      .sound(SoundType.METAL)
      .hardnessAndResistance(2.0f)
    );
    setRegistryName("grindstone");
    setCanRotate(true);
    setTileEntity(TileEntityGrindstone::new);
    setContainer(ContainerGrindstone::new);
  }

  @Override
  public RotationType getRotationType() {
    return RotationType.HORIZONTAL;
  }

}
