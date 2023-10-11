package com.buuz135.hotornot.network;

import com.buuz135.hotornot.HotOrNot;
import com.buuz135.hotornot.config.HotConfig;
import com.buuz135.hotornot.config.HotLists;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncClientConfig implements IMessage {

	@Override
	public void fromBytes(final ByteBuf buf) {
		HotLists.readListsFromByteBuf(buf);
		// This clobbers the client config but eh oh well it's not that big a deal
		HotConfig.TEMPERATURE_VALUES.hotItemTemp = buf.readInt();
		HotConfig.TEMPERATURE_VALUES.hotFluidTemp = buf.readInt();
		HotConfig.TEMPERATURE_VALUES.coldFluidTemp = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		HotLists.writeListsToByteBuf(buf);
		buf.writeInt(HotConfig.TEMPERATURE_VALUES.hotItemTemp);
		buf.writeInt(HotConfig.TEMPERATURE_VALUES.hotFluidTemp);
		buf.writeInt(HotConfig.TEMPERATURE_VALUES.coldFluidTemp);
	}

	public static class Handler implements IMessageHandler<SyncClientConfig, IMessage> {

		@Override
		public IMessage onMessage(final SyncClientConfig message, final MessageContext ctx) {
			HotOrNot.getLog().info("Synced lists with server");
			return null;
		}
	}
}
