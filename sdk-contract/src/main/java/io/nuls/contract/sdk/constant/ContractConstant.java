/**
 * 智能合约相关静态配置
 */
package io.nuls.contract.sdk.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ContractConstant{

    short MODULE_ID_CONTRACT = 10;

    /**
     * CONTRACT
     */
    int TX_TYPE_CREATE_CONTRACT = 100;
    int TX_TYPE_CALL_CONTRACT = 101;
    int TX_TYPE_DELETE_CONTRACT = 102;

    /**
     * contract transfer
     */
    int TX_TYPE_CONTRACT_TRANSFER = 103;
    long CONTRACT_TRANSFER_GAS_COST = 1000;

    String BALANCE_TRIGGER_METHOD_NAME = "_payable";
    String BALANCE_TRIGGER_METHOD_DESC = "() return void";

    String CONTRACT_CONSTRUCTOR = "<init>";


    String CALL = "call";
    String CREATE = "create";
    String DELETE = "delete";

    String GET = "get";

    String SEND_BACK_REMARK = "Contract execution failed, return funds.";

    long CONTRACT_CONSTANT_GASLIMIT = 1000000;
    long CONTRACT_CONSTANT_PRICE = 1;

    long MAX_GASLIMIT = 10000000;

    /**
     *
     */
    String CONTRACT_EVENT = "event";
    String CONTRACT_EVENT_ADDRESS = "contractAddress";
    String CONTRACT_EVENT_DATA = "payload";

    /**
     * NRC20
     */
    String NRC20_METHOD_NAME = "name";
    String NRC20_METHOD_SYMBOL = "symbol";
    String NRC20_METHOD_DECIMALS = "decimals";
    String NRC20_METHOD_TOTAL_SUPPLY = "totalSupply";
    String NRC20_METHOD_BALANCE_OF = "balanceOf";
    String NRC20_METHOD_TRANSFER = "transfer";
    String NRC20_METHOD_TRANSFER_FROM = "transferFrom";
    String NRC20_METHOD_APPROVE = "approve";
    String NRC20_METHOD_ALLOWANCE = "allowance";
    String NRC20_EVENT_TRANSFER = "TransferEvent";
    String NRC20_EVENT_APPROVAL = "ApprovalEvent";

    int CONTRACT_STATUS_FAILED = 0;
    int CONTRACT_STATUS_NORMAL = 1;
    int CONTRACT_STATUS_DELETED = 2;

    /**
     * 合约确认状态：0-未确认、1-已确认
     */
    int CONTRACT_STATUS_UNCONFIRMED = 0;
    int CONTRACT_STATUS_CONFIRMED = 1;

    /**
     * 合约交易状态：true-成功、false-失败
     */
    String CONTRACT_STATUS_SUCCESS = "true";
    String CONTRACT_STATUS_FAIL = "false";

    /**
     * 合约是否支持NRC20：0-否、1-是
     */
    int CONTRACT_NRC20_STATUS_NO = 0;
    int CONTRACT_NRC20_STATUS_YES = 1;

    /**
     * 合约交易类型编号
     */
    List<Integer> TX_TYPE_CONTRACT_LIST = new ArrayList<>(Arrays.asList(100, 101, 102, 103));
}
