package fr.redboxing.wakfu.server.network.packets.in;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.models.LoginResponseCode;
import fr.redboxing.wakfu.server.models.account.AccountInformations;
import fr.redboxing.wakfu.server.network.packets.IncomingPacket;
import fr.redboxing.wakfu.server.network.packets.out.*;
import fr.redboxing.wakfu.server.session.ClientSession;
import io.netty.buffer.ByteBuf;


public class PacketAuthenticationTokenRedeem implements IncomingPacket {
    @Override
    public void decode(ByteBuf packet, ClientSession session) {
        byte[] data = new byte[packet.readInt()];
        packet.readBytes(data);
        String ticket = new String(data);


        session.setAccountInformations(AccountInformations.loadFromSessionID(ticket));

        WakfuServer.getInstance().getLogger().info("Ticket was redeemed: {}.", ticket);

        session.write(new PacketSetIP(session.getChannel().remoteAddress()));
        session.write(new PacketAuthenticationResponse(LoginResponseCode.CORRECT_LOGIN, session.getAccountInformations()));
        session.write(new PacketWorldSelectionResponse(true));
        session.write(new PacketServerTime());
        session.write(new PacketCharactersList());
    }
}
