package com.buuz135.hotornot.client;

import com.buuz135.hotornot.HotOrNot;
import com.buuz135.hotornot.object.item.HONItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = HotOrNot.MOD_ID, value = Side.CLIENT)
public final class ClientRegistryEvents {

	@SubscribeEvent
	public static void onModelRegister(final ModelRegistryEvent event) {
		HONItems.getAllSimpleItems().forEach(ClientRegistryEvents::registerModel);
	}

	private static void registerModel(final Item item) {
		//noinspection DataFlowIssue
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}