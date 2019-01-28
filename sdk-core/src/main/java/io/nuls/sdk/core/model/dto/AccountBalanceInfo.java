package io.nuls.sdk.core.model.dto;

/**
 * @author :vivi
 */
public class AccountBalanceInfo {

    /**账户地址*/
    private String address;
    /**当前节点已同步到的高度*/
    private long synBlockHeight;
    /**当前网络最新高度*/
    private long netBlockHeight;
    /**所有余额*/
    private long nuls;
    /**锁定余额*/
    private long locked;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getSynBlockHeight() {
        return synBlockHeight;
    }

    public void setSynBlockHeight(long synBlockHeight) {
        this.synBlockHeight = synBlockHeight;
    }

    public long getNetBlockHeight() {
        return netBlockHeight;
    }

    public void setNetBlockHeight(long netBlockHeight) {
        this.netBlockHeight = netBlockHeight;
    }

    public long getNuls() {
        return nuls;
    }

    public void setNuls(long nuls) {
        this.nuls = nuls;
    }

    public long getLocked() {
        return locked;
    }

    public void setLocked(long locked) {
        this.locked = locked;
    }
}
