package io.nuls.contract.sdk.transaction;

import io.nuls.contract.sdk.constant.ContractConstant;
import io.nuls.contract.sdk.model.ContractTransfer;
import io.nuls.contract.sdk.model.ContractTransferData;
import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.model.Coin;
import io.nuls.sdk.core.model.Na;
import io.nuls.sdk.core.model.transaction.Transaction;
import io.nuls.sdk.core.utils.NulsByteBuffer;

/**
 * Created by wangkun23 on 2018/10/8.
 */
public class ContractTransferTransaction extends Transaction<ContractTransferData> {

    private transient ContractTransfer transfer;

    public ContractTransferTransaction() {
        super(ContractConstant.TX_TYPE_CONTRACT_TRANSFER);
    }

    @Override
    protected ContractTransferData parseTxData(NulsByteBuffer byteBuffer) throws NulsException {
        return byteBuffer.readNulsData(new ContractTransferData());
    }

    @Override
    public String getInfo(byte[] address) {
        Coin to = coinData.getTo().get(0);
        return "+" + to.getNa().toCoinString();
    }

    @Override
    public boolean isSystemTx() {
        return true;
    }

    @Override
    public boolean needVerifySignature() {
        return false;
    }

    public ContractTransfer getTransfer() {
        return transfer;
    }

    public ContractTransferTransaction setTransfer(ContractTransfer transfer) {
        this.transfer = transfer;
        return this;
    }

    @Override
    public Na getFee() {
        ContractTransferData data = this.txData;
        byte success = data.getSuccess();
        // 合约执行成功的内部转账，手续费已从Gas中扣除，此处不在收取手续费
        if (success == 1) {
            return Na.ZERO;
        }
        // 合约执行失败，退回调用者向合约地址转入的资金，需要额外收取手续费
        Na fee = Na.ZERO;
        if (null != coinData) {
            fee = coinData.getFee();
        }
        return fee;
    }
}
