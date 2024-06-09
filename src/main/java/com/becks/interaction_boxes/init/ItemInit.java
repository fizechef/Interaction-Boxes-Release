package com.becks.interaction_boxes.init;

import com.becks.interaction_boxes.InteractionBoxes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Initialiesierungsklasse f�r Items und BlockItems
 * @author erike
 *
 */
public class ItemInit {
	//"Liste" aller registrierten Items
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, InteractionBoxes.MOD_ID);

	public static final RegistryObject<BlockItem> PEDESTAL = ITEMS.register("pedestal", () -> new BlockItem(BlockInit.PEDESTAL.get(), new Item.Properties()));
	public static final RegistryObject<BlockItem> LOCK_PEDESTAL = ITEMS.register("lock_pedestal", () -> new BlockItem(BlockInit.LOCK_PEDESTAL.get(), new Item.Properties()));
	public static final RegistryObject<BlockItem> CITY_CENTER = ITEMS.register("candle_altar", () -> new BlockItem(BlockInit.CITY_CENTER.get(), new Item.Properties()));
	public static final RegistryObject<BlockItem> DEEPSLATE_BRICKS_LEVER = ITEMS.register("bricks_lever", () -> new BlockItem(BlockInit.DEEPSLATE_BRICKS_LEVER.get(), new Item.Properties()));
}
