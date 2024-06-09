package com.becks.interaction_boxes.common.exampleBlocks.candleAltar;

import com.becks.interaction_boxes.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * simple block entity class without functionality
 */
public class CandleAltarBlockEntity extends BlockEntity {
    public CandleAltarBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityInit.CITY_CENTER.get(), p_155229_, p_155230_);
    }
}
