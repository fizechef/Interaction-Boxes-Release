package com.becks.interaction_boxes.client.renderer;

import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.IInteractionBoxBlock;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.InteractionBoxBlock;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.InteractionBoxEntityBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.RenderHighlightEvent;
public class ClientRenderEventHandler {
    public static void onHighlightBlockEvent(RenderHighlightEvent event)
    {
        final Camera camera = event.getCamera();
        final PoseStack poseStack = event.getPoseStack();
        final Entity entity = camera.getEntity();
        final Level level = entity.level();
        final BlockHitResult hit =  (event.getTarget() instanceof BlockHitResult) ? (BlockHitResult)(event.getTarget()) : null;
        final BlockPos pos = (hit != null) ? hit.getBlockPos() : null;
        final BlockPos lookingAt = (pos != null) ? new BlockPos(pos) : null;

        if (lookingAt != null && entity instanceof Player player)
        {
            BlockState stateAt = level.getBlockState(lookingAt);
            Block blockAt = stateAt.getBlock();

            /*if (blockAt instanceof InteractionBoxEntityBlock multiHiBlockAt)
            {
                if (multiHiBlockAt.drawHighlight(level, lookingAt, player, hit, poseStack, event.getMultiBufferSource(), camera.getPosition()))
                {
                    if (!multiHiBlockAt.renderBaseHighlight()){
                        event.setCanceled(true);
                    }
                }
            }*/
            if (blockAt instanceof IInteractionBoxBlock multiHiBlockAt)
            {
                if (multiHiBlockAt.drawHighlight(level, lookingAt, player, hit, poseStack, event.getMultiBufferSource(), camera.getPosition()))
                {
                    if (!multiHiBlockAt.renderBaseHighlight()){
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
