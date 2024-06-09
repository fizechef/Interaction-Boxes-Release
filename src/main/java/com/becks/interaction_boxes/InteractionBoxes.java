package com.becks.interaction_boxes;

import com.becks.interaction_boxes.client.renderer.ClientRenderEventHandler;
import com.becks.interaction_boxes.common.predefinedInteractionBoxBlocktypes.InteractionBoxBlockEntityRenderer;
import com.becks.interaction_boxes.init.BlockEntityInit;
import com.becks.interaction_boxes.init.BlockInit;
import com.becks.interaction_boxes.init.ItemInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
@Mod("interaction_boxes")
public class InteractionBoxes {
    public static final String MOD_ID = "interaction_boxes";
    public InteractionBoxes() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> bus.register(InteractionBoxes.ClientModEvents.class));
    }
    @OnlyIn(Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            IEventBus forgeEventbus = MinecraftForge.EVENT_BUS;
            forgeEventbus.addListener(ClientRenderEventHandler::onHighlightBlockEvent);
        }
        @SubscribeEvent
        public static void onRenderRegistry(EntityRenderersEvent.RegisterRenderers event){
            event.registerBlockEntityRenderer(BlockEntityInit.PEDESTAL.get(), ctx -> new InteractionBoxBlockEntityRenderer<>());
            event.registerBlockEntityRenderer(BlockEntityInit.CITY_CENTER.get(), ctx -> new InteractionBoxBlockEntityRenderer<>());
            event.registerBlockEntityRenderer(BlockEntityInit.LOCK_PEDESTAL.get(), ctx -> new InteractionBoxBlockEntityRenderer<>());
        }
        @SubscribeEvent
        public static void onRegisterRenderLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        }
    }
}
