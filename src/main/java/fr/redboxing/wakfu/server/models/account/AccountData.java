package fr.redboxing.wakfu.server.models.account;

import fr.redboxing.wakfu.server.network.packets.OutPacket;
import fr.redboxing.wakfu.server.utils.ByteArray;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class AccountData {
    private final EnumMap<AccountDataFlagType, AccountDataFlag> m_flags;

    public AccountData() {
        super();
        this.m_flags = new EnumMap<AccountDataFlagType, AccountDataFlag>(AccountDataFlagType.class);
    }

    public void addFlag(final AccountDataFlag flag) {
        this.m_flags.put(flag.getType(), flag);
    }

    public boolean hasFlag(final AccountDataFlagType flagType) {
        return this.m_flags.containsKey(flagType);
    }

    public AccountDataFlag getFlag(final AccountDataFlagType flagType) {
        return this.m_flags.get(flagType);
    }

    public int serializedSize() {
        return 2 + this.m_flags.size() * AccountDataFlag.serializedSize();
    }

    public void serialize(final ByteBuffer buffer) {
        buffer.putShort((short)this.m_flags.size());
        for (final Map.Entry<AccountDataFlagType, AccountDataFlag> flag : this.m_flags.entrySet()) {
            buffer.put(flag.getKey().getId());
            buffer.putLong(flag.getValue().getValue());
        }
    }

    public byte[] serialize() {
        ByteArray buffer = new ByteArray();
        buffer.putShort((short)this.m_flags.size());
        for (final Map.Entry<AccountDataFlagType, AccountDataFlag> flag : this.m_flags.entrySet()) {
            buffer.put(flag.getKey().getId());
            buffer.putLong(flag.getValue().getValue());
        }
        return buffer.toArray();
    }

    public void unserialize(final ByteBuffer buffer) {
        final short flagCount = buffer.getShort();
        for (int i = 0; i < flagCount; ++i) {
            final byte flagId = buffer.get();
            final long flagValue = buffer.getLong();
            final AccountDataFlagType flagType = AccountDataFlagType.fromId(flagId);
            if (flagType != null) {
                this.m_flags.put(flagType, new AccountDataFlag(flagType, flagValue));
            }
        }
    }

    public static class AccountDataFlag {
        private final AccountDataFlagType m_type;
        private final long m_value;

        public AccountDataFlag(final AccountDataFlagType type, final long value) {
            super();
            this.m_type = type;
            this.m_value = value;
        }

        public AccountDataFlagType getType() {
            return this.m_type;
        }

        public long getValue() {
            return this.m_value;
        }

        public static int serializedSize() {
            return 9;
        }
    }

    public enum AccountDataFlagType
    {
        STEAMER_BETA((byte)1, "WKSTEAMER"),
        ANTI_ADDICTION((byte)2, "ANTIADDICT"),
        CHARACTER_SLOTS((byte)3, "WKCHARACTERS"),
        VAULT_UPGRADE((byte)4, "WKVAULTUP");

        private static final HashMap<Byte, AccountDataFlagType> m_flagsById = new HashMap<>();
        private static final HashMap<String, AccountDataFlagType> m_flagsByWebRepresentation = new HashMap<String, AccountDataFlagType>();
        private final byte m_id;
        private final String m_webRepresentation;

        private AccountDataFlagType(final byte id, final String webRepresentation) {
            this.m_id = id;
            this.m_webRepresentation = webRepresentation;
        }

        public byte getId() {
            return this.m_id;
        }

        public String getWebRepresentation() {
            return this.m_webRepresentation;
        }

        public static AccountDataFlagType fromId(final byte id) {
            return AccountDataFlagType.m_flagsById.get(id);
        }

        public static AccountDataFlagType fromWebRepresentation(final String webRepresentation) {
            return AccountDataFlagType.m_flagsByWebRepresentation.get(webRepresentation);
        }

        static {
            for (final AccountDataFlagType flag : values()) {
                AccountDataFlagType.m_flagsById.put(flag.getId(), flag);
                AccountDataFlagType.m_flagsByWebRepresentation.put(flag.getWebRepresentation(), flag);
            }
        }
    }
}
