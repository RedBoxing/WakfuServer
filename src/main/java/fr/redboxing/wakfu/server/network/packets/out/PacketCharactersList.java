package fr.redboxing.wakfu.server.network.packets.out;

import fr.redboxing.wakfu.server.network.packets.Opcodes;
import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.network.packets.OutgoingPacket;

public class PacketCharactersList implements OutgoingPacket {
    @Override
    public OutPacket encode() {
        OutPacket out = new OutPacket(Opcodes.CHARACTERS_LIST);
        out.writeByte(0); // number of characters

        /*for each characters
            out.writeShort(); size of character bytes
            out.writesBytes(character);
         */
        return out;
    }
}
