package fr.redboxing.wakfu.server.network.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.FileOutputStream;
import java.util.HashMap;

public class OutPacket {
    private ByteBuf data;
    private int packetId;
    private HashMap<Integer, Integer> marks = new HashMap<Integer, Integer>();

    public OutPacket(int packetId) {
        this.packetId = packetId;

        data = Unpooled.buffer();
        data.writeShort(0); //Size placeholder
        data.writeShort(packetId);
    }

    public void finish() {
        data.setShort(0, data.writerIndex());
    }

    public ByteBuf getData() {
        return data;
    }

    public int getPacketId() {
        return packetId;
    }

    public OutPacket writeByte(int b) {
        data.writeByte(b);
        return this;
    }

    public OutPacket writeShort(int s) {
        data.writeShort(s);
        return this;
    }

    public OutPacket writeInt(int i) {
        data.writeInt(i);
        return this;
    }

    public OutPacket writeLong(long l) {
        data.writeLong(l);
        return this;
    }

    public OutPacket writeBoolean(boolean b) {
        data.writeBoolean(b);
        return this;
    }

    public OutPacket writeBytes(byte[] b) {
        data.writeBytes(b);
        return this;
    }

    public OutPacket writeString(String s) {
        data.writeByte(s.length());
        data.writeBytes(s.getBytes());
        return this;
    }

    public OutPacket writeBigString(String s) {
        data.writeShort(s.length());
        data.writeBytes(s.getBytes());
        return this;
    }

    public OutPacket writeLargeString(String s) {
        data.writeInt(s.length());
        data.writeBytes(s.getBytes());
        return this;
    }

    public void markShort(int index) {
        writeShort(0);
        marks.put(index, data.writerIndex());
    }

    public void markByte(int index) {
        writeByte(0);
        marks.put(index, data.writerIndex());
    }

    public void markInt(int index) {
        writeInt(0);
        marks.put(index, data.writerIndex());
    }

    public void endMarkShort(int index) {
        data.setShort(marks.get(index) - 2, data.writerIndex() - marks.get(index));
    }

    public void endMarkShort(int index, int add) {
        data.setShort(marks.get(index) - 2, data.writerIndex() - marks.get(index) + add);
    }

    public void endMarkByte(int index) {
        data.setByte(marks.get(index) - 1, data.writerIndex() - marks.get(index));
    }

    public void endMarkByte(int index, int add) {
        data.setByte(marks.get(index) - 1, data.writerIndex() - marks.get(index) + add);
    }

    public void endMarkInt(int index) {
        data.setInt(marks.get(index) - 4, data.writerIndex() - marks.get(index));
    }

    public void endMarkShortAbsolute(int index) {
        data.setShort(marks.get(index) - 2, data.writerIndex());
    }

    public void endMarkByteAbsolute(int index) {
        data.setByte(marks.get(index) - 1, data.writerIndex());
    }

    public void endMarkIntAbsolute(int index) {
        data.setInt(marks.get(index) - 4, data.writerIndex());
    }

    public void endMarkShortAbsolute(int index, int add) {
        data.setShort(marks.get(index) - 2, data.writerIndex() + add);
    }

    public void endMarkByteAbsolute(int index, int add) {
        data.setByte(marks.get(index) - 1, data.writerIndex() + add);
    }

    public void endMarkIntAbsolute(int index, int add) {
        data.setInt(marks.get(index) - 4, data.writerIndex() + add);
    }

    public void printBuffer() {
        printBuffer(false);
    }

    public void printBuffer(boolean hex) {
        byte[] buf = new byte[data.writerIndex()];
        data.getBytes(0, buf);

        System.out.print("[");
        for (int i=0; i<buf.length; i++)
            System.out.print((hex ? "0x" + Integer.toHexString(buf[i] & 0xFF).toUpperCase() : String.valueOf(buf[i])) + (i == buf.length-1 ? "" : ", "));
        System.out.println("]");
    }

    public void dumpBuffer(String file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[data.writerIndex()];
            data.getBytes(0, buf);
            fos.write(buf);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
