package ca.fireball1725.simplygrindstone.common.tileentities.misc;

import ca.fireball1725.mods.firelib2.common.tileentities.TileEntityBase;
import ca.fireball1725.mods.firelib2.util.TileHelper;
import ca.fireball1725.simplygrindstone.common.blocks.misc.BlockCrank;
import ca.fireball1725.simplygrindstone.util.ICrankable;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class TileEntityCrank extends TileEntityBase implements ITickableTileEntity {
  private int rotation = 0;
  private boolean rotating = false;
  private int badCrankCount = 0;
  private int crankTickCounter = 400;

  public TileEntityCrank(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Override
  public void tick() {
    if (rotating) {
      rotation += 15;

      if (rotation % 180 == 0) {
        rotating = false;
        crankDone();
      }

      if (rotation == 360)
        rotation = 0;
    }
  }

  private void crankDone() {
    BlockPos pos = getPos().down();

    TileEntity tileEntity = TileHelper.getTileEntity(getWorld(), pos, TileEntity.class);
    if (tileEntity instanceof ICrankable)
      ((ICrankable) tileEntity).doCrank();
  }

  public float getRotation() {
    if (getWorld() != null && !isMachineValid())
      breakCrank(true, false);

    float rotation = this.rotation + calculateRotationOffset();
    if (rotation >= 360)
      rotation -= 360;
    return rotation;
  }

  public boolean isRotating() {
    return rotating;
  }

  private void breakCrank(boolean dropCrank, boolean dropSticks) {
    System.out.println(">>> Breaking Crank");
    if (dropSticks) {
      Random rand = new Random();
      ItemStack itemSticks = new ItemStack(Items.STICK, rand.nextInt(4));
      TileHelper.dropItemStack(itemSticks, this.world, this.pos);
    }
    world.destroyBlock(pos, dropCrank);
  }

  @Override
  public boolean hasFastRenderer() {
    return true;
  }

  public void doCrank() {
    if (rotating)
      return;

    if (this.getWorld().isRemote)
      return;

    BlockCrank.CrankMaterial material = ((BlockCrank) getBlockState().getBlock()).getCrankMaterial();

    TileEntity tileEntity = TileHelper.getTileEntity(getWorld(), getPos().down(), TileEntity.class);
    if (!(tileEntity != null && tileEntity instanceof ICrankable && ((ICrankable) tileEntity).canCrank())) {
      //todo: is material wood?
      if (badCrankCount >= 4 && material == BlockCrank.CrankMaterial.WOOD)
        breakCrank(false, true);

      badCrankCount++;

      this.markForUpdate();
      this.markDirty();
      return;
    }

    // Reset badCrankCount
    badCrankCount = 0;

    // todo: Random break if material is wood

    this.rotating = true;
    this.markForUpdate();
    this.markDirty();
  }

  private boolean isMachineValid() {
    if (getWorld() == null)
      return false;

    TileEntity tileEntity = TileHelper.getTileEntity(getWorld(), getPos().down(), TileEntity.class);
    return tileEntity instanceof ICrankable;
  }

  private int calculateRotationOffset() {
    if (getWorld() == null)
      return 0;

    BlockState blockState = getWorld().getBlockState(getPos().down());
    if (blockState.has(BlockStateProperties.HORIZONTAL_FACING)) {
      Direction direction = blockState.get(BlockStateProperties.HORIZONTAL_FACING);

      switch (direction) {
        case NORTH:
        default:
          return 90;
        case EAST:
          return 180;
        case SOUTH:
          return 270;
        case WEST:
          return 0;
      }
    }

    return 0;
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
