package com.buuz135.hotornot.client;

import com.buuz135.hotornot.HotOrNot;
import com.buuz135.hotornot.config.HotConfig;
import com.buuz135.hotornot.util.ItemEffect;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = HotOrNot.MOD_ID, value = Side.CLIENT)
public final class ClientEvents {

	@SubscribeEvent
	public static void onRenderItemTooltip(final ItemTooltipEvent event) {
		// Quit early if we shouldn't add a tooltip
		if (!HotConfig.renderEffectTooltip) return;

		final ItemStack itemStack = event.getItemStack();

		if (itemStack.isEmpty()) return;

		final boolean checkContents = itemStack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
				null) && HotConfig.EFFECT_HANDLING.checkItemContainerContents;

		for (final ItemEffect effect : ItemEffect.values()) {

			// This stack doesn't have this effect
			if (!effect.stackHasEffect(itemStack)) {
				if (checkContents) {
					final IItemHandler temp = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					// Checked this in order to reach here
					assert temp != null;

					if (contentsHaveEffect(temp, effect, 0)) {
						event.getToolTip().add(effect.color + I18n.format(effect.tooltip));
					}
				}
				continue;
			}

			event.getToolTip().add(effect.color + I18n.format(effect.tooltip));
		}
	}

	/**
	 * Checks if any of the contents of this item have the effect
	 *
	 * @param itemHandler Item handler to search
	 * @param effect The effect to check for
	 * @param containerDepth The depth of the search
	 *
	 * @return If any of the contents have the item effect
	 */
	private static boolean contentsHaveEffect(final IItemHandler itemHandler, final ItemEffect effect, int containerDepth) {
		for (int slotIndex = 0; slotIndex < itemHandler.getSlots(); slotIndex++) {
			final ItemStack slotStack = itemHandler.getStackInSlot(slotIndex);

			if (effect.stackHasEffect(slotStack)) return true;

			if (containerDepth < HotConfig.EFFECT_HANDLING.containerDepthLimit) {
				if (slotStack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
					final IItemHandler internalHandler = slotStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					// Just checked this
					assert internalHandler != null;

					if (contentsHaveEffect(itemHandler, effect, ++containerDepth)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}