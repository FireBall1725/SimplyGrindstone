package ca.fireball1725.simplygrindstone.common.tileentities.misc;

import ca.fireball1725.mods.firelib2.FireLib2;
import ca.fireball1725.mods.firelib2.common.tileentities.TileEntityBase;
import ca.fireball1725.mods.firelib2.util.TileHelper;
import ca.fireball1725.simplygrindstone.SimplyGrindstone;
import ca.fireball1725.simplygrindstone.common.blocks.Blocks;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityCrank extends TileEntityBase implements ITickableTileEntity {
  private int rotation = 0;
  private boolean rotating = false;
  private int badCrankCount = 0;
  private int crankTickCounter = 400;

  public TileEntityCrank() {
    super(Blocks.CRANK.getBlock().getTileEntityType());
  }

  @Override
  public void tick() {
    if (rotating) {
      rotation += 15;

      if (rotation % 180 == 0) {
        rotating = false;
        // todo: call crankdone
      }

      if (rotation == 360)
        rotation = 0;
    }
  }

  public float getRotation() {
    //todo: check block below
    return rotation;
  }

  public boolean isRotating() {
    return rotating;
  }

  private void breakCrank() {
    Random rand = new Random();
    ItemStack itemSticks = new ItemStack(Items.STICK, rand.nextInt(4));
    world.destroyBlock(pos, false);
    TileHelper.dropItemStack(itemSticks, this.world, this.pos);
  }

  @Override
  public boolean hasFastRenderer() {
    return true;
  }

  public void doCrank() {
    ItemStack itemStack = new ItemStack(Blocks.CRANK.getBlock());

    //breakCrank();
    //todo: check block below

    if (rotating)
      return;

    if (this.getWorld().isRemote)
      return;

    // todo: Random break

    this.rotating = true;
    this.markForUpdate();
    this.markDirty();
  }

  @Override
  public void read(CompoundNBT compound) {
    super.read(compound);

    this.rotating = compound.getBoolean("rotating");
    this.badCrankCount = compound.getInt("badCrankCount");
    this.crankTickCounter = compound.getInt("crankTickCounter");
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    compound.putBoolean("rotating", this.rotating);
    compound.putInt("badCrankCount", this.badCrankCount);
    compound.putInt("crankTickCounter", this.crankTickCounter);

    return super.write(compound);
  }
}
