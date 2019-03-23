package io.nuls.sdk.core.contast;

import io.nuls.sdk.core.model.Na;

/**
 * @author: Charlie
 */
public class SDKConstant {

    public static String DEFAULT_ENCODING = "UTF-8";

    /**
     * 默认链id（nuls主链）,链id会影响地址的生成，当前地址以“Ns”开头
     * The default chain id (nuls main chain), the chain id affects the generation of the address,
     * and the current address begins with "Ns".16402.
     */
    public static short DEFAULT_CHAIN_ID = 8964;


    public static String NULSCAN_URL = "https://api.nuls.io";
    /**
     * 默认的地址类型，一条链可以包含几种地址类型，地址类型包含在地址中
     * The default address type, a chain can contain several address types, and the address type is contained in the address.
     */
    public static byte DEFAULT_ADDRESS_TYPE = 1;
    /**
     * 智能合约地址类型
     * contract address type
     */
    public static byte CONTRACT_ADDRESS_TYPE = 2;

    /**
     * 多重签名地址
     * contract address type
     */
    public static byte P2SH_ADDRESS_TYPE = 3;

    /**
     * 空值占位符
     * Null placeholder.
     */
    public static byte[] PLACE_HOLDER = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    /**
     * 48位整型数据长度
     * 48 bit integer data length.
     */
    public static int INT48_VALUE_LENGTH = 6;

    public static Na SUM_OF_DEPOSIT_OF_AGENT_LOWER_LIMIT = Na.parseNuls(200000);
    public static Na SUM_OF_DEPOSIT_OF_AGENT_UPPER_LIMIT = Na.parseNuls(500000);

    public static Na AGENT_DEPOSIT_LOWER_LIMIT = Na.parseNuls(20000);
    public static Na AGENT_DEPOSIT_UPPER_LIMIT = Na.parseNuls(200000);
    public static Na ENTRUSTER_DEPOSIT_LOWER_LIMIT = Na.parseNuls(2000);

    public static long CONSENSUS_LOCK_TIME = -1;

    /**
     * 脚本标识位
     * Null placeholder.
     */
    public static byte[] SIGN_HOLDER = new byte[]{(byte) 0x00, (byte) 0x00};
}
