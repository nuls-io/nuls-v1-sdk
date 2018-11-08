package io.nuls.contract.sdk.transaction;

import io.nuls.contract.sdk.constant.ContractConstant;
import io.nuls.contract.sdk.model.CallContractData;
import io.nuls.contract.sdk.model.ContractResult;
import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.model.Coin;
import io.nuls.sdk.core.model.Na;
import io.nuls.sdk.core.model.transaction.Transaction;
import io.nuls.sdk.core.utils.NulsByteBuffer;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by wangkun23 on 2018/10/8.
 */
public class CallContractTransaction extends Transaction<CallContractData> implements ContractTransaction {

    private ContractResult contractResult;

    private transient Collection<ContractTransferTransaction> contractTransferTxs;

    private transient Na returnNa;

    private transient Object programExecutor;

    public CallContractTransaction() {
        super(ContractConstant.TX_TYPE_CALL_CONTRACT);
    }

    @Override
    protected CallContractData parseTxData(NulsByteBuffer byteBuffer) throws NulsException {
        return byteBuffer.readNulsData(new CallContractData());
    }

    @Override
    public String getInfo(byte[] address) {
        boolean isTransfer = false;
        Coin to = coinData.getTo().get(0);
        if (!Arrays.equals(address, to.getOwner())) {
            isTransfer = true;
        }
        if (isTransfer) {
            return "-" + to.getNa().add(getFee()).toCoinString();
        } else {
            return "-" + getFee().toCoinString();
        }
    }

    @Override
    public ContractResult getContractResult() {
        return contractResult;
    }

    @Override
    public void setContractResult(ContractResult contractResult) {
        this.contractResult = contractResult;
    }

    @Override
    public void setReturnNa(Na returnNa) {
        this.returnNa = returnNa;
    }

    @Override
    public Na getFee() {
        Na resultFee = super.getFee();
        if (returnNa != null) {
            resultFee = resultFee.minus(returnNa);
        }
        return resultFee;
    }

    @Override
    public Object getProgramExecutor() {
        return programExecutor;
    }

    @Override
    public void setProgramExecutor(Object programExecutor) {
        this.programExecutor = programExecutor;
    }

    public void setContractTransferTxs(Collection<ContractTransferTransaction> contractTransferTxs) {
        this.contractTransferTxs = contractTransferTxs;
    }

    public Collection<ContractTransferTransaction> getContractTransferTxs() {
        return contractTransferTxs;
    }
}
