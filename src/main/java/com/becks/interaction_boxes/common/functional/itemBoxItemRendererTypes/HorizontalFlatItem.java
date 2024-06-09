package com.becks.interaction_boxes.common.functional.itemBoxItemRendererTypes;

import com.becks.interaction_boxes.common.predefinedInteractionBoxes.inventoryBox.InventoryBox;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.joml.Quaternionf;

/**
 * Implementation of ItemBoxItemRendererType that fits the item model into the InventoryBox horizontally
 */
public class HorizontalFlatItem extends ItemBoxItemRendererType {
    @Override
    public double[] getRenderTranslation(InventoryBox itemBox, ItemStack stack) {
        if (stack.getItem() instanceof ShieldItem){
            return new double[] { itemBox.getMin(Direction.Axis.X) + (itemBox.getLength(Direction.Axis.X)/2), itemBox.getMin(Direction.Axis.Y) + itemBox.getLength(Direction.Axis.Y), itemBox.getMin(Direction.Axis.Z)  + (itemBox.getLength(Direction.Axis.Z)/4)};
        }
        return new double[] { itemBox.getMin(Direction.Axis.X) + (itemBox.getLength(Direction.Axis.X)/2), itemBox.getMin(Direction.Axis.Y), itemBox.getMin(Direction.Axis.Z)  + (itemBox.getLength(Direction.Axis.Z)/4)};
    }
    @Override
    public float getRenderScale(InventoryBox itemBox) {
        double shortestSide = Math.min(itemBox.getLength(Direction.Axis.X), itemBox.getLength(Direction.Axis.Z));
        return (float)(shortestSide * 2.0);
    }
    @Override
    public Quaternionf getRenderRotation(InventoryBox itemBox) {
        double radianRot = Math.PI/2.0;
        return new Quaternionf(1 * Math.sin(radianRot/2.0),0,0,Math.cos(radianRot/2.0));
    }
}
