package fr.redboxing.wakfu.server.network.packets.in;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.network.packets.IncomingPacket;
import fr.redboxing.wakfu.server.network.packets.out.PacketVersionResult;
import fr.redboxing.wakfu.server.session.ClientSession;
import fr.redboxing.wakfu.server.utils.DataUtils;
import io.netty.buffer.ByteBuf;

public class PacketVersion implements IncomingPacket {
    int major;
    int minor;
    int patch;
    String buildVersion;

    @Override
    public void decode(ByteBuf packet, ClientSession session) {
        major = packet.readUnsignedByte();
        minor = packet.readUnsignedShort();
        patch = packet.readUnsignedByte();
        buildVersion = DataUtils.readString(packet);

        WakfuServer.getInstance().getLogger().info("Received version packet: {build={}.{}.{} ({})}", new Object[]{major, minor, patch, buildVersion});
        session.write(new PacketVersionResult(major, minor, patch, buildVersion));
    }
}
