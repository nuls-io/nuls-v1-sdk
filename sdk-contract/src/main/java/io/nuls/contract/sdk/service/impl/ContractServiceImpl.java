package io.nuls.contract.sdk.service.impl;

import io.nuls.contract.sdk.ContractUtil;
import io.nuls.sdk.core.model.CallContractData;
import io.nuls.sdk.core.model.CreateContractData;
import io.nuls.sdk.core.model.DeleteContractData;
import io.nuls.contract.sdk.service.ContractService;
import io.nuls.contract.sdk.service.UTXOService;
import io.nuls.sdk.core.model.transaction.CallContractTransaction;
import io.nuls.sdk.core.model.transaction.CreateContractTransaction;
import io.nuls.sdk.core.model.transaction.DeleteContractTransaction;
import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.utils.ConvertCoinTool;
import io.nuls.sdk.core.contast.SDKConstant;
import io.nuls.sdk.core.contast.TransactionErrorCode;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.model.*;
import io.nuls.sdk.core.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.Arrays;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * contract sdk
 * Created by wangkun23 on 2018/10/8.
 */
public class ContractServiceImpl implements ContractService {

    final Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    private RestFulUtils restFulUtils = RestFulUtils.getInstance();

    UTXOService utxoService = UTXOServiceImpl.getInstance();

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
     * @throws NulsException
     * @throws IOException
     */
    @Override
    public Result createContractTransaction(String sender,
                                            long gasLimit,
                                            Long price,
                                            byte[] contractCode,
                                            Object[] args,
                                            String remark) throws NulsException, IOException {
        Na value = Na.ZERO;
        long totalNa = LongUtils.mul(gasLimit, price);

        // 生成一个地址作为智能合约地址
        Address contractAddress = AccountTool.createContractAddress();

        byte[] contractAddressBytes = contractAddress.getAddressBytes();
        byte[] senderBytes = AddressTool.getAddress(sender);
        // 组装txData
        CreateContractData createContractData = new CreateContractData();
        createContractData.setSender(senderBytes);
        createContractData.setContractAddress(contractAddressBytes);
        createContractData.setValue(value.getValue());
        createContractData.setGasLimit(gasLimit);
        createContractData.setPrice(price);
        createContractData.setCodeLen(contractCode.length);
        createContractData.setCode(contractCode);
        if (args != null) {
            createContractData.setArgsCount((byte) args.length);
            if (args.length > 0) {
                createContractData.setArgs(ContractUtil.twoDimensionalArray(args));
            }
        }
        /**
         * 组装交易数据
         */
        CreateContractTransaction tx = new CreateContractTransaction();

        tx.setTime(TimeService.currentTimeMillis());
        tx.setTxData(createContractData);

        //总共交易费用
        //每次累加一条未花费余额时，需要重新计算手续费
        //TODO.. i == 127为什么要加127就+1？
        Na trxFee = TransactionFeeCalculator.getFee(tx.size(), TransactionFeeCalculator.MIN_PRECE_PRE_1024_BYTES);
        Long amount = LongUtils.add(totalNa, trxFee.getValue());

        List<Input> inputList = utxoService.getUTXOs(sender, amount);

        Long balance = 0L;
        List<Coin> inputs = new ArrayList<>();
        for (Input input : inputList) {
            Coin coin = new Coin();
            byte[] txHashBytes = Hex.decode(input.getFromHash());
            coin.setOwner(Arrays.concatenate(txHashBytes, new VarInt(input.getFromIndex()).encode()));
            coin.setLockTime(input.getLockTime());
            coin.setNa(Na.valueOf(input.getValue()));
            inputs.add(coin);
            balance = LongUtils.add(balance, input.getValue());
        }

        Long outputAmount = balance - 1000000L;
        System.out.printf("outputAmount: " + outputAmount);
        List<Coin> outputs = new ArrayList<>();
        //if (value.isGreaterThan(Na.ZERO)) {
        Coin to = new Coin();
        to.setLockTime(0);
        to.setNa(Na.valueOf(outputAmount));
        to.setOwner(AddressTool.getAddress(sender));
        outputs.add(to);
        //}
        CoinData coinData = new CoinData();
        coinData.setFrom(inputs);
        coinData.setTo(outputs);

        tx.setCoinData(coinData);
        tx.setHash(NulsDigestData.calcDigestData(tx.serializeForHash()));
        if (StringUtils.isNotBlank(remark)) {
            try {
                tx.setRemark(remark.getBytes(SDKConstant.DEFAULT_ENCODING));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        String txHex = Hex.encode(tx.serialize());
        Map<String, String> map = new HashMap<>();
        map.put("value", txHex);
        return Result.getSuccess().setData(map);
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
    public Result callContractTransaction(String sender,
                                          Na value,
                                          Long gasLimit,
                                          Long price,
                                          String contractAddress,
                                          String methodName,
                                          String methodDesc,
                                          Object[] args,
                                          String remark,
                                          List<Input> utxos) {
        try {
            byte[] senderBytes = AddressTool.getAddress(sender);
            byte[] contractAddressBytes = AddressTool.getAddress(contractAddress);

            if (value == null) {
                value = Na.ZERO;
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
            if(tx.getSize() > TransactionFeeCalculator.MAX_TX_SIZE){
                return Result.getFailed(TransactionErrorCode.DATA_SIZE_ERROR);
            }

            String txHex = Hex.encode(tx.serialize());
            Map<String, String> map = new HashMap<>();
            map.put("value", txHex);
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
    public Result deleteContractTransaction(String sender,
                                            String contractAddress,
                                            String remark,
                                            List<Input> utxos) {

        DeleteContractTransaction tx = new DeleteContractTransaction();
        if (StringUtils.isNotBlank(remark)) {
            tx.setRemark(remark.getBytes());
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

        if (tx.getSize() > TransactionFeeCalculator.MAX_TX_SIZE) {
            return Result.getFailed(TransactionErrorCode.DATA_SIZE_ERROR);
        }
        tx.setCoinData(coinData);
        try {
            tx.setHash(NulsDigestData.calcDigestData(tx.serializeForHash()));
        } catch (IOException e) {
            logger.error("delete contact transaction serialize error", e);
        }
        try {
            String txHex = Hex.encode(tx.serialize());
            Map<String, String> map = new HashMap<>();
            map.put("value", txHex);
            return Result.getSuccess().setData(map);
        } catch (IOException e) {
            Log.error(e);
            return Result.getFailed(e.getMessage());
        }
    }
}
