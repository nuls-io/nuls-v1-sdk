package io.nuls.sdk.test;

import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.tool.NulsSDKTool;
import org.junit.Test;

public class SDKTest {

    @Test
    public void testAccount() {
        Result result = NulsSDKTool.createOfflineAccount();
        System.out.println(result.isSuccess());
    }
}
