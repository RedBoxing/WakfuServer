package fr.redboxing.wakfu.server.network;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.session.ClientSession;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.sql.PreparedStatement;

public class ServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final AttributeKey<ClientSession> CLIENTSESS_ATTR = AttributeKey.newInstance("ClientSession");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(CLIENTSESS_ATTR).setIfAbsent(new ClientSession(ctx.channel()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ClientSession sess = ctx.channel().attr(CLIENTSESS_ATTR).get();

     /*   if(sess.getAccountInformations().getAccountId() != 0) {
            try {
                PreparedStatement preparedStatement = WakfuServer.getInstance().getMySQLHelper().getConn().prepareStatement("UPDATE users SET session_id = ? WHERE id = ?");
                preparedStatement.setString(1,null);
                preparedStatement.setLong(2, sess.getAccountInformations().getAccountId());

                preparedStatement.executeUpdate();
                preparedStatement.close();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        ctx.channel().attr(CLIENTSESS_ATTR).set(null);*/
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

    }
}
