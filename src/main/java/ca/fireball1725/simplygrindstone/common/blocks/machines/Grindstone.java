package ca.fireball1725.simplygrindstone.common.blocks.machines;

import ca.fireball1725.mods.firelib2.common.blocks.BlockBase;
import ca.fireball1725.mods.firelib2.util.RotationType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class Grindstone extends BlockBase {
  public Grindstone() {
    super(Properties.create(Material.ROCK)
      .sound(SoundType.METAL)
      .hardnessAndResistance(2.0f)
    );
    setRegistryName("grindstone");
    setCanRotate(true);
  }

  @Override
  public RotationType getRotationType() {
    return RotationType.HORIZONTAL;
  }

}
