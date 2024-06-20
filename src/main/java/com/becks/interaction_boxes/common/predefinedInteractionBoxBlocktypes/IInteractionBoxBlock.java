package com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes;

import com.becks.interaction_boxes.common.predefinedInteractionBoxes.InteractionBox;
import com.becks.interaction_boxes.utils.DirectionHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.Collection;

import static com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.InteractionBoxBlock.FACING;

public interface IInteractionBoxBlock {
    /**
     * returns all InteractionBoxes a inheriting block has
     * @return
     */
    Collection<InteractionBox> getBoxes();

    /**
     * Wether the base shape highlight should be rendered even if an interaction box is selected
     * @return
     */
    boolean renderBaseHighlight();

    boolean drawHighlight(Level level, BlockPos pos, Player player, BlockHitResult rayTrace, PoseStack matrixStack, MultiBufferSource buffers, Vec3 renderPos);

    Color getHighlightColor(InteractionBox selection, Item heldItem, BlockState state);

    static InteractionBox getPlayerSelection(Collection<InteractionBox> boxes, BlockEntityType<? extends BlockEntity> blockEntityType, BlockGetter level, BlockPos pos, Player player, BlockHitResult result)
    {
        return level.getBlockEntity(pos, blockEntityType)
                .map(block -> {
                    final Vec3 hit = result.getLocation();
                    for (InteractionBox b : boxes){
                        AABB selectionAABB = (DirectionHelper.rotateAABBblockCenterRelated(b.getAabb(), DirectionHelper.getRotation(level.getBlockState(pos).getValue(FACING), Direction.SOUTH))).move(pos);
                        //System.out.println(selectionAABB + "  " + hit);
                        if (selectionAABB.contains(hit))
                        {
                            return b;
                        }
                    }

                    return null;
                })
                .orElse(null);
    }

    static InteractionBox getPlayerSelection(Collection<InteractionBox> boxes, BlockGetter level, BlockPos pos, Player player, BlockHitResult result)
    {
        final Vec3 hit = result.getLocation();
        for (InteractionBox b : boxes){
            AABB selectionAABB = (DirectionHelper.rotateAABBblockCenterRelated(b.getAabb(), DirectionHelper.getRotation(level.getBlockState(pos).getValue(FACING), Direction.SOUTH))).move(pos);
            if (selectionAABB.contains(hit))
            {
                return b;
            }
        }

        return null;
    }
}
