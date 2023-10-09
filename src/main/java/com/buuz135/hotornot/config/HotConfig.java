package com.buuz135.hotornot.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeInt;
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

	@LangKey("config." + MOD_ID + ".manual_entries")
	@Comment("Configuration for manually added items." +
			"Items are in the format <mod_id>:<registry_name> same as what you see in F3 + H")
	public static final ManualEntries MANUAL_ENTRIES = new ManualEntries();

	@LangKey("config." + MOD_ID + ".temperature_values")
	@Comment("Configuration for the temperature thresholds the effects happen at. Values are in Celsius")
	public static final TemperatureValues TEMPERATURE_VALUES = new TemperatureValues();

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
	public static boolean tossItems = false;

	@RangeInt(min = 0)
	@Comment("How often the tools should be damaged in ticks (there are 20 ticks per second). 0 will disable the damaging functionality")
	public static int damageRate = 1;

	@Comment("If broken Tongs or Mitts should be automatically replaced for you")
	@LangKey("config." + MOD_ID + ".replace_broken_hot_holder")
	public static boolean replaceBrokenHotHolder = false;

	@SubscribeEvent
	public static void onConfigChanged(final OnConfigChangedEvent event) {
		if (event.getModID().equals(MOD_ID)) {
			ConfigManager.sync(MOD_ID, Type.INSTANCE);
		}
	}
}