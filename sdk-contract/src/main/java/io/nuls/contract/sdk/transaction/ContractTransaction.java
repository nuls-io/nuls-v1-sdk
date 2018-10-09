package io.nuls.contract.sdk.transaction;

import io.nuls.contract.sdk.model.ContractResult;
import io.nuls.sdk.core.model.Na;

public interface ContractTransaction {
    ContractResult getContractResult();

    void setContractResult(ContractResult contractResult);

    void setReturnNa(Na returnNa);

    Object getProgramExecutor();

    void setProgramExecutor(Object programExecutor);

}
