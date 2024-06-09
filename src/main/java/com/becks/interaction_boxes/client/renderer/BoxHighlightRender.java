package com.becks.interaction_boxes.client.renderer;

import com.becks.interaction_boxes.mixin.LevelRendererMixin;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
public class BoxHighlightRender {
    public static void drawBox(PoseStack stack, VoxelShape shape, MultiBufferSource buffers, BlockPos pos, Vec3 renderPos, float red, float green, float blue, float alpha)
    {
        LevelRendererMixin.invoke$renderShape(stack, buffers.getBuffer(RenderType.lines()), shape, pos.getX() - renderPos.x, pos.getY() - renderPos.y, pos.getZ() - renderPos.z, red, green, blue, alpha);
    }
}
