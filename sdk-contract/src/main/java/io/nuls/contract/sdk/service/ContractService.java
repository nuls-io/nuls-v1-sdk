package io.nuls.contract.sdk.service;

import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.model.Na;
import io.nuls.sdk.core.model.Result;

import java.io.IOException;
import java.util.List;

public interface ContractService {
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
    Result createContractTransaction(String sender,
                                     long gasLimit,
                                     Long price,
                                     byte[] contractCode,
                                     Object[] args,
                                     String remark) throws NulsException, IOException;

    /**
     * call contract's method
     *
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
    Result callContractTransaction(String sender,
                                   Na value,
                                   Long gasLimit,
                                   Long price,
                                   String contractAddress,
                                   String methodName,
                                   String methodDesc,
                                   Object[] args,
                                   String remark,
                                   List<Input> utxos);
}
