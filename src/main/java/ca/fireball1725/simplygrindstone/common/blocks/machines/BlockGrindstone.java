package ca.fireball1725.simplygrindstone.common.blocks.machines;

import ca.fireball1725.mods.firelib2.common.blocks.BlockBase;
import ca.fireball1725.mods.firelib2.util.RotationType;
import ca.fireball1725.mods.firelib2.util.TileHelper;
import ca.fireball1725.simplygrindstone.client.gui.ScreenGrindstone;
import ca.fireball1725.simplygrindstone.common.blocks.misc.BlockCrank;
import ca.fireball1725.simplygrindstone.common.container.ContainerGrindstone;
import ca.fireball1725.simplygrindstone.common.tileentities.machines.TileEntityGrindstone;
import ca.fireball1725.simplygrindstone.common.tileentities.misc.TileEntityCrank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockGrindstone extends BlockBase {
  public BlockGrindstone() {
    super(Properties.create(Material.ROCK)
      .sound(SoundType.METAL)
      .hardnessAndResistance(2.0f)
    );
    setRegistryName("grindstone");
    setCanRotate(true);
    setTileEntity(TileEntityGrindstone::new);
    setGuiContainer(ContainerGrindstone::new);
    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> setGuiScreen(ScreenGrindstone::new));
  }

  @Override
  public RotationType getRotationType() {
    return RotationType.HORIZONTAL;
  }

  @Override
  public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    if (!worldIn.isRemote) {
      Block handItem = Block.getBlockFromItem(player.getHeldItemMainhand().getItem());
      if (handItem instanceof BlockCrank && hit.getFace() == Direction.UP)
        return false;

      TileEntity tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntity.class);
      if (tileEntity instanceof INamedContainerProvider) {
        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
      }
      return true;
    }
    return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
  }
}
