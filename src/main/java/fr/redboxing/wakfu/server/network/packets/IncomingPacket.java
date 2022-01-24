package fr.redboxing.wakfu.server.network.packets;

import fr.redboxing.wakfu.server.session.ClientSession;
import io.netty.buffer.ByteBuf;

public interface IncomingPacket {

    public void decode(ByteBuf packet, ClientSession session);
}
