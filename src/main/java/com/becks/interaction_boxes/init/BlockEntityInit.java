package com.becks.interaction_boxes.init;

import com.becks.interaction_boxes.InteractionBoxes;
import com.becks.interaction_boxes.common.exampleBlocks.candleAltar.CandleAltarBlockEntity;
import com.becks.interaction_boxes.common.exampleBlocks.lockPedestal.LockPedestalBlockEntity;
import com.becks.interaction_boxes.common.exampleBlocks.pedestal.PedestalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, InteractionBoxes.MOD_ID);

    public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL =
            BLOCK_ENTITIES.register("pedestal", () ->
                    BlockEntityType.Builder.of(PedestalBlockEntity::new,
                            BlockInit.PEDESTAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<LockPedestalBlockEntity>> LOCK_PEDESTAL =
            BLOCK_ENTITIES.register("lock_pedestal", () ->
                    BlockEntityType.Builder.of(LockPedestalBlockEntity::new,
                            BlockInit.LOCK_PEDESTAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<CandleAltarBlockEntity>> CITY_CENTER =
            BLOCK_ENTITIES.register("candle_altar", () ->
                    BlockEntityType.Builder.of(CandleAltarBlockEntity::new,
                            BlockInit.CITY_CENTER.get()).build(null));
}
