package io.nuls.sdk.contract.test;

import io.nuls.contract.sdk.service.UTXOService;
import io.nuls.contract.sdk.service.impl.UTXOServiceImpl;
import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.Output;
import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.crypto.ECKey;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.utils.RestFulUtils;
import io.nuls.sdk.tool.NulsSDKTool;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by wangkun23 on 2018/10/8.
 */
public class AppTest {

    final Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Before
    public void init() {
        SDKBootstrap.init("192.168.1.133", "8001");
    }

    /**
     * 测试获取utxo
     */
    @Test
    public void UTXOs() {
        RestFulUtils restFulUtils = RestFulUtils.getInstance();
        String url = "/utxo/amount/Nsdv1Hbu4TokdgbXreypXmVttYKdPT1g/1000";
        Result result = restFulUtils.get(url, new HashMap());
        Map data = (Map) result.getData();

        List<Map<String, Object>> utxos = (List<Map<String, Object>>) data.get("utxoDtoList");

        for (Map<String, Object> utxo : utxos) {
            logger.info("txHash {}", utxo.get("txHash"));
            logger.info("txIndex {}", utxo.get("txIndex"));
            logger.info("value {}", utxo.get("value"));
            logger.info("lockTime {}", utxo.get("lockTime"));
        }
    }

    /**
     * eckey的用法
     */
    @Test
    public void fromPrivate() {
        String priKey = "00ef8a6f90d707ab345740f0fab2d9f606165209ce41a71199f679f5dfd20bfd1d";
        ECKey ecKey = ECKey.fromPrivate(new BigInteger(Hex.decode(priKey)));
        logger.info("txHex {}", ecKey.getPrivateKeyAsHex());
        logger.info("txHex {}", ecKey.getPublicKeyAsHex());
    }

    /**
     * 测试转账交易流程
     */
    @Test
    public void testTransaction() {
        List<Input> inputs = new ArrayList<>();
        Input input = new Input();
        input.setFromHash("00209d313a408139095db2feced7b2619e3b88e479b6a2bf0fcd2e842a754f08af82");
        input.setFromIndex(1);
        input.setAddress("Nse2ogzwtchc7M6PpdcghEz6247oar5e");
        input.setValue(389799000000L);

        inputs.add(input);

        List<Output> outputs = new ArrayList<>();
        Output output = new Output();
        output.setAddress("NsdzvTimHHvWoUcnpG4PErNxhHPBpv7R");
        output.setIndex(1);
        output.setLockTime(0);
        output.setValue(10000000000L);
        outputs.add(output);


        output = new Output();
        output.setAddress("Nse2ogzwtchc7M6PpdcghEz6247oar5e");
        output.setIndex(0);
        output.setLockTime(0);
        output.setValue(389799000000L - 10000000000L - 1000000L);
        outputs.add(output);

        Result result = NulsSDKTool.createTransaction(inputs, outputs, "abcd");
        logger.info("result {}", result);

        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        String priKey = "40bf6fb1352694c311cd5defdda370582c18e73023fa14f7fade9c04ebc5e3ad";
        String address = "Nse2ogzwtchc7M6PpdcghEz6247oar5e";

        result = NulsSDKTool.signTransaction(txHex, priKey, address, null);

        logger.info("result {}", result);
        map = (Map<String, Object>) result.getData();

        String sign = (String) map.get("value");

        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.isSuccess());
    }


    /**
     * 验证json数据放回的数字是9位或者9位以下的数值类型转换的bug。
     */
    @Test
    public void app() {
        Map<String, Object> map = new HashMap<>();
        String sender = "Nsdv1Hbu4TokdgbXreypXmVttYKdPT1g";

        UTXOService utxoService = UTXOServiceImpl.getInstance();
        utxoService.getUTXOs(sender, 150L);
    }
}
