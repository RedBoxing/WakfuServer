package fr.redboxing.wakfu.server.network.packets.out;

import fr.redboxing.wakfu.server.network.packets.Opcodes;
import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.network.packets.OutgoingPacket;

public class PacketServerTime implements OutgoingPacket {
    @Override
    public OutPacket encode() {
        OutPacket out = new OutPacket(Opcodes.SERVER_TIME);
        out.writeLong(System.currentTimeMillis());
        return out;
    }
}
