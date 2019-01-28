package io.nuls.sdk.contract.test;

import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.Output;
import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.model.transaction.Transaction;
import io.nuls.sdk.core.utils.NulsByteBuffer;
import io.nuls.sdk.core.utils.TransactionTool;
import io.nuls.sdk.tool.NulsSDKTool;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkun23 on 2018/10/8.
 */
public class AppTest {

    //    final Logger logger = LoggerFactory.getLogger(AppTest.class);
//
//    @Before
//    public void init() {
//        SDKBootstrap.init("192.168.1.133", "8001");
//    }
//
//    /**
//     * 测试获取utxo
//     */
//    @Test
//    public void UTXOs() {
//        RestFulUtils restFulUtils = RestFulUtils.getInstance();
//        String url = "/utxo/amount/Nsdv1Hbu4TokdgbXreypXmVttYKdPT1g/1000";
//        Result result = restFulUtils.get(url, new HashMap());
//        Map data = (Map) result.getData();
//
//        List<Map<String, Object>> utxos = (List<Map<String, Object>>) data.get("utxoDtoList");
//
//        for (Map<String, Object> utxo : utxos) {
//            logger.info("txHash {}", utxo.get("txHash"));
//            logger.info("txIndex {}", utxo.get("txIndex"));
//            logger.info("value {}", utxo.get("value"));
//            logger.info("lockTime {}", utxo.get("lockTime"));
//        }
//    }
//
//    /**
//     * eckey的用法
//     */
//    @Test
//    public void fromPrivate() {
//        String priKey = "00ef8a6f90d707ab345740f0fab2d9f606165209ce41a71199f679f5dfd20bfd1d";
//        ECKey ecKey = ECKey.fromPrivate(new BigInteger(Hex.decode(priKey)));
//        logger.info("txHex {}", ecKey.getPrivateKeyAsHex());
//        logger.info("txHex {}", ecKey.getPublicKeyAsHex());
//    }
//
//    /**
//     * 测试转账交易流程
//     */
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
        output.setLockTime(0);
        output.setValue(10000000000L);
        outputs.add(output);


        output = new Output();
        output.setAddress("Nse2ogzwtchc7M6PpdcghEz6247oar5e");
        output.setLockTime(0);
        output.setValue(389799000000L - 10000000000L - 1000000L);
        outputs.add(output);

        Result result = NulsSDKTool.createTransaction(inputs, outputs, "abcd");
        if (result.isSuccess()) {
            Map map = (Map) result.getData();
            String txHex = (String) map.get("value");
            byte[] bytes = Hex.decode(txHex);
            try {
                Transaction tx = TransactionTool.getInstance(new NulsByteBuffer(bytes));
                System.out.println(tx.getHash().getDigestHex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        String hash = transaction.getHash().getDigestHex();
//
//
//        logger.info("result {}", result);
//
//        Map<String, Object> map = (Map<String, Object>) result.getData();
//        String txHex = (String) map.get("value");
//
//        String priKey = "40bf6fb1352694c311cd5defdda370582c18e73023fa14f7fade9c04ebc5e3ad";
//        String address = "Nse2ogzwtchc7M6PpdcghEz6247oar5e";
//
//        result = NulsSDKTool.signTransaction(txHex, priKey, address, null);
//
//        logger.info("result {}", result);
//        map = (Map<String, Object>) result.getData();
//
//        String sign = (String) map.get("value");
//
//        result = NulsSDKTool.broadcastTransaction(sign);
//        System.out.println(result.isSuccess());
    }
//
//
//    /**
//     * 验证json数据放回的数字是9位或者9位以下的数值类型转换的bug。
//     */
//    @Test
//    public void app() {
//        Map<String, Object> map = new HashMap<>();
//        String sender = "Nsdv1Hbu4TokdgbXreypXmVttYKdPT1g";
//
//        UTXOService utxoService = UTXOServiceImpl.getInstance();
//        utxoService.getUTXOs(sender, 150L);
//    }

    @Test
    public void testexport() {
        SDKBootstrap.init("127.0.0.1", "6001");
        Result result = NulsSDKTool.backupAccount("Nse5oPtPjgbyHujSxXu2YbWRmmf3ksCo", "E:/");
        System.out.println(result.isSuccess());
    }

    @Test
    public void testChainID() {
        SDKBootstrap.init("127.0.0.1", "6001", 8964);
        Result result = NulsSDKTool.createOfflineAccount();
        System.out.println(result.isSuccess());
    }

    @Test
    public void testAccountBalance() {
        SDKBootstrap.init("127.0.0.1", "8001", 261);
//        String address = "TTarjkrDZuxdnq8dumdra6ss3bkRRub1";
//
//        Result result = NulsSDKTool.getBalance(address);
//        System.out.println(result.isSuccess());
        Result result = NulsSDKTool.validateTransaction("0200a29ed979680100ffffffff02230020f3d089f430dcc0326532e74cac89652e24407d5029f32d272e699626b49f604700404b4c000000000000000000000023002052ec7ac07d11091a17112a51af1de08951afe4c9ce4a7a53e66b504f49891a20000031cce2010000000000000000000117050101a1dca62ba4367f46a4aca8bf654d92ec6e9fc731a0f516e301000000000000000000d52103f92b70f584efb1b0a3993b8d957f0638c379b60c5d0e63e9a738f5260fa1474b004630440220754bdaef104b35b4948783aac041716fce6e6b613b3ed05c96510b894d3557a202206dbddcd648b580a70f60f965161172c4d15bdb9e72f5b0407d8301c71df372252102d6f4fc6beeee05778795909287b22c4efce972fc7344d374c833dbc72fe966a700473045022100ec004afb2230e08f4be6cdc204e1d042ee9d7567d6c0335b64dcbb56b1af9b69022013bd3ac78292fce82403ec05e7db921cca44787f424238f235f0003b812409f1");
        System.out.println(result.isSuccess());
    }
}
