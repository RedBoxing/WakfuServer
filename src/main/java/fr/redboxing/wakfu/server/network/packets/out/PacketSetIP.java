package fr.redboxing.wakfu.server.network.packets.out;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.network.packets.Opcodes;
import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.network.packets.OutgoingPacket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class PacketSetIP implements OutgoingPacket {
    int ipaddress;

    public PacketSetIP(SocketAddress ip) {
        try {
            ipaddress = ((InetSocketAddress)ip).getAddress().hashCode();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public OutPacket encode() {
        try {
            WakfuServer.getInstance().getLogger().info(InetAddress.getByName(String.valueOf(ipaddress)).getHostAddress());
        }catch (IOException ex) {
            ex.printStackTrace();
        }


        OutPacket out = new OutPacket(Opcodes.SET_IP);
        out.writeInt(ipaddress);

        return out;
    }
}
