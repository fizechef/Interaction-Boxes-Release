package com.becks.interaction_boxes.common.functional.itemBoxPuttableItems;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

/**
 * basic implemetation of ItemBoxPuttable that prevents BlockItems from being put
 */
public class NoBlockItems implements ItemBoxPuttable {
    @Override
    public boolean puttable(Item item) {
        return !(item instanceof BlockItem);
    }
}
