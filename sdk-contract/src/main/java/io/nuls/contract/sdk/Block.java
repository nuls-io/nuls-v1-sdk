package io.nuls.contract.sdk;

public class Block {

    /**
     * 给定块的哈希值
     * hash of the given block
     *
     * @param blockNumber 区块高度
     * @return 区块hash
     */
    public static native byte[] blockhash(long blockNumber);

    /**
     * 给定块的哈希值
     * hash of the given block
     *
     * @param blockNumber 区块高度
     * @return 区块hash
     */
    public static native String blockhashHex(long blockNumber);

    /**
     * 当前块矿工地址
     * current block miner’s address
     *
     * @return 地址
     */
    public static native Address coinbase();

    /**
     * 当前块编号
     * current block number
     *
     * @return number
     */
    public static native long number();

    /**
     * 当前块时间戳
     * current block timestamp
     *
     * @return timestamp
     */
    public static native long timestamp();

}
