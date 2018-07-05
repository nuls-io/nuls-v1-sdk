package io.nuls.sdk.consensus.service.impl;


import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.consensus.model.AgentInfo;
import io.nuls.sdk.consensus.service.ConsensusService;
import io.nuls.sdk.core.contast.SDKConstant;
import io.nuls.sdk.core.contast.TransactionErrorCode;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.model.*;
import io.nuls.sdk.core.model.transaction.CreateAgentTransaction;
import io.nuls.sdk.core.utils.*;
import org.spongycastle.util.Arrays;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsensusServiceImpl implements ConsensusService {

    @Override
    public Result createAgentTransaction(AgentInfo info, List<Input> inputs) {
        Agent agent = new Agent();
        agent.setAgentAddress(AddressTool.getAddress(info.getAgentAddress()));
        agent.setPackingAddress(AddressTool.getAddress(info.getPackingAddress()));
        if (StringUtils.isBlank(info.getRewardAddress())) {
            agent.setRewardAddress(agent.getAgentAddress());
        } else {
            agent.setRewardAddress(AddressTool.getAddress(info.getRewardAddress()));
        }
        agent.setDeposit(Na.valueOf(info.getDeposit()));
        agent.setCommissionRate(info.getCommissionRate());

        List<Coin> toList = new ArrayList<>();
        toList.add(new Coin(agent.getAgentAddress(), agent.getDeposit(), SDKConstant.CONSENSUS_LOCK_TIME));

        List<Coin> inputsList = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            Input inputDto = inputs.get(i);
            byte[] key = Arrays.concatenate(Hex.decode(inputDto.getFromHash()), new VarInt(inputDto.getFromIndex()).encode());
            Coin coin = new Coin();
            coin.setOwner(key);
            coin.setNa(Na.valueOf(inputDto.getValue()));
            coin.setLockTime(inputDto.getLockTime());
            inputsList.add(coin);
        }

        io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createAgentTx(inputsList, toList, agent);
        if (!TransactionTool.isFeeEnough(tx)) {
            return Result.getFailed(TransactionErrorCode.FEE_NOT_RIGHT);
        }

        try {
            String txHex = Hex.encode(tx.serialize());
            Map<String, String> map = new HashMap<>();
            map.put("value", txHex);
            return Result.getSuccess().setData(map);
        } catch (IOException e) {
            Log.error(e);
            return Result.getFailed(e.getMessage());
        }
    }
}
