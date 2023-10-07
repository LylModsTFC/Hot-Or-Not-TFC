package com.buuz135.hotornot.object.item;

import net.dries007.tfc.api.capability.forge.ForgeableHeatableHandler;
import net.dries007.tfc.api.capability.metal.IMetalItem;
import net.dries007.tfc.api.types.Metal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class ItemMetalTongs extends ItemHotHolder implements IMetalItem {

	private final Metal metal;

	/**
	 * @param metal The Metal type these tongs are
	 */
	public ItemMetalTongs(final Metal metal) {
		super(metal.getTier());
		this.metal = metal;

		// Set max damage depending on tier
		switch (metal.getTier()) {
			case TIER_0:
				setMaxDamage(1);
				break;
			case TIER_I:
				setMaxDamage(1_000);
				break;
			case TIER_II:
				setMaxDamage(2_000);
				break;
			case TIER_III:
				setMaxDamage(3_000);
				break;
			case TIER_IV:
				setMaxDamage(4_000);
				break;
			case TIER_V:
				setMaxDamage(6_000);
				break;
			case TIER_VI:
				setMaxDamage(12_000);
				break;
		}
	}

	@Override
	public Metal getMetal(final ItemStack itemStack) {
		return metal;
	}

	@Override
	public int getSmeltAmount(final ItemStack itemStack) {
		// They are worth 100 units
		if (!isDamageable() || !itemStack.isItemDamaged()) return 200;

		final double damagePercent = (double) (itemStack.getMaxDamage() - itemStack.getItemDamage()) / itemStack.getMaxDamage() - 0.1D;
		return damagePercent < 0 ? 0 : MathHelper.floor(200 * damagePercent);
	}

	@Override
	public boolean canMelt(final ItemStack stack) {
		return true;
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack itemStack, @Nullable final NBTTagCompound nbt) {
		return new ForgeableHeatableHandler(nbt, metal.getSpecificHeat(), metal.getMeltTemp());
	}
}
