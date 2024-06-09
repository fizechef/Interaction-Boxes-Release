package com.becks.interaction_boxes.common.exampleBlocks.lockPedestal;

import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.inventoryBoxBlock.InventoryBoxBlockEntity;
import com.becks.interaction_boxes.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

/**
 * simple block entity class without additional functionality, inventory management is done in InventoryBoxBlockEntity
 */
public class LockPedestalBlockEntity extends InventoryBoxBlockEntity<ItemStackHandler> {
    public LockPedestalBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityInit.LOCK_PEDESTAL.get(), p_155229_, p_155230_, defaultInventory(2));
    }
}
