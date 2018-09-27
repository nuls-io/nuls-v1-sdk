package io.nuls.sdk.account.service.impl;

import io.nuls.sdk.account.model.AccountInfo;
import io.nuls.sdk.core.SDKBootstrap;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.utils.AccountTool;
import io.nuls.sdk.core.utils.AddressTool;
import io.nuls.sdk.tool.NulsSDKTool;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class AccountServiceImplTest {

    @Before
    public void setUp() throws Exception {
        SDKBootstrap.init("127.0.0.1", "8017");
    }

    @Test
    public void getInstance() {
        assertNotNull("Instance can not be null", AccountServiceImpl.getInstance());
    }

    @Test
    public void createOneAccountWithoutPassword() {
        Result result = NulsSDKTool.createAccount();
        assertTrue("response must be true", result.isSuccess());
        assertNotNull("response data can not be null", result.getData());
        Map<String, Object> resp = (Map<String, Object>) result.getData();
        List<String> accountAddressList = (List<String>) resp.get("list");
        assertNotNull("response must contains list", accountAddressList);
        assertEquals("response account must be 1", 1, accountAddressList.size());

        for (String addr : accountAddressList) {
            assertTrue("response must an correct address", AddressTool.validAddress(addr));
        }
    }

    @Test
    public void createOneAccountWithPassword() {
        String password = ":8!3#15TXUQVRSZ";
        Result result = NulsSDKTool.createAccount(1, password);
        assertTrue("response must be true", result.isSuccess());
        assertNotNull("response data can not be null", result.getData());
        Map<String, Object> resp = (Map<String, Object>) result.getData();
        List<String> accountAddressList = (List<String>) resp.get("list");
        assertNotNull("response must contains list", accountAddressList);
        assertEquals("response account must be 1", 1L, accountAddressList.size());

        for (String addr : accountAddressList) {
            assertTrue("response must an correct address", AddressTool.validAddress(addr));
        }
    }

    @Test
    public void createSomeAccountWithPassword() {
        Result result = NulsSDKTool.createAccount(30);
        assertTrue("response must be true", result.isSuccess());
        assertNotNull("response data can not be null", result.getData());
        Map<String, Object> resp = (Map<String, Object>) result.getData();
        List<String> accountAddressList = (List<String>) resp.get("list");
        assertNotNull("response must contains list", accountAddressList);
        assertEquals("response account must be 30", 30L, accountAddressList.size());

        for (String addr : accountAddressList) {
            assertTrue("response must an correct address", AddressTool.validAddress(addr));
        }
    }

    @Test
    public void createSomeAccountWithoutPassword() {
        Result result = NulsSDKTool.createAccount(18);
        assertTrue("response must be true", result.isSuccess());
        assertNotNull("response data can not be null", result.getData());
        Map<String, Object> resp = (Map<String, Object>) result.getData();
        List<String> accountAddressList = (List<String>) resp.get("list");
        assertNotNull("response must contains list", accountAddressList);
        assertEquals("response account must be 18", 18L, accountAddressList.size());

        for (String addr : accountAddressList) {
            assertTrue("response must an correct address", AddressTool.validAddress(addr));
        }
    }

    @Test
    public void createAccountCorrectNumber() {
        Result result = NulsSDKTool.createAccount(0);
        assertFalse("create zero account is't illegal", result.isSuccess());
        assertTrue("create zero account is't illegal", result.isFailed());

        result = NulsSDKTool.createAccount(AccountTool.CREATE_MAX_SIZE + 1);
        assertFalse("create more that " + AccountTool.CREATE_MAX_SIZE + " account is't illegal", result.isSuccess());
        assertTrue("create more that " + AccountTool.CREATE_MAX_SIZE + " account is't illegal", result.isFailed());
    }

    @Test
    public void createOfflineAccount() {
        Result result = NulsSDKTool.createOfflineAccount();
        assertTrue("response must be true", result.isSuccess());
        assertNotNull("response data can not be null", result.getData());
        Map<String, Object> resp = (Map<String, Object>) result.getData();
        List<AccountInfo> accountInfoList = (List<AccountInfo>) resp.get("list");
        assertNotNull("response must contains list", accountInfoList);
        assertEquals("response account must be 1", 1L, accountInfoList.size());
        for (AccountInfo accountInfo : accountInfoList) {
            assertTrue("address must correct", AddressTool.validAddress(accountInfo.getAddress()));
            assertNotNull("private key can not be null", accountInfo.getPriKey());
            assertNotNull("public key can not be null", accountInfo.getPubKey());
        }
    }

    @Test
    public void createMSAccount() {
        List<String> pubKeys = Arrays.asList(
                "02c7b2761a51c2a2ddecf41021d67ce2cf86fdc6dd44bac2742e3025917e7a749a",
                "023cee4d977974293ec436251e5c03840f5110e1c5155e4f357099404bd7be5ee3",
                "03052f7ccfc4c87e5d2454d846c78fdc02fcd480b234735c1724741d3e04be2fc4"
        );
        Result result = NulsSDKTool.createMSAccount(pubKeys, 2);
        assertTrue("Create multiple signature must success", result.isSuccess());
        Map<String, Object> resp = (Map<String, Object>) result.getData();
        assertNotNull("Create MSAccount must response value", resp);
        assertEquals("Create algorithm error", "NseVy6D3xAhwfYXi9Cn7YajG1QXJxN43", resp.get("address"));
        assertEquals("Response Pub Key List", pubKeys, resp.get("pubKeys"));
        assertEquals("Threshold must be equal", 2, resp.get("threshold"));

        result = NulsSDKTool.createMSAccount(pubKeys, 3);
        assertTrue("Create multiple signature must success", result.isSuccess());
        resp = (Map<String, Object>) result.getData();
        assertNotNull("Create MSAccount must response value", resp);
        assertEquals("Create algorithm error", "NseTwZQk1oM5vzKVBYhch6JF4tu2TDWd", resp.get("address"));
        assertEquals("Threshold must be equal", 3, resp.get("threshold"));
    }
}