package fr.redboxing.wakfu.server.network.packets.in;

import fr.redboxing.wakfu.server.network.packets.IncomingPacket;
import fr.redboxing.wakfu.server.network.packets.out.PacketProxiesResponse;
import fr.redboxing.wakfu.server.session.ClientSession;
import io.netty.buffer.ByteBuf;

public class PacketProxiesRequest implements IncomingPacket {
    @Override
    public void decode(ByteBuf packet, ClientSession session) {
        session.write(new PacketProxiesResponse());
    }
}
