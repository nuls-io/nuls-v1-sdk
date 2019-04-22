package io.nuls.sdk.contract.test;

import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.contract.model.ContractTransactionCreatedReturnInfo;
import io.nuls.sdk.contract.service.ContractService;
import io.nuls.sdk.contract.service.UTXOService;
import io.nuls.sdk.contract.service.impl.ContractServiceImpl;
import io.nuls.sdk.contract.service.impl.UTXOServiceImpl;
import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.model.JsonRPCResult;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.tool.NulsSDKTool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Created by wangkun23 on 2018/11/13.
 * Updated by pierreluo on 2019/03/08
 */
public class CreateContractTest {

    final Logger logger = LoggerFactory.getLogger(CreateContractTest.class);

    @Before
    public void init() {
        //TODO 本地节点的ip以及port
        SDKBootstrap.init("192.168.1.133", "8001");
    }

    /**
     *  离线创建合约，不计算手续费方式
     */
    @Test
    public void createContractTransaction() {
        ContractService contractService = ContractServiceImpl.getInstance();
        String sender = "Nsdv1Hbu4TokdgbXreypXmVttYKdPT1g";
        //TODO gasLimit需要调用预估gas的api，调用预估gas的api之前，需要先调用验证api，验证成功后再调用预估gas的api
        //TODO gasLimit需要调用预估gas的api，这里暂时写固定值
        /**
         *  验证api: /api/contract/validate/create
         *      {
         *        "gasLimit": 1000000,
         *        "price": 25,
         *        "contractCode": "string",
         *        "args": [
         *          {}
         *        ]
         *      }
         *
         *  预估gas的api: /api/contract/imputedgas/create
         *      {
         *        "sender": "string",
         *        "price": 25,
         *        "contractCode": "string",
         *        "args": [
         *          {}
         *        ]
         *      }
         *
         */
        long gasLimit = 27043L;
        Long price = 25L;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("vote-contract-hex.txt")));
            String buf = null;
            while ((buf = bufferedReader.readLine()) != null) {
                stringBuilder.append(buf);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String contractCode = stringBuilder.toString();
        Object[] args = {100_0000_0000L};
        String remark = "";

        JsonRPCResult utxoResult = NulsSDKTool.getUtxo(sender, 150_0000_0000L);
        List<Input> inputs = null;
        if (utxoResult.getResult() != null) {
            inputs = (List<Input>) utxoResult.getResult();
        }
        Assert.assertTrue("get utxo error.", inputs != null);
        Result result = contractService.createContractTransaction(sender, gasLimit, price, contractCode, args, remark, inputs);
        logger.info("result {}", result);
        Map<String, Object> map = (Map<String, Object>) result.getData();
        ContractTransactionCreatedReturnInfo info = (ContractTransactionCreatedReturnInfo) map.get("value");
        String txHex = info.getTxHex();
        logger.info("txHex {}", txHex);
        //TODO sender地址的私钥
        String priKey = "00ef8a6f90d707ab345740f0fab2d9f606165209ce41a71199f679f5dfd20bfd1d";
        result = NulsSDKTool.signTransaction(txHex, priKey, sender, null);
        logger.info("signTransaction {}", result);

        map = (Map<String, Object>) result.getData();
        String hex2 = (String) map.get("value");
        result = NulsSDKTool.broadcastTransaction(hex2);

        logger.info("broadcastTransaction {}", result);
    }
}
