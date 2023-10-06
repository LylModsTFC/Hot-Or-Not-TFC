package com.buuz135.hotornot.config;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class HotLists {

	/**
	 * Checks if the stack is exempt from our checks
	 *
	 * @param itemStack The item stack to check
	 *
	 * @return If the item stack is exempt from the checks
	 */
	public static boolean isExempt(final @Nonnull ItemStack itemStack) {
		final String regName = itemStack.getItem().getRegistryName().toString();
		for (String s : HotConfig.ITEM_REMOVALS) {
			if (regName.equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if an item is hot
	 *
	 * @param itemStack The item stack to check
	 *
	 * @return If the item is hot
	 */
	public static boolean isHot(final @Nonnull ItemStack itemStack) {
		final String regName = itemStack.getItem().getRegistryName().toString();
		for (final String s : HotConfig.HOT_ITEM_ADDITIONS) {
			if (regName.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isCold(ItemStack stack) {
		String regName = stack.getItem().getRegistryName().toString();
		for (String s : HotConfig.COLD_ITEM_ADDITIONS) {
			if (regName.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isGaseous(ItemStack stack) {
		String regName = stack.getItem().getRegistryName().toString();
		for (String s : HotConfig.GASEOUS_ITEM_ADDITIONS) {
			if (regName.equals(s)) {
				return true;
			}
		}
		return false;
	}
}