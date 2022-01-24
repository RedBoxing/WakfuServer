package fr.redboxing.wakfu.server.network.packets.in;

import fr.redboxing.wakfu.server.Constant;
import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.crypto.RSACertificateManager;
import fr.redboxing.wakfu.server.models.LoginResponseCode;
import fr.redboxing.wakfu.server.models.Player;
import fr.redboxing.wakfu.server.models.account.AccountInformations;
import fr.redboxing.wakfu.server.models.account.Community;
import fr.redboxing.wakfu.server.network.packets.IncomingPacket;
import fr.redboxing.wakfu.server.network.packets.out.PacketDispatchAuthenticationResponse;
import fr.redboxing.wakfu.server.session.ClientSession;
import fr.redboxing.wakfu.server.utils.DataUtils;
import io.netty.buffer.ByteBuf;

public class PacketAuthentication implements IncomingPacket {
    @Override
    public void decode(ByteBuf packet, ClientSession session) {
        byte[] b = new byte[packet.readInt()];
        packet.readBytes(b);

        byte[] decoded = RSACertificateManager.INSTANCE.decode(b);
        ByteBuf decbuffer = DataUtils.bufferFromBytes(decoded);

        long rsaVerification = decbuffer.readLong();
        String username = DataUtils.readString(decbuffer);
        String password = DataUtils.readString(decbuffer);

        if (rsaVerification != Constant.RSA_VERIFICATION_LONG) {
            WakfuServer.getInstance().getLogger().error("Error decoding RSA data: invalid verification long!");
            return;
        }

        WakfuServer.getInstance().getLogger().info("Login packet: { username: " + username + ", password:" + password + " }");

        AccountInformations accountInformations = AccountInformations.load(username);

        if(accountInformations != null) {
            if (accountInformations.getAccountPassword().equals(password)) {
                session.setAccountInformations(accountInformations);
                session.write(new PacketDispatchAuthenticationResponse(LoginResponseCode.CORRECT_LOGIN, session.getAccountInformations()));

                WakfuServer.getInstance().getLogger().info(session.getAccountInformations());

            } else {
                session.write(new PacketDispatchAuthenticationResponse(LoginResponseCode.INVALID_LOGIN, null));
            }
        }else {
            session.write(new PacketDispatchAuthenticationResponse(LoginResponseCode.INVALID_LOGIN, null));
        }
    }
}
