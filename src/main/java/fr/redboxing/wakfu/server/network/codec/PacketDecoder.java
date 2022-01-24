package fr.redboxing.wakfu.server.network.codec;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.network.ServerHandler;
import fr.redboxing.wakfu.server.network.packets.IncomingPacket;
import fr.redboxing.wakfu.server.network.packets.Opcodes;
import fr.redboxing.wakfu.server.network.packets.in.*;
import fr.redboxing.wakfu.server.session.ClientSession;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static final HashMap<Integer, IncomingPacket> INCOMING_PACKET_MAP = new HashMap<>();

    public PacketDecoder() {
        initPacketList();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ClientSession session = ctx.channel().attr(ServerHandler.CLIENTSESS_ATTR).get();
        if (session != null && msg.readableBytes() > 0) {
            int size = msg.readUnsignedShort();
            int type = msg.readByte();
            int opcode = msg.readUnsignedShort();

            if(INCOMING_PACKET_MAP.containsKey(opcode)) {
                IncomingPacket packet = INCOMING_PACKET_MAP.get(opcode);
                WakfuServer.getInstance().getLogger().info("[CLIENT] Incoming packet: { name: " + packet.getClass().getSimpleName() + ", size: " + size + ", type: " + type + ", opcode: " + opcode + " }");
                packet.decode(msg, session);
            }else {
                WakfuServer.getInstance().getLogger().info("[CLIENT] Unknown packet incoming: { size: " + size + ", type: " + type + ", opcode: " + opcode + " }");
            }
        }
    }

    private static void initPacketList() {
        INCOMING_PACKET_MAP.put(114, new PacketRequestIP());
        INCOMING_PACKET_MAP.put(Opcodes.VERSION, new PacketVersion());
        INCOMING_PACKET_MAP.put(Opcodes.REQUEST_RSA, new PacketRequestRSA());
        INCOMING_PACKET_MAP.put(Opcodes.AUTHENTICATION, new PacketAuthentication());
        INCOMING_PACKET_MAP.put(Opcodes.PROXIES_REQUEST, new PacketProxiesRequest());
        INCOMING_PACKET_MAP.put(Opcodes.AUTHENTICATION_TOKEN_REQUEST, new PacketAuthenticationTokenRequest());
        INCOMING_PACKET_MAP.put(Opcodes.AUTHENTICATION_TOKEN_REDEEM, new PacketAuthenticationTokenRedeem());
        INCOMING_PACKET_MAP.put(Opcodes.LANGUAGE, new PacketLanguage());
        INCOMING_PACKET_MAP.put(Opcodes.CREATE_CHARACTER, new PacketCreateCharacter());
    }
}
