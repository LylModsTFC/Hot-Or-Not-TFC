package com.buuz135.hotornot.types;

import com.buuz135.hotornot.HotOrNot;
import com.buuz135.hotornot.object.item.HONItems;
import net.dries007.tfc.api.recipes.knapping.KnappingRecipe;
import net.dries007.tfc.api.recipes.knapping.KnappingRecipeSimple;
import net.dries007.tfc.api.recipes.knapping.KnappingType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = HotOrNot.MOD_ID)
public final class RegisterKnappingRecipe {

	@SubscribeEvent
	public static void onRegisterKnappingRecipeEvent(final Register<KnappingRecipe> event) {
		event.getRegistry().register(
				// TODO change the recipe to be something more interesting than a straight line
				new KnappingRecipeSimple(KnappingType.CLAY, false,new ItemStack(HONItems.TONGS_JAW_UNFIRED_MOLD),
						"X","X","X","X","X").setRegistryName("unfired_tongs_jaw_mold")
		);
	}
}
