package ca.fireball1725.firelib2.util;

import ca.fireball1725.firelib2.common.blocks.BlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class OrientationTools {
  public static Direction getOrientationHorizontal(BlockState blockState) {
    return blockState.get(BlockStateProperties.HORIZONTAL_FACING);
  }

  public static Direction getOrientation(BlockState blockState) {
    return ((BlockBase) blockState.getBlock()).getFrontDirection(blockState);
  }

  public static Direction determineOrientation(BlockPos blockPos, LivingEntity livingEntity) {
    return determineOrientation(blockPos.getX(), blockPos.getY(), blockPos.getZ(), livingEntity);
  }

  public static Direction determineOrientation(int x, int y, int z, LivingEntity livingEntity) {
    if (MathHelper.abs((float) livingEntity.posX - x) < 2.0F && MathHelper.abs((float) livingEntity.posZ - z) < 2.0F) {
      double d0 = livingEntity.posY + 1.82D - livingEntity.getYOffset();

      if (d0 - y > 2.0D)
        return Direction.UP;

      if (y - d0 > 0.0D)
        return Direction.DOWN;
    }

    return determineOrientationHorizontal(livingEntity);
  }

  public static Direction determineOrientationHorizontal(LivingEntity livingEntity) {
    int i = (int) ((livingEntity.rotationYaw * 4.0F / 360.0F) + 0.5D);
    int l = ((livingEntity.rotationYaw * 4.0F / 360.0F) + 0.5D < i ? i - 1 : i) & 3;
    return l == 0 ? Direction.NORTH : (l == 1 ? Direction.EAST : (l == 2 ? Direction.SOUTH : (l == 3 ? Direction.WEST : Direction.DOWN)));
  }

  public static Direction getFacingFromEntity(BlockPos blockPos, Entity entity) {
    return getFacingFromEntity(blockPos.getX(), blockPos.getY(), blockPos.getZ(), entity);
  }

  public static Direction getFacingFromEntity(int x, int y, int z, Entity entity) {
    if (MathHelper.abs((float) entity.posX - x) < 2.0F && MathHelper.abs((float) entity.posZ - z) < 2.0F) {
      double d0 = entity.posY + entity.getEyeHeight();

      if (d0 - y > 2.0D)
        return Direction.UP;

      if (y - d0 > 0.0D)
        return Direction.DOWN;
    }

    return entity.getHorizontalFacing().getOpposite();
  }
}
