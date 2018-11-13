package io.nuls.sdk.contract.test;

import io.nuls.contract.sdk.service.ContractService;
import io.nuls.contract.sdk.service.UTXOService;
import io.nuls.contract.sdk.service.impl.ContractServiceImpl;
import io.nuls.contract.sdk.service.impl.UTXOServiceImpl;
import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.tool.NulsSDKTool;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkun23 on 2018/11/13.
 */
public class CreateContractTest {

    final Logger logger = LoggerFactory.getLogger(CreateContractTest.class);

    @Before
    public void init() {
        SDKBootstrap.init("192.168.1.133", "8001");
    }

    @Test
    public void createContractTransaction() {
        ContractService contractService = ContractServiceImpl.getInstance();
        UTXOService utxoService = UTXOServiceImpl.getInstance();
        String sender = "Nsdv1Hbu4TokdgbXreypXmVttYKdPT1g";
        long gasLimit = 27043L;
        Long price = 25L;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("vote-contract-hex.txt")));//构造一个BufferedReader类来读取文件
            String buf = null;
            while ((buf = bufferedReader.readLine()) != null) {
                stringBuilder.append(buf);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String contractCode = stringBuilder.toString();
        byte[] contractCodeBytes = Hex.decode(contractCode);
        Object[] args = {100_0000_0000L};
        String remark = "";

        List<Input> utxos = utxoService.getUTXOs(sender, 150_0000_0000L);
        try {
            Result result = contractService.createContractTransaction(sender, gasLimit, price, contractCodeBytes, args, remark, utxos);
            logger.info("result {}", result);
            Map<String, Object> map = (Map<String, Object>) result.getData();
            String txHex = (String) map.get("value");
            logger.info("txHex {}", txHex);
            String priKey = "00ef8a6f90d707ab345740f0fab2d9f606165209ce41a71199f679f5dfd20bfd1d";
            result = NulsSDKTool.signTransaction(txHex, priKey, sender, null);
            logger.info("signTransaction {}", result);

            map = (Map<String, Object>) result.getData();
            String hex2 = (String) map.get("value");
            result = NulsSDKTool.broadcastTransaction(hex2);

            logger.info("broadcastTransaction {}", result);
        } catch (NulsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
