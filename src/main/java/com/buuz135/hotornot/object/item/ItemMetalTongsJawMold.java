package com.buuz135.hotornot.object.item;

import net.dries007.tfc.api.capability.IMoldHandler;
import net.dries007.tfc.api.capability.heat.Heat;
import net.dries007.tfc.api.capability.heat.ItemHeatHandler;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.container.CapabilityContainerListener;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.objects.items.ceramics.ItemPottery;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemMetalTongsJawMold extends ItemPottery {

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(final ItemStack itemStack, @Nullable final NBTTagCompound nbt) {
		return new FilledMoldCapability(nbt);
	}

	@Override
	public int getItemStackLimit(final ItemStack itemStack) {
		final IMoldHandler moldHandler = (IMoldHandler) itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
		if (moldHandler != null && moldHandler.getMetal() != null) {
			return 1;
		}
		return super.getItemStackLimit(itemStack);
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack) {
		final IFluidHandler capFluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
		if (!(capFluidHandler instanceof IMoldHandler)) return super.getTranslationKey(stack);

		final Metal metal = ((IMoldHandler) capFluidHandler).getMetal();
		//noinspection DataFlowIssue
		return metal != null ? super.getTranslationKey(stack) + "." + metal.getRegistryName().getPath() : super.getTranslationKey(stack);
	}

	@Nullable
	@Override
	public NBTTagCompound getNBTShareTag(final ItemStack itemStack) {
		return CapabilityContainerListener.readShareTag(itemStack);
	}

	@Override
	public void readNBTShareTag(final ItemStack itemStack, final @Nullable NBTTagCompound nbt) {
		CapabilityContainerListener.applyShareTag(itemStack, nbt);
	}

	/**
	 * Copy of the TFC mold capability which we don't have access to
	 */
	private static class FilledMoldCapability extends ItemHeatHandler implements ICapabilityProvider, IMoldHandler {

		private final FluidTank tank = new FluidTank(100);
		private final IFluidTankProperties[] fluidTankProperties = {new FluidTankPropertiesWrapper(tank)};

		FilledMoldCapability(final @Nullable NBTTagCompound nbt) {
			if (nbt != null) {
				deserializeNBT(nbt);
			}
		}

		@Nullable
		@Override
		public Metal getMetal() {
			return tank.getFluid() != null ? FluidsTFC.getMetalFromFluid(tank.getFluid().getFluid()) : null;
		}

		@Override
		public int getAmount() {
			return tank.getFluidAmount();
		}

		@Override
		public IFluidTankProperties[] getTankProperties() {
			return fluidTankProperties;
		}

		@Override
		public int fill(final FluidStack resource, final boolean doFill) {
			final int fillAmount = tank.fill(resource, doFill);
			if (fillAmount == tank.getFluidAmount()) {
				updateFluidData();
			}

			return fillAmount;
		}

		@Nullable
		@Override
		public FluidStack drain(final FluidStack resource, final boolean doDrain) {
			return getTemperature() >= meltTemp ? tank.drain(resource, doDrain) : null;
		}

		@Nullable
		@Override
		public FluidStack drain(final int maxDrain, final boolean doDrain) {
			if (getTemperature() > meltTemp) {
				final FluidStack drained = tank.drain(maxDrain, doDrain);
				if (tank.getFluidAmount() == 0) {
					updateFluidData();
				}

				return drained;
			}

			return null;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void addHeatInfo(final ItemStack itemStack, final List<String> text) {
			final Metal metal = getMetal();
			if (metal != null) {
				String desc = TextFormatting.DARK_GREEN + I18n.format(Helpers.getTypeName(metal)) + ": " + I18n.format("tfc.tooltip.units",
						getAmount());
				if (isMolten()) {
					desc += I18n.format("tfc.tooltip.liquid");
				} else {
					desc += I18n.format("tfc.tooltip.solid");
				}

				text.add(desc);
			}

			super.addHeatInfo(itemStack, text);
		}

		@Override
		public float getHeatCapacity() {
			return heatCapacity;
		}

		@Override
		public float getMeltTemp() {
			return meltTemp;
		}

		@Override
		public boolean hasCapability(final Capability<?> capability, final @Nullable EnumFacing facing) {
			return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
		}

		@Nullable
		@Override
		@SuppressWarnings("unchecked")
		public <T> T getCapability(final Capability<T> capability, final @Nullable EnumFacing facing) {
			return hasCapability(capability, facing) ? (T) this : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			final NBTTagCompound compound = new NBTTagCompound();
			if (getTemperature() <= 0.0F) {
				compound.setLong("ticks", -1L);
				compound.setFloat("heat", 0.0F);
			} else {
				compound.setLong("ticks", lastUpdateTick);
				compound.setFloat("heat", temperature);
			}

			return tank.writeToNBT(compound);
		}

		@Override
		public void deserializeNBT(final @Nullable NBTTagCompound compound) {
			if (compound != null) {
				temperature = compound.getFloat("heat");
				lastUpdateTick = compound.getLong("ticks");
				tank.readFromNBT(compound);
			}

			updateFluidData();
		}

		private void updateFluidData() {
			updateFluidData(tank.getFluid());
		}

		private void updateFluidData(final @Nullable FluidStack fluidStack) {
			meltTemp = Heat.maxVisibleTemperature();
			heatCapacity = 1.0F;
			if (fluidStack != null) {
				final Metal metal = FluidsTFC.getMetalFromFluid(fluidStack.getFluid());
				meltTemp = metal.getMeltTemp();
				heatCapacity = metal.getSpecificHeat();
			}

		}
	}
}