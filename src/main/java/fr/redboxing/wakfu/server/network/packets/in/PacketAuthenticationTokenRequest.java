package fr.redboxing.wakfu.server.network.packets.in;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.network.packets.IncomingPacket;
import fr.redboxing.wakfu.server.network.packets.out.PacketAuthenticationTokenResponse;
import fr.redboxing.wakfu.server.session.ClientSession;
import io.netty.buffer.ByteBuf;

import java.sql.PreparedStatement;
import java.util.UUID;

public class PacketAuthenticationTokenRequest implements IncomingPacket {
    @Override
    public void decode(ByteBuf packet, ClientSession session) {
        int serverId = packet.readInt();
        long accountId = packet.readLong();
        UUID token = UUID.randomUUID();

        WakfuServer.getInstance().getLogger().info("ServerID: " + serverId);
        WakfuServer.getInstance().getLogger().info("AccountID: " + accountId);
        WakfuServer.getInstance().getLogger().info(session.getAccountInformations());

        try {
            PreparedStatement preparedStatement = WakfuServer.getInstance().getMySQLHelper().getConn().prepareStatement("UPDATE users SET session_id = ? WHERE id = ?");
            preparedStatement.setString(1, token.toString());
            preparedStatement.setLong(2, accountId == 0 ? session.getAccountInformations().getAccountId() : accountId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        session.write(new PacketAuthenticationTokenResponse(token.toString()));
    }
}
