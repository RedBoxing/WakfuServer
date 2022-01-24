package fr.redboxing.wakfu.server.network.packets.out;

import fr.redboxing.wakfu.server.Constant;
import fr.redboxing.wakfu.server.crypto.RSACertificateManager;
import fr.redboxing.wakfu.server.network.packets.Opcodes;
import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.network.packets.OutgoingPacket;

public class PacketRSA implements OutgoingPacket {
    @Override
    public OutPacket encode() {
        OutPacket out = new OutPacket(Opcodes.RSA_RESULT);

        out.writeLong(Constant.RSA_VERIFICATION_LONG);
        out.writeBytes(RSACertificateManager.INSTANCE.getPublicKey());

        return out;
    }
}
