package com.becks.interaction_boxes.init;

import com.becks.interaction_boxes.InteractionBoxes;
import com.becks.interaction_boxes.common.exampleBlocks.brickLever.BrickLever;
import com.becks.interaction_boxes.common.exampleBlocks.candleAltar.CandleAltar;
import com.becks.interaction_boxes.common.exampleBlocks.lockPedestal.LockPedestal;
import com.becks.interaction_boxes.common.exampleBlocks.pedestal.Pedestal;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Klasse zum Registrieren aller Blöcke
 * @author erike
 *
 */
public class BlockInit {
	//"Liste" aller Bl�cke
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, InteractionBoxes.MOD_ID);
	
	//Hinzufügen eines Blocks
	public static final RegistryObject<Block> PEDESTAL = 
			BLOCKS.register("pedestal", 
					() -> new Pedestal(
							Block.Properties.of().mapColor(MapColor.COLOR_RED)
									.requiresCorrectToolForDrops().noOcclusion()
									.strength(3f,  5f)
									.sound(SoundType.BASALT)));

	public static final RegistryObject<Block> LOCK_PEDESTAL =
			BLOCKS.register("lock_pedestal",
					() -> new LockPedestal(
							Block.Properties.of().mapColor(MapColor.COLOR_YELLOW)
									.requiresCorrectToolForDrops().noOcclusion()
									.strength(3f,  5f)
									.sound(SoundType.BASALT)));
	public static final RegistryObject<Block> CITY_CENTER =
			BLOCKS.register("candle_altar",
					() -> new CandleAltar(
							Block.Properties.of().mapColor(MapColor.DEEPSLATE)
									.requiresCorrectToolForDrops().noOcclusion()
									.strength(50f,  50f)
									.sound(SoundType.DEEPSLATE_BRICKS)));

	public static final RegistryObject<Block> DEEPSLATE_BRICKS_LEVER =
			BLOCKS.register("bricks_lever",
					() -> new BrickLever(
							Block.Properties.of().mapColor(MapColor.DEEPSLATE)
									.requiresCorrectToolForDrops()
									.noOcclusion()
									.strength(2f,  2f)
									.sound(SoundType.DEEPSLATE_BRICKS)));



}
