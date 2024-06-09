package com.becks.interaction_boxes.common.predefinedInteractionBoxes.inventoryBox;

import com.becks.interaction_boxes.common.functional.itemBoxItemRendererTypes.ItemBoxItemRendererType;
import com.becks.interaction_boxes.common.functional.itemBoxPuttableItems.ItemBoxPuttable;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.inventoryBoxBlock.InventoryBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * implementation of virtual item box that only lets a plyer set the item, but not remove it. The item contained in the linked slot is compared to a second item contained in a different slot to set a "lock"
 */
public class LockItemBox extends VirtualItemInventoryBox {
    private final SingleItemInventoryBox lockBox;
    public LockItemBox(SingleItemInventoryBox lockBox, VoxelShape shape, AABB aabb, int itemSlotNumber, ItemBoxItemRendererType render, ItemBoxPuttable puttable) {
        super(shape, aabb, itemSlotNumber, render, puttable);
        this.lockBox = lockBox;
    }
    public LockItemBox(SingleItemInventoryBox lockBox, VoxelShape shape, int itemSlotNumber, ItemBoxItemRendererType render, ItemBoxPuttable puttable) {
        super(shape, itemSlotNumber, render, puttable);
        this.lockBox = lockBox;
    }
    public SingleItemInventoryBox getLockBox(){
        return this.lockBox;
    }
    @Override
    void onRemoveItem(Level pLevel, BlockPos pPos, BlockState pState, InventoryBoxBlockEntity bEntity, Player pPlayer) {
    }
}
