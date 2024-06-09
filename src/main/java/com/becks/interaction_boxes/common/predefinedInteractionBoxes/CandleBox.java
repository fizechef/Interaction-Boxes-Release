package com.becks.interaction_boxes.common.predefinedInteractionBoxes;

import com.becks.interaction_boxes.common.functional.ClickConsumer;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.candleBoxBlock.CandleBoxBlock;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.InteractionBoxEntityBlock;
import com.becks.interaction_boxes.utils.DirectionHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import java.awt.*;
import java.util.function.Function;

/**
 * Exmaple implementation to show ho an interaction box linked to a block-entity block can highjack the blockentity renderer to render any arbitrary model as its content
 */
public class CandleBox extends ClickableBox {
    private final Function<Level, Object> candleOut;
    private final Function<Level, Object> candleOn;
    private final SimpleParticleType fireParticle;
    private final SimpleParticleType smokeParticle;
    private final int candleIndex;
    private final ClickConsumer clickToLightOrExtinguishCandle = (BlockState pState, Level pLevel, BlockPos pPos,
                                                                  Player pPlayer, InteractionHand pHand, BlockHitResult pHit) -> {
        Item interactionItem = pPlayer.getMainHandItem().getItem();
        if (interactionItem.getDefaultInstance().is(Items.FLINT_AND_STEEL)){
            if (!((CandleBoxBlock)pState.getBlock()).isCandleLit(pState,this.getCandleIndex())){
                if(!pPlayer.isCreative()) {
                    pPlayer.getMainHandItem().setDamageValue(pPlayer.getItemInHand(pHand).getDamageValue() + 1);
                }
                pLevel.playSound(null, pPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.PLAYERS, 0.3F, 1);
                pLevel.setBlockAndUpdate(pPos, ((CandleBoxBlock)pState.getBlock()).lightCandle(pState,this.getCandleIndex()));
            }
            return InteractionResult.CONSUME;
        }
        else if (interactionItem.getDefaultInstance().is(Items.WATER_BUCKET)){
            if (((CandleBoxBlock)pState.getBlock()).isCandleLit(pState,this.getCandleIndex())){
                pLevel.playSound(null, pPos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.PLAYERS, 0.3F, 1);
                pLevel.setBlockAndUpdate(pPos, ((CandleBoxBlock)pState.getBlock()).extinguishCandle(pState,this.getCandleIndex()));
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
    };
    public CandleBox(VoxelShape shape, int candleIndex, Function<Level,Object> candleOn, Function<Level,Object> candleOut, SimpleParticleType fireParticle, SimpleParticleType smokeParticle) {
        super(shape);
        this.addLateClickConsumer(clickToLightOrExtinguishCandle);
        this.candleIndex = candleIndex;
        this.candleOut = candleOut;
        this.candleOn = candleOn;
        this.fireParticle = fireParticle;
        this.smokeParticle = smokeParticle;
    }

    public CandleBox(VoxelShape shape, AABB aabb, int candleIndex, Function<Level,Object> candleOn, Function<Level,Object> candleOut, SimpleParticleType fireParticle, SimpleParticleType smokeParticle) {
        super(shape, aabb);
        this.addLateClickConsumer(clickToLightOrExtinguishCandle);
        this.candleIndex = candleIndex;
        this.candleOut = candleOut;
        this.candleOn = candleOn;
        this.fireParticle = fireParticle;
        this.smokeParticle = smokeParticle;
    }
    public int getCandleIndex(){
        return candleIndex;
    }
    public InteractionResult clickedConditional(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit, Function<CandleBox, Boolean> condition) {
        if (condition.apply(this)){
            return this.clicked(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
        return InteractionResult.FAIL;
    }
    @Override
    public Color getHighlightColor(Item heldItem, BlockState state) {
        boolean lightable = heldItem.getDefaultInstance().is(Items.FLINT_AND_STEEL);
        return new Color(lightable?255:0 , lightable?125:0, 0, 255);
    }
    @Override
    public void renderContents(BlockEntity blockEntity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedOverlayIn, int lightLevel, Rotation relativeRotation) {
        BlockState state = blockEntity.getBlockState();
        this.renderCandle(matrixStackIn, bufferIn, combinedOverlayIn, lightLevel, relativeRotation, state);
    }

    /**
     * this method actually renders the model of the candle,
     * note that the itemRenderer is used, but the item model is replaced by some arbitrary predefined model
     * @param matrixStack
     * @param buffer
     * @param combinedOverlay
     * @param lightLevel
     * @param relativeRotation
     * @param state
     */
    private void renderCandle(PoseStack matrixStack, MultiBufferSource buffer, int combinedOverlay, int lightLevel, Rotation relativeRotation, BlockState state) {
        Vector3d blockMiddleOffset = new Vector3d(-8, -8, -8);
        Vector3d translation = new Vector3d(this.getMin(Direction.Axis.X) + (this.getLength(Direction.Axis.X) / 2), this.getMin(Direction.Axis.Y) - (this.getLength(Direction.Axis.Y) / 2), this.getMin(Direction.Axis.Z) + (this.getLength(Direction.Axis.Z) / 2));
        translation.mul(16);
        translation.add(blockMiddleOffset);
        translation = DirectionHelper.getRotatedVec(translation, relativeRotation);
        blockMiddleOffset.mul(-1);
        translation.add(blockMiddleOffset);
        translation.div(16.0);
        Quaternionf relativeQuat = new Quaternionf(0, 0, 0, 1);
        matrixStack.pushPose();
        matrixStack.translate(translation.x, translation.y, translation.z);
        matrixStack.mulPose(relativeQuat);
        Vector3d scale = new Vector3d(4*(this.getLength(Direction.Axis.X) * 8), 2*(this.getLength(Direction.Axis.Y) * 4), 4*(this.getLength(Direction.Axis.Z) * 8));
        scale = DirectionHelper.getRotatedVec(scale, relativeRotation);
        matrixStack.scale((float)Math.abs(scale.x), Math.abs((float)scale.y), Math.abs((float)scale.z));
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().render(Items.CANDLE.getDefaultInstance(), ItemDisplayContext.GROUND, true, matrixStack, buffer,
                lightLevel, combinedOverlay, (state.getBlock() instanceof CandleBoxBlock)?((((CandleBoxBlock)state.getBlock()).isCandleLit(state,this.getCandleIndex()))?(BakedModel)candleOn.apply(mc.level):(BakedModel)candleOut.apply(mc.level)):(BakedModel)candleOut.apply(mc.level));
        matrixStack.popPose();
    }

    @Override
    public void addParticlesAndSound(Level pLevel, BlockState state, BlockPos pos, RandomSource pRandom) {
        if (!((CandleBoxBlock)state.getBlock()).isCandleLit(state, this.getCandleIndex())){
            return;
        }
        float f = pRandom.nextFloat();
        Vector3d translation = new Vector3d(this.getMin(Direction.Axis.X)+this.getLength(Direction.Axis.X)/2, this.getMin(Direction.Axis.Y)+this.getLength(Direction.Axis.Y), this.getMin(Direction.Axis.Z)+this.getLength(Direction.Axis.Z)/2);
        Rotation relativeRotation = DirectionHelper.getRotation(state.getValue(InteractionBoxEntityBlock.FACING), Direction.SOUTH);
        Vector3d blockMiddleOffset = new Vector3d(-8, -8, -8);
        translation.mul(16);
        translation.add(blockMiddleOffset);
        translation = DirectionHelper.getRotatedVec(translation, relativeRotation);
        blockMiddleOffset.mul(-1);
        translation.add(blockMiddleOffset);
        translation.mul(1.0 / 16.0);
        //System.out.println(translation);
        if (f < 0.3F) {
            pLevel.addParticle(smokeParticle, pos.getX() + translation.x, pos.getY() + translation.y, pos.getZ() + translation.z, 0.0D, 0.0D, 0.0D);
            if (f < 0.17F) {
                pLevel.playLocalSound(pos.getX() + translation.x, pos.getY() + translation.y, pos.getZ() + translation.z, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.3F, false);
            }
        }
        pLevel.addParticle(fireParticle, pos.getX() + translation.x, pos.getY() + translation.y, pos.getZ() + translation.z, 0.0D, 0.0D, 0.0D);
    }
}
