package ca.fireball1725.firelib2.util;

import ca.fireball1725.firelib2.common.blocks.BlockBase;
import ca.fireball1725.firelib2.common.blocks.IBlockItemProvider;
import ca.fireball1725.firelib2.common.blocks.IItemPropertiesFiller;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class RegistrationHelper {
  private static final Map<String, ModData> modDataMap = new HashMap<>();

  public static void registerBlock(Block block) {
    register(block);

    if (block.hasTileEntity(null) && block instanceof BlockBase) {
      register(((BlockBase) block).getTileEntityType());
    }

    Item blockItem = createBlockItem(block);
    registerItem(blockItem);
  }

  public static void registerItem(Item item) {
    register(item);
  }

  public static void registerTileEntity(TileEntityType<? extends TileEntity> tileEntityType) {
    register(tileEntityType);
  }

  private static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistryEntry<T> object) {
    if (object == null)
      throw new IllegalArgumentException("Cannot register a null object");
    if (object.getRegistryName() == null)
      throw new IllegalArgumentException("Cannot register an object without a registry name");

    getModData().modDefers.put(object.getRegistryType(), () -> object);
  }

  private static ModData getModData() {
    String modID = ModLoadingContext.get().getActiveNamespace();

    ModData data = modDataMap.get(modID);
    if (data == null) {
      data = new ModData();
      modDataMap.put(modID, data);

      FMLJavaModLoadingContext.get().getModEventBus().addListener(RegistrationHelper::onRegistryEvent);
    }

    return data;
  }

  private static void onRegistryEvent(RegistryEvent.Register<?> event) {
    getModData().register(event.getRegistry());
  }

  private static Item createBlockItem(Block block) {
    Item.Properties itemProperties = new Item.Properties();
    ResourceLocation registryName = Objects.requireNonNull(block.getRegistryName());

    if (block instanceof IItemPropertiesFiller)
      ((IItemPropertiesFiller) block).fillProperties(itemProperties);

    BlockItem blockItem;
    if (block instanceof IBlockItemProvider)
      blockItem = ((IBlockItemProvider) block).provideBlockItem(block, itemProperties);
    else blockItem = new BlockItem(block, itemProperties);

    return blockItem.setRegistryName(registryName);
  }

  private static class ModData {
    private ArrayListMultimap<Class<?>, Supplier<IForgeRegistryEntry<?>>> modDefers = ArrayListMultimap.create();

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void register(IForgeRegistry registry) {
      Class<?> registryType = registry.getRegistrySuperType();

      if (modDefers.containsKey(registryType)) {
        Collection<Supplier<IForgeRegistryEntry<?>>> modEntries = modDefers.get(registryType);
        modEntries.forEach(supplier -> {
          IForgeRegistryEntry<?> entry = supplier.get();
          registry.register(entry);
        });
      }
    }
  }
}