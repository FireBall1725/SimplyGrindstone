package ca.fireball1725.simplygrindstone.common.container;

import ca.fireball1725.mods.firelib2.common.container.ContainerBase;
import ca.fireball1725.mods.firelib2.common.container.slots.SlotOutput;
import ca.fireball1725.simplygrindstone.common.blocks.Blocks;
import ca.fireball1725.simplygrindstone.common.tileentities.machines.TileEntityGrindstone;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerGrindstone extends ContainerBase {
  public ContainerGrindstone(int windowID, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(Blocks.GRINDSTONE.getBlock().getContainerType(), windowID, world, pos, playerInventory, player);

    tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
      // Input Slots
      addSlot(new SlotItemHandler(iItemHandler, 0, 8, 24));
      addSlot(new SlotItemHandler(iItemHandler, 1, 26, 24));
      addSlot(new SlotItemHandler(iItemHandler, 2, 44, 24));

      // Processing Slot
      //addSlot(new SlotOutput(player, iItemHandler, 5, 80, 24));
      addSlot(new SlotItemHandler(iItemHandler, 3, 80, 24));

      // Output Slots
      addSlot(new SlotItemHandler(iItemHandler, 4, 116, 24));
      addSlot(new SlotItemHandler(iItemHandler, 5, 134, 24));
      addSlot(new SlotItemHandler(iItemHandler, 6, 152, 24));
    });

    addPlayerInventorySlots(8, 68);
  }

  public int getPercentProcessed() {
    return ((TileEntityGrindstone)tileEntity).getPercentProcessed();
  }
}
