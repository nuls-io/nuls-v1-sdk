package io.nuls.contract.sdk;

import java.math.BigInteger;

public class Address {

    private final String address;

    public Address(String address) {
        valid(address);
        this.address = address;
    }

    /**
     * 获取该地址的可用余额
     *
     * @return BigInteger
     */
    public native BigInteger balance();

    /**
     * 获取该地址的总余额
     *
     * @return BigInteger
     */
    public native BigInteger totalBalance();

    /**
     * 合约向该地址转账(NULS, 可锁定)
     *
     * @param value         转账金额（多少Na）
     * @param lockedTime    锁定时间
     */
    public native void transferLocked(BigInteger value, long lockedTime);

    /**
     * 合约向该地址转账(NULS)
     *
     * @param value
     */
    public void transfer(BigInteger value) {
        this.transferLocked(value, 0);
    }


    /**
     * 合约向该地址转账指定的资产(可锁定)
     *
     * @param value          转账金额
     * @param assetChainId   资产链ID
     * @param assetId        资产ID
     * @param lockedTime     锁定时间
     */
    public native void transferLocked(BigInteger value, int assetChainId, int assetId, long lockedTime);

    /**
     * 合约向该地址转账指定的资产
     *
     * @param value          转账金额
     * @param assetChainId   资产链ID
     * @param assetId        资产ID
     */
    public void transfer(BigInteger value, int assetChainId, int assetId) {
        this.transferLocked(value, assetChainId, assetId, 0);
    }

    /**
     * 获取该地址指定资产的可用余额
     *
     * @param assetChainId   资产链ID
     * @param assetId        资产ID
     * @return
     */
    public native BigInteger balance(int assetChainId, int assetId);

    /**
     * 获取该地址指定资产的总余额
     *
     * @param assetChainId   资产链ID
     * @param assetId        资产ID
     * @return
     */
    public native BigInteger totalBalance(int assetChainId, int assetId);

    /**
     * 调用该地址的合约方法
     *
     * @param methodName 方法名
     * @param methodDesc 方法签名
     * @param args       参数
     * @param value      附带的货币量（多少Na）
     */
    public native void call(String methodName, String methodDesc, String[][] args, BigInteger value);

    /**
     * 调用该地址的合约方法并带有返回值(String)
     *
     * @param methodName 方法名
     * @param methodDesc 方法签名
     * @param args       参数
     * @param value      附带的货币量（多少Na）
     * @return 调用合约后的返回值
     */
    public native String callWithReturnValue(String methodName, String methodDesc, String[][] args, BigInteger value);

    /**
     * 调用该地址的合约方法并带有返回值(String)
     *
     * @param methodName    方法名
     * @param methodDesc    方法签名
     * @param args          参数
     * @param value         转入资产数量
     * @param assetChainId  转入资产链ID
     * @param assetId       转入资产ID
     * @return 调用合约后的返回值
     */
    public native String callWithReturnValue(String methodName, String methodDesc, String[][] args, BigInteger value, MultyAssetValue[] multyAssetValues);

    /**
     * 验证地址
     *
     * @param address 地址
     */
    private native void valid(String address);

    /**
     * 验证地址是否是合约地址
     *
     */
    public native boolean isContract();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address1 = (Address) o;
        return address != null ? address.equals(address1.address) : address1.address == null;
    }

    @Override
    public int hashCode() {
        return address != null ? address.hashCode() : 0;
    }

    @Override
    public String toString() {
        return address;
    }

}
