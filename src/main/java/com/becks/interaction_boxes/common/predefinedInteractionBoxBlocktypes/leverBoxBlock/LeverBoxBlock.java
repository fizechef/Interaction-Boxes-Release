package com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.leverBoxBlock;

import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.InteractionBoxBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * example implementation of a basic block using a clickable-box to imitate the functionality of a lever (no block entity)
 */
public abstract class LeverBoxBlock extends InteractionBoxBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    protected LeverBoxBlock(Properties p_49224_) {
        super(p_49224_);
        this.defaultBlockState().setValue(POWERED, Boolean.FALSE);
    }

    /**
     * Method that will be called if a clickable-box in the for now undefined set of lever-like clickable boxes is clicked, imitates a lever being clicked.
     * A reference to this method needs to be provided by inheriting classes to any clickable-box that should activate the lever functionality, see BrickLever
     * @param pState
     * @param pLevel
     * @param pPos
     * @param pPlayer
     * @param pHand
     * @param pHit
     * @return
     */
    protected InteractionResult clickLeverBoxCallBack(BlockState pState, Level pLevel, BlockPos pPos,
                                                      Player pPlayer, InteractionHand pHand, BlockHitResult pHit){
        BlockState blockstate = this.pull(pState, pLevel, pPos);
        float f = blockstate.getValue(POWERED) ? 0.6F : 0.5F;
        pLevel.playSound(null, pPos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 0.3F, f);
        pLevel.gameEvent(pPlayer, blockstate.getValue(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pPos);
        return InteractionResult.SUCCESS;
    }
    private BlockState pull(BlockState pState, Level pLevel, BlockPos pPos) {
        pState = pState.cycle(POWERED);
        pLevel.setBlock(pPos, pState, 3);
        pLevel.updateNeighborsAt(pPos, this);
        return pState;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(POWERED);
    }
    @Override
    public int getSignal(BlockState pBlockState, @NotNull BlockGetter pBlockAccess, @NotNull BlockPos pPos, @NotNull Direction pSide) {
        return pBlockState.getValue(POWERED) ? 15 : 0;
    }
    @Override
    public int getDirectSignal(BlockState pBlockState, @NotNull BlockGetter pBlockAccess, @NotNull BlockPos pPos, @NotNull Direction pSide) {
        return pBlockState.getValue(POWERED) ? 15 : 0;
    }
    @Override
    public boolean isSignalSource(@NotNull BlockState pState) {
        return true;
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(POWERED, Boolean.FALSE);
    }


}
