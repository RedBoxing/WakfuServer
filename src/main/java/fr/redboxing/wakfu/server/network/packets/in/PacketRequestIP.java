package fr.redboxing.wakfu.server.network.packets.in;

import fr.redboxing.wakfu.server.network.packets.IncomingPacket;
import fr.redboxing.wakfu.server.network.packets.out.PacketSetIP;
import fr.redboxing.wakfu.server.session.ClientSession;
import io.netty.buffer.ByteBuf;

public class PacketRequestIP implements IncomingPacket {
    @Override
    public void decode(ByteBuf packet, ClientSession session) {
        session.write(new PacketSetIP(session.getChannel().remoteAddress()));
    }
}
