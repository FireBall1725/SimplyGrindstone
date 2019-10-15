package ca.fireball1725.firelib2.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public interface IBlockItemProvider {
  BlockItem provideBlockItem(Block block, Item.Properties itemProperties);
}
