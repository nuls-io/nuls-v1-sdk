package io.nuls.sdk.core.contast;

import io.nuls.sdk.core.model.Na;

/**
 * @author: Charlie
 */
public interface SDKConstant {

    String DEFAULT_ENCODING = "UTF-8";

    /**
     * 默认链id（nuls主链）,链id会影响地址的生成，当前地址以“Ns”开头
     * The default chain id (nuls main chain), the chain id affects the generation of the address,
     * and the current address begins with "Ns".16402.
     */
    short DEFAULT_CHAIN_ID = 1;
    byte DEFAULT_ADDRESS_TYPE = 1;
    /**
     * 空值占位符
     * Null placeholder.
     */
    byte[] PLACE_HOLDER = new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    /**
     * 48位整型数据长度
     * 48 bit integer data length.
     */
    int INT48_VALUE_LENGTH = 6;

    Na SUM_OF_DEPOSIT_OF_AGENT_LOWER_LIMIT = Na.parseNuls(200000);
    Na SUM_OF_DEPOSIT_OF_AGENT_UPPER_LIMIT = Na.parseNuls(500000);

    Na AGENT_DEPOSIT_LOWER_LIMIT = Na.parseNuls(20000);
    Na AGENT_DEPOSIT_UPPER_LIMIT = Na.parseNuls(200000);
    Na ENTRUSTER_DEPOSIT_LOWER_LIMIT = Na.parseNuls(2000);

    long CONSENSUS_LOCK_TIME = -1;
}
