package ca.fireball1725.simplygrindstone.common.tileentities.machines;

import ca.fireball1725.mods.firelib2.common.tileentities.TileEntityBase;
import ca.fireball1725.simplygrindstone.common.blocks.Blocks;
import ca.fireball1725.simplygrindstone.common.container.ContainerGrindstone;
import ca.fireball1725.simplygrindstone.util.ICrankable;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityGrindstone extends TileEntityBase implements ICrankable, INamedContainerProvider {

  private LazyOptional<IItemHandler> handlerItem = LazyOptional.of(this::itemHandler);
  private int percentProcessed = 0;
  private boolean canCrank = false;

  public TileEntityGrindstone() {
    super(Blocks.GRINDSTONE.getBlock().getTileEntityType());
  }

  @Override
  public void doCrank() {
    percentProcessed += 20;

    if (percentProcessed >= 100) {
      percentProcessed = 0;
      // todo: item finished...
    }

    System.out.println(">>> Crank Progress: " + percentProcessed + "%");
  }

  @Override
  public boolean canCrank() {
    handlerItem.ifPresent(handler -> {
      boolean processingItem = !handler.getStackInSlot(3).isEmpty();

      if (!processingItem) {
        for (int i = 2; i >= 0; i--) {
          System.out.println(">>> " + i);
          ItemStack itemStack = handler.getStackInSlot(i);
          if (!itemStack.isEmpty()) {
            System.out.println(">>> Slot " + i + " <<< slot has something to process");
            ItemStack itemToProcess = handler.extractItem(i, 1, false);
            ItemStack result = handler.insertItem(3, itemToProcess, false);

            System.out.println(">>> Slot 3:" + handler.getStackInSlot(3).toString());

            processingItem = true;
            break;
          }
        }
      }

      canCrank = processingItem;
    });

    System.out.println(">>> Can Crank?");
    return canCrank;
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

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return handlerItem.cast();
    }

    return super.getCapability(cap, side);
  }

  private IItemHandler itemHandler() {
    return new ItemStackHandler(7) {
      @Override
      protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        if (slot == 3) {
          // todo: reset progress counter
        }
      }

      @Override
      public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        //if (slot >= 3)
        //  return false;

        // todo: check to see if item can be ground down...

        return super.isItemValid(slot, stack);
      }
    };
  }

  @Override
  public void read(CompoundNBT compound) {
    // Block Inventory
    CompoundNBT compoundInventory = compound.getCompound("inventory");
    handlerItem.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(compoundInventory));

    // Percent Processed
    percentProcessed = compound.getInt("percentProcessed");

    super.read(compound);
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    // Block Inventory
    handlerItem.ifPresent(h -> {
      CompoundNBT compoundInventory = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      compound.put("inventory", compoundInventory);
    });

    // Percent Processed
    compound.putInt("percentProcessed", percentProcessed);

    return super.write(compound);
  }

  public int getPercentProcessed() {
    return percentProcessed;
  }
}
