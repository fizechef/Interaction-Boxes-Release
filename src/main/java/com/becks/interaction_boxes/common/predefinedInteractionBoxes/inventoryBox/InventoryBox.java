package com.becks.interaction_boxes.common.predefinedInteractionBoxes.inventoryBox;

import com.becks.interaction_boxes.common.functional.ClickConsumer;
import com.becks.interaction_boxes.common.functional.itemBoxItemRendererTypes.ItemBoxItemRendererType;
import com.becks.interaction_boxes.common.functional.itemBoxPuttableItems.ItemBoxPuttable;
import com.becks.interaction_boxes.common.predefinedInteractionBoxes.ClickableBox;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.inventoryBoxBlock.InventoryBoxBlockEntity;
import com.becks.interaction_boxes.utils.DirectionHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
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

/**
 * implementation of clickable box that interacts with a specified slot of a block entity inventory
 */
public abstract class InventoryBox extends ClickableBox {
    private final int itemSlotNumber;
    private final ItemBoxItemRendererType render;
    private final ItemBoxPuttable puttable;
    private final ClickConsumer clickToPutItem = (BlockState pState, Level pLevel, BlockPos pPos,
                                                  Player pPlayer, InteractionHand pHand, BlockHitResult pHit) -> {
        BlockEntity bEntityTemp = pLevel.getBlockEntity(pPos);
        if (!(bEntityTemp instanceof InventoryBoxBlockEntity<?> bEntity)){
            return InteractionResult.FAIL;
        }
        Item interactionItem = pPlayer.getMainHandItem().getItem();
        if (interactionItem.equals(Items.AIR)){
            if (bEntity.getItemType(this.getSlot()) != null && !(bEntity.getItemType(this.getSlot()).equals(Items.AIR))){
                this.onRemoveItem(pLevel, pPos, pState, bEntity, pPlayer);
                return InteractionResult.CONSUME;
            }
        }
        else if (this.puttable(interactionItem)){
            if (bEntity.getItemType(this.getSlot()) == null || bEntity.getItemType(this.getSlot()).equals(Items.AIR)){
                this.onAddItem(pLevel, pPos, pState, bEntity, pPlayer);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.FAIL;
    };
    public InventoryBox(VoxelShape shape, AABB aabb, int itemSlotNumber, ItemBoxItemRendererType render, ItemBoxPuttable puttable) {
        super(shape, aabb);
        this.addLateClickConsumer(clickToPutItem);
        this.itemSlotNumber = itemSlotNumber;
        this.render = render;
        this.puttable = puttable;
    }
    public InventoryBox(VoxelShape shape, int itemSlotNumber, ItemBoxItemRendererType render, ItemBoxPuttable puttable) {
        super(shape);
        this.addLateClickConsumer(clickToPutItem);
        this.itemSlotNumber = itemSlotNumber;
        this.render = render;
        this.puttable = puttable;
    }
    public int getSlot(){
        return itemSlotNumber;
    }
    public double[] getRenderTranslation(ItemStack stack){
        return this.render.getRenderTranslation(this, stack);
    }
    public float getRenderScale(){
        return this.render.getRenderScale(this);
    }
    public Quaternionf getRenderRotation(){
        return render.getRenderRotation(this);
    };
    public boolean puttable(Item item){
        return puttable.puttable(item);
    }
    public void renderItem(ItemStack stack, PoseStack matrixStack,
                           MultiBufferSource buffer, int combinedOverlay, int lightLevel, Rotation relativeRotation) {
        Minecraft mc = Minecraft.getInstance();
        Vector3d blockMiddleOffset = new Vector3d(-8,-8,-8);
        Vector3d translation = new Vector3d(this.getRenderTranslation(stack)[0], this.getRenderTranslation(stack)[1], this.getRenderTranslation(stack)[2]);
        translation.mul(16);
        translation.add(blockMiddleOffset);
        translation = DirectionHelper.getRotatedVec(translation, relativeRotation);
        blockMiddleOffset.mul(-1);
        translation.add(blockMiddleOffset);
        translation.div(16.0);
        Quaternionf relativeQuat = this.getRenderRotation();
        float radianRot = -DirectionHelper.getRotationRadians(relativeRotation);
        Quaternionf blockRotationQuat = new Quaternionf(0,1 * Math.sin(radianRot/2.0),0, Math.cos(radianRot/2.0));
        matrixStack.pushPose();
        matrixStack.translate(translation.x, translation.y, translation.z);
        matrixStack.mulPose(blockRotationQuat);
        matrixStack.mulPose(relativeQuat);
        float scale = this.getRenderScale();
        matrixStack.scale(scale, scale, scale);
        BakedModel model = mc.getItemRenderer().getModel(stack, null, null, 1);
        mc.getItemRenderer().render(stack, ItemDisplayContext.GROUND, true, matrixStack, buffer,
                lightLevel, combinedOverlay, model);
        matrixStack.popPose();
    }
    @Override
    public Color getHighlightColor(Item heldItem, BlockState state) {
        boolean puttable = this.puttable(heldItem);
        return new Color(puttable?0:255 , puttable?255:0, 0, 255);
    }
    @Override
    public void renderContents(BlockEntity blockEntity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedOverlayIn, int lightLevel, Rotation relativeRotation) {
        if (!(blockEntity instanceof InventoryBoxBlockEntity<?>)) {
            super.renderContents(blockEntity, matrixStackIn, bufferIn, combinedOverlayIn, lightLevel, relativeRotation);
            return;
        }
        if (!(((InventoryBoxBlockEntity<?>)blockEntity).getItemType(this.getSlot()).equals(Items.AIR))){
            ItemStack stack = ((InventoryBoxBlockEntity<?>)blockEntity).getItem(this.getSlot());
            this.renderItem(stack, matrixStackIn, bufferIn, combinedOverlayIn, lightLevel, relativeRotation);
        }
    }
    public abstract void onRemoveItem(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity bEntity, Player pPlayer);
    public abstract void onAddItem(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity bEntity, Player pPlayer);

}
