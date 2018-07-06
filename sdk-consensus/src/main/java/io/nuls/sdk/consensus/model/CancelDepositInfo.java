package io.nuls.sdk.consensus.model;

public class CancelDepositInfo {

    private String address;

    private String joinTxHash;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJoinTxHash() {
        return joinTxHash;
    }

    public void setJoinTxHash(String joinTxHash) {
        this.joinTxHash = joinTxHash;
    }
}
