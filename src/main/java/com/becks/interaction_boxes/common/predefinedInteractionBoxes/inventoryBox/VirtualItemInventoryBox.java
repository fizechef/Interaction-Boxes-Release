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
 * implementation of inventory box that lets a player set the item contained in the linked slot wiout removing the item from a players inventory, removing the item will simply delete it
 */
public class VirtualItemInventoryBox extends InventoryBox{
    public VirtualItemInventoryBox(VoxelShape shape, AABB aabb, int itemSlotNumber, ItemBoxItemRendererType render, ItemBoxPuttable puttable) {
        super(shape, aabb, itemSlotNumber, render, puttable);
    }
    public VirtualItemInventoryBox(VoxelShape shape, int itemSlotNumber, ItemBoxItemRendererType render, ItemBoxPuttable puttable) {
        super(shape, itemSlotNumber, render, puttable);
    }
    @Override
    public void onRemoveItem(Level pLevel, BlockPos pPos, BlockState pState, InventoryBoxBlockEntity<?> bEntity, Player pPlayer) {
        bEntity.removeItem(this.getSlot());
        pLevel.sendBlockUpdated(pPos, pState, pState, 3);
    }
    @Override
    public void onAddItem(Level pLevel, BlockPos pPos, BlockState pState, InventoryBoxBlockEntity<?> bEntity, Player pPlayer) {
        bEntity.setItem(this.getSlot(), pPlayer.getMainHandItem().copy());
        pLevel.sendBlockUpdated(pPos, pState, pState, 3);
    }
}
