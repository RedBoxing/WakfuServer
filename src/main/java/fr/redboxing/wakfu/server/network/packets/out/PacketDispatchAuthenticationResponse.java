package fr.redboxing.wakfu.server.network.packets.out;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.models.LoginResponseCode;
import fr.redboxing.wakfu.server.models.account.AccountInformations;
import fr.redboxing.wakfu.server.network.packets.Opcodes;
import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.network.packets.OutgoingPacket;

public class PacketDispatchAuthenticationResponse implements OutgoingPacket {
    private LoginResponseCode code;
    private AccountInformations accountInformations;

    public PacketDispatchAuthenticationResponse(LoginResponseCode code, AccountInformations accountInformations) {
        this.code = code;
        this.accountInformations = accountInformations;
    }

    @Override
    public OutPacket encode() {
        OutPacket out = new OutPacket(Opcodes.DISPATCH_AUTHENTICATION_RESPONSE);

        /*
        {
            code: CORRECT_LOGIN,
            accountInformations: {
                m_account_id=675255875781709713,
                m_community=FR,
                m_adminInformation=Optional.absent(),
                m_accountNickName=RedBoxingg
            }
        }
         */


        out.writeByte(code.getCode());
        out.writeBoolean(code == LoginResponseCode.CORRECT_LOGIN); //success

        if (code == LoginResponseCode.CORRECT_LOGIN) {
            out.writeLong(accountInformations.getAccountId()); //account id
            out.writeInt(accountInformations.getAccountCommunity().getId()); // Community
            out.writeBigString(accountInformations.getAccountNickName());
            out.writeBoolean(false); // Has admin info
        }

        return out;
    }
}
