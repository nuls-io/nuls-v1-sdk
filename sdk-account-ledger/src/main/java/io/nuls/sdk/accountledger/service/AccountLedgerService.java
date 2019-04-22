package io.nuls.sdk.accountledger.service;

import io.nuls.sdk.accountledger.model.*;
import io.nuls.sdk.core.model.JsonRPCResult;
import io.nuls.sdk.core.model.Result;

import java.util.List;

/**
 * @author: Charlie
 */
public interface AccountLedgerService {

    /**
     * Get transaction details based on transaction hash
     *
     * @param hash Transaction hash
     * @return If the operation is successful, 'success' is true, and data is Transaction;
     * If the operation fails, "success" is false and the result has error information
     */
    Result getTxByHash(String hash);

    /**
     * Get transaction details based on transaction hash
     *
     * @param hash Transaction hash
     * @return If the operation is successful, 'success' is true, and data is Transaction;
     * If the operation fails, "success" is false and the result has error information
     */
    Result getTxWithBytesByHash(String hash);

    /**
     * Transfer
     *
     * @param address   Remittance account address
     * @param toAddress Beneficiary account Address
     * @param password  Remittance account password
     * @param amount    Transfer amount
     * @param remark    remark
     * @return If the operation is successful, 'success' is true
     * If the operation fails, "success" is false and the result has error information
     */
    Result transfer(String address, String toAddress, String password, long amount, String remark);

    /**
     * Transfer
     *
     * @param address   Remittance account address
     * @param toAddress Beneficiary account Address
     * @param amount    Transfer amount
     * @param remark    remark
     * @return If the operation is successful, 'success' is true
     * If the operation fails, "success" is false and the result has error information
     */
    Result transfer(String address, String toAddress, long amount, String remark);

    /**
     * sendToAddress
     *
     * @param address   Remittance account address
     * @param toAddress Beneficiary account Address
     * @param password  Remittance account password
     * @param amount    Transfer amount include fee
     * @param remark    remark
     * @return If the operation is successful, 'success' is true
     * If the operation fails, "success" is false and the result has error information
     */
    Result sendToAddress(String address, String toAddress, String password, long amount, String remark);

    /**
     * sendToAddress
     *
     * @param address   Remittance account address
     * @param toAddress Beneficiary account Address
     * @param amount    Transfer amount include fee
     * @param remark    remark
     * @return If the operation is successful, 'success' is true
     * If the operation fails, "success" is false and the result has error information
     */
    Result sendToAddress(String address, String toAddress, long amount, String remark);

    /***
     * multipleAddressTransfer
     * @param froms address and password for each address
     * @param tos tos toAddress and amount for each toAddress
     * @return
     */
    Result multipleAddressTransfer(List<TransferFrom> froms, List<TransferTo> tos, String remark);

    /**
     * Get account balance
     *
     * @param address address
     * @return If the operation is successful, 'success' is true, and data is BalanceInfo
     * If the operation fails, "success" is false and the result has error information
     */
    Result getBalance(String address);

    /**
     * Get account utxo
     *
     * @param address address
     * @param amount  amount
     * @return If the operation is successful, 'success' is true, and data is utxoList
     * If the operation fails, "success" is false and the result has error information
     */
    JsonRPCResult getUTXO(String address, long amount);

    /**
     * 创建交易
     * Create Transaction
     *
     * @param inputs  inputs
     * @param outputs outputs
     * @param remark  remark
     * @return Result
     */
    Result createTransaction(List<Input> inputs, List<Output> outputs, String remark);

    /**
     * 创建交易
     * Create Transaction
     *
     * @param address   Remittance account address
     * @param toAddress Beneficiary account Address
     * @param amount    Transfer amount
     * @param remark    remark
     * @param utxos     list of available utxo owned by the remittance account
     * @return If the operation is successful, 'success' is true
     * If the operation fails, "success" is false and the result has error information
     */
    Result createTransaction(String address, String toAddress, long amount, String remark, List<Input> utxos);

    /**
     * 创建多地址转账交易
     * Create Multiple Address Transaction
     *
     * @param inputs        inputs
     * @param nInputAccount nInputAccount
     * @param outputs       outputs
     * @param remark        remark
     * @return Result
     */
    Result createMultipleInputAddressTransaction(List<Input> inputs, int nInputAccount, List<Output> outputs, String remark);

    /**
     * 签名交易
     * Sign Transaction
     *
     * @param txHex    txHex
     * @param priKey   priKey
     * @param address  address
     * @param password password
     * @return Result
     */
    Result signTransaction(String txHex, String priKey, String address, String password);

    Result signHash(String hash, String priKey);
    /**
     * 广播交易
     * Broadcast Transaction
     *
     * @param txHex txHex
     * @return Result
     */
    Result broadcastTransaction(String txHex);

    /**
     * 验证交易
     *
     * @param txHex txHex
     * @return Result
     */
    Result validateTransaction(String txHex);

    /**
     * 签名 input 为多个地址的转账交易
     *
     * @param txHex     txHex
     * @param privKeys  privKeys
     * @param passwords passwords
     * @return
     */
    Result signMultipleAddressTransaction(String txHex, List<String> privKeys, List<String> passwords);

    /**
     * 创建多重签名转账交易
     *
     * @param inputs  inputs
     * @param outputs outputs
     * @param remark  remark
     * @return Result
     */
    Result createMSAccountTransferTransaction(MSAccount account, List<Input> inputs, List<Output> outputs, String remark);

    /**
     * 零钱换整
     *
     * @param inputs  inputs
     * @param address address
     * @return Result
     */
    Result createChangeCoinTransaction(List<Input> inputs, String address);

    /**
     * 多签账户交易签名
     *
     * @param txHex     txHex
     * @param privKeys  privKeys
     * @param passwords passwords
     * @return Result
     */
    Result signMSTransaction(String txHex, List<String> privKeys, List<String> passwords);
}
