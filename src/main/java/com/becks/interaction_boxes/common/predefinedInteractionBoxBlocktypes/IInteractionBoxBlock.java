package com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes;

import com.becks.interaction_boxes.common.predefinedInteractionBoxes.InteractionBox;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

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
}
