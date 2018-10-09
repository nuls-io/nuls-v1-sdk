package io.nuls.contract.sdk.transaction;

import io.nuls.contract.sdk.constant.ContractConstant;
import io.nuls.contract.sdk.model.ContractResult;
import io.nuls.contract.sdk.model.CreateContractData;
import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.model.Na;
import io.nuls.sdk.core.model.transaction.Transaction;
import io.nuls.sdk.core.utils.NulsByteBuffer;

/**
 * Created by wangkun23 on 2018/10/8.
 */
public class CreateContractTransaction extends Transaction<CreateContractData> implements ContractTransaction {

    private ContractResult contractResult;

    private transient Na returnNa;

    private transient Object programExecutor;

    public CreateContractTransaction() {
        super(ContractConstant.TX_TYPE_CREATE_CONTRACT);
    }

    @Override
    protected CreateContractData parseTxData(NulsByteBuffer byteBuffer) throws NulsException {
        return byteBuffer.readNulsData(new CreateContractData());
    }

    @Override
    public String getInfo(byte[] address) {
        return "-" + getFee().toCoinString();
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
    public Object getProgramExecutor() {
        return programExecutor;
    }

    @Override
    public void setProgramExecutor(Object programExecutor) {
        this.programExecutor = programExecutor;
    }

    @Override
    public Na getFee() {
        Na resultFee = super.getFee();
        if(returnNa != null) {
            resultFee = resultFee.minus(returnNa);
        }
        return resultFee;
    }
}
