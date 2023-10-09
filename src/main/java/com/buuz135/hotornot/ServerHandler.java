package com.buuz135.hotornot;

import com.buuz135.hotornot.config.HotConfig;
import com.buuz135.hotornot.config.HotLists;
import com.buuz135.hotornot.network.SyncClientLists;
import com.buuz135.hotornot.object.item.ItemHotHolder;
import com.buuz135.hotornot.util.FluidEffect;
import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = HotOrNot.MOD_ID)
public class ServerHandler {

	@SubscribeEvent
	public static void onPlayerLogin(final PlayerLoggedInEvent event) {

		HotOrNot.getNetwork().sendTo(new SyncClientLists(), (EntityPlayerMP) event.player);
		HotOrNot.getLog().info("Synced server lists with {}", event.player.getName());
	}

	@SubscribeEvent
	public static void onTick(final WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.START) return;

		if (event.side != Side.SERVER) return;

		for (final EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
			if (player.isBurning() || player.isCreative()) return;

			final IItemHandler playerItemHandler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			// If players don't have the item handler capability somebody did some nasty mixin and messed things up for more than just us
			assert playerItemHandler != null;


			final ItemStack heldItemOffhand = player.getHeldItemOffhand();

			final boolean hasHotHolder = heldItemOffhand.getItem() instanceof ItemHotHolder;
			boolean damageHotHolder = false;
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
						if (!effect.isValid(fluidStack)) continue;

						if (hasHotHolder) {
							damageHotHolder = true;
							break;
						}

						// Try to toss an item every 20 ticks (1 second)
						if (event.world.getTotalWorldTime() % 20 == 0) {
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

						if (hasHotHolder) {
							damageHotHolder = true;
							break;
						}

						// Try to toss an item every 20 ticks (1 second)
						if (event.world.getTotalWorldTime() % 20 == 0) {
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

					if (hasHotHolder) {
						damageHotHolder = true;
						break;
					}

					// Try to toss an item every 20 ticks (1 second)
					if (event.world.getTotalWorldTime() % 20 == 0) {
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

					if (hasHotHolder) {
						damageHotHolder = true;
						break;
					}

					if (event.world.getTotalWorldTime() % 20 == 0) {
						player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 21, 1));
						player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 21, 1));
					}
					continue;
				}

				// Items added to manual gas config
				if (HotLists.isGaseous(slotStack)) {

					if (hasHotHolder) {
						damageHotHolder = true;
						break;
					}

					if (event.world.getTotalWorldTime() % 20 == 0) {
						player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 21, 1));
					}
				}
			}

			// Damage the item only once, no matter how many effects we prevent
			if (hasHotHolder && damageHotHolder) {
				// Prevent divide by 0 & disable the tool damage functionality
				if (HotConfig.damageRate == 0) continue;

				if (event.world.getTotalWorldTime() % HotConfig.damageRate == 0) {
					heldItemOffhand.damageItem(1, player);
					// If it's empty the item broke
					if (heldItemOffhand.isEmpty() && HotConfig.replaceBrokenHotHolder) {
						if (findAndReplaceHotHolder(player, playerItemHandler)) {
							event.world.playSound(null, player.posX, player.posY, player.posZ,
									SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
									0.8F,
									0.8F + player.world.rand.nextFloat() * 0.4F);
						}
					}
				}
			}
		}
	}

	/**
	 * Searches through the player inventory for a {@link ItemHotHolder} if one is found it sets the offhand to this stack
	 *
	 * @param player Player entity
	 * @param playerItemHandler The players Item Handler
	 *
	 * @return If a Hot Holder item was able to be found
	 */
	public static boolean findAndReplaceHotHolder(final EntityPlayer player, final IItemHandler playerItemHandler) {
		for (int slotIndex = 0; slotIndex < playerItemHandler.getSlots(); slotIndex++) {
			final ItemStack slotStack = playerItemHandler.getStackInSlot(slotIndex);

			// Not a Hot Holder item
			if (!(slotStack.getItem() instanceof ItemHotHolder)) continue;

			player.setHeldItem(EnumHand.OFF_HAND, playerItemHandler.extractItem(slotIndex, slotStack.getCount(), false));
			return true;
		}

		return false;
	}
}