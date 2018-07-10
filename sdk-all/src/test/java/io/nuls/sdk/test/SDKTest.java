package io.nuls.sdk.test;

import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.Output;
import io.nuls.sdk.consensus.model.AgentInfo;
import io.nuls.sdk.consensus.model.DepositInfo;
import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.model.Account;
import io.nuls.sdk.core.model.Deposit;
import io.nuls.sdk.core.model.Na;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.tool.NulsSDKTool;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SDKTest {

/*
    @Test
    public void testAccount() {
        SDKBootstrap.init();
        Result result1 = NulsSDKTool.createOfflineAccount();
        Account account = (Account) result1.getData();
        System.out.println(account.getAddress().getBase58());
    }

    private static String address = null;
    private static String addressPwd = null;

NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM
630c2e83e40dc5683774cc31e1a291b46b409f01f9685e92055a367e81ae48c0

Nsdz8mKKFMehRDVRZFyXNuuenugUYM7M


    @BeforeClass
    public static void init(){
        SDKBootstrap.init();

        Result result1 = NulsSDKTool.createAccount();
        address = (String)((List)((Map)result1.getData()).get("list")).get(0);

        Result result2 = NulsSDKTool.createAccount("nuls123456");
        addressPwd = (String)((List)((Map)result2.getData()).get("list")).get(0);
    }



    @Test
    public void testAccount() {
        Result result1 = NulsSDKTool.createAccount();
        Result result2 = NulsSDKTool.createAccount(3);
        Result result3 = NulsSDKTool.createAccount("nuls123456");
        Result result4 = NulsSDKTool.createAccount(3, "nuls123456");
        String addr = (String)((List)((Map)result1.getData()).get("list")).get(0);

        Result result5 = NulsSDKTool.getAccount(addr);
        Result result6 = NulsSDKTool.createOfflineAccount();
        Result result7 = NulsSDKTool.createOfflineAccount(3);
        Result result8 = NulsSDKTool.createOfflineAccount("nuls123456");
        Result result9 = NulsSDKTool.createOfflineAccount(3, "nuls123456");


        Result result10 = NulsSDKTool.setPassword(addr, "nuls111111");
        Result result11 = NulsSDKTool.resetPassword(addr, "nuls111111", "nuls123456");
        Result result12 = NulsSDKTool.removeAccount(addr, "nuls123456");
        Result result13 = NulsSDKTool.setAlias(addr, "charlie", "nuls123456");
        Result result14 = NulsSDKTool.setPasswordOffline(
                    "6HgGtPyfXGicocJhpobJx9VwZGcY7rLv",
                    "31954ab587546bc3d513568f7d1eb5942ff6d6c9aecf61f98b63fbb8fefdd578",
                    "nuls123456");
        Result result15 = NulsSDKTool.resetPasswordOffline(
                "6HgGtPyfXGicocJhpobJx9VwZGcY7rLv",
                "df66803f61cd91e011ebd8ef59bf6a08c22c6c580219bb868040c4f1e2d52a4a4a7a8d40fae83de1175688b5529f878e",
                "nuls123456", "nuls111111");

        try {
            System.out.println(JSONUtils.obj2json(result6));
            System.out.println(JSONUtils.obj2json(result14));
            System.out.println(JSONUtils.obj2json(result15));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(result1.isSuccess());
        assertTrue(result2.isSuccess());
        assertTrue(result3.isSuccess());
        assertTrue(result4.isSuccess());
        assertTrue(result5.isSuccess());
        assertTrue(result6.isSuccess());
        assertTrue(result7.isSuccess());
        assertTrue(result8.isSuccess());
        assertTrue(result9.isSuccess());
        assertTrue(result10.isSuccess());
        assertTrue(result11.isSuccess());
        assertTrue(result12.isSuccess());
        assertFalse(result13.isSuccess());
        assertFalse(result14.isSuccess());
        assertFalse(result15.isSuccess());
    }

    @Test
    public void backupAccount() {
        Result result1 = NulsSDKTool.backupAccount(address, "");
        Result result2 = NulsSDKTool.backupAccount(addressPwd, "", "nuls123456");
        assertTrue(result1.isSuccess());
        assertTrue(result2.isSuccess());
    }

    @Test
    public void operateAccount() {
        Result result1 = NulsSDKTool.getAccountList(1,99);

        //Result result2 = NulsSDKTool.getAliasFee(address, "charlie");
        //Result result3 = NulsSDKTool.getAddressByAlias("charlie");
        Result result2 = NulsSDKTool.getPrikey(address);
        Result result3 = NulsSDKTool.getPrikey(addressPwd, "nuls123456");
        Result result4 = NulsSDKTool.isAliasUsable("charlie");
        Result result5 = NulsSDKTool.importAccountByKeystore("/Users/lichao/Downloads/6HgcJ8fvWkzxPQL3Yf3JXqGLJWykbnB1.accountkeystore",
                "nuls123456", true);
        Result result6 = NulsSDKTool.importAccountByKeystore("/Users/lichao/Downloads/6Hga5EqmGiGM4iMb8ntwBN3Ecx4D933u.accountkeystore",
                 true);
        Result result7 = NulsSDKTool.isEncrypted(addressPwd);
        Result result8 = NulsSDKTool.getWalletTotalBalance();
        Result result9 = NulsSDKTool.getAssets(address);

        try {
            System.out.println(JSONUtils.obj2json(result1));
            System.out.println(JSONUtils.obj2json(result2));
            System.out.println(JSONUtils.obj2json(result3));
            System.out.println(JSONUtils.obj2json(result4));
            System.out.println(JSONUtils.obj2json(result5));
            System.out.println(JSONUtils.obj2json(result6));
            System.out.println(JSONUtils.obj2json(result7));
            System.out.println(JSONUtils.obj2json(result8));
            System.out.println(JSONUtils.obj2json(result9));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(result1.isSuccess());
        assertTrue(result2.isSuccess());
        assertTrue(result3.isSuccess());
        assertTrue(result4.isSuccess());
        assertTrue(result5.isSuccess());
        assertTrue(result6.isSuccess());
        assertTrue(result7.isSuccess());
        assertTrue(result8.isSuccess());
        assertTrue(result9.isSuccess());
    }
    @Test
    public void tx(){
        Result result1 = NulsSDKTool.transfer("6HgaqsowQbVM8AXbHbssSAAHddeypwcc",
                    addressPwd, "nuls123456",
                99900000000L,"转账");
        //0020a467827d5f06feb3e78b4603eb03677711219cb5232d145b3e9d4ab48a3eb366
        Result result2 = NulsSDKTool.getTxByHash("0020a467827d5f06feb3e78b4603eb03677711219cb5232d145b3e9d4ab48a3eb366");
        Result result3 = NulsSDKTool.getBalance("6HgaqsowQbVM8AXbHbssSAAHddeypwcc");

        try {
            System.out.println(JSONUtils.obj2json(result1));
            System.out.println(JSONUtils.obj2json(result2));
            System.out.println(JSONUtils.obj2json(result3));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(result1.isSuccess());
        assertTrue(result2.isSuccess());
        assertTrue(result3.isSuccess());

    }

    @Test
    public void blockAccount() {
        Result result1 = NulsSDKTool.getBlockHeader(10);
        String hash = ((BlockHeader)result1.getData()).getHash();

        Result result2 = NulsSDKTool.getBlockHeader(hash);
        Result result3 = NulsSDKTool.getBlock(10);
        Result result4 = NulsSDKTool.getBlock(hash);
        Result result5 = NulsSDKTool.getNewestBlockHash();
        Result result6 = NulsSDKTool.getNewestBlockHeader();
        Result result7 = NulsSDKTool.getNewestBlockHight();

        assertTrue(result1.isSuccess());
        assertTrue(result2.isSuccess());
        assertTrue(result3.isSuccess());
        assertTrue(result4.isSuccess());
        assertTrue(result5.isSuccess());
        assertTrue(result6.isSuccess());
        assertTrue(result7.isSuccess());
    }



    @Test
    public void testBlock() {
        SDKBootstrap.init();
        Result result = NulsSDKTool.getBlock(4829);
        System.out.println(result.isSuccess());
    }

    NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM
630c2e83e40dc5683774cc31e1a291b46b409f01f9685e92055a367e81ae48c0

0020b3455590664eea19ff78963a815077da0c3f7e4eebba2632ed479cdecae36233


    @Test
    public void testAgentTransaction() {
        SDKBootstrap.init();

        List<Input> inputs = new ArrayList<>();
        Input input = new Input();
        input.setFromHash("002072d5df7545bfa20c26222b0f9a498c23dd91e9ba94027a2c8be47c5e8243373e");
        input.setFromIndex(0);
        input.setAddress("NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM");
        input.setValue(2035999999500000L);
        inputs.add(input);


        AgentInfo info = new AgentInfo();
        info.setAgentAddress("NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM");
        info.setPackingAddress("NsduaufRwHptedaJZwfAodWAb9XVFESx");
        info.setDeposit(200000 * 100000000L);
        info.setCommissionRate(10.0);

        Na fee = Na.valueOf(1000000L);
        Result result = NulsSDKTool.createAgentTransaction(info, inputs, fee);
        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        result = NulsSDKTool.signTransaction(txHex, "630c2e83e40dc5683774cc31e1a291b46b409f01f9685e92055a367e81ae48c0", "NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM", null);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");

        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.isSuccess());
    }


    @Test
    public void testDepositTransaction() {
        SDKBootstrap.init();

        List<Input> inputs = new ArrayList<>();
        Input input = new Input();
        input.setFromHash("0020b3455590664eea19ff78963a815077da0c3f7e4eebba2632ed479cdecae36233");
        input.setFromIndex(1);
        input.setAddress("NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM");
        input.setValue(2015999998500000L);
        inputs.add(input);

        DepositInfo info = new DepositInfo();
        info.setAddress("NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM");
        info.setDeposit(300000 * 100000000L);
        info.setAgentHash("0020b3455590664eea19ff78963a815077da0c3f7e4eebba2632ed479cdecae36233");

        Na fee = Na.valueOf(1000000L);
        Result result = NulsSDKTool.createDepositTransaction(info, inputs, fee);
        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        result = NulsSDKTool.signTransaction(txHex, "630c2e83e40dc5683774cc31e1a291b46b409f01f9685e92055a367e81ae48c0", "NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM", null);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");

        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.getData());
    }

    @Test
    public void testCancelDeposit() {
        SDKBootstrap.init();

        Output output = new Output();
        output.setTxHash("0020b3455590664eea19ff78963a815077da0c3f7e4eebba2632ed479cdecae36233");
        output.setIndex(0);
        output.setValue(20000000000000L);
        output.setLockTime(-1);
        output.setAddress("NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM");
        Result result = NulsSDKTool.createCancelDepositTransaction(output);

        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        result = NulsSDKTool.signTransaction(txHex, "630c2e83e40dc5683774cc31e1a291b46b409f01f9685e92055a367e81ae48c0", "NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM", null);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");

        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.getData());
    }

    @Test
    public void testStopAgent() {
        SDKBootstrap.init();

        Output output = new Output();
        output.setTxHash("0020b3455590664eea19ff78963a815077da0c3f7e4eebba2632ed479cdecae36233");
        output.setIndex(0);
        output.setValue(20000000000000L);
        output.setLockTime(-1);
        output.setAddress("NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM");
        Result result = NulsSDKTool.createStopAgentTransaction(output);

        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        result = NulsSDKTool.signTransaction(txHex, "630c2e83e40dc5683774cc31e1a291b46b409f01f9685e92055a367e81ae48c0", "NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM", null);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");

        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.getData());
    }

    @Test
    public void testTransaction() {

        SDKBootstrap.init();

        List<Input> inputs = new ArrayList<>();
        Input input = new Input();
        input.setFromHash("00202731a61925edaa12cff9646105f5edf56daeb72a3d0d19381d8f94794d485f38");
        input.setFromIndex(1);
        input.setAddress("Nse5x9foSzFjuwkwZLSvSjAHHLVf3MKJ");
        input.setValue(1000000000000000L);
        inputs.add(input);

        List<Output> outputs = new ArrayList<>();
        Output output = new Output();
        output.setAddress("NsdwUo8XU52DtB9Zqjo2YkuLBW8VhGaQ");
        output.setIndex(0);
        output.setLockTime(0);
        output.setValue(1230000000L);
        outputs.add(output);

        output = new Output();
        output.setAddress("Nse5x9foSzFjuwkwZLSvSjAHHLVf3MKJ");
        output.setIndex(0);
        output.setLockTime(0);
        output.setValue(1000000000000000L - 1230000000L - 10000000L);
        outputs.add(output);

        Result result = NulsSDKTool.createTransaction(inputs, outputs, "abcd");
        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        result = NulsSDKTool.signTransaction(txHex, "008e2b5c10370a46f72552b3b69c4d56bfd000e584134d1159157c811f53366307", "Nse5x9foSzFjuwkwZLSvSjAHHLVf3MKJ", null);
        map = (Map<String, Object>) result.getData();
        String sign = (String) map.get("value");

        result = NulsSDKTool.broadcastTransaction(sign);
        System.out.println(result.isSuccess());
    }

        @Test
    public void testDeposit() {
        SDKBootstrap.init("192.168.1.163", "6001");
        String address = "NsdyD94pXWpxZudbtJ4zpkBHhh8XmBQA";
        Result result = NulsSDKTool.getDeposits(address, 1, 10);

        System.out.println(result.isSuccess());
    }
    */
}
