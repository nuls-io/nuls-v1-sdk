package io.nuls.sdk.consensus.service;


import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.Output;
import io.nuls.sdk.consensus.model.AgentInfo;
import io.nuls.sdk.consensus.model.CancelDepositInfo;
import io.nuls.sdk.consensus.model.DepositInfo;
import io.nuls.sdk.core.model.Na;
import io.nuls.sdk.core.model.Result;

import java.util.List;

public interface ConsensusService {

    Result createAgentTransaction(AgentInfo agent, List<Input> inputs, Na fee);

    Result createDepositTransaction(DepositInfo info, List<Input> inputs, Na fee);

    Result createCancelDepositTransaction(Output output);

    Result createStopAgentTransaction(Output output);

    Result getDeposits(String address, int pageNumber, int pageSize);
}
