package fr.redboxing.wakfu.server.network;

import fr.redboxing.wakfu.server.network.codec.PacketDecoder;
import fr.redboxing.wakfu.server.network.codec.PacketEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pl = ch.pipeline();

        pl.addLast("decoder", new PacketDecoder());
        pl.addLast("encoder", new PacketEncoder());
        pl.addLast("handler", new ServerHandler());
    }
}
