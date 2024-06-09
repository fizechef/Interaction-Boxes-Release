package com.becks.interaction_boxes.common.exampleBlocks.brickLever;

import com.becks.interaction_boxes.common.predefinedInteractionBoxes.ClickableBox;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.leverBoxBlock.LeverBoxBlock;
import com.becks.interaction_boxes.common.predefinedInteractionBoxes.InteractionBox;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple example block showing how a basic LeverBoxBlock can be implemented,
 * note that all functionality is in the LeverBoxBlock class and only the specific box parameters need to be defined here
 */
public class BrickLever extends LeverBoxBlock {
    private final ClickableBox leverBox = new ClickableBox(Shapes.or(Block.box(7, 8D, -0.1, 14, 11, 0.1)), this::clickLeverBoxCallBack);
    public BrickLever(Properties p_49224_) {
        super(p_49224_);
    }
    @Override
    public Collection<InteractionBox> getBoxes() {
        List<InteractionBox> boxes = new ArrayList<>();
        boxes.add(leverBox);
        return boxes;
    }

    @Override
    public boolean renderBaseHighlight() {
        return true;
    }
}
