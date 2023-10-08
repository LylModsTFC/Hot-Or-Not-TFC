package com.buuz135.hotornot.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.buuz135.hotornot.HotOrNot.MOD_ID;
import static com.buuz135.hotornot.HotOrNot.MOD_NAME;

@EventBusSubscriber(modid = MOD_ID)
@Config(modid = MOD_ID, type = Type.INSTANCE, name = MOD_NAME)
public class HotConfig {


	@Comment("Configuration for manually added items." +
			"Items are in the format <mod_id>:<registry_name> same as what you see in F3 + H")
	@LangKey("config." + MOD_ID + ".manual_entries")
	public static final ManualEntries MANUAL_ENTRIES = new ManualEntries();

	@Comment("If true, hot effects for items will be enabled")
	public static boolean handleHotItems = true;

	@Comment("If true, hot effects for fluids will be enabled")
	public static boolean handleHotFluids = true;

	@Comment("If true, cold effects for fluids will be enabled")
	public static boolean handleColdFluids = true;

	@Comment("If true, gaseous effects for fluids will be enabled")
	public static boolean handleGaseousFluids = true;

	@Comment("If true, items causing effects will get a tooltip")
	public static boolean renderEffectTooltip = true;

	@Comment("If true, hot items make the player yeet them")
	public static boolean tossItems = true;

	@Comment("How hot a fluid should be to start burning the player (in Celsius)")
	public static int hotFluidTemp = 480;

	@Comment("How cold a fluid should be to start adding effects the player (in Celsius)")
	public static int coldFluidTemp = 0;

	@Comment("How hot an item should be to start burning the player (in Celsius)")
	public static int hotItemTemp = 480;

	@Comment("Max durability of the wooden tongs, 0 for infinite durability")
	public static int WOODEN_TONGS_DURABILITY = 120;

	@Comment("Max durability of the mitts, 0 for infinite durability")
	public static int MITTS_DURABILITY = 12_000;

	@SubscribeEvent
	public static void onConfigChanged(final OnConfigChangedEvent event) {
		if (event.getModID().equals(MOD_ID)) {
			ConfigManager.sync(MOD_ID, Type.INSTANCE);
		}
	}
}