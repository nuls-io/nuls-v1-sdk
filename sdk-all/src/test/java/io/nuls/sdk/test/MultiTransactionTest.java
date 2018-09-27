package io.nuls.sdk.test;
import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.Output;
import io.nuls.sdk.consensus.model.AgentInfo;
import io.nuls.sdk.consensus.model.DepositInfo;
import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.model.Na;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.tool.NulsSDKTool;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.*;

public class MultiTransactionTest {
    @BeforeClass
    public static void init(){
        SDKBootstrap.init("127.0.0.1", "8011");
    }

    @Test
    public void testChangeCoin(){

    }

    @Test
    public void testCreateMSAgent(){
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
        result = NulsSDKTool.signMultiTransaction(txHex, priKeys,passwords);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");
        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.isSuccess());
    }

    @Test
    public void TestCreateStopMSAgent(){
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
        result = NulsSDKTool.signMultiTransaction(txHex, priKeys,passwords);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");
        result = NulsSDKTool.validateTransaction(sign);
        System.out.println(result.getData());
        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.getData());
    }

    @Test
    public void TestCreateMSAccountDeposit(){
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
        result = NulsSDKTool.signMultiTransaction(txHex, priKeys,passwords);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");
        result = NulsSDKTool.validateTransaction(sign);
        System.out.println(result.getData());
        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.getData());
    }

    @Test
    public void TestCreateMSAccountCancelDeposit(){
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
        result = NulsSDKTool.signMultiTransaction(txHex, priKeys,passwords);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");

        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.getData());
    }
}
