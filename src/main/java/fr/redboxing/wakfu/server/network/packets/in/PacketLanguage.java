package fr.redboxing.wakfu.server.network.packets.in;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.models.account.Community;
import fr.redboxing.wakfu.server.network.packets.IncomingPacket;
import fr.redboxing.wakfu.server.session.ClientSession;
import fr.redboxing.wakfu.server.utils.DataUtils;
import io.netty.buffer.ByteBuf;

import java.sql.PreparedStatement;

public class PacketLanguage implements IncomingPacket {
    @Override
    public void decode(ByteBuf packet, ClientSession session) {
        String lang = DataUtils.readLargeString(packet);
        Community community = Community.getFromName(lang);

        try {
            PreparedStatement preparedStatement = WakfuServer.getInstance().getMySQLHelper().getConn().prepareStatement("UPDATE users SET community = ? WHERE id = ?");
            preparedStatement.setInt(1, community.getId());
            preparedStatement.setLong(2, session.getAccountInformations().getAccountId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
