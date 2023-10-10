package com.buuz135.hotornot.util;

import com.buuz135.hotornot.config.HotConfig;
import com.buuz135.hotornot.config.HotLists;
import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.function.Consumer;
import java.util.function.Predicate;

public enum ItemEffect {
	FLUID_HOT(itemStack -> {
		if (!HotConfig.EFFECT_HANDLING.handleHotFluids) return false;

		// Item has been added to the hot list
		if (HotLists.isHot(itemStack)) return true;

		final IFluidHandlerItem itemFluidHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		// Make sure we have a fluid handler
		if (itemFluidHandler == null) return false;

		final FluidStack fluidStack = itemFluidHandler.drain(1, false);
		// Null fluid stack means it's empty
		if (fluidStack == null) return false;

		return fluidStack.getFluid().getTemperature(fluidStack) >= HotConfig.TEMPERATURE_VALUES.hotFluidTemp + 273;
	},
			player -> player.setFire(1),
			TextFormatting.RED,
			"tooltip.hotornot.toohot",
			true),
	FLUID_COLD(itemStack -> {
		if (!HotConfig.EFFECT_HANDLING.handleColdFluids) return false;

		// Item has been added to the cold list
		if (HotLists.isCold(itemStack)) return true;

		final IFluidHandlerItem itemFluidHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		// Make sure we have a fluid handler
		if (itemFluidHandler == null) return false;

		final FluidStack fluidStack = itemFluidHandler.drain(1, false);
		// Null fluid stack means it's empty
		if (fluidStack == null) return false;

		return fluidStack.getFluid().getTemperature(fluidStack) <= HotConfig.TEMPERATURE_VALUES.coldFluidTemp + 273;
	},
			player -> {
				player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 21, 1));
				player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 21, 1));
			},
			TextFormatting.AQUA,
			"tooltip.hotornot.toocold",
			false),
	FLUID_GAS(itemStack -> {
		if (!HotConfig.EFFECT_HANDLING.handleGaseousFluids) return false;

		// Item has been added to the gas list
		if (HotLists.isGaseous(itemStack)) return true;

		final IFluidHandlerItem itemFluidHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		// Make sure we have a fluid handler
		if (itemFluidHandler == null) return false;

		final FluidStack fluidStack = itemFluidHandler.drain(1, false);
		// Null fluid stack means it's empty
		if (fluidStack == null) return false;

		return fluidStack.getFluid().isGaseous(fluidStack);
	},
			player -> player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 21, 1)),
			TextFormatting.YELLOW,
			"tooltip.hotornot.toolight",
			false),
	HOT_ITEM(itemStack -> {
		if (!HotConfig.EFFECT_HANDLING.handleHotItems) return false;

		final IItemHeat itemHeat = itemStack.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
		// Just checked this
		if (itemHeat == null) return false;

		return itemHeat.getTemperature() >= HotConfig.TEMPERATURE_VALUES.hotItemTemp;
	}, player -> player.setFire(1),
			TextFormatting.RED,
			"tooltip.hotornot.item_hot",
			true);

	public final Predicate<ItemStack> effectPredicate;
	public final Consumer<EntityPlayer> interactPlayer;
	public final TextFormatting color;
	public final String tooltip;

	public final boolean doToss;

	ItemEffect(final Predicate<ItemStack> isValid, final Consumer<EntityPlayer> interactPlayer, final TextFormatting color,
			final String tooltip, final boolean doToss) {
		this.effectPredicate = isValid;
		this.interactPlayer = interactPlayer;
		this.color = color;
		this.tooltip = tooltip;
		this.doToss = doToss;
	}

	/**
	 * Checks if this effect is active on the {@link ItemStack}
	 *
	 * @param itemStack Item Stack to check
	 *
	 * @return If this item stack is considered to have this effect active
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean stackHasEffect(final ItemStack itemStack) {
		return effectPredicate.test(itemStack);
	}

	/**
	 * Performs the effect on the given player
	 *
	 * @param player The player to perform an effect on
	 */
	public void doEffect(final EntityPlayer player) {
		interactPlayer.accept(player);
	}
}