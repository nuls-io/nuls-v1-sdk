package io.nuls.sdk.core;

import io.nuls.sdk.core.contast.RpcConstant;
import io.nuls.sdk.core.contast.SDKConstant;
import io.nuls.sdk.core.utils.RestFulUtils;
import io.nuls.sdk.core.utils.StringUtils;
import io.nuls.sdk.core.utils.TransactionTool;

/**
 * @author: Charlie
 */
public class SDKBootstrap {

    public static void init() {
        init(null, null);
    }

    public static void init(String ip, String port) {
        if (StringUtils.isBlank(ip) || StringUtils.isBlank(port)) {
            RestFulUtils.getInstance().setServerUri("http://" + RpcConstant.DEFAULT_IP + ":" + RpcConstant.DEFAULT_PORT + RpcConstant.PREFIX);
        } else {
            RestFulUtils.getInstance().setServerUri("http://" + ip + ":" + port + RpcConstant.PREFIX);
        }
    }

    public static void init(String ip, String port, int chainId) {
        init(ip, port);
        SDKConstant.DEFAULT_CHAIN_ID = (short) chainId;
    }

}
