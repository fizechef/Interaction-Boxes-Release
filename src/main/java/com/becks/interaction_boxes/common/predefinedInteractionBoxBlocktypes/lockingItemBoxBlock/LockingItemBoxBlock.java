package com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.lockingItemBoxBlock;

import com.becks.interaction_boxes.common.exampleBlocks.lockPedestal.LockPedestalBlockEntity;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.InteractionBoxEntityBlock;
import com.becks.interaction_boxes.common.predefinedInteractionBoxes.inventoryBox.LockItemBox;
import com.becks.interaction_boxes.common.predefinedInteractionBoxes.inventoryBox.SingleItemInventoryBox;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.inventoryBoxBlock.InventoryBoxBlockEntity;
import com.becks.interaction_boxes.common.predefinedInteractionBoxes.InteractionBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * basic example implementation of interaction boxes interactiing with each other
 * This block has multiple (two) InventoryBoxes comparing their content and setting the blockstate accordingly
 * This block emits a redstone signal if the items match, a more detailed signal can be obtained by comparator emitting different signal strength depending on the state combination of both inventories
 */
public abstract class LockingItemBoxBlock extends InteractionBoxEntityBlock {
    public static final BooleanProperty LOCKED = BlockStateProperties.POWERED;
    protected LockingItemBoxBlock(Properties p_49224_) {
        super(p_49224_);
    }

    /**
     * return the inventory-box acting as the fixed key definer, this method existing is shit for any further inheritance and should be reworked if used for any more complex blocks
     * @return the inventory-box acting as the fixed key definer,
     */
    public abstract InteractionBox getLockItemBox();

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                          @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        InteractionResult superResult = super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        boolean newLock = locked(pLevel, pPos);
        if (newLock != pLevel.getBlockState(pPos).getValue(LOCKED)){
            pLevel.setBlockAndUpdate(pPos, pLevel.getBlockState(pPos).setValue(LOCKED, newLock));
        }
        return superResult;
    }
    private boolean locked(Level pLevel, BlockPos pPos){
        InventoryBoxBlockEntity bEntity = (InventoryBoxBlockEntity) pLevel.getBlockEntity(pPos);
        assert bEntity != null;
        ItemStack lockItem = bEntity.getItem(((LockItemBox)this.getLockItemBox()).getSlot());
        if (!lockItem.getItem().equals(Items.AIR)){
            ItemStack compareItem = bEntity.getItem(((LockItemBox)this.getLockItemBox()).getLockBox().getSlot());
            if (!compareItem.getItem().equals(Items.AIR)){
                ItemStack compareItemNoDamage = compareItem.copy();
                compareItemNoDamage.setDamageValue(0);
                compareItemNoDamage.setCount(1);
                ItemStack lockItemNoDamage = lockItem.copy();
                lockItemNoDamage.setDamageValue(0);
                lockItemNoDamage.setCount(1);
                return ItemStack.matches(compareItemNoDamage, lockItemNoDamage);
            }
        }
        return false;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LOCKED);
        super.createBlockStateDefinition(pBuilder);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(LOCKED, false);
    }
    @Override
    public int getSignal(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull Direction pDirection) {
        return pState.getValue(LOCKED)?15:0;
    }
    @Override
    public boolean isSignalSource(@NotNull BlockState pState) {
        return true;
    }
    @Override
    public int getAnalogOutputSignal(@NotNull BlockState pBlockState, Level pLevel, @NotNull BlockPos pPos) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof LockPedestalBlockEntity) {
            return (((InventoryBoxBlockEntity<?>)blockentity).getItemType(((SingleItemInventoryBox)this.getLockItemBox()).getSlot()).equals(Items.AIR) || ((InventoryBoxBlockEntity)blockentity).getItemType(((SingleItemInventoryBox)this.getLockItemBox()).getSlot()) == null)?0:(pBlockState.getValue(LOCKED)?7:14);
        }
        return 0;
    }
    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState pState) {
        return true;
    }
}
