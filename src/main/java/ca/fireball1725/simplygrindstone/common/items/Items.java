package ca.fireball1725.simplygrindstone.common.items;

import ca.fireball1725.mods.firelib2.common.items.IFireItem;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

public enum Items implements IFireItem {
  GRINDWHEEL_STONE(() -> {
    return new ItemGrindWheel();
  }),
  ;

  private final Item item;

  Items(Supplier<Item> itemSupplier) {
    Objects.requireNonNull(itemSupplier);
    item = itemSupplier.get();
  }

  public static ArrayList<Item> toList() {
    ArrayList<Item> items = new ArrayList<>();

    for (Items item : Items.values()) {
      items.add(item.getItem());
    }

    return items;
  }

  @Override
  public Item getItem() {
    return item;
  }
}
