package com.buuz135.hotornot;

import com.buuz135.hotornot.config.HotConfig;
import com.buuz135.hotornot.config.HotLists;
import com.buuz135.hotornot.object.item.ItemHotHolder;
import com.buuz135.hotornot.util.ItemEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = HotOrNot.MOD_ID)
public class ServerHandler {

	private static final Map<EntityPlayer, Boolean> PLAYER_REPLACE_BROKEN_HOTHOLDER_MAP = new HashMap<>();

	@SubscribeEvent
	public static void onPlayerLogin(final PlayerLoggedInEvent event) {
		// TODO need to sync more values
		HotOrNot.getNetwork().sendTo(HotLists.getServerConfigPacket(), (EntityPlayerMP) event.player);
		HotOrNot.getLog().info("Synced server lists with {}", event.player.getName());
	}

	@SubscribeEvent
	public static void onPlayerLogout(final PlayerLoggedOutEvent event) {
		PLAYER_REPLACE_BROKEN_HOTHOLDER_MAP.remove(event.player);
	}

	/**
	 * Saves the player hot holder replacement config
	 *
	 * @param player The player
	 * @param replaceBrokenHotHolder If this player wants their hot holders to be replaced or not
	 */
	public static void setReplaceHotHolderConfigForPlayer(final EntityPlayer player, final boolean replaceBrokenHotHolder) {
		PLAYER_REPLACE_BROKEN_HOTHOLDER_MAP.put(player, replaceBrokenHotHolder);
	}

	/**
	 * Checks if the given player wants their hot holders to be replaced on break
	 *
	 * @param player The player to check
	 *
	 * @return If the hot holder should be replaced for this player
	 */
	public static boolean shouldReplaceHotHolder(final EntityPlayer player) {
		return PLAYER_REPLACE_BROKEN_HOTHOLDER_MAP.get(player);
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

			if (tryDoItemEffect(event.world, player, playerItemHandler, heldItemOffhand.getItem() instanceof ItemHotHolder, 0)) {
				// Prevent divide by 0 & disable the tool damage functionality
				if (HotConfig.EFFECT_HANDLING.damageRate == 0) continue;

				if (event.world.getTotalWorldTime() % HotConfig.EFFECT_HANDLING.damageRate == 0) {

					final float damageChance = ((ItemHotHolder) heldItemOffhand.getItem()).damageChance(heldItemOffhand);

					if (event.world.rand.nextFloat() < damageChance) {
						heldItemOffhand.damageItem(1, player);

						if (!shouldReplaceHotHolder(player)) continue;

						// If it's empty the item broke
						if (!heldItemOffhand.isEmpty()) continue;

						if (HotConfig.EFFECT_HANDLING.replaceBrokenHotHolder) {
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
	}

	/**
	 * Looks through the given item handler and applies effects to the player if needed
	 *
	 * @param world World
	 * @param player The player to apply effects to
	 * @param itemHandler The item handler, should be the players
	 * @param preventEffect If the effect should be prevented (holding an item that prevents them)
	 * @param containerDepth The starting search depth
	 *
	 * @return If we handled an effect
	 */
	private static boolean tryDoItemEffect(final World world, final EntityPlayer player, final IItemHandler itemHandler,
			final boolean preventEffect, final int containerDepth) {
		boolean damageHotHolder = false;
		for (int playerSlotIndex = 0; playerSlotIndex < itemHandler.getSlots(); playerSlotIndex++) {
			final ItemStack slotStack = itemHandler.getStackInSlot(playerSlotIndex);

			// Stack is empty
			if (slotStack.isEmpty()) continue;

			// Do the optional recursive check
			if (HotConfig.EFFECT_HANDLING.checkItemContainerContents && containerDepth < HotConfig.EFFECT_HANDLING.containerDepthLimit) {
				if (slotStack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
					final IItemHandler internalHandler = slotStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					// Just checked this
					assert internalHandler != null;

					damageHotHolder = tryDoItemEffect(world, player, internalHandler, preventEffect, containerDepth + 1);
				}
			}

			for (final ItemEffect effect : ItemEffect.values()) {
				if (!effect.stackHasEffect(slotStack)) continue;

				// We found an item that has a valid effect, but we should prevent it
				if (preventEffect) {
					return true;
				}

				// Try to toss an item every 20 ticks (1 second)
				if (world.getTotalWorldTime() % 20 == 0) {
					effect.doEffect(player);
					// TODO toss the item in the player inventory and remove the baindaid containerDepth check which prevents
					//  the contents of a item container from being thrown
					if (effect.doToss && HotConfig.EFFECT_HANDLING.tossItems && containerDepth == 0) {
						final ItemStack extractedStack = itemHandler.extractItem(playerSlotIndex, slotStack.getCount(), false);
						player.dropItem(extractedStack, true);
					}
				}
			}
		}

		return damageHotHolder;
	}


	/**
	 * Searches through the player inventory for a {@link ItemHotHolder} if one is found it sets the offhand to this stack
	 *
	 * @param player Player entity
	 * @param playerItemHandler The players Item Handler
	 *
	 * @return If a Hot Holder item was able to be found
	 */
	private static boolean findAndReplaceHotHolder(final EntityPlayer player, final IItemHandler playerItemHandler) {
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