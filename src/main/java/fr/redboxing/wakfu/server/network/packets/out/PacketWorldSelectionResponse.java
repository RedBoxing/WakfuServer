package fr.redboxing.wakfu.server.network.packets.out;

import fr.redboxing.wakfu.server.network.packets.Opcodes;
import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.network.packets.OutgoingPacket;

public class PacketWorldSelectionResponse implements OutgoingPacket {
    private boolean success;

    public PacketWorldSelectionResponse(boolean success) {
        this.success = success;
    }

    @Override
    public OutPacket encode() {
        OutPacket out = new OutPacket(Opcodes.WORLD_SELECTION_RESPONSE);
        out.writeBoolean(success);
        return out;
    }
}
