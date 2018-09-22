package io.nuls.sdk.accountledger.service.impl;

import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.MSAccount;
import io.nuls.sdk.accountledger.model.Output;
import io.nuls.sdk.core.SDKBootstrap;
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

import static org.junit.Assert.*;

public class AccountLedgerServiceImplTest {

    @Before
    public void setup() {
        SDKBootstrap.init("127.0.0.1", "8017");
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
}