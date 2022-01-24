package fr.redboxing.wakfu.server.models.account;

import fr.redboxing.wakfu.server.WakfuServer;
import fr.redboxing.wakfu.server.models.account.admin.AdminRightHelper;
import fr.redboxing.wakfu.server.utils.Utils;
import fr.redboxing.wakfu.server.utils.serialization.BinarSerial;
import fr.redboxing.wakfu.server.utils.serialization.BinarSerialPart;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountInformations extends BinarSerial {
    private long accountId;
    private String accountPassword;
    private String accountNickName;
    private Community accountCommunity;
    private int subscriptionLevel;
    private int antiAddictionLevel;
    private byte additionalSlots;
    private byte vaultUpgrades;
    private long accountExpirationDate;
    private long banEndDate;
    private byte[][] accountConnectedIp;
    private long[] accountConnectedDate;
    private long accountCreationDate;
    private List<SubscriptionPeriod> subscriptionPeriods;
    private long baseTimeElapsed;
    private long sessionStartTime;
    private AccountData accountData;
    private int[] adminRights;
    protected final BinarSerialPart PUBLIC_INFORMATIONS;

    public AccountInformations() {
        super();
        this.accountConnectedIp = new byte[3][];
        this.accountConnectedDate = new long[3];
        this.adminRights = new int[AdminRightHelper.getMaskArraySize()];
        this.accountData = new AccountData();
        this.sessionStartTime = System.currentTimeMillis();
        this.subscriptionPeriods = new ArrayList<>();

        this.PUBLIC_INFORMATIONS = new BinarSerialPart() {
            @Override
            public void serialize(ByteBuffer buffer) {
                buffer.putLong(AccountInformations.this.accountId);
                buffer.putInt(AccountInformations.this.subscriptionLevel);
                buffer.putInt(AccountInformations.this.antiAddictionLevel);

                buffer.put(AccountInformations.this.additionalSlots);

                buffer.putLong(AccountInformations.this.accountExpirationDate);
                for (final int right : AccountInformations.this.adminRights) {
                    buffer.putInt(right);
                }
                final byte[] nickName = Utils.toUTF8(AccountInformations.this.accountNickName);
                buffer.put((byte)nickName.length);
                buffer.put(nickName);
                buffer.putInt(AccountInformations.this.accountCommunity.getId());

                buffer.putInt(AccountInformations.this.subscriptionPeriods.size());
                for(SubscriptionPeriod subscriptionPeriod : AccountInformations.this.subscriptionPeriods) {
                    buffer.putInt(subscriptionPeriod.getSubscriptionLevel());
                    buffer.putLong(subscriptionPeriod.getStartDate().getTime());
                    buffer.putLong(subscriptionPeriod.getEndDate().getTime());
                }
                buffer.putLong(AccountInformations.this.accountConnectedDate[1]);

                AccountInformations.this.accountData.serialize(buffer);
            }

            @Override
            public void unserialize(ByteBuffer buffer, int p1) {
                AccountInformations.this.accountId = buffer.getLong();
                AccountInformations.this.subscriptionLevel = buffer.getInt();
                AccountInformations.this.antiAddictionLevel = buffer.getInt();

                AccountInformations.this.additionalSlots = buffer.get();

                AccountInformations.this.accountExpirationDate = buffer.getLong();
                for (int i = 0; i < AdminRightHelper.getMaskArraySize(); ++i) {
                    AccountInformations.this.adminRights[i] = buffer.getInt();
                }
                final byte[] nickName = new byte[buffer.get() & 0xFF];
                buffer.get(nickName);
                AccountInformations.this.accountNickName = Utils.fromUTF8(nickName);
                AccountInformations.this.accountCommunity = Community.getFromId(buffer.getInt());

                int s = buffer.getInt();
                for(int i = 0; i < s; i++) {
                    AccountInformations.this.subscriptionPeriods.add(new SubscriptionPeriod(buffer.getInt(), new Timestamp(buffer.getLong()), new Timestamp(buffer.getLong())));
                }

                AccountInformations.this.accountData.unserialize(buffer);
            }

            @Override
            public int expectedSize() {
                int size = 0;
                size += 25 + 4 * AdminRightHelper.getMaskArraySize();
                final byte[] nickName = Utils.toUTF8(AccountInformations.this.accountNickName);
                size += 1 + nickName.length;
                size += 4;
                size += 4;
                size += AccountInformations.this.subscriptionPeriods.size() * 20;
                size += 8;
                size += AccountInformations.this.accountData.serializedSize();
                return size;
            }
        };
    }

    public static AccountInformations load(String username) {
        try {
            ResultSet rs = WakfuServer.getInstance().getMySQLHelper().execute("SELECT * FROM users WHERE name = '" + username + "'");
            while (rs.next()) {
                AccountInformations accountInformations = new AccountInformations();
                accountInformations.setAccountId(rs.getLong("id"));
                accountInformations.setAccountNickName(rs.getString("name"));
                accountInformations.setAccountPassword(rs.getString("password"));
                accountInformations.setAccountCommunity(Community.getFromId(rs.getInt("community")));
                accountInformations.setSubscriptionLevel(rs.getInt("subscription_level"));
                accountInformations.setAntiAddictionLevel(rs.getInt("anti_addiction_level"));
                accountInformations.setAccountExpirationDate(rs.getDate("account_expiration_date").getTime());

                return accountInformations;
            }

            rs.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static AccountInformations loadFromSessionID(String sessionID) {
        try {
            ResultSet rs = WakfuServer.getInstance().getMySQLHelper().execute("SELECT * FROM users WHERE session_id = '" + sessionID + "'");
            while (rs.next()) {
                AccountInformations accountInformations = new AccountInformations();
                accountInformations.setAccountId(rs.getLong("id"));
                accountInformations.setAccountNickName(rs.getString("name"));
                accountInformations.setAccountPassword(rs.getString("password"));
                accountInformations.setAccountCommunity(Community.getFromId(rs.getInt("community")));
                accountInformations.setSubscriptionLevel(rs.getInt("subscription_level"));
                accountInformations.setAntiAddictionLevel(rs.getInt("anti_addiction_level"));
                accountInformations.setAccountExpirationDate(rs.getDate("account_expiration_date").getTime());

                return accountInformations;
            }

            rs.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
    public byte[] serialize() {
        return this.build(this.PUBLIC_INFORMATIONS);
    }

    public final void setAccountConnectedInfo(final int i, final byte[] ip, final long date) {
        if (i < 0 || i >= 3) {
            throw new IllegalArgumentException("On ne conserve que 3 derni\u00e8res IP utilis\u00e9es");
        }
        this.accountConnectedIp[i] = ip;
        this.accountConnectedDate[i] = date;
    }

    public byte[] getAccountConnectedIp(final int i) {
        if (i < 0 || i >= 3) {
            throw new IllegalArgumentException("On ne conserve que 3 derni\u00e8res IP utilis\u00e9es");
        }
        return this.accountConnectedIp[i];
    }

    public long getAccountConnectedDate(final int i) {
        if (i < 0 || i >= 3) {
            throw new IllegalArgumentException("On ne conserve que 3 derni\u00e8res IP utilis\u00e9es");
        }
        return this.accountConnectedDate[i];
    }

    public void updateAccountConnectedInfo(final byte[] currentIp, final long currentDate) {
        if (currentIp[0] == this.accountConnectedIp[0][0] && currentIp[1] == this.accountConnectedIp[0][1] && currentIp[2] == this.accountConnectedIp[0][2] && currentIp[3] == this.accountConnectedIp[0][3]) {
            this.accountConnectedDate[0] = currentDate;
        }
        else {
            this.accountConnectedIp[2] = this.accountConnectedIp[1];
            this.accountConnectedIp[1] = this.accountConnectedIp[0];
            this.accountConnectedIp[0] = currentIp;
            this.accountConnectedDate[2] = this.accountConnectedDate[1];
            this.accountConnectedDate[1] = this.accountConnectedDate[0];
            this.accountConnectedDate[0] = currentDate;
        }
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getAccountNickName() {
        return accountNickName;
    }

    public void setAccountNickName(String accountNickName) {
        this.accountNickName = accountNickName;
    }

    public Community getAccountCommunity() {
        return accountCommunity;
    }

    public void setAccountCommunity(Community accountCommunity) {
        this.accountCommunity = accountCommunity;
    }

    public int getSubscriptionLevel() {
        return subscriptionLevel;
    }

    public void setSubscriptionLevel(int subscriptionLevel) {
        this.subscriptionLevel = subscriptionLevel;
    }

    public int getAntiAddictionLevel() {
        return antiAddictionLevel;
    }

    public void setAntiAddictionLevel(int antiAddictionLevel) {
        this.antiAddictionLevel = antiAddictionLevel;
    }

    public byte getAdditionalSlots() {
        return additionalSlots;
    }

    public void setAdditionalSlots(byte additionalSlots) {
        this.additionalSlots = additionalSlots;
    }

    public byte getVaultUpgrades() {
        return vaultUpgrades;
    }

    public void setVaultUpgrades(byte vaultUpgrades) {
        this.vaultUpgrades = vaultUpgrades;
    }

    public long getAccountExpirationDate() {
        return accountExpirationDate;
    }

    public void setAccountExpirationDate(long accountExpirationDate) {
        this.accountExpirationDate = accountExpirationDate;
    }

    public List<SubscriptionPeriod> getSubscriptionPeriods() {
        return subscriptionPeriods;
    }

    public void setSubscriptionPeriods(List<SubscriptionPeriod> subscriptionPeriods) {
        this.subscriptionPeriods = subscriptionPeriods;
    }

    public long getBanEndDate() {
        return banEndDate;
    }

    public void setBanEndDate(long banEndDate) {
        this.banEndDate = banEndDate;
    }

    public byte[][] getAccountConnectedIp() {
        return accountConnectedIp;
    }

    public void setAccountConnectedIp(byte[][] accountConnectedIp) {
        this.accountConnectedIp = accountConnectedIp;
    }

    public long[] getAccountConnectedDate() {
        return accountConnectedDate;
    }

    public void setAccountConnectedDate(long[] accountConnectedDate) {
        this.accountConnectedDate = accountConnectedDate;
    }

    public long getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(long accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    public long getBaseTimeElapsed() {
        return baseTimeElapsed;
    }

    public void setBaseTimeElapsed(long baseTimeElapsed) {
        this.baseTimeElapsed = baseTimeElapsed;
    }

    public long getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public AccountData getAccountData() {
        return accountData;
    }

    public void setAccountData(AccountData accountData) {
        this.accountData = accountData;
    }

    public int[] getAdminRights() {
        return adminRights;
    }

    public void setAdminRights(int[] adminRights) {
        this.adminRights = adminRights;
    }

    @Override
    public String toString() {
        return "AccountInformations{" +
                "account_id=" + accountId +
                ", accountNickName='" + accountNickName + '\'' +
                ", accountCommunity=" + accountCommunity +
                ", subscriptionLevel=" + subscriptionLevel +
                ", antiAddictionLevel=" + antiAddictionLevel +
                ", additionalSlots=" + additionalSlots +
                ", vaultUpgrades=" + vaultUpgrades +
                ", accountExpirationDate=" + accountExpirationDate +
                ", banEndDate=" + banEndDate +
                ", accountConnectedIp=" + Arrays.toString(accountConnectedIp) +
                ", accountConnectedDate=" + Arrays.toString(accountConnectedDate) +
                ", accountCreationDate=" + accountCreationDate +
                ", baseTimeElapsed=" + baseTimeElapsed +
                ", sessionStartTime=" + sessionStartTime +
                ", accountData=" + accountData +
                ", adminRights=" + Arrays.toString(adminRights) +
                '}';
    }

    @Override
    public BinarSerialPart[] partsEnumeration() {
        return new BinarSerialPart[] { this.PUBLIC_INFORMATIONS, };
    }
}
