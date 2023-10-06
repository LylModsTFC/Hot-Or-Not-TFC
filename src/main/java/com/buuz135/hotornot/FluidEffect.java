package com.buuz135.hotornot;

import com.buuz135.hotornot.config.HotConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;
import java.util.function.Predicate;

public enum FluidEffect {
	HOT(fluidStack -> fluidStack.getFluid().getTemperature(fluidStack) >= HotConfig.hotFluidTemp + 273 && HotConfig.handleHotFluids,
			entityPlayerMP -> entityPlayerMP.setFire(1), TextFormatting.RED, "tooltip.hotornot.toohot"),
	COLD(fluidStack -> fluidStack.getFluid().getTemperature(fluidStack) <= HotConfig.coldFluidTemp + 273 && HotConfig.handleColdFluids,
			entityPlayerMP ->
			{
				entityPlayerMP.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 21, 1));
				entityPlayerMP.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 21, 1));
			}, TextFormatting.AQUA, "tooltip.hotornot.toocold"),
	GAS(fluidStack -> fluidStack.getFluid().isGaseous(fluidStack) && HotConfig.handleGaseousFluids,
			entityPlayerMP -> entityPlayerMP.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 21, 1)), TextFormatting.YELLOW,
			"tooltip.hotornot.toolight");

	public final Predicate<FluidStack> isValid;
	public final Consumer<EntityPlayerMP> interactPlayer;
	public final TextFormatting color;
	public final String tooltip;

	FluidEffect(final Predicate<FluidStack> isValid, final Consumer<EntityPlayerMP> interactPlayer, final TextFormatting color,
			final String tooltip) {
		this.isValid = isValid;
		this.interactPlayer = interactPlayer;
		this.color = color;
		this.tooltip = tooltip;
	}

	public String getTooltip() {
		return color + new TextComponentTranslation(tooltip).getUnformattedText();
	}
}