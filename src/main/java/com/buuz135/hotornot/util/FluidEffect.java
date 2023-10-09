package com.buuz135.hotornot.util;

import com.buuz135.hotornot.config.HotConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;

public enum FluidEffect {
	HOT(fluidStack -> fluidStack.getFluid().getTemperature(fluidStack) >= HotConfig.TEMPERATURE_VALUES.hotFluidTemp + 273 && HotConfig.handleHotFluids,
			entityPlayerMP -> entityPlayerMP.setFire(1),
			TextFormatting.RED,
			"tooltip.hotornot.toohot"),
	COLD(fluidStack -> fluidStack.getFluid()
			.getTemperature(fluidStack) <= HotConfig.TEMPERATURE_VALUES.coldFluidTemp + 273 && HotConfig.handleColdFluids,
			entityPlayerMP -> {
				entityPlayerMP.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 21, 1));
				entityPlayerMP.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 21, 1));
			},
			TextFormatting.AQUA,
			"tooltip.hotornot.toocold"),
	GAS(fluidStack -> fluidStack.getFluid().isGaseous(fluidStack) && HotConfig.handleGaseousFluids,
			entityPlayerMP -> entityPlayerMP.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 21, 1)),
			TextFormatting.YELLOW,
			"tooltip.hotornot.toolight");

	public final Predicate<FluidStack> effectPredicate;
	public final Consumer<EntityPlayerMP> interactPlayer;
	public final TextFormatting color;
	public final String tooltip;

	FluidEffect(final Predicate<FluidStack> isValid, final Consumer<EntityPlayerMP> interactPlayer, final TextFormatting color,
			final String tooltip) {
		this.effectPredicate = isValid;
		this.interactPlayer = interactPlayer;
		this.color = color;
		this.tooltip = tooltip;
	}

	/**
	 * Tests the fluidStack using the appropriate effect predicate
	 *
	 * @param fluidStack The fluid stack to test
	 *
	 * @return If the fluid stack passes the predicate
	 */
	public boolean isValid(final @Nonnull FluidStack fluidStack) {
		return effectPredicate.test(fluidStack);
	}
}