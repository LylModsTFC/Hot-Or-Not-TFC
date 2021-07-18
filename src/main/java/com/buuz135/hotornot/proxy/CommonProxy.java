package com.buuz135.hotornot.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.buuz135.hotornot.item.CopperTongsItem;
import com.buuz135.hotornot.item.BronzeTongsItem;
import com.buuz135.hotornot.item.BismuthBronzeTongsItem;
import com.buuz135.hotornot.item.BlackBronzeTongsItem;
import com.buuz135.hotornot.item.WroughtIronTongsItem;
import com.buuz135.hotornot.item.SteelTongsItem;
import com.buuz135.hotornot.item.BlackSteelTongsItem;
import com.buuz135.hotornot.item.RedSteelTongsItem;
import com.buuz135.hotornot.item.BlueSteelTongsItem;
import com.buuz135.hotornot.item.MittsItem;
import com.buuz135.hotornot.item.WoodenTongsItem;

public class CommonProxy
{
    public static WoodenTongsItem WOODEN_TONGS = new WoodenTongsItem();
    public static MittsItem MITTS = new MittsItem();
    public static CopperTongsItem COPPER_TONGS = new CopperTongsItem();
    public static BronzeTongsItem BRONZE_TONGS = new BronzeTongsItem();
    public static BismuthBronzeTongsItem BISMUTH_BRONZE_TONGS = new BismuthBronzeTongsItem();
    public static BlackBronzeTongsItem BLACK_BRONZE_TONGS = new BlackBronzeTongsItem();
    public static WroughtIronTongsItem WROUGHT_IRON_TONGS = new WroughtIronTongsItem();
    public static SteelTongsItem STEEL_TONGS = new SteelTongsItem();
    public static BlackSteelTongsItem BLACK_STEEL_TONGS = new BlackSteelTongsItem();
    public static RedSteelTongsItem RED_STEEL_TONGS = new RedSteelTongsItem();
    public static BlueSteelTongsItem BLUE_STEEL_TONGS = new BlueSteelTongsItem();

    public void preInit(FMLPreInitializationEvent event)
    {

    }

    public void init(FMLInitializationEvent event)
    {

    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }

    public void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(WOODEN_TONGS);
        event.getRegistry().register(MITTS);
        event.getRegistry().register(COPPER_TONGS);
        event.getRegistry().register(BRONZE_TONGS);
        event.getRegistry().register(BISMUTH_BRONZE_TONGS);
        event.getRegistry().register(BLACK_BRONZE_TONGS);
        event.getRegistry().register(WROUGHT_IRON_TONGS);
        event.getRegistry().register(STEEL_TONGS);
        event.getRegistry().register(BLACK_STEEL_TONGS);
        event.getRegistry().register(BLUE_STEEL_TONGS);
        event.getRegistry().register(RED_STEEL_TONGS);
    }

    @SideOnly(Side.CLIENT)
    public void modelRegistryEvent(ModelRegistryEvent event)
    {

    }
}