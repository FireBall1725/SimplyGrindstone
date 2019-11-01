package ca.fireball1725.simplygrindstone.common.container;

import ca.fireball1725.mods.firelib2.common.container.ContainerBase;
import ca.fireball1725.simplygrindstone.common.blocks.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class ContainerGrindstone extends ContainerBase {
  public ContainerGrindstone(int windowID, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(Blocks.GRINDSTONE.getBlock().getContainerType(), windowID, world, pos, playerInventory, player);

    tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
      //todo: this is where the slots go...
    });

    addPlayerInventorySlots(8, 68);
  }
}
