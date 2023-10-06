package com.buuz135.hotornot.object.item;

import net.dries007.tfc.api.capability.size.IItemSize;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class HotHolderItem extends Item implements IItemSize {

	private final String tooltipKey;

	public HotHolderItem(final String tooltipKey) {
		this.tooltipKey = tooltipKey;
		setMaxStackSize(1);
	}

	@Override
	public void addInformation(final ItemStack itemStack, final @Nullable World world, final List<String> tooltip, final ITooltipFlag tooltipFlag) {
		tooltip.add(new TextComponentTranslation(tooltipKey).getUnformattedComponentText());
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public Size getSize(final ItemStack itemStack) {
		return Size.NORMAL;
	}

	@Override
	public Weight getWeight(final ItemStack itemStack) {
		return Weight.LIGHT;
	}
}