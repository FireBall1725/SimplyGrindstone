package ca.fireball1725.simplygrindstone.common.blocks;

import ca.fireball1725.firelib2.common.blocks.BlockBase;
import ca.fireball1725.firelib2.common.blocks.IFireBlock;
import ca.fireball1725.simplygrindstone.common.blocks.machines.Grindstone;
import net.minecraft.item.BlockItem;

import java.util.Objects;
import java.util.function.Supplier;

public enum Blocks implements IFireBlock {
  GRINDSTONE(Grindstone::new)
  ;

  private final BlockBase block;

  Blocks(Supplier<BlockBase> blockSupplier) {
    Objects.requireNonNull(blockSupplier);
    this.block = blockSupplier.get();
  }

  @Override
  public BlockBase getBlock() {
    return this.block;
  }
}
