package io.nuls.contract.sdk.service;

import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.model.Result;

import java.io.IOException;

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
}
