package io.nuls.sdk.accountledger.service.impl;

import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.MSAccount;
import io.nuls.sdk.accountledger.model.Output;
import io.nuls.sdk.consensus.model.AgentInfo;
import io.nuls.sdk.consensus.model.DepositInfo;
import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.model.Na;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.utils.JSONUtils;
import io.nuls.sdk.tool.NulsSDKTool;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AccountLedgerServiceImplTest {

    @Before
    public void setup() {
        SDKBootstrap.init("127.0.0.1", "8011");
    }

    @Test
    public void createMSAccountTransferTransaction() throws Exception {
        //language=JSON
        String json = "{\n" +
                "  \"success\": true,\n" +
                "  \"data\": {\n" +
                "    \"utxoDtoList\": [\n" +
                "      {\n" +
                "        \"txHash\": \"002001e273d650d1ae0b30bd267761ffca724bdc4e66867824040b92b750d964df68\",\n" +
                "        \"txIndex\": 0,\n" +
                "        \"value\": 100000000,\n" +
                "        \"lockTime\": 0,\n" +
                "        \"address\": \"NseVy6D3xAhwfYXi9Cn7YajG1QXJxN43\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"txHash\": \"00202426e6029dc596f73ec09e275d2dd232520624a61e21fd667cb3dc0f25cd0cd0\",\n" +
                "        \"txIndex\": 0,\n" +
                "        \"value\": 100000000,\n" +
                "        \"lockTime\": 0,\n" +
                "        \"address\": \"NseVy6D3xAhwfYXi9Cn7YajG1QXJxN43\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"txHash\": \"002025de048a6517c02a8090bf0c5da622122e528d085bbe1eb1782b50e30b82567c\",\n" +
                "        \"txIndex\": 0,\n" +
                "        \"value\": 100000000,\n" +
                "        \"lockTime\": 0,\n" +
                "        \"address\": \"NseVy6D3xAhwfYXi9Cn7YajG1QXJxN43\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"txHash\": \"002030c3e8bcccd0e81490ab3b786e25d6d1d7501725887cbfe67bcfe32efcdc2026\",\n" +
                "        \"txIndex\": 0,\n" +
                "        \"value\": 100000000,\n" +
                "        \"lockTime\": 0,\n" +
                "        \"address\": \"NseVy6D3xAhwfYXi9Cn7YajG1QXJxN43\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"txHash\": \"0020324093e23341557621cf2b4b665e02703c230d1392fb1a20680bd445537f3e8f\",\n" +
                "        \"txIndex\": 0,\n" +
                "        \"value\": 100000000,\n" +
                "        \"lockTime\": 0,\n" +
                "        \"address\": \"NseVy6D3xAhwfYXi9Cn7YajG1QXJxN43\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        MSAccount account = new MSAccount();
        account.setThreshold(2);
        account.setPubKeys(
                Arrays.asList(
                        "02c7b2761a51c2a2ddecf41021d67ce2cf86fdc6dd44bac2742e3025917e7a749a",
                        "023cee4d977974293ec436251e5c03840f5110e1c5155e4f357099404bd7be5ee3",
                        "03052f7ccfc4c87e5d2454d846c78fdc02fcd480b234735c1724741d3e04be2fc4"));

        List<Input> inputs = convertJson2Inputs(json);
        List<Output> outputs = new ArrayList<>(7);
        Output output1 = new Output();
        output1.setAddress("Nse6kWfoNyDSktjf2rA1CVRLTDemZ5TZ");
        output1.setValue(160000000);
        output1.setLockTime(0);
        outputs.add(output1);

        Result result = NulsSDKTool.createMSAccountTransferTransaction(account, inputs, outputs, "remark");
        assertTrue("", result.isSuccess());
        Map<String, Object> data = (Map<String, Object>) result.getData();
        String txDataHex = (String) data.get("txdata");

        NulsSDKTool.signMultiTransaction(txDataHex,
                Arrays.asList("08a605b754bd1be1ba765fabd5cd218a545eb38b54ad26a7eb8a3378f724e5be",
                        "711018fa9f729a9848d9588b6238295eed05b6a91f069578b54797eac9bb3361"),
                Arrays.asList("", ""));
    }

    private List<Input> convertJson2Inputs(String jsonStr) throws Exception {
        Map<String, Object> result = JSONUtils.json2map(jsonStr);
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        ArrayList<Map> utxo = (ArrayList<Map>) data.get("utxoDtoList");

        return utxo.stream()
                .map(p -> {
                    Input input = new Input();
                    input.setFromHash((String) p.get("txHash"));
                    input.setFromIndex((Integer) p.get("txIndex"));
                    input.setLockTime((Integer) p.get("lockTime"));
                    input.setValue((Integer) p.get("value"));
                    input.setAddress((String) p.get("address"));
                    return input;
                }).collect(Collectors.toList());
    }

    @Test
    public void testCreateMSAgent() {
        List<Input> inputs = new ArrayList<>();
        Input input = new Input();
        input.setFromHash("0020a1909a5b7582a2bb808e049e5681809c2233041269ce0c2055fe76918ca9dc14");
        input.setFromIndex(0);
        input.setAddress("NseWwCKxV1ToMCZnYHbFrEmKgU7p4fBm");
        input.setValue(300000 * 100000000L);
        inputs.add(input);

        AgentInfo info = new AgentInfo();
        info.setAgentAddress("NseWwCKxV1ToMCZnYHbFrEmKgU7p4fBm");
        info.setPackingAddress("Nsdxvus7HDY9pQPnuG1DWygh7rW9wvBR");
        info.setDeposit(200000 * 100000000L);
        info.setCommissionRate(10.0);

        Na fee = Na.valueOf(1000000L);
        Result result = NulsSDKTool.createMSAgentTransaction(info, inputs, fee);
        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        List<String> priKeys = new ArrayList<>();
        priKeys.add("31773bce0f96f97449205dc1ecc7366f90e2e848b0217b2b2842b3caea00e0cc");
        priKeys.add("0256d2015e7d84bb4db3021d127332afe9e2dfa76b8762b1252ee88578f8ff65");
        List<String> passwords = new ArrayList<>();
        passwords.add("");
        passwords.add("");
        result = NulsSDKTool.signMultiTransaction(txHex, priKeys, passwords);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");
        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.isSuccess());
    }

    @Test
    public void TestCreateStopMSAgent() {
        Output output = new Output();
        output.setTxHash("00206372ba9856aa4e97a79838dfe79ff5968920b468979f592fe8aed21ddd97e790");
        output.setIndex(0);
        output.setValue(20000000000000L);
        output.setLockTime(-1);
        output.setAddress("NseWwCKxV1ToMCZnYHbFrEmKgU7p4fBm");
        Result result = NulsSDKTool.createStopMSAgentTransaction(output);
        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        List<String> priKeys = new ArrayList<>();
        priKeys.add("31773bce0f96f97449205dc1ecc7366f90e2e848b0217b2b2842b3caea00e0cc");
        priKeys.add("0256d2015e7d84bb4db3021d127332afe9e2dfa76b8762b1252ee88578f8ff65");
        List<String> passwords = new ArrayList<>();
        passwords.add("");
        passwords.add("");
        result = NulsSDKTool.signMultiTransaction(txHex, priKeys, passwords);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");
        result = NulsSDKTool.validateTransaction(sign);
        System.out.println(result.getData());
        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.getData());
    }

    @Test
    public void TestCreateMSAccountDeposit() {
        List<Input> inputs = new ArrayList<>();
        Input input = new Input();
        input.setFromHash("002031f210834b031bfbc2abcf77808a079e86a23ca2279161beacf375b160f7e697");
        input.setFromIndex(0);
        input.setAddress("NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM");
        input.setValue(300000 * 100000000L);
        inputs.add(input);

        DepositInfo info = new DepositInfo();
        info.setAddress("NseWwCKxV1ToMCZnYHbFrEmKgU7p4fBm");
        info.setDeposit(200000 * 100000000L);
        info.setAgentHash("00205492d3bf12dd32729a86de6fcb690c92539637935fbbc584e57c5fec46d027ef");

        Na fee = Na.valueOf(1000000L);
        Result result = NulsSDKTool.createMSAccountDepositTransaction(info, inputs, fee);
        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        List<String> priKeys = new ArrayList<>();
        priKeys.add("31773bce0f96f97449205dc1ecc7366f90e2e848b0217b2b2842b3caea00e0cc");
        priKeys.add("0256d2015e7d84bb4db3021d127332afe9e2dfa76b8762b1252ee88578f8ff65");
        List<String> passwords = new ArrayList<>();
        passwords.add("");
        passwords.add("");
        result = NulsSDKTool.signMultiTransaction(txHex, priKeys, passwords);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");
        result = NulsSDKTool.validateTransaction(sign);
        System.out.println(result.getData());
        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.getData());
    }

    @Test
    public void TestCreateMSAccountCancelDeposit() {
        Output output = new Output();
        output.setTxHash("002076ecfadcd365b469deabaf2baa5daf42e367fb233330d94688cd316119a129b6");
        output.setIndex(0);
        output.setValue(20000000000000L);
        output.setLockTime(0);
        output.setAddress("NseWwCKxV1ToMCZnYHbFrEmKgU7p4fBm");
        Result result = NulsSDKTool.createMSAccountCancelDepositTransaction(output);

        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        List<String> priKeys = new ArrayList<>();
        priKeys.add("31773bce0f96f97449205dc1ecc7366f90e2e848b0217b2b2842b3caea00e0cc");
        priKeys.add("0256d2015e7d84bb4db3021d127332afe9e2dfa76b8762b1252ee88578f8ff65");
        List<String> passwords = new ArrayList<>();
        passwords.add("");
        passwords.add("");
        result = NulsSDKTool.signMultiTransaction(txHex, priKeys, passwords);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");

        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.getData());
    }

    @Test
    public void createMultipleAddressTransferTransaction() {
        // 构建 Input
        List<Input> inputs = new ArrayList<>();
        Input input = new Input();
        input.setAddress("Nse9cEgqCzhX2tU5et4ABRLuAw8QPXA6");
        input.setValue(100000000);
        input.setLockTime(0);
        input.setFromHash("0020abf27a82873f71281ffc9d81dce023597a93c3a7809e2ea3efb0f0661dc5883d");
        input.setFromIndex(0);
        inputs.add(input);

        input = new Input();
        input.setAddress("Nse4kxLjaGZiooipvfm56Xh3oTCDwGG2");
        input.setValue(3994986137900000L);
        input.setLockTime(0);
        input.setFromHash("0020abf27a82873f71281ffc9d81dce023597a93c3a7809e2ea3efb0f0661dc5883d");
        input.setFromIndex(1);
        inputs.add(input);

        // 构建 Output
        List<Output> outputs = new ArrayList<>();
        Output output = new Output();
        output.setAddress("Nse2SBn4y5KseX78ez5to83ZrHmRxvUv");
        output.setValue(450000000);
        outputs.add(output);

        output = new Output();
        output.setAddress("Nse4kxLjaGZiooipvfm56Xh3oTCDwGG2");
        output.setValue(3994985787800000L);
        outputs.add(output);

        Result result = NulsSDKTool.createTransaction(inputs, outputs, "remark");
        assertTrue("Create transaction must be true", result.isSuccess());
        Map<String, String> data = (Map<String, String>) result.getData();
        String hexData = data.get("value");
        result = NulsSDKTool.signMultiTransaction(hexData, Arrays.asList("08a605b754bd1be1ba765fabd5cd218a545eb38b54ad26a7eb8a3378f724e5be", "00a710f9679fb6b5953bcfbea67a198e9c0d8888c43bad78a7241258e36aeaf65d"), Arrays.asList("", ""));
        assertTrue("Sign the transaction must be true", result.isSuccess());
        data = (Map<String, String>) result.getData();
        String txData = data.get("value");
        assertNotNull("Sign data must not be null", txData);
    }


    @Test
    public void testChangeCoin() {
        // 构建 Input
        List<Input> inputs = new ArrayList<>();
        Input input = new Input();
        input.setAddress("Nse3PAyfC3tj7gpj8uWx9KtjsYvw5pVp");
        input.setValue(57654167);
        input.setLockTime(6220);
        input.setFromHash("0020a0c217872f7a4acedc983067184d17dd1eb135c59947e0c883e1b623b798a8d1");
        input.setFromIndex(0);
        inputs.add(input);

        input = new Input();
        input.setAddress("Nse3PAyfC3tj7gpj8uWx9KtjsYvw5pVp");
        input.setValue(57835985);
        input.setLockTime(6218);
        input.setFromHash("002032a9ecdcdf52747823c81fb9e21f3e433598f3979326cdffd1c674254131793d");
        input.setFromIndex(0);
        inputs.add(input);

        Result result = NulsSDKTool.createChangeCoinTransaction(inputs,"Nse3PAyfC3tj7gpj8uWx9KtjsYvw5pVp");
        Map<String, Object> map = (Map<String, Object>) result.getData();
        List<String> txList =  (List<String>) map.get("value");
        for (String txHex:txList) {
            result = NulsSDKTool.signMultiTransaction(txHex, Arrays.asList("00e0784c09d16d7bab6f35f74a2b90c525397cee87864385e47df27e8c72061237"), Arrays.asList(""));
            assertTrue("Sign the transaction must be true", result.isSuccess());
            Map<String, String> txdata = (Map<String, String>) result.getData();
            result = NulsSDKTool.validateTransaction(txdata.get("value"));
            System.out.println(result.getData());
            result = NulsSDKTool.broadcastTransaction(txdata.get("value"));
            System.out.println(result.getData());
        }
    }
}