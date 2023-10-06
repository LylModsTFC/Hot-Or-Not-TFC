/*
 * This file is part of Hot or Not.
 *
 * Copyright 2018, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.hotornot;

import com.buuz135.hotornot.config.HotConfig;
import com.buuz135.hotornot.config.HotLists;
import com.buuz135.hotornot.proxy.CommonProxy;
import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@Mod(modid = HotOrNot.MOD_ID, name = HotOrNot.MOD_NAME, version = HotOrNot.VERSION, useMetadata = true)
public class HotOrNot {

	public static final String MOD_ID = "hotornot";
	public static final String MOD_NAME = "Hot Or Not - TFC";
	public static final String VERSION = "1.1.6";

	public static final CreativeTabs HOTORNOT_TAB = new HotOrNotTab();

	@SidedProxy(clientSide = "com.buuz135.hotornot.proxy.ClientProxy", serverSide = "com.buuz135.hotornot.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventBusSubscriber
	public static class ObjectRegistryHandler {

		@SubscribeEvent
		public static void addItems(RegistryEvent.Register<Item> event) {
			proxy.registerItems(event);
		}

		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void modelRegistryEvent(ModelRegistryEvent event) {
			proxy.modelRegistryEvent(event);
		}
	}

	@Mod.EventBusSubscriber
	public static class ServerTick {

		@SubscribeEvent
		public static void onTick(TickEvent.WorldTickEvent event) {
			if (event.phase == TickEvent.Phase.START) {
				for (EntityPlayerMP entityPlayerMP : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
					if (!entityPlayerMP.isBurning() && !entityPlayerMP.isCreative() && entityPlayerMP.hasCapability(
							CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
						IItemHandler handler = entityPlayerMP.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
						for (int i = 0; i < handler.getSlots(); i++) {
							ItemStack stack = handler.getStackInSlot(i);

							// FLUIDS
							if (!stack.isEmpty() && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
									null) && !HotLists.isRemoved(stack)) {
								IFluidHandlerItem fluidHandlerItem = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
								FluidStack fluidStack = fluidHandlerItem.drain(1000, false);
								if (fluidStack != null) {
									for (FluidEffect effect : FluidEffect.values()) {
										if (effect.isValid.test(fluidStack)) {
											ItemStack offHand = entityPlayerMP.getHeldItemOffhand();
											if (offHand.getItem().equals(CommonProxy.MITTS)) {
												if (HotConfig.MITTS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.WOODEN_TONGS)) {
												if (HotConfig.WOODEN_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.COPPER_TONGS)) {
												if (HotConfig.COPPER_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.BRONZE_TONGS)) {
												if (HotConfig.BRONZE_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.BISMUTH_BRONZE_TONGS)) {
												if (HotConfig.BISMUTH_BRONZE_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.BLACK_BRONZE_TONGS)) {
												if (HotConfig.BLACK_BRONZE_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.WROUGHT_IRON_TONGS)) {
												if (HotConfig.WROUGHT_IRON_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.STEEL_TONGS)) {
												if (HotConfig.STEEL_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.BLACK_STEEL_TONGS)) {
												if (HotConfig.BLACK_STEEL_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.RED_STEEL_TONGS)) {
												if (HotConfig.RED_STEEL_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.BLUE_STEEL_TONGS)) {
												if (HotConfig.BLUE_STEEL_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (event.world.getTotalWorldTime() % 20 == 0) {
												effect.interactPlayer.accept(entityPlayerMP);
												if (HotConfig.YEET) {
													entityPlayerMP.dropItem(stack, false, true);
													entityPlayerMP.inventory.deleteStack(stack);
												}
											}
										}
									}
								}
							}

							if (HotConfig.HOT_ITEMS && !stack.isEmpty() && !HotLists.isRemoved(stack)) {
								if (Loader.isModLoaded("tfc")) {
									// TFC ITEMS
									if (stack.hasCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null)) {
										IItemHeat heatHandlerItem = stack.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
										if (heatHandlerItem.getTemperature() >= HotConfig.HOT_ITEM) {
											ItemStack offHand = entityPlayerMP.getHeldItemOffhand();
											if (offHand.getItem().equals(CommonProxy.MITTS)) {
												if (HotConfig.MITTS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.WOODEN_TONGS)) {
												if (HotConfig.WOODEN_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.COPPER_TONGS)) {
												if (HotConfig.COPPER_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.BRONZE_TONGS)) {
												if (HotConfig.BRONZE_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.BISMUTH_BRONZE_TONGS)) {
												if (HotConfig.BISMUTH_BRONZE_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.BLACK_BRONZE_TONGS)) {
												if (HotConfig.BLACK_BRONZE_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.WROUGHT_IRON_TONGS)) {
												if (HotConfig.WROUGHT_IRON_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.STEEL_TONGS)) {
												if (HotConfig.STEEL_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.BLACK_STEEL_TONGS)) {
												if (HotConfig.BLACK_STEEL_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.RED_STEEL_TONGS)) {
												if (HotConfig.RED_STEEL_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (offHand.getItem().equals(CommonProxy.BLUE_STEEL_TONGS)) {
												if (HotConfig.BLUE_STEEL_TONGS_DURABILITY != 0) {
													offHand.damageItem(1, entityPlayerMP);
												}
											} else if (event.world.getTotalWorldTime() % 10 == 0) {
												entityPlayerMP.setFire(1);
												if (HotConfig.YEET) {
													entityPlayerMP.dropItem(stack, false, true);
													entityPlayerMP.inventory.deleteStack(stack);
												}
											}
										}
									}
								}
								// MANUALLY ADDED ITEMS
								else if (HotLists.isHot(stack)) {
									ItemStack offHand = entityPlayerMP.getHeldItemOffhand();
									if (offHand.getItem().equals(CommonProxy.MITTS)) {
										if (HotConfig.MITTS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.WOODEN_TONGS)) {
										if (HotConfig.WOODEN_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.COPPER_TONGS)) {
										if (HotConfig.COPPER_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.BRONZE_TONGS)) {
										if (HotConfig.BRONZE_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.BISMUTH_BRONZE_TONGS)) {
										if (HotConfig.BISMUTH_BRONZE_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.BLACK_BRONZE_TONGS)) {
										if (HotConfig.BLACK_BRONZE_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.WROUGHT_IRON_TONGS)) {
										if (HotConfig.WROUGHT_IRON_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.STEEL_TONGS)) {
										if (HotConfig.STEEL_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.BLACK_STEEL_TONGS)) {
										if (HotConfig.BLACK_STEEL_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.RED_STEEL_TONGS)) {
										if (HotConfig.RED_STEEL_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.BLUE_STEEL_TONGS)) {
										if (HotConfig.BLUE_STEEL_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (event.world.getTotalWorldTime() % 10 == 0) {
										entityPlayerMP.setFire(1);
										if (HotConfig.YEET) {
											entityPlayerMP.dropItem(stack, false, true);
											entityPlayerMP.inventory.deleteStack(stack);
										}
									}
								} else if (HotLists.isCold(stack)) {
									ItemStack offHand = entityPlayerMP.getHeldItemOffhand();
									if (offHand.getItem().equals(CommonProxy.MITTS)) {
										if (HotConfig.MITTS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.WOODEN_TONGS)) {
										if (HotConfig.WOODEN_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.COPPER_TONGS)) {
										if (HotConfig.COPPER_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.BRONZE_TONGS)) {
										if (HotConfig.BRONZE_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.BISMUTH_BRONZE_TONGS)) {
										if (HotConfig.BISMUTH_BRONZE_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.BLACK_BRONZE_TONGS)) {
										if (HotConfig.BLACK_BRONZE_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.WROUGHT_IRON_TONGS)) {
										if (HotConfig.WROUGHT_IRON_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.STEEL_TONGS)) {
										if (HotConfig.STEEL_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.BLACK_STEEL_TONGS)) {
										if (HotConfig.BLACK_STEEL_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.RED_STEEL_TONGS)) {
										if (HotConfig.RED_STEEL_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (offHand.getItem().equals(CommonProxy.BLUE_STEEL_TONGS)) {
										if (HotConfig.BLUE_STEEL_TONGS_DURABILITY != 0) {
											offHand.damageItem(1, entityPlayerMP);
										}
									} else if (event.world.getTotalWorldTime() % 10 == 0) {
										entityPlayerMP.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 21, 1));
										entityPlayerMP.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 21, 1));
									}
								} else if (HotLists.isGaseous(stack)) {
									if (event.world.getTotalWorldTime() % 10 == 0) {
										entityPlayerMP.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 21, 1));
									}
								}

							}
						}
					}
				}
			}
		}
	}

	@Mod.EventBusSubscriber(value = Side.CLIENT)
	public static class HotTooltip {

		@SubscribeEvent
		public static void onTooltip(ItemTooltipEvent event) {
			ItemStack stack = event.getItemStack();
			if (HotConfig.TOOLTIP && !stack.isEmpty() && !HotLists.isRemoved(stack)) {
				if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
					IFluidHandlerItem fluidHandlerItem = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
					FluidStack fluidStack = fluidHandlerItem.drain(1000, false);
					if (fluidStack != null) {
						for (FluidEffect effect : FluidEffect.values()) {
							if (effect.isValid.test(fluidStack)) {
								event.getToolTip().add(effect.color + new TextComponentTranslation(effect.tooltip).getUnformattedText());
							}
						}
					}
				} else if (HotLists.isHot(stack)) {
					event.getToolTip().add(FluidEffect.HOT.color + new TextComponentTranslation(FluidEffect.HOT.tooltip).getUnformattedText());
				} else if (HotLists.isCold(stack)) {
					event.getToolTip().add(FluidEffect.COLD.color + new TextComponentTranslation(FluidEffect.COLD.tooltip).getUnformattedText());
				} else if (HotLists.isGaseous(stack)) {
					event.getToolTip().add(FluidEffect.GAS.color + new TextComponentTranslation(FluidEffect.GAS.tooltip).getUnformattedText());
				} else if (Loader.isModLoaded("tfc")) {
					if (stack.hasCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null)) {
						IItemHeat heat = stack.getCapability(CapabilityItemHeat.ITEM_HEAT_CAPABILITY, null);
						if (heat.getTemperature() >= HotConfig.HOT_ITEM) {
							event.getToolTip()
									.add(FluidEffect.HOT.color + new TextComponentTranslation(FluidEffect.HOT.tooltip).getUnformattedText());
						}
					}
				}
			}
		}
	}
}