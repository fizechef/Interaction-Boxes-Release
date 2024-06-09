package com.becks.interaction_boxes.common.functional.itemBoxItemRendererTypes;

import com.becks.interaction_boxes.common.predefinedInteractionBoxes.inventoryBox.InventoryBox;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
/**
 * Implementation of ItemBoxItemRendererType that fits the item model into the InventoryBox vertically
 */
public class VerticalFlatItem extends ItemBoxItemRendererType{
    @Override
    public double[] getRenderTranslation(InventoryBox itemBox, ItemStack stack) {
        return new double[] { itemBox.getMin(Direction.Axis.X) + (itemBox.getLength(Direction.Axis.X)/2), itemBox.getMin(Direction.Axis.Y)  + (itemBox.getLength(Direction.Axis.Y)/4), itemBox.getMin(Direction.Axis.Z)};
    }
    @Override
    public float getRenderScale(InventoryBox itemBox) {
        double shortestSide = Math.min(itemBox.getLength(Direction.Axis.X), itemBox.getLength(Direction.Axis.Y));
        return (float)(shortestSide * 2.0);
    }
    @Override
    public Quaternionf getRenderRotation(InventoryBox itemBox) {
        return new Quaternionf(0,0,0,1);
    }
}
