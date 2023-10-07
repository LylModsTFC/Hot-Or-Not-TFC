package com.buuz135.hotornot.network;

import com.buuz135.hotornot.HotOrNot;
import com.buuz135.hotornot.config.HotLists;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncClientLists implements IMessage {

	@Override
	public void fromBytes(final ByteBuf buf) {
		HotLists.readListsFromByteBuf(buf);
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		HotLists.writeListsToByteBuf(buf);
	}

	public static class Handler implements IMessageHandler<SyncClientLists, IMessage> {

		@Override
		public IMessage onMessage(final SyncClientLists message, final MessageContext ctx) {
			HotOrNot.getLog().info("Synced lists with server");
			return null;
		}
	}
}
