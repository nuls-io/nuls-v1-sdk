package io.nuls.sdk.core.utils;


import io.nuls.sdk.core.contast.KernelErrorCode;
import io.nuls.sdk.core.contast.TransactionConstant;
import io.nuls.sdk.core.crypto.ECKey;
import io.nuls.sdk.core.exception.NulsRuntimeException;
import io.nuls.sdk.core.model.*;
import io.nuls.sdk.core.model.transaction.*;
import io.nuls.sdk.core.script.P2PHKSignature;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionTool {
    public static final int MAX_TX_SIZE = 300 * 1024;
    private static final Map<Integer, Class<? extends Transaction>> TYPE_TX_MAP = new HashMap<>();

    public static void init() {
//        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_COINBASE, TransferTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_TRANSFER, TransferTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_REGISTER_AGENT, CreateAgentTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_JOIN_CONSENSUS, DepositTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_CANCEL_DEPOSIT, CancelDepositTransaction.class);
        TYPE_TX_MAP.put(TransactionConstant.TX_TYPE_STOP_AGENT, StopAgentTransaction.class);
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
        P2PHKSignature sig = new P2PHKSignature();
        sig.setPublicKey(ecKey.getPubKey());
        sig.setSignData(signDigest(tx.getHash().getDigestBytes(), ecKey));
        tx.setTransactionSignature(sig.serialize());
        return tx;
    }

    public static NulsSignData signDigest(byte[] digest, ECKey ecKey) {
        byte[] signbytes = ecKey.sign(digest);
        NulsSignData nulsSignData = new NulsSignData();
        nulsSignData.setSignAlgType(NulsSignData.SIGN_ALG_ECC);
        nulsSignData.setSignBytes(signbytes);
        return nulsSignData;
    }

    public static boolean isFeeEnough(Transaction tx, int type) {
        int size = tx.size() + P2PHKSignature.SERIALIZE_LENGTH;
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
}
