package com.buuz135.hotornot.config;

import com.buuz135.hotornot.HotOrNot;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = HotOrNot.MOD_ID)
public class HotConfig {

	@Comment("If true, hot effects for items will be enabled")
	public static boolean HOT_ITEMS = true;

	@Comment("If true, hot effects for fluids will be enabled")
	public static boolean handleHotFluids = true;

	@Comment("If true, cold effects for fluids will be enabled")
	public static boolean handleColdFluids = true;

	@Comment("If true, gaseous effects for fluids will be enabled")
	public static boolean handleGaseousFluids = true;

	@Comment("If true, items causing effects will get a tooltip")
	public static boolean renderEffectTooltip = true;

	@Comment("If true, hot items make the player yeet them")
	public static boolean YEET = true;

	@Comment("How hot a fluid should be to start burning the player (in Celsius)")
	public static int hotFluidTemp = 480;

	@Comment("How cold a fluid should be to start adding effects the player (in Celsius)")
	public static int coldFluidTemp = 0;

	@Comment("How hot an item should be to start burning the player (in Celsius)")
	public static int hotItemTemp = 480;

	@Comment("Max durability of the wooden tongs, 0 for infinite durability")
	public static int WOODEN_TONGS_DURABILITY = 120;

	@Comment("Max durability of the mitts, 0 for infinite durability")
	public static int MITTS_DURABILITY = 12000;

	@Comment("Max durability of the tongs, 0 for infinite durability")
	public static int COPPER_TONGS_DURABILITY = 1000;

	@Comment("Max durability of the tongs, 0 for infinite durability")
	public static int BRONZE_TONGS_DURABILITY = 2000;

	@Comment("Max durability of the tongs, 0 for infinite durability")
	public static int BISMUTH_BRONZE_TONGS_DURABILITY = 2200;

	@Comment("Max durability of the tongs, 0 for infinite durability")
	public static int BLACK_BRONZE_TONGS_DURABILITY = 1800;

	@Comment("Max durability of the tongs, 0 for infinite durability")
	public static int WROUGHT_IRON_TONGS_DURABILITY = 3000;

	@Comment("Max durability of the tongs, 0 for infinite durability")
	public static int STEEL_TONGS_DURABILITY = 4000;

	@Comment("Max durability of the tongs, 0 for infinite durability")
	public static int BLACK_STEEL_TONGS_DURABILITY = 6000;

	@Comment("Max durability of the tongs, 0 for infinite durability")
	public static int RED_STEEL_TONGS_DURABILITY = 12000;

	@Comment("Max durability of the tongs, 0 for infinite durability")
	public static int BLUE_STEEL_TONGS_DURABILITY = 12000;

	@Comment("Hot items that are included manually")
	public static String[] HOT_ITEM_ADDITIONS = new String[] {"minecraft:blaze_rod"};

	@Comment("Cold items that are included manually")
	public static String[] COLD_ITEM_ADDITIONS = new String[] {"minecraft:ice", "minecraft:packed_ice"};

	@Comment("Gaseous items that are included manually")
	public static String[] GASEOUS_ITEM_ADDITIONS = new String[] {"mod_id:item"};

	@Comment("Items that are excluded")
	public static String[] ITEM_REMOVALS = new String[] {"immersiveengineering:drill", "immersiveengineering:chemthrower", "immersivepetroleum:fluid_diesel", "immersivepetroleum:fluid_gasoline"};

	@Mod.EventBusSubscriber(modid = HotOrNot.MOD_ID)
	private static class EventHandler {

		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(HotOrNot.MOD_ID)) {
				ConfigManager.sync(HotOrNot.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}