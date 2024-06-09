package com.becks.interaction_boxes.common.functional.itemBoxItemRendererTypes;

import com.becks.interaction_boxes.common.predefinedInteractionBoxes.inventoryBox.InventoryBox;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

/**
 * ItemBoxItemRendererType describes how the visual item content representation of a InventoryBox is displayed in the world.
 * Extends this to create a new rendering scheme for some new InventoryBox or use example implementations HorizontalFlatItem and VerticalFlatItem
 */
public abstract class ItemBoxItemRendererType {
    /**
     * Gets a translation to apply to the rendered item model, this should be somehow relative to the size and position of the box to make sense, but doesn't have to be
     * See example implementations HorizontalFlatItem and VerticalFlatItem for more info
     * @param itemBox the InventoryBox the item is in, usefull to get size and positioning info
     * @param stack the item stack that is actually rendered
     * @return double[3] with translation for each dimension x y z
     */
    public abstract double[] getRenderTranslation(InventoryBox itemBox, ItemStack stack);

    /**
     * Gets a scaling factor apllied to the size of the rendered item model, this should be somehow relative to the size of the box to make sense, but doesn't have to be
     * @param itemBox the InventoryBox the item is in, usefull to get size info
     * @return float value multiplied onto the item models base size
     */
    public abstract float getRenderScale(InventoryBox itemBox);

    /**
     * gets a Rotation Quaternion to apply to the base item model, this should be somehow relative to the size and position of the box to make sense, but doesn't have to be
     * @param itemBox the InventoryBox the item is in, usefull to get size and positioning info
     * @return Quaternion representing a rotation in 3d space
     */
    public abstract Quaternionf getRenderRotation(InventoryBox itemBox);
}
