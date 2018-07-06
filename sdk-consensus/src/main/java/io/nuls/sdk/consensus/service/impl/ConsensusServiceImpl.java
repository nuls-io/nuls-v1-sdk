package io.nuls.sdk.consensus.service.impl;


import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.consensus.model.AgentInfo;
import io.nuls.sdk.consensus.model.DepositInfo;
import io.nuls.sdk.consensus.service.ConsensusService;
import io.nuls.sdk.core.contast.AccountErrorCode;
import io.nuls.sdk.core.contast.ErrorCode;
import io.nuls.sdk.core.contast.SDKConstant;
import io.nuls.sdk.core.contast.TransactionErrorCode;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.exception.NulsException;
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

    private static ConsensusServiceImpl instance = new ConsensusServiceImpl();

    private ConsensusServiceImpl() {

    }

    public static ConsensusService getInstance() {
        return instance;
    }

    @Override
    public Result createAgentTransaction(AgentInfo info, List<Input> inputs, Na fee) {
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

        List<Coin> inputsList = new ArrayList<>();

        Na inputTotal = Na.ZERO;
        for (int i = 0; i < inputs.size(); i++) {
            Input inputDto = inputs.get(i);
            byte[] key = Arrays.concatenate(Hex.decode(inputDto.getFromHash()), new VarInt(inputDto.getFromIndex()).encode());
            Coin coin = new Coin();
            coin.setOwner(key);
            coin.setNa(Na.valueOf(inputDto.getValue()));
            coin.setLockTime(inputDto.getLockTime());
            inputsList.add(coin);
            inputTotal = inputTotal.add(coin.getNa());
        }

        List<Coin> toList = new ArrayList<>();
        toList.add(new Coin(agent.getAgentAddress(), agent.getDeposit(), SDKConstant.CONSENSUS_LOCK_TIME));
        //找零
        toList.add(new Coin(agent.getAgentAddress(), inputTotal.subtract(agent.getDeposit()).subtract(fee), SDKConstant.CONSENSUS_LOCK_TIME));

        io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createAgentTx(inputsList, toList, agent);
        if (!TransactionTool.isFeeEnough(tx, 2)) {
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

    @Override
    public Result createDepositTransaction(DepositInfo info, List<Input> inputs, Na fee) {
        Deposit deposit = new Deposit();
        deposit.setAddress(AddressTool.getAddress(info.getAddress()));
        try {
            deposit.setAgentHash(NulsDigestData.fromDigestHex(info.getAgentHash()));
        } catch (NulsException e) {
            return Result.getFailed(AccountErrorCode.PARAMETER_ERROR, "agentHash error");
        }
        deposit.setDeposit(Na.valueOf(info.getDeposit()));

        List<Coin> inputsList = new ArrayList<>();

        Na inputTotal = Na.ZERO;
        for (int i = 0; i < inputs.size(); i++) {
            Input inputDto = inputs.get(i);
            byte[] key = Arrays.concatenate(Hex.decode(inputDto.getFromHash()), new VarInt(inputDto.getFromIndex()).encode());
            Coin coin = new Coin();
            coin.setOwner(key);
            coin.setNa(Na.valueOf(inputDto.getValue()));
            coin.setLockTime(inputDto.getLockTime());
            inputsList.add(coin);
            inputTotal = inputTotal.add(coin.getNa());
        }

        List<Coin> toList = new ArrayList<>();
        toList.add(new Coin(deposit.getAddress(), deposit.getDeposit(), SDKConstant.CONSENSUS_LOCK_TIME));
        toList.add(new Coin(deposit.getAddress(), inputTotal.subtract(deposit.getDeposit()).subtract(fee), SDKConstant.CONSENSUS_LOCK_TIME));

        io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createDepositTx(inputsList, toList, deposit);

        if (!TransactionTool.isFeeEnough(tx, 2)) {
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
