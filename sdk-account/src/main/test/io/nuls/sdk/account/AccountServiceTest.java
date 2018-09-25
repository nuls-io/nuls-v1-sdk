package io.nuls.sdk.account;

import io.nuls.sdk.account.service.AccountService;
import io.nuls.sdk.account.service.impl.AccountServiceImpl;
import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.utils.JSONUtils;
import org.junit.Test;

/**
 * @author: Charlie
 * @date: 2018/9/25
 */

public class AccountServiceTest {

    @Test
    public void test(){

        /** ########################################################################################################### */
        /** ########################################################################################################### */
        SDKBootstrap.init("127.0.0.1", "7001");
        AccountService accountService = AccountServiceImpl.getInstance();
//        Result result1 = accountService.createAccount(1);
//        Result result2 = accountService.createAccount(1, "nuls111111");
//
//        Result result3 = accountService.getAliasFee("Nse1sPrrTFWzZiTsNjG33sgGYnge3xBF","charlie1");
//        Result result4 = accountService.backupAccount("Nse6rbpiaUTrkYqTRqCoJBYRzUUrbR5v", "/Users/lichao/Downloads", "nuls111111");
//        Result result5 = accountService.getAccount("NsdxG1k3ai41SNTqDun3x1zAZgbLMvNB");
//        Result result6 = accountService.getAccountList(1,10);
//        Result result7 = accountService.getAssets("Nse1sPrrTFWzZiTsNjG33sgGYnge3xBF");
//        Result result8 = accountService.setAlias("Nse1sPrrTFWzZiTsNjG33sgGYnge3xBF","charlie2");
//        Result result9 = accountService.getPrikey("Nsdy6KPkHbAz2PZv7VmNNxgfSwVqz3wZ", "nuls111111");
//        Result result10 = accountService.importAccountByKeystore("/Users/lichao/Downloads/Nse1sPrrTFWzZiTsNjG33sgGYnge3xBF.accountkeystore", "", true);
//        Result result11 = accountService.getAddressByEncryptedPriKey("870e7b0e4d46e9d7dd95d58e34f5c246e812c88734e1b6c27f0ee92609e77a28bfb0766eb2b0fd3bebc8f0d16d9499aa", "nuls111111");
        Result result12 = accountService.getAddressByPriKey("08a92d17d4743ac4e490d8ffd05298146e0dfcf771138176d329e183cac6eb3f");
//        Result result13 = accountService.getAddressByAlias("charlie2");
//        Result result14 = accountService.resetPassword("Nse6rbpiaUTrkYqTRqCoJBYRzUUrbR5v", "nuls111111", "nuls123456");
        Result result15 = accountService.createOfflineAccount(1);
        Result result16 = accountService.createOfflineAccount(1, "nuls123456");
        try {
//            System.out.println(JSONUtils.obj2json(result1));
//            System.out.println(JSONUtils.obj2json(result2));
//            System.out.println(JSONUtils.obj2json(result3));
//            System.out.println(JSONUtils.obj2json(result4));
//            System.out.println(JSONUtils.obj2json(result5));
//            System.out.println(JSONUtils.obj2json(result6));
//            System.out.println(JSONUtils.obj2json(result7));
//            System.out.println(JSONUtils.obj2json(result8));
//            System.out.println(JSONUtils.obj2json(result9));
//            System.out.println(JSONUtils.obj2json(result10));
//            System.out.println(JSONUtils.obj2json(result11));
            System.out.println(JSONUtils.obj2json(result12));
//            System.out.println(JSONUtils.obj2json(result13));
//            System.out.println(JSONUtils.obj2json(result14));
            System.out.println(JSONUtils.obj2PrettyJson(result15));
            System.out.println(JSONUtils.obj2PrettyJson(result16));
        } catch (Exception e) {
            e.printStackTrace();
        }
    /** ########################################################################################################### */
    /** ########################################################################################################### */

    }
}
