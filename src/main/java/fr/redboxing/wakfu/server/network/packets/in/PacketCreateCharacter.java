package fr.redboxing.wakfu.server.network.packets.in;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.models.Breed;
import fr.redboxing.wakfu.server.network.packets.IncomingPacket;
import fr.redboxing.wakfu.server.session.ClientSession;
import fr.redboxing.wakfu.server.utils.DataUtils;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class PacketCreateCharacter implements IncomingPacket {
    @Override
    public void decode(ByteBuf packet, ClientSession session) {
        long character_id = packet.readLong();
        short breed_id = packet.readShort();
        Breed breed = Breed.getBreedFromId(breed_id);
        String character_name = DataUtils.readString(packet);
        boolean unknown = packet.readBoolean();
        byte[] bytes = new byte[packet.readShort()];
        packet.readBytes(bytes);

        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        WakfuServer.getInstance().getLogger().info(buffer.get()); //x17
        WakfuServer.getInstance().getLogger().info(buffer.get());
        WakfuServer.getInstance().getLogger().info(buffer.get());
        WakfuServer.getInstance().getLogger().info(buffer.get());

        WakfuServer.getInstance().getLogger().info("CreateCharacter: { id: " + character_id + ", breed_id: " + breed_id + ", breed: " + breed + ", name: " + character_name + ", unknown: " + unknown + " }");
        WakfuServer.getInstance().getLogger().info("CreateCharacter: " + Arrays.toString(bytes));
    }
}
