package io.nuls.sdk.accountledger.model;

import java.util.List;

/**
 * @author davi
 */
public class MSAccount {
    private Integer threshold;
    private List<String> pubKeys;

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public List<String> getPubKeys() {
        return pubKeys;
    }

    public void setPubKeys(List<String> pubKeys) {
        this.pubKeys = pubKeys;
    }
}
