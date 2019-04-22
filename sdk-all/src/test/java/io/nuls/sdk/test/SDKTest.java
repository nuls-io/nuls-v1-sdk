package io.nuls.sdk.test;

import io.nuls.sdk.account.model.AccountInfo;
import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.Output;
import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.contast.SDKConstant;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.model.Account;
import io.nuls.sdk.core.model.JsonRPCResult;
import io.nuls.sdk.core.model.Na;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.model.transaction.Transaction;
import io.nuls.sdk.core.model.transaction.TransferTransaction;
import io.nuls.sdk.core.utils.JSONUtils;
import io.nuls.sdk.core.utils.NulsByteBuffer;
import io.nuls.sdk.core.utils.TimeService;
import io.nuls.sdk.core.utils.TransactionFeeCalculator;
import io.nuls.sdk.tool.NulsSDKTool;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SDKTest {


    /*
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
        public void v1(){
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
        public void testDeposit() {
            SDKBootstrap.init("192.168.1.163", "6001");
            String address = "NsdyD94pXWpxZudbtJ4zpkBHhh8XmBQA";
            Result result = NulsSDKTool.getDeposits(address, 1, 10);

            System.out.println(result.isSuccess());
        }


        @Test
        public void testBroadcast() {
            SDKBootstrap.init();
            String txHex = "0600674944a76401000020d9e55d433903622f76cf3387789a713a20a9a993a06aa253e734445bf5eb720301230020d9e55d433903622f76cf3387789a713a20a9a993a06aa253e734445bf5eb7203000000000000000000ffffffffffff011701000144fdd048f38e1b0c0a54124b6d626fe1791c592fc0bdf0ffffffffff0000000000006a21037281566ae1be0f64a3241faaa32093a105cb1ecd470cc6d8d20b5856142f346800463044022033d8537b502dccaa60d2706283d9801b9ae86e6b44c004a296e6ba9b3f331f9e022020596bcb78512cecdb07b5a0053b4c653964cdd07947bab1c301bef54a13e4cc";
            Result result = NulsSDKTool.broadcastTransaction(txHex);
            System.out.println(result.isSuccess());
        }

        @Test
        public void testGetBlock() {
            SDKBootstrap.init("192.168.1.109", "8001");
            Result result = NulsSDKTool.getBlock(53118);
            System.out.println(result.isSuccess());

        }

        @Test
        public void testGetAgentDeposit() {
            SDKBootstrap.init();
            Result result = NulsSDKTool.getAgentDeposits("0020dfef1f368771fbdb5a82859c0eea74fa305b298267c6dc0b87d160634ea2feb8", 1, 10);
        }






        @Test
        public void testTransaction() {
            SDKBootstrap.init();
            String txHex = "0200857579d5640100ffffffff0123002095a9bb894601e3ea6c46cd1961573d5023a067ce52735418b24d4c94234d1e0b0000c2eb0b0000000000000000000002170423016413d985f1bdf75e023f208ab99ec7543150c17840420f000000000000000000000017042301411e1faaef10846d1dadb7cb48337eb253320cc120f9da0b0000000000000000000000";
            String priKey = "0a710f7140e484c7a8e8902156e2b6756966ddc4ede6bdbf2f5e63cbccfcec312dccc24fd641953b5357603584c1c011";
            String address = "Nsdx8A7vi24zMGtnonJ3pGPFDxWwkaBn";
            String password = "nuls123456";


            Result result = NulsSDKTool.signTransaction(txHex, priKey, address, password);
            Map<String, Object> map = (Map<String, Object>) result.getData();
            String sign = (String) map.get("value");

            result = NulsSDKTool.broadcastTransaction(sign);
            System.out.println(result.isSuccess());
        }

    @Test
    public void test1() {
        SDKBootstrap.init("127.0.0.1", "6001");

        List<TransferFrom> inputs = new ArrayList<>();
        TransferFrom from1 = new TransferFrom("Nse5oPtPjgbyHujSxXu2YbWRmmf3ksCo", "abcd1234");
        inputs.add(from1);

        TransferFrom from2 = new TransferFrom("Nsdz9go1hcQrrssG2Kqu57h6v9rH8puC", "abcd5678");
        inputs.add(from2);

        List<TransferTo> outputs = new ArrayList<>();
        TransferTo to1 = new TransferTo("Nse7N3aVXqaKdECrepueKMYCfcXrwLxE", 10000000000L);
        outputs.add(to1);

        TransferTo to2 = new TransferTo("NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM", 10000000000L);
        outputs.add(to2);

        String remark = "test multipleAddressTransfer";

        Result result = NulsSDKTool.multipleAddressTransfer(inputs, outputs, remark);

    }

    @Test
    public void testTransaction() {

        SDKBootstrap.init("127.0.0.1", "6001");

        List<Input> inputs = new ArrayList<>();
        Input input = new Input();
        input.setFromHash("002058e49a9a865910dba8783da174d841c4345af8bd56edbb1fa8c6dc85e5dff9d2");
        input.setFromIndex(1);
        input.setAddress("Nse5oPtPjgbyHujSxXu2YbWRmmf3ksCo");
        input.setValue(176248794886L);

        inputs.add(input);

        List<Output> outputs = new ArrayList<>();
        Output output = new Output();
        output.setAddress("Nsdz9go1hcQrrssG2Kqu57h6v9rH8puC");
        output.setIndex(0);
        output.setLockTime(0);
        output.setValue(10000000L);
        outputs.add(output);

        output = new Output();
        output.setAddress("Nse5oPtPjgbyHujSxXu2YbWRmmf3ksCo");
        output.setIndex(1);
        output.setLockTime(0);
        output.setValue(176248794886L - 10000000L - 10000000L);
        outputs.add(output);

        Result result = NulsSDKTool.createTransaction(inputs, outputs, "abcd");
        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        String priKey = "08f3102c7f9738ac30258c1378d35ef4d65694";
        String address = "Nse5oPtPjgbyHujSxXu2YbWRmmf3ksCo";

        result = NulsSDKTool.signTransaction(txHex, priKey, address, null);
        map = (Map<String, Object>) result.getData();
        String signTxHex = (String) map.get("value");
        result = NulsSDKTool.validateTransaction(signTxHex);
        //  result = NulsSDKTool.broadcastTransaction(signTxHex);
        System.out.println(result.isSuccess());
    }


    @Test
    public void testSignTransaction() {
        Result result = NulsSDKTool.getAddressByEncryptedPriKey("0a710f7140e484c7a8e8902156e2b6756966ddc4ede6bdbf2f5e63cbccfcec312dccc24fd641953b5357603584c1c011", "nuls123456");
        System.out.println(result.isSuccess());
    }


 */

    @Test
    public void testvaliDate() {
        SDKBootstrap.init("192.168.1.127", "8001", 261);
        String txHex = "0200639e127a69010d74657374207472616e73666572ffffffff01230020a123d3c1b4b381ece6d2ec926aee5d4fd387deff638433da865c141ba1186ffe01516b320000000000000000000000011705010121c43883f9b14ee84b469a8cb9d056f94e2deae300b4c404000000000000000000006a21032dd7aaff8d2c3ae6597877b67f87702f44f5998b3da4459ddeb6eec8d39171c900463044022058f0710594f1d8abc7da65495d3c164d8e5df446d00ccd9b6e38e95a9c069735022012db02b1d125e838adfc6822e6d35626c2fea1c9a5eecae040ba962ad1b403b7";
        Result result = NulsSDKTool.validateTransaction(txHex);
        System.out.println(result.getData());
    }

    @Test
    public void testSyncBlock() {
        SDKBootstrap.init("127.0.0.1", "8001", 261);
//        Result result = NulsSDKTool.getBlockWithBytes(0);
//        System.out.println(result.isSuccess());
    }


    @Test
    public void testCreateAccountAndCheckPassword() {
        SDKBootstrap.init("127.0.0.1", "8001", 261);
        String password = "abcd1234";
        Result result = NulsSDKTool.createOfflineAccount(1, password);
        Map map = (Map) result.getData();
        List<AccountInfo> accountInfos = (List<AccountInfo>) map.get("list");
        AccountInfo accountInfo = accountInfos.get(0);

        String address = accountInfo.getAddress();
        String encryptedPriKey = accountInfo.getEncryptedPriKey();

//         address : TTasM8iokK7PQDCMJMBmXrmtkJ1CFiRa
//         encryptedPriKey: 1a182885deca70dcf893dffc1a70b71ca3c390b347c53e70e41ee4f95fb7250fc1a671cc94aec4bfe34fc69ec22b53ce


        result = NulsSDKTool.getPrikeyOffline(encryptedPriKey, password);
        map = (Map) result.getData();
        String priKey = (String) map.get("value");
        System.out.println(priKey);
    }

    @Test
    public void testTransaction() throws Exception {
        SDKBootstrap.init("127.0.0.1", "8001", 261, "http://116.62.135.185:8081");

        String fromAddress = "TTatEiRFHJPdwNNoLhMraez4yoXrSQab";
        String toAddress = "TTavdER27gqugcDbMseekdesdf6Qd9cB";
        long transferAmount = 10000000L;
        String remark = "transfer 1 nuls";

        //find fromAddress's utxo
        List<Input> inputs = null;
        JsonRPCResult jsonRPCResult = NulsSDKTool.getUtxo(fromAddress, transferAmount);
        if (jsonRPCResult.getResult() != null) {
            inputs = (List<Input>) jsonRPCResult.getResult();
        }

        // totalFromNuls always >= transferAmount;
        long totalFromNuls = 0;
        for (Input input : inputs) {
            totalFromNuls += input.getValue();
        }

        //create output
        List<Output> outputs = new ArrayList<>();
        Output output = new Output();
        output.setAddress(toAddress);
        output.setIndex(0);
        output.setLockTime(0);
        output.setValue(transferAmount);
        outputs.add(output);

        /***Calculation fee*/
        int size = 124 + 50 * inputs.size() + 38 * outputs.size() + remark.getBytes().length;
        Na fee = TransactionFeeCalculator.getTransferFee(size, 1);
        //create change
        output = new Output();
        outputs.add(output);
        output.setAddress(fromAddress);
        output.setIndex(1);
        output.setLockTime(0);
        output.setValue(totalFromNuls - transferAmount - fee.getValue());

        /**create transferTx**/
        Result result = NulsSDKTool.createTransaction(inputs, outputs, remark);
        Map<String, Object> map = (Map<String, Object>) result.getData();
        String txHex = (String) map.get("value");

        byte[] txBytes = Hex.decode(txHex);
        Transaction tx = new TransferTransaction();
        tx.parse(new NulsByteBuffer(txBytes));
        System.out.println(tx.getHash().getDigestHex());


        /**prikey of fromAddress*/
        String priKey = "abacb54e596ae22ccde6bbf1bd2eb968dca0a6aa98ded7e383ffed4cd9d7d7db";
        /**sign tx**/
        result = NulsSDKTool.signTransaction(txHex, priKey, fromAddress, ":8!3#15TXUQVRSZ");
        map = (Map<String, Object>) result.getData();
        String signTxHex = (String) map.get("value");

        byte[] txBytes1 = Hex.decode(txHex);
        Transaction tx1 = new TransferTransaction();
        tx1.parse(new NulsByteBuffer(txBytes1));
        System.out.println(tx1.getHash().getDigestHex());

        /** validate**/
        result = NulsSDKTool.validateTransaction(signTxHex);
        System.out.println(JSONUtils.obj2json(result));
    }


    @Test
    public void testValidateTx() {
        SDKBootstrap.init("127.0.0.1", "6001", 8964);
        String txHex = "0200f09e52c4690100ffffffff01230020391106f46213f3b4385a1b68d46e060bde943c1c560108c2a2d344be3884b3240151a8fdfd3a60000000000000000004170423012e8f805b2c4c7eaf985faf2195ead51f44a3e48cc047d5a90a000000000000000000170423012e8f805b2c4c7eaf985faf2195ead51f44a3e48cc04fa3e304000000000000000000170423012e8f805b2c4c7eaf985faf2195ead51f44a3e48cc05e116a10000000000000000000170423017ee35f71f596572bb804902d690cb6c2fd370426712b72061b6000000000000000006b21028baee0195105f1046c7acbc9bd38d7bd587584d761b9fd1eb602fb903a1b667f004730450221009f746e15607291bbe8dc46793384631ff43dd426d0d5cde174ff0c54184bed540220319ac8166e70edbac783d4fce0dac9f85a612c01b16a59fc450a39e887621123";
        Result result = NulsSDKTool.validateTransaction(txHex);
        System.out.println(result.getData());
    }

    @Test
    public void timeServerTest() {
        TimeService.getInstance().start();

        System.out.println("-------");
    }

    @Test
    public void balanceTest() {
        SDKBootstrap.init("127.0.0.1", "8001", 261);
        Result result = NulsSDKTool.getAccountBalance("TTaqFxuD1xc6gpixUiMVQsjMZ5fdYJ2o");
        System.out.println(result.isSuccess());
    }

    @Test
    public void testUTXO() {
        SDKBootstrap.init("127.0.0.1", "8001", 8964);
        JsonRPCResult result = NulsSDKTool.getUtxo("Nse3KFUNQhCjadUeSE7VKMhJYdWqju9n", 1);
        if (result.getResult() != null) {
            List<Input> inputs = (List<Input>) result.getResult();
            System.out.println(inputs.size());
        }
        System.out.println(1);
    }

    @Test
    public void signTx() {
//        SDKConstant.DEFAULT_CHAIN_ID = (short) 261;
//        SDKConstant.NULSCAN_URL ="http://116.62.135.185:8081";
        String txHex = "6500586e8bff690100042301a0595e655ab120e0a60700183bdae56d6a3e7ab10423024b19f2f9975afdedda0396e854355c227a8359fa0000000000000000593e0000000000001900000000000000087472616e73666572000201204e73653757326476564b46543255596f617655347656466553594739726a62310103353030012300203103de09bf86168f0a0e71d05688d543d0e710ae4481abd95cdb163914a3a2490166a1600b000000000000000000000117042301a0595e655ab120e0a60700183bdae56d6a3e7ab11504590b0000000000000000000000";
        String priKey = "00d08673753875de9460a2d77282e17888ba49c2e45a5c3c6cf594f2d7eb2f4f6a";
        String address = "TTe3yoTsQ9C4WMHGCrk3QJM9hnfNVgFh";

        Result result = NulsSDKTool.signTransaction(txHex, priKey, address, null);
        Map map = (Map) result.getData();
        String signTxHex = (String) map.get("value");
        System.out.println(signTxHex);
    }

    @Test
    public void signHash() {
//        SDKConstant.DEFAULT_CHAIN_ID = (short) 261;
        String txHash = "0020d87cc1cc001a90d12f527243f665872527d07a1e94d5cf38158558d57292cc00";
        String priKey = "00d08673753875de9460a2d77282e17888ba49c2e45a5c3c6cf594f2d7eb2f4f6a";

        Result result = NulsSDKTool.signTransaction(txHash, priKey);
        Map map = (Map) result.getData();
        String signTxHex = (String) map.get("value");
        System.out.println(signTxHex);
    }
}