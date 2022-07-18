package io.nuls.contract.sdk;

import java.math.BigInteger;
import java.util.List;

public class Utils {

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
     * @param seed a private seed
     * @return pseudo random number (0 ~ 1)
     */
    public static float pseudoRandom(long seed) {
        int hash1 = Block.currentBlockHeader().getPackingAddress().toString().substring(2).hashCode();
        int hash2 = Msg.address().toString().substring(2).hashCode();
        int hash3 = Msg.sender() != null ? Msg.sender().toString().substring(2).hashCode() : 0;
        int hash4 = Long.valueOf(Block.timestamp()).toString().hashCode();

        long hash = seed ^ hash1 ^ hash2 ^ hash3 ^ hash4;

        seed = (hash * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        return ((int) (seed >>> 24) / (float) (1 << 24));
    }

    /**
     * @return pseudo random number (0 ~ 1)
     */
    public static float pseudoRandom() {
        return pseudoRandom(0x5DEECE66DL);
    }

    /**
     *
     * Please note that this is the SHA-3 FIPS 202 standard, not Keccak-256.
     *
     * @param hexString source string (hex encoding string)
     * @return sha3-256 hash (hex encoding string)
     */
    public static native String sha3(String hexString);

    /**
     *
     * Please note that this is the SHA-3 FIPS 202 standard, not Keccak-256.
     *
     * @param bytes source byte array
     * @return sha3-256 hash (hex encoding string)
     */
    public static native String sha3(byte[] bytes);

    /**
     * verify signature data(ECDSA)
     *
     * @param data(hex encoding string)
     * @param signature(hex encoding string)
     * @param pubkey(compressed public key hex string)
     * @return verify result
     */
    public static native boolean verifySignatureData(String data, String signature, String pubkey);

    /**
     * 根据截止高度和原始种子数量，用特定的算法生成一个随机种子
     *
     * @param endHeight 截止高度
     * @param seedCount 原始种子数量
     * @param algorithm hash算法标识
     * @return 原始种子字节数组合并后, 使用hash算法得到32位hash字节数组, 再转化为BigInteger(new BigInteger(byte[] bytes))
     */
    public static native BigInteger getRandomSeed(long endHeight, int seedCount, String algorithm);

    /**
     * 根据截止高度和原始种子数量，用`SHA3-256`hash算法生成一个随机种子
     *
     * @param endHeight 截止高度
     * @param seedCount 原始种子数量
     * @return 原始种子字节数组合并后, 使用`SHA3-256`hash算法得到32位hash字节数组, 再转化为BigInteger(new BigInteger(byte[] bytes))
     */
    public static BigInteger getRandomSeed(long endHeight, int seedCount) {
        return getRandomSeed(endHeight, seedCount, "SHA3");
    }

    /**
     * 根据高度范围，用特定的算法生成一个随机种子
     *
     * @param startHeight 起始高度
     * @param endHeight   截止高度
     * @param algorithm   hash算法标识
     * @return 原始种子字节数组合并后, 使用hash算法得到32位hash字节数组, 再转化为BigInteger(new BigInteger(byte[] bytes))
     */
    public static native BigInteger getRandomSeed(long startHeight, long endHeight, String algorithm);

    /**
     * 根据高度范围，用`SHA3-256`hash算法生成一个随机种子
     *
     * @param startHeight 起始高度
     * @param endHeight   截止高度
     * @return 原始种子字节数组合并后, 使用`SHA3-256`hash算法得到32位hash字节数组, 再转化为BigInteger(new BigInteger(byte[] bytes))
     */
    public static BigInteger getRandomSeed(long startHeight, long endHeight){
        return getRandomSeed(startHeight, endHeight, "SHA3");
    }

    /**
     * 根据截止高度和原始种子数量，获取原始种子的集合
     *
     * @param endHeight 截止高度
     * @param seedCount 原始种子数量
     * @return 返回原始种子的集合，元素是字节数组转化的BigInteger(new BigInteger(byte[] bytes))
     */
    public static native List<BigInteger> getRandomSeedList(long endHeight, int seedCount);

    /**
     * 根据高度范围，获取原始种子的集合
     *
     * @param startHeight 起始高度
     * @param endHeight   截止高度
     * @return 返回原始种子的集合，元素是字节数组转化的BigInteger(new BigInteger(byte[] bytes))
     */
    public static native List<BigInteger> getRandomSeedList(long startHeight, long endHeight);

    /**
     * 调用链上其他模块的命令
     *
     * @see <a href="https://docs.nuls.io/zh/NULS2.0/vm-sdk.html">调用命令详细说明</a>
     * @param cmdName 命令名称
     * @param args 命令参数
     * @return 命令返回值(根据注册命令的返回类型可返回字符串,字符串数组,字符串二维数组)
     */
    public static native Object invokeExternalCmd(String cmdName, String[] args);

    /**
     * 把对象转换成json字符串
     * 注意：对象内如果包含复杂对象，序列化深度不得超过3级
     *
     * @param obj
     * @return json字符串
     */
    public static native String obj2Json(Object obj);

    /**
     * 内部创建合约
     * @param salt 参与新生成的合约地址计算的因素
     * @param codeCopy 指定合约模板
     * @param args 部署的参数
     * @return 部署的合约地址
     */
    public static String deploy(String[] salt, Address codeCopy, String[] args) {
        require(codeCopy.isContract(), "not contract address");
        String finalSalt = encodePacked(salt);
        int argsLength = 0;
        if (args != null) {
            argsLength = args.length;
        }
        String[] arr = new String[argsLength + 2];
        arr[0] = codeCopy.toString();
        arr[1] = finalSalt;
        for (int i = 0; i < argsLength; i++) {
            arr[2 + i] = args[i];
        }
        return (String) Utils.invokeExternalCmd("createContract", arr);
    }

    /**
     * 字符串数组编码
     */
    public static String encodePacked(String[] args) {
        return (String) Utils.invokeExternalCmd("encodePacked", args);
    }

    /**
     * 计算合约地址
     * @param salt 参与新生成的合约地址计算的因素
     * @param codeHash 合约代码的hash值
     * @param sender 合约部署者
     * @return 合约地址
     */
    public static String computeAddress(String[] salt, String codeHash, Address sender) {
        String finalSalt = encodePacked(salt);
        return (String) Utils.invokeExternalCmd("computeAddress", new String[]{finalSalt, codeHash, sender.toString()});
    }

    /**
     * @param codeAddress 合约地址
     * @return 合约代码的hash值
     */
    public static String getCodeHash(Address codeAddress) {
        require(codeAddress.isContract(), "not contract address");
        return (String) Utils.invokeExternalCmd("getCodeHash", new String[]{codeAddress.toString()});
    }

    /**
     * 资产decimals
     * @param assetChainId 资产链ID
     * @param assetId 资产ID
     * @return 资产decimals
     */
    public static int assetDecimals(int assetChainId, int assetId) {
        return Integer.parseInt((String) Utils.invokeExternalCmd("assetDecimals", new String[]{assetChainId + "", assetId + ""}));
    }

    /**
     * @return 当前链ID
     */
    public static int chainId() {
        return Integer.parseInt((String) Utils.invokeExternalCmd("currentChainId", new String[0]));
    }
}
