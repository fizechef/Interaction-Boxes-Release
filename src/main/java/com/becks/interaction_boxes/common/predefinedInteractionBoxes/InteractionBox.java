package com.becks.interaction_boxes.common.predefinedInteractionBoxes;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.awt.*;

/**
 * basic selection box interface
 */
public interface InteractionBox {

    /**
     * get the shape of the selection box, this shape is drawn as highlighted when selecting the box
     * @return a voxel shape
     */
    VoxelShape getShape();
    AABB getAabb();
    double getLength(Direction.Axis a);
    double getMin(Direction.Axis a);
    double getMax(Direction.Axis a);
    void selected();

    /**
     * method called when this selection box is clicked
     * @param pState
     * @param pLevel
     * @param pPos
     * @param pPlayer
     * @param pHand
     * @param pHit
     * @return
     */
    InteractionResult clicked(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit);

    /**
     * Determines the color of the outline drawn when hovering over this box
     * @param heldItem item held whlie hovering
     * @param state blockstate of the block being hovered
     * @return a color
     */
    Color getHighlightColor(Item heldItem, BlockState state);

    /**
     * renders the "content" of this interaction box, this can render anything, see implementations for redering arbitrary models in CandleBox and InventoryBox
     * @param blockEntity
     * @param matrixStackIn
     * @param bufferIn
     * @param combinedOverlayIn
     * @param lightLevel
     * @param relativeRotation
     */
    @OnlyIn(Dist.CLIENT)
    void renderContents(BlockEntity blockEntity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedOverlayIn, int lightLevel, Rotation relativeRotation);

    /**
     * adds particles and sounds to an interaction box, note that this is called every tick
     * @param pLevel
     * @param state
     * @param pos
     * @param pRandom
     */
    void addParticlesAndSound(Level pLevel, BlockState state, BlockPos pos, RandomSource pRandom);
}
