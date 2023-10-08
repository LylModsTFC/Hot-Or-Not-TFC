package com.buuz135.hotornot.client;

import com.buuz135.hotornot.FluidEffect;
import com.buuz135.hotornot.HotOrNot;
import com.buuz135.hotornot.config.HotConfig;
import com.buuz135.hotornot.config.HotLists;
import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = HotOrNot.MOD_ID, value = Side.CLIENT)
public final class ClientEvents {

	@SubscribeEvent
	public static void onItemTooltip(final ItemTooltipEvent event) {
		// Quit early if we shouldn't add a tooltip
		if (!HotConfig.renderEffectTooltip) return;

		final ItemStack itemStack = event.getItemStack();

		if (itemStack.isEmpty()) return;

		if (HotLists.isExempt(itemStack)) return;

		// Fluid item container
		if (itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			final IFluidHandlerItem fluidHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			assert fluidHandler != null;

			final FluidStack fluidStack = fluidHandler.drain(1, false);
			// No fluid to worry about
			if (fluidStack == null) return;

			for (final FluidEffect effect : FluidEffect.values()) {
				if (!effect.isValid(fluidStack)) continue;

				event.getToolTip().add(effect.color + I18n.format(effect.tooltip));
			}
		}

		// TFC heat capability
		if (itemStack.hasCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null)) {
			final IItemHeat heat = itemStack.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
			assert heat != null;

			if (heat.getTemperature() >= HotConfig.hotItemTemp) {
				event.getToolTip().add(FluidEffect.HOT.color + I18n.format(FluidEffect.HOT.tooltip));
			}
		}

		if (HotLists.isHot(itemStack)) {
			event.getToolTip().add(FluidEffect.HOT.color + I18n.format(FluidEffect.HOT.tooltip));
		}

		if (HotLists.isCold(itemStack)) {
			event.getToolTip().add(FluidEffect.COLD.color + I18n.format(FluidEffect.COLD.tooltip));
		}

		if (HotLists.isGaseous(itemStack)) {
			event.getToolTip().add(FluidEffect.GAS.color + I18n.format(FluidEffect.GAS.tooltip));
		}
	}
}