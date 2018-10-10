package io.nuls.contract.sdk.service.impl;

import io.nuls.contract.sdk.ContractUtil;
import io.nuls.contract.sdk.model.CreateContractData;
import io.nuls.contract.sdk.service.ContractService;
import io.nuls.contract.sdk.service.UTXOService;
import io.nuls.contract.sdk.transaction.CreateContractTransaction;
import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.Output;
import io.nuls.sdk.core.contast.SDKConstant;
import io.nuls.sdk.core.crypto.ECKey;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.model.*;
import io.nuls.sdk.core.utils.*;
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

        /**
         * 创建投票时
         */
        Na value = Na.ZERO;
        long totalNa = LongUtils.mul(gasLimit, price);

        ECKey ecKey = new ECKey();
        // 生成一个地址作为智能合约地址
        Address contractAddress = AccountTool.newAddress(ecKey);

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

        List<Coin> inputs = new ArrayList<>();
        for (Input input : inputList) {
            Coin coin = new Coin();
            byte[] txHashBytes = Hex.decode(input.getFromHash());
            coin.setOwner(Arrays.concatenate(txHashBytes, new VarInt(input.getFromIndex()).encode()));
            coin.setLockTime(input.getLockTime());
            coin.setNa(Na.valueOf(input.getValue()));
            inputs.add(coin);
        }
        List<Coin> outputs = new ArrayList<>();
        if (value.isGreaterThan(Na.ZERO)) {
            Coin to = new Coin();
            to.setLockTime(0);
            to.setNa(value);
            to.setOwner(contractAddressBytes);
            outputs.add(to);
        }
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
}
