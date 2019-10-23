package ca.fireball1725.simplygrindstone.common.blocks;

import ca.fireball1725.mods.firelib2.common.blocks.BlockBase;
import ca.fireball1725.mods.firelib2.common.blocks.IFireBlock;
import ca.fireball1725.simplygrindstone.common.blocks.machines.BlockGrindstone;
import ca.fireball1725.simplygrindstone.common.blocks.misc.BlockCrank;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

public enum Blocks implements IFireBlock {
  GRINDSTONE(BlockGrindstone::new),
  CRANK(BlockCrank::new)
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

  public static ArrayList<Block> toList() {
    ArrayList<Block> blocks = new ArrayList<>();

    for (Blocks block : Blocks.values()) {
      blocks.add(block.getBlock());
    }

    return blocks;
  }
}
