package io.nuls.sdk.contract.service.impl;

import io.nuls.sdk.contract.ContractUtil;
import io.nuls.sdk.contract.service.ContractService;
import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.TransactionCreatedReturnInfo;
import io.nuls.sdk.accountledger.utils.ConvertCoinTool;
import io.nuls.sdk.accountledger.utils.LedgerUtil;
import io.nuls.sdk.core.contast.SDKConstant;
import io.nuls.sdk.core.contast.TransactionErrorCode;
import io.nuls.sdk.core.model.*;
import io.nuls.sdk.core.model.transaction.CallContractTransaction;
import io.nuls.sdk.core.model.transaction.CreateContractTransaction;
import io.nuls.sdk.core.model.transaction.DeleteContractTransaction;
import io.nuls.sdk.core.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * contract sdk
 * Created by wangkun23 on 2018/10/8.
 */
public class ContractServiceImpl implements ContractService {

    final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    private static ContractService instance = new ContractServiceImpl();

    /**
     * instance
     *
     * @return
     */
    public static ContractService getInstance() {
        return instance;
    }

    /**
     * create contract transaction
     *
     * @param sender
     * @param gasLimit
     * @param price
     * @param contractCode
     * @param args
     * @param remark
     * @return
     */
    @Override
    public Result createContractTransaction(String sender, Long gasLimit, Long price, byte[] contractCode, Object[] args, String remark, List<Input> utxos) {
        try {
            Na value = Na.ZERO;
            long totalGas = LongUtils.mul(gasLimit, price);
            Na totalNa = Na.valueOf(totalGas);

            byte[] senderBytes = AddressTool.getAddress(sender);
            // 生成一个地址作为智能合约地址
            Address contractAddress = AccountTool.createContractAddress();
            byte[] contractAddressBytes = contractAddress.getAddressBytes();
            // 组装txData
            CreateContractData txData = new CreateContractData();
            txData.setSender(senderBytes);
            txData.setContractAddress(contractAddressBytes);
            txData.setValue(value.getValue());
            txData.setGasLimit(gasLimit);
            txData.setPrice(price);
            txData.setCodeLen(contractCode.length);
            txData.setCode(contractCode);
            if (args != null) {
                txData.setArgsCount((byte) args.length);
                if (args.length > 0) {
                    txData.setArgs(ContractUtil.twoDimensionalArray(args));
                }
            }
            /**
             * 组装交易数据
             */
            CreateContractTransaction tx = new CreateContractTransaction();
            if (StringUtils.isNotBlank(remark)) {
                try {
                    tx.setRemark(remark.getBytes(SDKConstant.DEFAULT_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            tx.setTime(TimeService.currentTimeMillis());
            tx.setTxData(txData);

            CoinData coinData = new CoinData();
            List<Coin> coinList = ConvertCoinTool.convertCoinList(utxos);
            CoinDataResult coinDataResult = TransactionTool.getCoinData(senderBytes, totalNa, tx.size(), TransactionFeeCalculator.MIN_PRECE_PRE_1024_BYTES, coinList);

            if (!coinDataResult.isEnough()) {
                return Result.getFailed(TransactionErrorCode.INSUFFICIENT_BALANCE);
            }
            coinData.setFrom(coinDataResult.getCoinList());
            // 找零的UTXO
            if (coinDataResult.getChange() != null) {
                coinData.getTo().add(coinDataResult.getChange());
            }
            tx.setCoinData(coinData);

            // 重置为0，重新计算交易对象的size
            tx.setSize(0);
            if (tx.getSize() > TransactionFeeCalculator.MAX_TX_SIZE) {
                return Result.getFailed(TransactionErrorCode.DATA_SIZE_ERROR);
            }

            TransactionCreatedReturnInfo returnInfo = LedgerUtil.makeReturnInfo(tx);
            Map<String, TransactionCreatedReturnInfo> map = new HashMap<>();
            map.put("value", returnInfo);
            return Result.getSuccess().setData(map);
        } catch (Exception e) {
            Log.error(e);
            return Result.getFailed(e.getMessage());
        }
    }

    /**
     * @param sender
     * @param value
     * @param gasLimit
     * @param price
     * @param contractAddress
     * @param methodName
     * @param methodDesc
     * @param args
     * @param remark
     * @param utxos
     * @return
     */
    @Override
    public Result callContractTransaction(String sender, Long longValue, Long gasLimit, Long price, String contractAddress, String methodName, String methodDesc, Object[] args, String remark, List<Input> utxos) {
        try {
            byte[] senderBytes = AddressTool.getAddress(sender);
            byte[] contractAddressBytes = AddressTool.getAddress(contractAddress);

            Na value;
            if (longValue == null) {
                value = Na.ZERO;
            } else {
                value = Na.valueOf(longValue);
            }

            CallContractTransaction tx = new CallContractTransaction();
            if (StringUtils.isNotBlank(remark)) {
                try {
                    tx.setRemark(remark.getBytes(SDKConstant.DEFAULT_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    Log.error(e);
                    throw new RuntimeException(e);
                }
            }
            tx.setTime(TimeService.currentTimeMillis());
            long gasUsed = gasLimit.longValue();
            Na imputedNa = Na.valueOf(LongUtils.mul(gasUsed, price));
            // 总花费
            Na totalNa = imputedNa.add(value);

            // 组装txData
            CallContractData callContractData = new CallContractData();
            callContractData.setContractAddress(contractAddressBytes);
            callContractData.setSender(senderBytes);
            callContractData.setValue(value.getValue());
            callContractData.setPrice(price.longValue());
            callContractData.setGasLimit(gasLimit.longValue());
            callContractData.setMethodName(methodName);
            callContractData.setMethodDesc(methodDesc);
            if (args != null) {
                callContractData.setArgsCount((byte) args.length);
                callContractData.setArgs(ContractUtil.twoDimensionalArray(args));
            }
            tx.setTxData(callContractData);

            CoinData coinData = new CoinData();
            // 向智能合约账户转账
            if (value.isGreaterThan(Na.ZERO)) {
                Coin toCoin = new Coin(contractAddressBytes, value);
                coinData.getTo().add(toCoin);
            }

            List<Coin> coinList = ConvertCoinTool.convertCoinList(utxos);
            CoinDataResult coinDataResult = TransactionTool.getCoinData(senderBytes, totalNa, tx.size() + coinData.size(), TransactionFeeCalculator.MIN_PRECE_PRE_1024_BYTES, coinList);

            if (!coinDataResult.isEnough()) {
                return Result.getFailed(TransactionErrorCode.INSUFFICIENT_BALANCE);
            }
            coinData.setFrom(coinDataResult.getCoinList());
            // 找零的UTXO
            if (coinDataResult.getChange() != null) {
                coinData.getTo().add(coinDataResult.getChange());
            }
            tx.setCoinData(coinData);

            // 重置为0，重新计算交易对象的size
            tx.setSize(0);
            if (tx.getSize() > TransactionFeeCalculator.MAX_TX_SIZE) {
                return Result.getFailed(TransactionErrorCode.DATA_SIZE_ERROR);
            }

            TransactionCreatedReturnInfo returnInfo = LedgerUtil.makeReturnInfo(tx);
            Map<String, TransactionCreatedReturnInfo> map = new HashMap<>();
            map.put("value", returnInfo);
            return Result.getSuccess().setData(map);
        } catch (Exception e) {
            Log.error(e);
            return Result.getFailed(e.getMessage());
        }
    }

    /**
     * delete smart contract
     *
     * @param sender
     * @param contractAddress
     * @param remark
     * @param utxos
     * @return
     */
    @Override
    public Result deleteContractTransaction(String sender, String contractAddress, String remark, List<Input> utxos) {

        try {
            DeleteContractTransaction tx = new DeleteContractTransaction();
            if (StringUtils.isNotBlank(remark)) {
                try {
                    tx.setRemark(remark.getBytes(SDKConstant.DEFAULT_ENCODING));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            tx.setTime(TimeService.currentTimeMillis());

            byte[] senderBytes = AddressTool.getAddress(sender);
            byte[] contractAddressBytes = AddressTool.getAddress(contractAddress);

            // 组装txData
            DeleteContractData deleteContractData = new DeleteContractData();
            deleteContractData.setContractAddress(contractAddressBytes);
            deleteContractData.setSender(senderBytes);
            tx.setTxData(deleteContractData);
            /**
             * calculator transfer fee
             */
            List<Coin> coinList = ConvertCoinTool.convertCoinList(utxos);

            CoinDataResult coinDataResult = TransactionTool.getCoinData(senderBytes, Na.ZERO, tx.size(), TransactionFeeCalculator.MIN_PRECE_PRE_1024_BYTES, coinList);
            if (!coinDataResult.isEnough()) {
                return Result.getFailed(TransactionErrorCode.INSUFFICIENT_BALANCE);
            }
            /**
             * build coin data
             */
            CoinData coinData = new CoinData();
            coinData.setFrom(coinDataResult.getCoinList());
            // 找零的UTXO
            if (coinDataResult.getChange() != null) {
                coinData.getTo().add(coinDataResult.getChange());
            }
            tx.setCoinData(coinData);

            // 重置为0，重新计算交易对象的size
            tx.setSize(0);
            if (tx.getSize() > TransactionFeeCalculator.MAX_TX_SIZE) {
                return Result.getFailed(TransactionErrorCode.DATA_SIZE_ERROR);
            }

            TransactionCreatedReturnInfo returnInfo = LedgerUtil.makeReturnInfo(tx);
            Map<String, TransactionCreatedReturnInfo> map = new HashMap<>();
            map.put("value", returnInfo);
            return Result.getSuccess().setData(map);
        } catch (Exception e) {
            Log.error(e);
            return Result.getFailed(e.getMessage());
        }
    }
}
