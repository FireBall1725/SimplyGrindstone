package ca.fireball1725.firelib2.common.blocks;

import ca.fireball1725.firelib2.common.tileentities.TileEntityBase;
import ca.fireball1725.firelib2.util.OrientationTools;
import ca.fireball1725.firelib2.util.RotationType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class BlockBase extends Block {
  public BlockBase(Properties properties) {
    super(properties);
  }
  private TileEntityType<? extends TileEntity> tileEntityType;
  private Supplier<TileEntity> tileEntitySupplier;
  private boolean canRotate = false;

  public static final IProperty<?>[] ROTATION_HORIZONTAL_PROPERTIES = new IProperty[]{BlockStateProperties.HORIZONTAL_FACING};
  public static final IProperty<?>[] ROTATION_FULL_PROPERTIES = new IProperty[]{BlockStateProperties.FACING};
  public static final IProperty<?>[] ROTATION_NONE_PROPERTIES = new IProperty[0];

  public Item.Properties getItemProperties() {
    return new Item.Properties();
  }

  public void setTileEntity(Supplier<TileEntity> tileEntitySupplier) {
    this.tileEntitySupplier = tileEntitySupplier;
    this.tileEntityType = TileEntityType.Builder.create(Objects.requireNonNull(tileEntitySupplier), this).build(null)
      .setRegistryName(Objects.requireNonNull(getRegistryName()));
  }

  @Nullable
  public TileEntityType<? extends TileEntity> getTileEntityType() {
    return tileEntityType;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return tileEntitySupplier != null;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return tileEntitySupplier.get();
  }

  public void setCanRotate(boolean canRotate) {
    this.canRotate = canRotate;
  }




  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING);
  }





  public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
    return Direction.getFacingFromVector((float) (entity.posX - clickedBlock.getX()), (float) (entity.posY - clickedBlock.getY()), (float) (entity.posZ - clickedBlock.getZ()));
  }

  public RotationType getRotationType() {
    return RotationType.FULL;
  }

  protected IProperty<?>[] getProperties() {
    return getProperties(getRotationType());
  }

  public static IProperty<?>[] getProperties(RotationType rotationType) {
    switch (rotationType) {
      case FULL:
        return ROTATION_FULL_PROPERTIES;
      case HORIZONTAL:
        return ROTATION_HORIZONTAL_PROPERTIES;
      case NONE:
      default:
        return ROTATION_NONE_PROPERTIES;
    }
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    PlayerEntity placer = context.getPlayer();
    BlockPos pos = context.getPos();
    BlockState state = super.getStateForPlacement(context);
    switch (getRotationType()) {
      case FULL:
        return state.with(BlockStateProperties.FACING, OrientationTools.getFacingFromEntity(pos, placer));
      case HORIZONTAL:
        return state.with(BlockStateProperties.HORIZONTAL_FACING, placer.getHorizontalFacing().getOpposite());
      default:
        return state;
    }
  }

  protected Direction getOrientation(BlockPos pos, LivingEntity entity) {
    switch (getRotationType()) {
      case FULL:
        return OrientationTools.determineOrientation(pos, entity);
      case HORIZONTAL:
        return OrientationTools.determineOrientationHorizontal(entity);
      default:
        return null;
    }
  }

  public Direction getFrontDirection(BlockState state) {
    switch (getRotationType()) {
      case FULL:
        return state.get(BlockStateProperties.FACING);
      case HORIZONTAL:
        return state.get(BlockStateProperties.HORIZONTAL_FACING);
      default:
        return Direction.NORTH;
    }
  }

  @Override
  public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
    switch (getRotationType()) {
      case FULL:
        state = state.with(BlockStateProperties.FACING, direction.rotate(state.get(BlockStateProperties.FACING)));
        break;
      case HORIZONTAL:
        state = state.with(BlockStateProperties.HORIZONTAL_FACING, direction.rotate(state.get(BlockStateProperties.HORIZONTAL_FACING)));
        break;
      default:
    }
    TileEntity tileEntity = world.getTileEntity(pos);
    if (tileEntity instanceof TileEntityBase) {
      //((TileEntityBase) tileEntity).rotateBlock(direction);
    }
    return state;
  }

  public Direction getRightDirection(BlockState state) {
    return getFrontDirection(state).rotateYCCW();
  }

  public Direction getLeftDirection(BlockState state) {
    return getFrontDirection(state).rotateY();
  }

  public static Direction getFrontDirection(RotationType rotationType, BlockState state) {
    switch (rotationType) {
      case FULL:
        return OrientationTools.getOrientation(state);
      case HORIZONTAL:
        return OrientationTools.getOrientationHorizontal(state);
      default:
        return Direction.SOUTH;
    }
  }
}
