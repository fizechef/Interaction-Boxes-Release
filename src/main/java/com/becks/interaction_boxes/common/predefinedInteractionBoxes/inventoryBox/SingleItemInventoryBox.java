package com.becks.interaction_boxes.common.predefinedInteractionBoxes.inventoryBox;

import com.becks.interaction_boxes.common.functional.itemBoxItemRendererTypes.ItemBoxItemRendererType;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.InteractionBoxEntityBlock;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.inventoryBoxBlock.InventoryBoxBlockEntity;
import com.becks.interaction_boxes.common.functional.itemBoxPuttableItems.ItemBoxPuttable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * implementation of inventory box that lets a player only insert a single item into the linked inventory slot
 */
public class SingleItemInventoryBox extends InventoryBox {
    public SingleItemInventoryBox(VoxelShape shape, AABB aabb, int itemSlotNumber, ItemBoxItemRendererType render, ItemBoxPuttable puttable) {
        super(shape, aabb, itemSlotNumber, render, puttable);
    }
    public SingleItemInventoryBox(VoxelShape shape, int itemSlotNumber, ItemBoxItemRendererType render, ItemBoxPuttable puttable) {
        super(shape, itemSlotNumber, render, puttable);
    }
    @Override
    public void onRemoveItem(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity bEntity, Player pPlayer) {
        if (bEntity instanceof InventoryBoxBlockEntity bIEntity){
            pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY(), pPos.getZ() + 0.5, bIEntity.removeItem(this.getSlot())));
            pLevel.sendBlockUpdated(pPos, pState, pState, 3);
        }
    }

    @Override
    public void onAddItem(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity bEntity, Player pPlayer) {
        if (bEntity instanceof InventoryBoxBlockEntity bIEntity){
            bIEntity.setItem(this.getSlot(), pPlayer.getMainHandItem().split(1));
            pLevel.sendBlockUpdated(pPos, pState, pState, 3);
        }
    }
}
