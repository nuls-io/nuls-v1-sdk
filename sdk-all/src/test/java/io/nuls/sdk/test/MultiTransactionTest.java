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
        SDKBootstrap.init("192.168.1.133", "8011");
    }

    @Test
    public void testChangeCoin(){

    }

    @Test
    public void testCreateMSAgent(){
        List<Input> inputs = new ArrayList<>();
        Input input = new Input();
        input.setFromHash("0020228e6aeb3bd85520287e56bceec50a5527177ccb5e585a505489420b73623167");
        input.setFromIndex(0);
        input.setAddress("NseWwCKxV1ToMCZnYHbFrEmKgU7p4fBm");
        input.setValue(5000000 * 100000000L);
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
        output.setTxHash("0020b3455590664eea19ff78963a815077da0c3f7e4eebba2632ed479cdecae36233");
        output.setIndex(0);
        output.setValue(20000000000000L);
        output.setLockTime(-1);
        output.setAddress("NseWwCKxV1ToMCZnYHbFrEmKgU7p4fBm");
        Result result = NulsSDKTool.createStopAgentTransaction(output);

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

    @Test
    public void TestCreateMSAccountDeposit(){
        List<Input> inputs = new ArrayList<>();
        Input input = new Input();
        input.setFromHash("0020b3455590664eea19ff78963a815077da0c3f7e4eebba2632ed479cdecae36233");
        input.setFromIndex(1);
        //input.setAddress("NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM");
        input.setValue(200000 * 100000000L);
        inputs.add(input);

        DepositInfo info = new DepositInfo();
        info.setAddress("NseWwCKxV1ToMCZnYHbFrEmKgU7p4fBm");
        info.setDeposit(200000 * 100000000L);
        info.setAgentHash("0020b3455590664eea19ff78963a815077da0c3f7e4eebba2632ed479cdecae36233");

        Na fee = Na.valueOf(1000000L);
        Result result = NulsSDKTool.createDepositTransaction(info, inputs, fee);
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

    @Test
    public void TestCreateMSAccountCancelDeposit(){
        Output output = new Output();
        output.setTxHash("0020b3455590664eea19ff78963a815077da0c3f7e4eebba2632ed479cdecae36233");
        output.setIndex(0);
        output.setValue(20000000000000L);
        output.setLockTime(-1);
        output.setAddress("NseWwCKxV1ToMCZnYHbFrEmKgU7p4fBm");
        Result result = NulsSDKTool.createCancelDepositTransaction(output);

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

    /*public String TestSignMultiTransaction(String txHex,List<String>priKeys,List<String>passwords){
        Result  result = NulsSDKTool.signMultiTransaction(txHex, priKeys,passwords);
        Map<String, Object> map = (Map<String, Object>) result.getData();
        return (String) map.get("value");
    }*/

}
