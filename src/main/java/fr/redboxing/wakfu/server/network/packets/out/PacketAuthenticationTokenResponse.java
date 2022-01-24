package fr.redboxing.wakfu.server.network.packets.out;

import fr.redboxing.wakfu.server.models.LoginResponseCode;
import fr.redboxing.wakfu.server.network.packets.Opcodes;
import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.network.packets.OutgoingPacket;

public class PacketAuthenticationTokenResponse implements OutgoingPacket {
    String token;

    public PacketAuthenticationTokenResponse(String token) {
        this.token = token;
    }

    @Override
    public OutPacket encode() {
        OutPacket out = new OutPacket(Opcodes.AUTHENTICATION_TOKEN_RESPONSE);
        out.writeByte(LoginResponseCode.CORRECT_LOGIN.getCode()); //code
        out.writeLargeString(token);

        return out;
    }
}
