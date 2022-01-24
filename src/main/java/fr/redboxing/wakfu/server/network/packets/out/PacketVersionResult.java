package fr.redboxing.wakfu.server.network.packets.out;

import fr.redboxing.wakfu.server.Constant;
import fr.redboxing.wakfu.server.network.packets.Opcodes;
import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.network.packets.OutgoingPacket;

public class PacketVersionResult implements OutgoingPacket {
    boolean accepted;

    public PacketVersionResult(int major, int minor, int patch, String buildVersion) {
        this.accepted = major == Constant.major && minor == Constant.minor && patch == Constant.patch && buildVersion.equals(Constant.build);
    }

    @Override
    public OutPacket encode() {
        OutPacket out = new OutPacket(Opcodes.VERSION_RESULT);
        out.writeBoolean(accepted);

        out.writeByte(Constant.major);
        out.writeShort(Constant.minor);
        out.writeByte(Constant.patch);
        out.writeString(Constant.build);

        return out;
    }
}
