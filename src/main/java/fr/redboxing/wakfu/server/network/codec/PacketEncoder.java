package fr.redboxing.wakfu.server.network.codec;

import fr.redboxing.wakfu.server.network.packets.OutPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<OutPacket> {
    public PacketEncoder() {
        super(OutPacket.class);
    }

    @Override
    public void encode(ChannelHandlerContext ctx, OutPacket msg, ByteBuf out) throws Exception {
        msg.finish();
        out.writeBytes(msg.getData());
        ctx.channel().flush();

        //WakfuProxy.getInstance().getForm().addPacketToTable(msg.getClass(), msg);
    }
}