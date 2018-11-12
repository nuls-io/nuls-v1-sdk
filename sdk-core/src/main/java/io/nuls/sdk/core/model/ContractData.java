package io.nuls.sdk.core.model;

/**
 * Created by wangkun23 on 2018/10/8.
 */
public interface ContractData {

    /**
     * get gasLimit
     *
     * @return
     */
    long getGasLimit();

    /**
     * get create account address
     *
     * @return
     */
    byte[] getSender();

    /**
     * get contract address
     *
     * @return
     */
    byte[] getContractAddress();

    /**
     * get contract price
     *
     * @return
     */
    long getPrice();

    /**
     * deposit
     *
     * @return
     */
    long getValue();
}
