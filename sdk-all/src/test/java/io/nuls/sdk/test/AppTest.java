package io.nuls.sdk.test;

import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.utils.RestFulUtils;
import io.nuls.sdk.tool.NulsSDKTool;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkun23 on 2018/10/8.
 */
public class AppTest {

    final Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Before
    public void init() {
        SDKBootstrap.init("192.168.1.119","8001");
    }

    @Test
    public void latestBlock() {
        Result result = NulsSDKTool.getNewestBlockHeight();
        logger.info("latest block height {}", result);
        Map data = (Map) result.getData();
        logger.info("latest block height {}", data.get("value"));
    }


    @Test
    public void getBlock() {
        int height = 768332;
        Result result = NulsSDKTool.getBlock(height);
        logger.info("block {}", result);
    }


    @Test
    public void UTXOs() {
        RestFulUtils restFulUtils = RestFulUtils.getInstance();
        String url = "/utxo/amount/Nsdv1Hbu4TokdgbXreypXmVttYKdPT1g/1000";
        Result result = restFulUtils.get(url, new HashMap());
        Map data = (Map) result.getData();

        List<Map<String, Object>> utxos = (List<Map<String, Object>>) data.get("utxoDtoList");

        for (Map<String, Object> utxo : utxos) {
            logger.info("txHash {}",utxo.get("txHash"));
            logger.info("txIndex {}",utxo.get("txIndex"));
            logger.info("value {}",utxo.get("value"));
            logger.info("lockTime {}",utxo.get("lockTime"));
        }
    }
}
