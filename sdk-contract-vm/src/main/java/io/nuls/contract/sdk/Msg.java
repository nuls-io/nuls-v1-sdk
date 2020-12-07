package io.nuls.contract.sdk;

import java.math.BigInteger;

public class Msg {

    /**
     * 剩余Gas
     * remaining gas
     *
     * @return 剩余gas
     */
    public static native long gasleft();

    /**
     * 合约发送者地址
     * sender of the contract
     *
     * @return 消息发送者地址
     */
    public static native Address sender();

    /**
     * 合约发送者地址公钥
     * sender public key of the contract
     *
     * @return 消息发送者地址公钥
     */
    public static native String senderPublicKey();

    /**
     * 合约发送者转入合约地址的NULS资产数量，单位是Na，1Nuls=1亿Na，等等……
     * The amount of assets transferred to the contract address by the contract sender, supports multi-asset transfer, such as NULS, the unit is Na, 1Nuls=1 billion Na, etc...
     *
     * @return
     */
    public static native BigInteger value();

    /**
     * 合约发送者转入合约地址的其他资产列表
     *
     */
    public static native MultyAssetValue[] multyAssetValues();

    /**
     * Gas价格
     * gas price
     *
     * @return Gas价格
     */
    public static native long gasprice();

    /**
     * 合约地址
     * contract address
     *
     * @return 合约地址
     */
    public static native Address address();

}
