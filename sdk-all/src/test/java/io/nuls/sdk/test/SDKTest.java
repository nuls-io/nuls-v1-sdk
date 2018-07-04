package io.nuls.sdk.test;

import org.junit.Test;

public class SDKTest {

/*
    private static String address = null;
    private static String addressPwd = null;


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
    }*/

    @Test
    public void testTransaction() {
//        List<Input>
//        NulsSDKTool.createTransaction()
    }

}
