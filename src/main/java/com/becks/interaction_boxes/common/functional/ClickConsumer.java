package com.becks.interaction_boxes.common.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Function type used by ClickableBox to execute when the box is clicked, allows for different "implementations" of ClickableBox without custom classes
 * see LeverBoxBlock as an example
 */
@FunctionalInterface
public interface ClickConsumer {
    InteractionResult run(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit);
}
