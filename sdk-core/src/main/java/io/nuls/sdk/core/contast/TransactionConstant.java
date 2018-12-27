package io.nuls.sdk.core.contast;

public interface TransactionConstant {

    int TX_TYPE_COINBASE = 1;
    int TX_TYPE_TRANSFER = 2;
    int TX_TYPE_ALIAS = 3;
    int TX_TYPE_REGISTER_AGENT = 4;
    int TX_TYPE_JOIN_CONSENSUS = 5;
    int TX_TYPE_CANCEL_DEPOSIT = 6;
    int TX_TYPE_YELLOW_PUNISH = 7;
    int TX_TYPE_RED_PUNISH = 8;
    int TX_TYPE_STOP_AGENT = 9;

    /**
     * CONTRACT
     */
    int TX_TYPE_CREATE_CONTRACT = 100;
    int TX_TYPE_CALL_CONTRACT = 101;
    int TX_TYPE_DELETE_CONTRACT = 102;
    int TX_TYPE_CONTRACT_TRANSFER = 103;
}
