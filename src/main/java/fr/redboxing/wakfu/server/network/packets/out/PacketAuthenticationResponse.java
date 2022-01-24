package fr.redboxing.wakfu.server.network.packets.out;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.models.LoginResponseCode;
import fr.redboxing.wakfu.server.models.account.AccountInformations;
import fr.redboxing.wakfu.server.network.packets.Opcodes;
import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.network.packets.OutgoingPacket;

public class PacketAuthenticationResponse implements OutgoingPacket {
    LoginResponseCode code;
    AccountInformations accountInformations;

    public PacketAuthenticationResponse(LoginResponseCode code, AccountInformations accountInformations) {
        this.code = code;
        this.accountInformations = accountInformations;
    }

    @Override
    public OutPacket encode() {
        WakfuServer.getInstance().getLogger().info(accountInformations);

        OutPacket out = new OutPacket(Opcodes.AUTHENTICATION_RESPONSE);
        out.writeByte(code.getCode());

        if(code == LoginResponseCode.ACCOUNT_BANNED) {
            out.writeInt(50);
        }
        if(code == LoginResponseCode.CORRECT_LOGIN){
            final byte[] accountInfos = accountInformations.serialize();
            WakfuServer.getInstance().getLogger().info(accountInfos.length);

            out.writeShort(accountInfos.length);
            out.writeBytes(accountInfos);
        }

        return out;
    }
}
