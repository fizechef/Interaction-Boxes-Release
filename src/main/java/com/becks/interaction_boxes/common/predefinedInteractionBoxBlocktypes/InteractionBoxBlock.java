package com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes;

import com.becks.interaction_boxes.client.renderer.BoxHighlightRender;
import com.becks.interaction_boxes.common.predefinedInteractionBoxes.InteractionBox;
import com.becks.interaction_boxes.utils.DirectionHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collection;

/**
 * abstract class extending block and implementing functionality used to detect interaction with interaction boxes and call their abstract methods "clicked" and "addParticlesAndSound"
 * extend this class to create a block using interaction boxes
 */
public abstract class InteractionBoxBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected InteractionBoxBlock(Properties p_49224_) {
        super(p_49224_);
        this.defaultBlockState().setValue(FACING, Direction.NORTH);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    /**
     * returns all InteractionBoxes a inheriting block has
     * @return
     */
    public abstract Collection<InteractionBox> getBoxes();

    /**
     * Wether the base shape highlight should be rendered even if an interaction box is selected
     * @return
     */
    public abstract boolean renderBaseHighlight();

    /**
     * renders the selection outline of a InteractionBox
     * @param level
     * @param pos
     * @param player
     * @param rayTrace
     * @param matrixStack
     * @param buffers
     * @param renderPos
     * @return
     */
    public boolean drawHighlight(Level level, BlockPos pos, Player player, BlockHitResult rayTrace, PoseStack matrixStack, MultiBufferSource buffers, Vec3 renderPos)
    {
        InteractionBox selection = getPlayerSelection(getBoxes(), level, pos, player, rayTrace);
        if (selection != null)
        {
            Color highlightColor = this.getHighlightColor(selection, player.getItemInHand(InteractionHand.MAIN_HAND).getItem(), level.getBlockState(pos));
            BoxHighlightRender.drawBox(matrixStack, Shapes.create(DirectionHelper.rotateAABBblockCenterRelated(selection.getShape().bounds(), DirectionHelper.getRotation(level.getBlockState(pos).getValue(FACING), Direction.SOUTH))), buffers, pos, renderPos, highlightColor.getRed()/255f, highlightColor.getGreen()/255f, highlightColor.getBlue()/255f, highlightColor.getAlpha()/255f);
            return true;
        }
        return false;
    }

    /**
     * Override the use method of basic blocks to inject the specific functionality of the interaction boxes
     * @param pState
     * @param pLevel
     * @param pPos
     * @param pPlayer
     * @param pHand
     * @param pHit
     * @return
     */
    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                          Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        InteractionBox selection = (InteractionBox) getPlayerSelection(this.getBoxes(), pLevel, pPos, pPlayer, pHit);
        if (selection != null){
            return selection.clicked(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    protected Color getHighlightColor(InteractionBox selection, Item heldItem, BlockState state){
        return selection.getHighlightColor(heldItem, state);
    }
    protected static InteractionBox getPlayerSelection(Collection<InteractionBox> boxes, BlockGetter level, BlockPos pos, Player player, BlockHitResult result)
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
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING,
                (context.getNearestLookingDirection().equals(Direction.UP) || context.getNearestLookingDirection().equals(Direction.DOWN))
                ?
                Direction.NORTH
                :
                context.getNearestLookingDirection().getOpposite());
    }
    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
    @Override
    public void animateTick(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        for (InteractionBox b : getBoxes()){
            b.addParticlesAndSound(pLevel, pState, pPos, pRandom);
        }
        super.animateTick(pState, pLevel, pPos, pRandom);
    }
}
