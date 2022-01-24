package fr.redboxing.wakfu.server.network.packets.out;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.models.Server;
import fr.redboxing.wakfu.server.models.account.Community;
import fr.redboxing.wakfu.server.network.packets.Opcodes;
import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.network.packets.OutgoingPacket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PacketProxiesResponse implements OutgoingPacket {
    ArrayList<Server> servers = new ArrayList<>();

    public PacketProxiesResponse() {
        servers.add(new Server(1, "URSS", Community.RU, "127.0.0.1", 5558, 0));

        ResultSet rs = WakfuServer.getInstance().getMySQLHelper().execute("SELECT * FROM worlds");

        try {
            while (rs.next())   {
                servers.add(new Server(rs.getInt("id"), rs.getString("name"), Community.getFromId(rs.getInt("community")), rs.getString("address"), rs.getInt("port"), rs.getInt("order")));
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OutPacket encode() {
        OutPacket out = new OutPacket(Opcodes.PROXIES_RESPONSE);

        try {
            out.writeInt(servers.size());
            for(Server server : servers) {
                out.writeBytes(server.serializeProxy());
            }

            out.writeInt(servers.size());
            for(Server server : servers) {
                out.writeBytes(server.serializeWorld());

                WakfuServer.getInstance().getLogger().info(server.toString());
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return out;
    }
}