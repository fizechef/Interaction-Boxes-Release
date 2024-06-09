package com.becks.interaction_boxes.common.functional.itemBoxPuttableItems;

import net.minecraft.world.item.Item;

/**
 * Function that decides if an itemStack can be put into an InventoryBox inventory by hand, this does not block the item from entering the InventoryBox by other means by default as the underlying inventory implementation is default
 */
@FunctionalInterface
public interface ItemBoxPuttable {
    /**
     * Method that decides if an ItemStack may be put by hand
     * @param item the ItemStack that needs to be checked
     * @return boolean value
     */
    boolean puttable(Item item);
}
