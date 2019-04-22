package io.nuls.sdk.core.utils;


import io.nuls.sdk.core.contast.KernelErrorCode;
import io.nuls.sdk.core.contast.SDKConstant;
import io.nuls.sdk.core.contast.TransactionConstant;
import io.nuls.sdk.core.crypto.ECKey;
import io.nuls.sdk.core.exception.NulsRuntimeException;
import io.nuls.sdk.core.model.*;
import io.nuls.sdk.core.model.transaction.*;
import io.nuls.sdk.core.script.P2PHKSignature;
import io.nuls.sdk.core.script.SignatureUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionTool {
    public static final int MAX_TX_SIZE = 300 * 1024;
    private static final Map<Integer, Class<? extends Transaction>> TYPE_TX_MAP = new HashMap<>();
    private static RestFulUtils restFul = RestFulUtils.getInstance();

    public static void init() {
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_COINBASE, CoinBaseTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_TRANSFER, TransferTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_ALIAS, AliasTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_REGISTER_AGENT, CreateAgentTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_JOIN_CONSENSUS, DepositTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_CANCEL_DEPOSIT, CancelDepositTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_STOP_AGENT, StopAgentTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_YELLOW_PUNISH, YellowPunishTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_RED_PUNISH, RedPunishTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_DATA, DataTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_CALL_CONTRACT, CallContractTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_CREATE_CONTRACT, CreateContractTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_DELETE_CONTRACT, DeleteContractTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_CONTRACT_TRANSFER, ContractTransferTransaction.class);
    }

    public static Transaction createTransferTx(List<Coin> inputs, List<Coin> outputs, byte[] remark) {
        TransferTransaction tx = new TransferTransaction();
        CoinData coinData = new CoinData();
        coinData.setFrom(inputs);
        coinData.setTo(outputs);
        tx.setCoinData(coinData);
        tx.setTime(TimeService.currentTimeMillis());
        tx.setRemark(remark);
        return tx;
    }

    public static Transaction createAgentTx(List<Coin> inputs, List<Coin> outputs, Agent agent) {
        CreateAgentTransaction tx = new CreateAgentTransaction();
        CoinData coinData = new CoinData();
        coinData.setFrom(inputs);
        coinData.setTo(outputs);
        tx.setCoinData(coinData);
        tx.setTime(TimeService.currentTimeMillis());
        tx.setTxData(agent);
        return tx;
    }

    public static Transaction createDepositTx(List<Coin> inputs, List<Coin> outputs, Deposit deposit) {
        DepositTransaction tx = new DepositTransaction();
        CoinData coinData = new CoinData();
        coinData.setFrom(inputs);
        coinData.setTo(outputs);
        tx.setCoinData(coinData);
        tx.setTime(TimeService.currentTimeMillis());
        tx.setTxData(deposit);
        return tx;
    }

    public static Transaction createCancelDepositTx(List<Coin> inputs, List<Coin> outputs, CancelDeposit cancelDeposit) {
        CancelDepositTransaction tx = new CancelDepositTransaction();
        CoinData coinData = new CoinData();
        coinData.setFrom(inputs);
        coinData.setTo(outputs);
        tx.setCoinData(coinData);
        tx.setTime(TimeService.currentTimeMillis());
        tx.setTxData(cancelDeposit);
        return tx;
    }


    public static Transaction createStopAgentTx(List<Coin> inputs, List<Coin> outputs, StopAgent stopAgent) {
        StopAgentTransaction tx = new StopAgentTransaction();
        CoinData coinData = new CoinData();
        coinData.setFrom(inputs);
        coinData.setTo(outputs);
        tx.setCoinData(coinData);
        tx.setTime(TimeService.currentTimeMillis());
        tx.setTxData(stopAgent);
        return tx;
    }

    public static Transaction signTransaction(Transaction tx, ECKey ecKey) throws IOException {
        tx.setHash(NulsDigestData.calcDigestData(tx.serializeForHash()));
        P2PHKSignature sig = SignatureUtil.createSignatureByEckey(tx, ecKey);
        tx.setTransactionSignature(sig.serialize());
        return tx;
    }

    public static byte[] signHash(String txHash, ECKey ecKey) throws IOException {
        P2PHKSignature sign = SignatureUtil.createSignatureByEckey(txHash, ecKey);
        return sign.serialize();
    }


    public static NulsSignData signDigest(byte[] digest, ECKey ecKey) {
        byte[] signbytes = ecKey.sign(digest);
        NulsSignData nulsSignData = new NulsSignData();
        nulsSignData.setSignAlgType(NulsSignData.SIGN_ALG_ECC);
        nulsSignData.setSignBytes(signbytes);
        return nulsSignData;
    }

    public static boolean isFeeEnough(Transaction tx, int signatureSize, int type) {
        int size = tx.size() + signatureSize;
        Na minFee = TransactionFeeCalculator.getTransferFee(size, type);
        //计算inputs和outputs的差额 ，求手续费
        Na fee = Na.ZERO;
        for (Coin coin : tx.getCoinData().getFrom()) {
            fee = fee.add(coin.getNa());
        }
        for (Coin coin : tx.getCoinData().getTo()) {
            fee = fee.subtract(coin.getNa());
        }
        if (fee.isLessThan(minFee)) {
            return false;
        }
        return true;
    }

    public static Transaction getInstance(NulsByteBuffer byteBuffer) throws Exception {
        int txType = byteBuffer.readUint16();
        byteBuffer.setCursor(byteBuffer.getCursor() - SerializeUtils.sizeOfUint16());
        Class<? extends Transaction> txClass = TYPE_TX_MAP.get(txType);
        if (null == txClass) {
            throw new NulsRuntimeException(KernelErrorCode.FAILED, "transaction type not exist!");
        }
        Transaction tx = byteBuffer.readNulsData(txClass.newInstance());
        return tx;
    }


    public static List<Transaction> getInstances(NulsByteBuffer byteBuffer, long txCount) throws Exception {
        List<Transaction> list = new ArrayList<>();
        for (int i = 0; i < txCount; i++) {
            list.add(getInstance(byteBuffer));
        }
        return list;
    }

    public static List<Transaction> getInstancesWithVersion(NulsByteBuffer byteBuffer, long txCount, int version) throws Exception {
        List<Transaction> list = new ArrayList<>();
        for (int i = 0; i < txCount; i++) {
            list.add(getInstancesWithVersion(byteBuffer, version));
        }
        return list;
    }


    public static Transaction getInstancesWithVersion(NulsByteBuffer byteBuffer, int version) throws Exception {
        int txType = byteBuffer.readUint16();
        byteBuffer.setCursor(byteBuffer.getCursor() - SerializeUtils.sizeOfUint16());
        Class<? extends Transaction> txClass = TYPE_TX_MAP.get(txType);
        if (null == txClass) {
            throw new NulsRuntimeException(KernelErrorCode.FAILED, "transaction type not exist!");
        }
        Transaction tx = byteBuffer.readNulsDataWithVersion(txClass.newInstance(), version);
        return tx;
    }

    public static int getMainVersion() {
        Result result = restFul.get("/client/version", null);
        if (result.isFailed()) {
            return 1;
        }
        Map<String, Object> map = ((Map) result.getData());
        return (int) map.get("networkVersion");
    }

    public static CoinDataResult getCoinData(byte[] address, Na amount, int size, Na price, List<Coin> coinList) {
        if (null == price) {
            throw new NulsRuntimeException(KernelErrorCode.PARAMETER_ERROR);
        }
        CoinDataResult coinDataResult = new CoinDataResult();
        coinDataResult.setEnough(false);

        coinList = coinList.stream()
                .filter(coin1 -> !Na.ZERO.equals(coin1.getNa()))
                .sorted(CoinComparator.getInstance())
                .collect(Collectors.toList());

        if (coinList.isEmpty()) {
            return coinDataResult;
        }
        List<Coin> coins = new ArrayList<>();
        Na values = Na.ZERO;
        // 累加到足够支付转出额与手续费
        for (int i = 0; i < coinList.size(); i++) {
            Coin coin = coinList.get(i);
            coins.add(coin);
            size += coin.size();
            if (i == 127) {
                size += 1;
            }
            //每次累加一条未花费余额时，需要重新计算手续费
            Na fee = TransactionFeeCalculator.getFee(size, price);
            values = values.add(coin.getNa());

            /**
             * 判断是否是脚本验证UTXO
             * */
            int signType = coinDataResult.getSignType();
            if (signType != 3) {
                if ((signType & 0x01) == 0 && coin.getTempOwner().length == 23) {
                    coinDataResult.setSignType((byte) (signType | 0x01));
                    size += P2PHKSignature.SERIALIZE_LENGTH;
                } else if ((signType & 0x02) == 0 && coin.getTempOwner().length != 23) {
                    coinDataResult.setSignType((byte) (signType | 0x02));
                    size += P2PHKSignature.SERIALIZE_LENGTH;
                }
            }

            //需要判断是否找零，如果有找零，则需要重新计算手续费
            if (values.isGreaterThan(amount.add(fee))) {
                Na change = values.subtract(amount.add(fee));
                Coin changeCoin = new Coin();
                if (address[2] == SDKConstant.P2SH_ADDRESS_TYPE) {
                    changeCoin.setOwner(SignatureUtil.createOutputScript(address).getProgram());
                } else {
                    changeCoin.setOwner(address);
                }
                changeCoin.setNa(change);
                fee = TransactionFeeCalculator.getFee(size + changeCoin.size(), price);
                if (values.isLessThan(amount.add(fee))) {
                    continue;
                }
                changeCoin.setNa(values.subtract(amount.add(fee)));
                if (!changeCoin.getNa().equals(Na.ZERO)) {
                    coinDataResult.setChange(changeCoin);
                }
            }
            coinDataResult.setFee(fee);
            if (values.isGreaterOrEquals(amount.add(fee))) {
                coinDataResult.setEnough(true);
                coinDataResult.setCoinList(coins);
                break;
            }
        }
        return coinDataResult;
    }


}
