package com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes;

import com.becks.interaction_boxes.common.predefinedInteractionBoxes.InteractionBox;
import com.becks.interaction_boxes.utils.DirectionHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class InteractionBoxBlockEntityRenderer<T extends net.minecraft.world.level.block.entity.BlockEntity> implements BlockEntityRenderer<T> {
    @Override
    public void render(BlockEntity blockEntity, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn){
        InteractionBoxEntityBlock block = (InteractionBoxEntityBlock)(blockEntity.getBlockState().getBlock());
        BlockState state = blockEntity.getBlockState();
        Rotation relativeRotation = DirectionHelper.getRotation(state.getValue(InteractionBoxEntityBlock.FACING), Direction.SOUTH);
        for (InteractionBox b : block.getBoxes()){
            b.renderContents(blockEntity, matrixStackIn, bufferIn, combinedOverlayIn, getLightLevel(Objects.requireNonNull(blockEntity.getLevel()), blockEntity.getBlockPos().above()), relativeRotation);
        }
    }
    protected int getLightLevel(Level world, BlockPos pos) {
        int bLight = world.getBrightness(LightLayer.BLOCK, pos);
        int sLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
