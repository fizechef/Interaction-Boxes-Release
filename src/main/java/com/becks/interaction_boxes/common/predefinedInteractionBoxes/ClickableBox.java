package com.becks.interaction_boxes.common.predefinedInteractionBoxes;

import com.becks.interaction_boxes.common.functional.ClickConsumer;
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

import javax.annotation.Nullable;
import java.awt.*;

/**
 * implementation of selection box with configurable functionality when clicked
 */
public class ClickableBox implements InteractionBox {
    private final VoxelShape shape;
    private final AABB aabb;
    private ClickConsumer clickCallback;

    /**
     *
     * @param shape VoxelShape of this selection box, bounding box will be set to the outline of this shape
     * @param clickCallback method reference to call when clicked
     */
    public ClickableBox(VoxelShape shape, @Nullable ClickConsumer clickCallback) {
        this.shape = shape;
        this.aabb = shape.bounds();
        this.clickCallback = clickCallback;
    }

    /**
     *
     * @param shape VoxelShape of this selection box
     * @param aabb bounding box of this selection box
     * @param clickCallback method reference to call when clicked
     */
    public ClickableBox(VoxelShape shape, AABB aabb, @Nullable ClickConsumer clickCallback) {
        this.shape = shape;
        this.aabb = aabb;
        this.clickCallback = clickCallback;
    }

    /**
    *
    * @param shape VoxelShape of this selection box, bounding box will be set to the outline of this shape, clickCallback needs to be specified using "addLateClickConsumer" or no method will be called when clicked
    *
    **/
    public ClickableBox(VoxelShape shape) {
        this.shape = shape;
        this.aabb = shape.bounds();
        this.clickCallback = null;
    }
    public ClickableBox(VoxelShape shape, AABB aabb) {
        this.shape = shape;
        this.aabb = aabb;
        this.clickCallback = null;
    }
    @Override
    public double getLength(Direction.Axis a){
        return shape.max(a) - shape.min(a);
    }
    @Override
    public double getMin(Direction.Axis a){
        return shape.min(a);
    }
    @Override
    public double getMax(Direction.Axis a){
        return shape.max(a);
    }
    @Override
    public void selected() {}
    @Override
    public InteractionResult clicked(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (clickCallback != null){
            return clickCallback.run(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
        return InteractionResult.FAIL;
    }
    @Override
    public Color getHighlightColor(Item heldItem, BlockState state) {
        return new Color(0 , 0, 0, 255);
    }
    @Override
    public void renderContents(BlockEntity blockEntity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedOverlayIn, int lightLevel, Rotation relativeRotation) {}
    @Override
    public void addParticlesAndSound(Level pLevel, BlockState state, BlockPos pos, RandomSource pRandom) {}
    @Override
    public VoxelShape getShape(){
        return shape;
    }
    @Override
    public AABB getAabb(){
        return aabb;
    }

    /**
     * method for adding a clickCallback after initilization of a clickable box object, can not override clickCallbacks specified in constructor
     * @param clickCallback method reference to be called when box is clicked
     */
    protected void addLateClickConsumer(ClickConsumer clickCallback){
        this.clickCallback = clickCallback;
    }
}
