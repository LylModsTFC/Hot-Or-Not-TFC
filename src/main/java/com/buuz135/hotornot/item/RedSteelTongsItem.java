package com.buuz135.hotornot.item;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import com.buuz135.hotornot.HotOrNot;
import com.buuz135.hotornot.config.HotConfig;

public class RedSteelTongsItem extends Item
{
    public RedSteelTongsItem()
    {
        setRegistryName(HotOrNot.MOD_ID, "red_steel_tongs");
        setTranslationKey(HotOrNot.MOD_ID + ".red_steel_tongs");
        setMaxStackSize(1);
        if (HotConfig.RED_STEEL_TONGS_DURABILITY != 0)
        {
            setMaxDamage(HotConfig.RED_STEEL_TONGS_DURABILITY);
        }
        setCreativeTab(HotOrNot.HOTORNOT_TAB);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TextComponentTranslation("item.hotornot.red_steel_tongs.tooltip").getUnformattedComponentText());
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
    }
}