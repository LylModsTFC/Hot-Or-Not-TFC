package com.buuz135.hotornot;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HotOrNotTab extends CreativeTabs {

	public HotOrNotTab() {
		super(HotOrNot.MOD_ID);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack createIcon() {
		//ToDo: Replace with TFC Blue Steel Lava Bucket
		return new ItemStack(Items.LAVA_BUCKET);
	}
}