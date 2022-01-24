package fr.redboxing.wakfu.server.models;

import fr.redboxing.wakfu.server.Constant;
import fr.redboxing.wakfu.server.models.account.Community;
import fr.redboxing.wakfu.server.utils.ByteArray;
import fr.redboxing.wakfu.server.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class Server {
    private int id;
    private String name;
    private Community community;
    private String address;
    public int port;
    private int order;
    private boolean locked;
    private Map<PropertiesType, String> properties;

    public Server(int id, String name, Community community, String address, int port, int order) {
        this.id = id;
        this.name = name;
        this.community = community;
        this.address = address;
        this.port = port;
        this.order = order;

        this.locked = false;
        this.properties = new HashMap<>();

        properties.put(PropertiesType.SERVER_ID, String.valueOf(id));
    }

    public byte[] serializeProxy() {
        ByteArray bb = new ByteArray();

        bb.putInt(this.id);
        final byte[] utfName = Utils.toUTF8(this.name);
        bb.putInt(utfName.length);
        bb.put(utfName);
        bb.putInt(this.community.getId());
        final byte[] utfAddress = Utils.toUTF8(this.address);
        bb.putInt(utfAddress.length);
        bb.put(utfAddress);
        bb.putInt(1);
        bb.putInt(this.port);
        bb.put((byte)this.order);

        return bb.toArray();
    }

    public byte[] serializeWorld() {
        ByteArray bb = new ByteArray();

        bb.putInt(this.id);

        ByteArray bb2 = new ByteArray();
        bb2.put((byte) Constant.major);
        bb2.putShort((short) Constant.minor);
        bb2.put((byte) Constant.patch);
        final byte[] utfBuild = Utils.toUTF8(Constant.build);
        bb2.putInt(utfBuild.length);
        bb2.put(utfBuild);

        bb.putInt(bb2.size());
        bb.put(bb2);

        ByteArray propertiesArray = new ByteArray();
        propertiesArray.putInt(properties.size());
        for(Map.Entry<PropertiesType, String> entry : properties.entrySet()) {
            propertiesArray.putShort(entry.getKey().getId());

            final byte[] utfValue = Utils.toUTF8(entry.getValue());
            propertiesArray.putInt(utfValue.length);
            propertiesArray.put(utfValue);
        }

        bb.putInt(propertiesArray.size());
        bb.put(propertiesArray);

        bb.putBoolean(this.locked);

        return bb.toArray();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Community getCommunity() {
        return community;
    }

    public String getAddress() {
        return address;
    }

    public int getOrder() {
        return order;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Map<PropertiesType, String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", community=" + community +
                ", address='" + address + '\'' +
                ", port=" + port +
                ", order=" + order +
                ", locked=" + locked +
                ", properties=" + properties +
                '}';
    }

    public enum PropertiesType {
        SERVER_ID(420);

        short id;

        PropertiesType(int id) {
            this.id = (short) id;
        }

        public short getId() {
            return id;
        }
    }
}
