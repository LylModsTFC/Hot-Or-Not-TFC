package com.buuz135.hotornot;

import com.buuz135.hotornot.config.HotConfig;
import com.buuz135.hotornot.config.HotLists;
import com.buuz135.hotornot.object.item.HotHolderItem;
import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = HotOrNot.MOD_ID)
public class ServerHandler {

	@SubscribeEvent
	public static void onPlayerLogin(final PlayerLoggedInEvent event) {
		// TODO sync config to client
	}

	@SubscribeEvent
	public static void onTick(final WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.START) return;

		for (final EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
			if (player.isBurning() || player.isCreative()) return;

			final IItemHandler playerItemHandler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			// If players don't have the item handler capability somebody did some nasty mixin and messed things up for more than just us
			assert playerItemHandler != null;

			for (int playerSlotIndex = 0; playerSlotIndex < playerItemHandler.getSlots(); playerSlotIndex++) {
				final ItemStack slotStack = playerItemHandler.getStackInSlot(playerSlotIndex);

				// Stack is empty
				if (slotStack.isEmpty()) continue;

				// Exempt from our handing
				if (HotLists.isExempt(slotStack)) continue;

				// Item contains fluid
				if (slotStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
					final IFluidHandlerItem itemFluidHandler = slotStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
					// Just checked this
					assert itemFluidHandler != null;

					final FluidStack fluidStack = itemFluidHandler.drain(1, false);
					// Means it's empty
					if (fluidStack == null) continue;

					for (final FluidEffect effect : FluidEffect.values()) {
						if (!effect.isValid.test(fluidStack)) continue;

						final ItemStack heldItemOffhand = player.getHeldItemOffhand();

						if (heldItemOffhand.getItem() instanceof HotHolderItem) {
							heldItemOffhand.damageItem(1, player);
							// Try to toss an item every 20 ticks (1 second)
						} else if (event.world.getTotalWorldTime() % 20 == 0) {
							effect.interactPlayer.accept(player);
							if (HotConfig.tossItems) {
								final ItemStack extractedStack = playerItemHandler.extractItem(playerSlotIndex, slotStack.getCount(), false);
								player.dropItem(extractedStack, false, true);
							}
						}
					}
				}

				// Item has TFC heat capability
				if (slotStack.hasCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null)) {
					if (!HotConfig.handleHotItems) continue;

					final IItemHeat itemHeat = slotStack.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
					// Just checked this
					assert itemHeat != null;

					if (itemHeat.getTemperature() >= HotConfig.hotItemTemp) {

						final ItemStack heldItemOffhand = player.getHeldItemOffhand();

						if (heldItemOffhand.getItem() instanceof HotHolderItem) {
							heldItemOffhand.damageItem(1, player);
							// Try to toss an item every 10 ticks (0.5 seconds)
						} else if (event.world.getTotalWorldTime() % 10 == 0) {
							player.setFire(1);
							if (HotConfig.tossItems) {
								final ItemStack extractedStack = playerItemHandler.extractItem(playerSlotIndex, slotStack.getCount(), false);
								player.dropItem(extractedStack, false, true);
							}
						}
					}
				}

				// Items added to manual hot config
				if (HotLists.isHot(slotStack)) {
					final ItemStack heldItemOffhand = player.getHeldItemOffhand();

					if (heldItemOffhand.getItem() instanceof HotHolderItem) {
						heldItemOffhand.damageItem(1, player);

						// Try to toss an item every 10 ticks (0.5 seconds)
					} else if (event.world.getTotalWorldTime() % 10 == 0) {
						player.setFire(1);
						if (HotConfig.tossItems) {
							final ItemStack extractedStack = playerItemHandler.extractItem(playerSlotIndex, slotStack.getCount(), false);
							player.dropItem(extractedStack, false, true);
						}
					}
					continue;
				}

				// Items added to manual cold config
				if (HotLists.isCold(slotStack)) {
					final ItemStack heldItemOffhand = player.getHeldItemOffhand();

					if (heldItemOffhand.getItem() instanceof HotHolderItem) {
						heldItemOffhand.damageItem(1, player);
						// Try to toss an item every 10 ticks (0.5 seconds)
					} else if (event.world.getTotalWorldTime() % 10 == 0) {
						player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 21, 1));
						player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 21, 1));
					}
					continue;
				}

				// Items added to manual gas config
				if (HotLists.isGaseous(slotStack)) {
					if (event.world.getTotalWorldTime() % 10 == 0) {
						player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 21, 1));
					}
				}
			}
		}
	}
}