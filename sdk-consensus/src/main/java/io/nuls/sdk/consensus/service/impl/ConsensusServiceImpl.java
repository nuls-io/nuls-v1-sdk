package io.nuls.sdk.consensus.service.impl;


import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.Output;
import io.nuls.sdk.consensus.model.AgentInfo;
import io.nuls.sdk.consensus.model.DepositInfo;
import io.nuls.sdk.consensus.service.ConsensusService;
import io.nuls.sdk.core.contast.AccountErrorCode;
import io.nuls.sdk.core.contast.SDKConstant;
import io.nuls.sdk.core.contast.TransactionErrorCode;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.model.*;
import io.nuls.sdk.core.script.*;
import io.nuls.sdk.core.utils.*;
import org.spongycastle.util.Arrays;
import sun.management.resources.agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsensusServiceImpl implements ConsensusService {

    private static ConsensusServiceImpl instance = new ConsensusServiceImpl();

    private RestFulUtils restFul = RestFulUtils.getInstance();

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
        toList.add(new Coin(agent.getAgentAddress(), inputTotal.subtract(agent.getDeposit()).subtract(fee), 0));

        io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createAgentTx(inputsList, toList, agent);
        if (!TransactionTool.isFeeEnough(tx, P2PHKSignature.SERIALIZE_LENGTH, 2)) {
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
        toList.add(new Coin(deposit.getAddress(), inputTotal.subtract(deposit.getDeposit()).subtract(fee), 0));

        io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createDepositTx(inputsList, toList, deposit);

        if (!TransactionTool.isFeeEnough(tx, P2PHKSignature.SERIALIZE_LENGTH, 2)) {
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
    public Result createCancelDepositTransaction(Output output) {
        CancelDeposit cancelDeposit = new CancelDeposit();
        cancelDeposit.setAddress(AddressTool.getAddress(output.getAddress()));
        try {
            NulsDigestData hash = NulsDigestData.fromDigestHex(output.getTxHash());
            cancelDeposit.setJoinTxHash(hash);
        } catch (NulsException e) {
            return Result.getFailed(AccountErrorCode.PARAMETER_ERROR, "joinTxHash error");
        }

        //组装input
        Coin from = new Coin();
        byte[] key = Arrays.concatenate(Hex.decode(output.getTxHash()), new VarInt(output.getIndex()).encode());
        List<Coin> inputsList = new ArrayList<>();
        from.setOwner(key);
        from.setNa(Na.valueOf(output.getValue()));
        from.setLockTime(-1);
        inputsList.add(from);

        //手续费
        Na fee = Na.valueOf(1000000L);
        //组装output
        List<Coin> toList = new ArrayList<>();
        toList.add(new Coin(cancelDeposit.getAddress(), Na.valueOf(output.getValue()).subtract(fee), 0));

        io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createCancelDepositTx(inputsList, toList, cancelDeposit);

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
    public Result createStopAgentTransaction(Output output) {
        NulsDigestData hash = null;
        try {
            hash = NulsDigestData.fromDigestHex(output.getTxHash());
        } catch (NulsException e) {
            return Result.getFailed(AccountErrorCode.PARAMETER_ERROR, "agentTxHash error");
        }

        StopAgent stopAgent = new StopAgent();
        stopAgent.setAddress(AddressTool.getAddress(output.getAddress()));
        stopAgent.setCreateTxHash(hash);

        //组装input
        Coin from = new Coin();
        byte[] key = Arrays.concatenate(Hex.decode(output.getTxHash()), new VarInt(output.getIndex()).encode());
        List<Coin> inputsList = new ArrayList<>();
        from.setOwner(key);
        from.setNa(Na.valueOf(output.getValue()));
        from.setLockTime(-1);
        inputsList.add(from);

        //手续费
        Na fee = Na.valueOf(1000000L);
        //组装output
        List<Coin> toList = new ArrayList<>();
        toList.add(new Coin(stopAgent.getAddress(), Na.valueOf(output.getValue()).subtract(fee), 0));

        io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createStopAgentTx(inputsList, toList, stopAgent);

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
    public Result getDeposits(String address, int pageNumber, int pageSize) {
        if (!AddressTool.validAddress(address)) {
            return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("pageNumber", pageNumber);
        parameters.put("pageSize", pageSize);
        Result result = restFul.get("/consensus/deposit/address/" + address, parameters);
        if (result.isSuccess()) {
            Map<String, Object> data = (Map<String, Object>) result.getData();
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("list");
            List<DepositInfo> depositInfos = new ArrayList<>();
            for (Map<String, Object> map : list) {
                DepositInfo info = new DepositInfo();
                info.setDeposit((Long) map.get("deposit"));
                info.setAgentHash((String) map.get("agentHash"));
                info.setAddress((String) map.get("address"));
                info.setAgentAddress((String) map.get("agentAddress"));
                info.setBlockHeight(Long.parseLong(map.get("blockHeight").toString()));
                info.setTxHash((String) map.get("txHash"));
                depositInfos.add(info);
            }
            data.put("list", depositInfos);
        }
        return result;
    }

    @Override
    public Result getAgentDeposits(String agentHash, int pageNumber, int pageSize) {
        Result result = restFul.get("/consensus/deposit/agent/" + agentHash, null);
        if (result.isSuccess()) {
            Map<String, Object> data = (Map<String, Object>) result.getData();
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("list");
            List<DepositInfo> depositInfos = new ArrayList<>();
            for (Map<String, Object> map : list) {
                DepositInfo info = new DepositInfo();
                info.setDeposit((Long) map.get("deposit"));
                info.setAgentHash((String) map.get("agentHash"));
                info.setAddress((String) map.get("address"));
                info.setAgentAddress((String) map.get("agentAddress"));
                info.setBlockHeight(Long.parseLong(map.get("blockHeight").toString()));
                info.setTxHash((String) map.get("txHash"));
                depositInfos.add(info);
            }
            data.put("list", depositInfos);
        }
        return result;
    }

    @Override
    public Result createMSAgentTransaction(AgentInfo agentInfo, List<Input> inputs, Na fee) {
        Agent agent = new Agent();
        String address = agentInfo.getAgentAddress();
        //根据Address获取，多签账户信息
        if(!AddressTool.validAddress(address)){
            return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
        }
        if(Hex.decode(address)[2] != SDKConstant.P2SH_ADDRESS_TYPE){
            return Result.getFailed("Not a multi signature address!");
        }
        TransactionSignature transactionSignature = getTransactionSignature(agentInfo.getAgentAddress());
        if(transactionSignature == null){
            return Result.getFailed("There is no multi sign account!");
        }
        agent.setAgentAddress(AddressTool.getAddress(address));
        agent.setPackingAddress(AddressTool.getAddress(agentInfo.getPackingAddress()));
        if (StringUtils.isBlank(agentInfo.getRewardAddress())) {
            agent.setRewardAddress(agent.getAgentAddress());
        } else {
            agent.setRewardAddress(AddressTool.getAddress(agentInfo.getRewardAddress()));
        }
        agent.setDeposit(Na.valueOf(agentInfo.getDeposit()));
        agent.setCommissionRate(agentInfo.getCommissionRate());
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
        if(agent.getAgentAddress()[2] == SDKConstant.P2SH_ADDRESS_TYPE){
            Script scriptPubkey = SignatureUtil.createOutputScript(agent.getAgentAddress());
            toList.add(new Coin(scriptPubkey.getProgram(), agent.getDeposit(), SDKConstant.CONSENSUS_LOCK_TIME));
            toList.add(new Coin(scriptPubkey.getProgram(), inputTotal.subtract(agent.getDeposit()).subtract(fee), 0));
        }else{
            toList.add(new Coin(agent.getAgentAddress(), agent.getDeposit(), SDKConstant.CONSENSUS_LOCK_TIME));
            //找零
            toList.add(new Coin(agent.getAgentAddress(), inputTotal.subtract(agent.getDeposit()).subtract(fee), 0));
        }
        try {
            io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createAgentTx(inputsList, toList, agent);
            int scriptSignLenth = transactionSignature.getScripts().get(0).getProgram().length + SignatureUtil.getM(transactionSignature.getScripts().get(0))* 72;
            tx.setTransactionSignature(transactionSignature.serialize());
            if (!TransactionTool.isFeeEnough(tx, scriptSignLenth, 2)) {
                return Result.getFailed(TransactionErrorCode.FEE_NOT_RIGHT);
            }
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
    public Result createStopMSAgentTransaction(Output output) {
        NulsDigestData hash = null;
        try {
            hash = NulsDigestData.fromDigestHex(output.getTxHash());
        } catch (NulsException e) {
            return Result.getFailed(AccountErrorCode.PARAMETER_ERROR, "agentTxHash error");
        }
        TransactionSignature transactionSignature = getTransactionSignature(output.getAddress());
        if(transactionSignature == null){
            return Result.getFailed("There is no multi sign account!");
        }
        StopAgent stopAgent = new StopAgent();
        stopAgent.setAddress(AddressTool.getAddress(output.getAddress()));
        stopAgent.setCreateTxHash(hash);
        //组装input
        Coin from = new Coin();
        byte[] key = Arrays.concatenate(Hex.decode(output.getTxHash()), new VarInt(output.getIndex()).encode());
        List<Coin> inputsList = new ArrayList<>();
        from.setOwner(key);
        from.setNa(Na.valueOf(output.getValue()));
        from.setLockTime(-1);
        inputsList.add(from);

        //手续费
        Na fee = Na.valueOf(1000000L);
        //组装output
        List<Coin> toList = new ArrayList<>();
        if(stopAgent.getAddress()[2] == SDKConstant.P2SH_ADDRESS_TYPE){
            Script scriptPubkey = SignatureUtil.createOutputScript(stopAgent.getAddress());
            toList.add(new Coin(scriptPubkey.getProgram(), Na.valueOf(output.getValue()).subtract(fee), SDKConstant.CONSENSUS_LOCK_TIME));
        }else{
            toList.add(new Coin(stopAgent.getAddress(), Na.valueOf(output.getValue()).subtract(fee), 0));
        }
        io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createStopAgentTx(inputsList, toList, stopAgent);
        try {
            tx.setTransactionSignature(transactionSignature.serialize());
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
    public Result createMSAccountDepositTransaction(DepositInfo info, List<Input> inputs, Na fee) {
        Deposit deposit = new Deposit();
        deposit.setAddress(AddressTool.getAddress(info.getAddress()));
        try {
            deposit.setAgentHash(NulsDigestData.fromDigestHex(info.getAgentHash()));
        } catch (NulsException e) {
            return Result.getFailed(AccountErrorCode.PARAMETER_ERROR, "agentHash error");
        }
        TransactionSignature transactionSignature = getTransactionSignature(info.getAddress());
        if(transactionSignature == null){
            return Result.getFailed("There is no multi sign account!");
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
        if(deposit.getAddress()[2] == SDKConstant.P2SH_ADDRESS_TYPE){
            Script scriptPubkey = SignatureUtil.createOutputScript(deposit.getAddress());
            toList.add(new Coin(scriptPubkey.getProgram(), deposit.getDeposit().subtract(fee), SDKConstant.CONSENSUS_LOCK_TIME));
            toList.add(new Coin(scriptPubkey.getProgram(), inputTotal.subtract(deposit.getDeposit()).subtract(fee), 0));
        }else{
            toList.add(new Coin(deposit.getAddress(), deposit.getDeposit(), SDKConstant.CONSENSUS_LOCK_TIME));
            toList.add(new Coin(deposit.getAddress(), inputTotal.subtract(deposit.getDeposit()).subtract(fee), 0));
        }
        io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createDepositTx(inputsList, toList, deposit);
        int scriptSignLenth = transactionSignature.getScripts().get(0).getProgram().length + SignatureUtil.getM(transactionSignature.getScripts().get(0))* 72;
        if (!TransactionTool.isFeeEnough(tx, scriptSignLenth, 2)) {
            return Result.getFailed(TransactionErrorCode.FEE_NOT_RIGHT);
        }
        try {
            tx.setTransactionSignature(transactionSignature.serialize());
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
    public Result createMSAccountCancelDepositTransaction(Output output) {
        CancelDeposit cancelDeposit = new CancelDeposit();
        cancelDeposit.setAddress(AddressTool.getAddress(output.getAddress()));
        try {
            NulsDigestData hash = NulsDigestData.fromDigestHex(output.getTxHash());
            cancelDeposit.setJoinTxHash(hash);
        } catch (NulsException e) {
            return Result.getFailed(AccountErrorCode.PARAMETER_ERROR, "joinTxHash error");
        }

        TransactionSignature transactionSignature = getTransactionSignature(output.getAddress());
        if(transactionSignature == null){
            return Result.getFailed("There is no multi sign account!");
        }
        //组装input
        Coin from = new Coin();
        byte[] key = Arrays.concatenate(Hex.decode(output.getTxHash()), new VarInt(output.getIndex()).encode());
        List<Coin> inputsList = new ArrayList<>();
        from.setOwner(key);
        from.setNa(Na.valueOf(output.getValue()));
        from.setLockTime(-1);
        inputsList.add(from);
        //手续费
        Na fee = Na.valueOf(1000000L);
        //组装output
        List<Coin> toList = new ArrayList<>();
        if(cancelDeposit.getAddress()[2] == SDKConstant.P2SH_ADDRESS_TYPE){
            Script scriptPubkey = SignatureUtil.createOutputScript(cancelDeposit.getAddress());
            toList.add(new Coin(scriptPubkey.getProgram(), Na.valueOf(output.getValue()).subtract(fee),  SDKConstant.CONSENSUS_LOCK_TIME));
        }else{
            toList.add(new Coin(cancelDeposit.getAddress(), Na.valueOf(output.getValue()).subtract(fee), 0));
        }
        io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createCancelDepositTx(inputsList, toList, cancelDeposit);
        try {
            tx.setTransactionSignature(transactionSignature.serialize());
            String txHex = Hex.encode(tx.serialize());
            Map<String, String> map = new HashMap<>();
            map.put("value", txHex);
            return Result.getSuccess().setData(map);
        } catch (IOException e) {
            Log.error(e);
            return Result.getFailed(e.getMessage());
        }
    }

    public TransactionSignature getTransactionSignature(String address){
        Result result = restFul.get("/multiAccount/"+address  , null);
        if(result.isFailed()){
            return  null;
        }
        Map<String, Object> data = (Map<String, Object>) result.getData();
        List<byte[]> pubkeys =(List<byte[]>)data.get("pubKeyList");
        long n = (Long)data.get("m");
        TransactionSignature transactionSignature = new TransactionSignature();
        List<Script> scripts = new ArrayList<>();
        Script redeemScript = ScriptBuilder.createByteNulsRedeemScript((int)n,pubkeys);
        scripts.add(redeemScript);
        transactionSignature.setScripts(scripts);
        return  transactionSignature;
    }
}
