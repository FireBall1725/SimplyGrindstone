package ca.fireball1725.simplygrindstone.common.tileentities.machines;

import ca.fireball1725.mods.firelib2.common.tileentities.TileEntityBase;
import ca.fireball1725.simplygrindstone.common.blocks.Blocks;
import ca.fireball1725.simplygrindstone.common.container.ContainerGrindstone;
import ca.fireball1725.simplygrindstone.util.ICrankable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public class TileEntityGrindstone extends TileEntityBase implements ICrankable, INamedContainerProvider {

  public TileEntityGrindstone() {
    super(Blocks.GRINDSTONE.getBlock().getTileEntityType());
  }

  @Override
  public void doCrank() {
    System.out.println(">>> Block Cranked");
  }

  @Override
  public boolean canCrank() {
    return true;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
    return new ContainerGrindstone(p_createMenu_1_, world, pos, p_createMenu_2_, p_createMenu_3_);
  }
}
