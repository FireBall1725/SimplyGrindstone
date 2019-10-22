package ca.fireball1725.simplygrindstone.common.blocks.misc;

import ca.fireball1725.mods.firelib2.common.blocks.BlockBase;
import ca.fireball1725.mods.firelib2.util.RotationHelper;
import ca.fireball1725.mods.firelib2.util.RotationType;
import ca.fireball1725.mods.firelib2.util.TileHelper;
import ca.fireball1725.simplygrindstone.common.tileentities.misc.TileEntityCrank;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCrank extends BlockBase {
  private final VoxelShape crankTop = VoxelShapes.create(3 / 16d, 8 / 16d, 7 / 16d, 13 / 16d, 10 / 16d, 9 / 16d);
  private final VoxelShape crankShaft = VoxelShapes.create(7 / 16d, 0 / 16d, 7 / 16d, 9 / 16d, 8 / 16d, 9 / 16d);

  public BlockCrank() {
    super(Properties.create(Material.WOOD)
      .sound(SoundType.WOOD)
      .hardnessAndResistance(2.0f)
    );
    setRegistryName("crank");
    setCanRotate(false);
    setTileEntity(TileEntityCrank::new);
  }

  @Override
  public RotationType getRotationType() {
    return RotationType.HORIZONTAL;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    VoxelShape crankTopRotated = RotationHelper.rotateShape(crankTop, state.get(BlockStateProperties.HORIZONTAL_FACING));
    return VoxelShapes.or(crankShaft, crankTopRotated);
  }

  @Override
  public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    TileEntityCrank crank = TileHelper.getTileEntity(worldIn, pos, TileEntityCrank.class);
    if (crank == null)
      return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);

    crank.doCrank();

    return true;
  }
}