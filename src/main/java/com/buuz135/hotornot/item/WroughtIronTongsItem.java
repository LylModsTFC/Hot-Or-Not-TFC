package com.buuz135.hotornot.item;

import com.buuz135.hotornot.HotOrNot;
import com.buuz135.hotornot.config.HotConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class WroughtIronTongsItem extends Item {

	public WroughtIronTongsItem() {
		setRegistryName(HotOrNot.MOD_ID, "wrought_iron_tongs");
		setTranslationKey(HotOrNot.MOD_ID + ".wrought_iron_tongs");
		setMaxStackSize(1);
		if (HotConfig.WROUGHT_IRON_TONGS_DURABILITY != 0) {
			setMaxDamage(HotConfig.WROUGHT_IRON_TONGS_DURABILITY);
		}
		setCreativeTab(HotOrNot.HOTORNOT_TAB);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TextComponentTranslation("item.hotornot.wrought_iron_tongs.tooltip").getUnformattedComponentText());
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}
}