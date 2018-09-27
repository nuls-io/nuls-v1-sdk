package io.nuls.contract.sdk;

public class Utils {

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private Utils() {
    }

    /**
     * 检查条件，如果条件不满足则回滚
     *
     * @param expression 检查条件
     */
    public static void require(boolean expression) {
        if (!expression) {
            revert();
        }
    }

    /**
     * 检查条件，如果条件不满足则回滚
     *
     * @param expression 检查条件
     * @param errorMessage 错误信息
     */
    public static void require(boolean expression, String errorMessage) {
        if (!expression) {
            revert(errorMessage);
        }
    }

    /**
     * 终止执行并还原改变的状态
     */
    public static void revert() {
        revert(null);
    }

    /**
     * 终止执行并还原改变的状态
     *
     * @param errorMessage 错误信息
     */
    public static native void revert(String errorMessage);

    /**
     * 发送事件
     *
     * @param event 事件
     */
    public static native void emit(Event event);

    /**
     * Returns a power of two size for the given target capacity.
     *
     * @param cap capacity
     */
    private static int powerSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * @param seed a private seed
     * @return pseudo random number (0 ~ 1073741823)
     */
    public static int pseudoRandom(int seed) {
        return pseudoRandom(seed, null, null);
    }

    /**
     * @param seed  a private seed
     * @return pseudo random number (0 ~ 1073741823)
     */
    public static int pseudoRandom(String seed) {
        return pseudoRandom(null, seed, null);
    }

    /**
     * @param seed a private seed
     * @param initialCapacity initial capacity, it will be assigned a power of two size for the given target capacity.
     * @return pseudo random number (0 ~ (powerSizeFor(initialCapacity) - 1))
     */
    public static int pseudoRandom(int seed, Integer initialCapacity) {
        return pseudoRandom(seed, null, initialCapacity);
    }

    /**
     * @param seed a private seed
     * @param initialCapacity initial capacity, it will be assigned a power of two size for the given target capacity.
     * @return pseudo random number (0 ~ (powerSizeFor(initialCapacity) - 1))
     */
    public static int pseudoRandom(String seed, Integer initialCapacity) {
        return pseudoRandom(null, seed, initialCapacity);
    }

    /**
     * @param seed a private seed
     * @param strSeed a private seed
     * @param initialCapacity initial capacity, it will be assigned a power of two size for the given target capacity.
     * @return pseudo random number (0 ~ (powerSizeFor(initialCapacity) - 1))
     */
    private static int pseudoRandom(Integer seed, String strSeed, Integer initialCapacity) {
        BlockHeader blockHeader = Block.currentBlockHeader();
        if(initialCapacity != null) {
            initialCapacity = powerSizeFor(initialCapacity);
        } else {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        long time = blockHeader.getTime();
        long txCount = blockHeader.getTxCount();
        String contractAddress = Msg.address().toString();
        int result = contractAddress != null ? contractAddress.hashCode() : 0;
        result = strSeed != null ? strSeed.hashCode() : 0;
        if(seed != null) {
            result = 31 * result + (int) (seed ^ (seed >>> 32));
        }
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (int) (txCount ^ (txCount >>> 32));
        result = result ^ (result >>> 16);
        result = (initialCapacity - 1) & result;
        return result;
    }

}
