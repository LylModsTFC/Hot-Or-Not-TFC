package com.buuz135.hotornot.config;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParametersAreNonnullByDefault
public final class HotLists {

	private static final List<Item> hotList = new ArrayList<>();
	private static final List<Item> coldList = new ArrayList<>();
	private static final List<Item> gaseousList = new ArrayList<>();
	private static final List<Item> exemptionList = new ArrayList<>();

	/**
	 * Only call this from the server side. The server initializes these lists on the client when it connects
	 */
	public static void init() {
		Arrays.stream(HotConfig.MANUAL_ENTRIES.itemRemovals)
				.map(itemRegistryName -> Item.REGISTRY.getObject(new ResourceLocation(itemRegistryName)))
				.forEach(exemptionList::add);
		Arrays.stream(HotConfig.MANUAL_ENTRIES.hotItemAdditions)
				.map(itemRegistryName -> Item.REGISTRY.getObject(new ResourceLocation(itemRegistryName)))
				.forEach(hotList::add);
		Arrays.stream(HotConfig.MANUAL_ENTRIES.coldItemAdditions)
				.map(itemRegistryName -> Item.REGISTRY.getObject(new ResourceLocation(itemRegistryName)))
				.forEach(coldList::add);
		Arrays.stream(HotConfig.MANUAL_ENTRIES.gaseousItemAdditions)
				.map(itemRegistryName -> Item.REGISTRY.getObject(new ResourceLocation(itemRegistryName)))
				.forEach(gaseousList::add);
	}

	/**
	 * Checks if the stack is exempt from our checks
	 *
	 * @param itemStack The item stack to check
	 *
	 * @return If the item stack is exempt from the checks
	 */
	public static boolean isExempt(final ItemStack itemStack) {
		return exemptionList.contains(itemStack.getItem());
	}

	/**
	 * Checks if an item is hot
	 *
	 * @param itemStack The item stack to check
	 *
	 * @return If the item is considered hot
	 */
	public static boolean isHot(final ItemStack itemStack) {
		return hotList.contains(itemStack.getItem());
	}

	/**
	 * Checks if an item is in the cold list
	 *
	 * @param itemStack The item stack to check
	 *
	 * @return If the item is considered cold
	 */
	public static boolean isCold(final ItemStack itemStack) {
		return coldList.contains(itemStack.getItem());
	}

	/**
	 * Checks if an item is in the gaseous list
	 *
	 * @param itemStack The item stack to check
	 *
	 * @return If the item is considered gaseous
	 */
	public static boolean isGaseous(final ItemStack itemStack) {
		return gaseousList.contains(itemStack.getItem());
	}

	/**
	 * Writes our lists to the packet buffer
	 *
	 * @param buf Buffer to populate with our lists
	 */
	public static void writeListsToByteBuf(final ByteBuf buf) {
		ByteBufUtils.writeRegistryEntries(buf, exemptionList);
		ByteBufUtils.writeRegistryEntries(buf, hotList);
		ByteBufUtils.writeRegistryEntries(buf, coldList);
		ByteBufUtils.writeRegistryEntries(buf, gaseousList);
	}

	/**
	 * Clears then reads out lists from the buffer
	 *
	 * @param buf Buffer to populate our lists from
	 */
	public static void readListsFromByteBuf(final ByteBuf buf) {
		final IForgeRegistry<Item> temp = GameRegistry.findRegistry(Item.class);

		exemptionList.clear();
		exemptionList.addAll(ByteBufUtils.readRegistryEntries(buf, temp));
		hotList.clear();
		hotList.addAll(ByteBufUtils.readRegistryEntries(buf, temp));
		coldList.clear();
		coldList.addAll(ByteBufUtils.readRegistryEntries(buf, temp));
		gaseousList.clear();
		gaseousList.addAll(ByteBufUtils.readRegistryEntries(buf, temp));
	}
}