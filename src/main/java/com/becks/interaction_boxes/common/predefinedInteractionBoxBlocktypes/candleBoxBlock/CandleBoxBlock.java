package com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.candleBoxBlock;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Interface of an example for a more complex intractable block, for implementation see CandleAltar
 */
public interface CandleBoxBlock {
    BlockState lightCandle(BlockState state, int candleIndex);

    BlockState extinguishCandle(BlockState state, int candleIndex);

    boolean isCandleLit(BlockState state, int candleIndex);
}
