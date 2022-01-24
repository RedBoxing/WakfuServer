package fr.redboxing.wakfu.server.session;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.models.account.AccountInformations;
import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.network.packets.OutgoingPacket;
import io.netty.channel.Channel;

public class ClientSession {
    private Channel channel;
    private AccountInformations accountInformations;

    public ClientSession(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public AccountInformations getAccountInformations() {
        return accountInformations;
    }

    public void setAccountInformations(AccountInformations accountInformations) {
        this.accountInformations = accountInformations;
    }

    public void write(OutgoingPacket data) {
        WakfuServer.getInstance().getLogger().info("[SERVER] Outgoing packet: { name: " + data.getClass().getSimpleName() + " }");
        OutPacket out = data.encode();
        channel.writeAndFlush(out);
    }
}
