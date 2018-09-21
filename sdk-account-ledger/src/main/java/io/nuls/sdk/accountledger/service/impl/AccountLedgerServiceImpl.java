package io.nuls.sdk.accountledger.service.impl;

import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.InputCompare;
import io.nuls.sdk.accountledger.model.Output;
import io.nuls.sdk.accountledger.model.Transaction;
import io.nuls.sdk.accountledger.service.AccountLedgerService;
import io.nuls.sdk.core.contast.AccountErrorCode;
import io.nuls.sdk.core.contast.SDKConstant;
import io.nuls.sdk.core.contast.TransactionErrorCode;
import io.nuls.sdk.core.crypto.AESEncrypt;
import io.nuls.sdk.core.crypto.ECKey;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.model.*;
import io.nuls.sdk.core.model.dto.BalanceInfo;
import io.nuls.sdk.core.model.transaction.TransferTransaction;
import io.nuls.sdk.core.script.P2PHKSignature;
import io.nuls.sdk.core.script.Script;
import io.nuls.sdk.core.script.SignatureUtil;
import io.nuls.sdk.core.script.TransactionSignature;
import io.nuls.sdk.core.utils.*;
import org.spongycastle.util.Arrays;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Charlie
 */
public class AccountLedgerServiceImpl implements AccountLedgerService {

    private static final AccountLedgerService INSTANCE = new AccountLedgerServiceImpl();

    private AccountLedgerServiceImpl() {

    }

    public static AccountLedgerService getInstance() {
        return INSTANCE;
    }

    private RestFulUtils restFul = RestFulUtils.getInstance();

    @Override
    public Result getTxByHash(String hash) {
        if (StringUtils.isBlank(hash)) {
            return Result.getFailed(AccountErrorCode.PARAMETER_ERROR);
        }

        Result result = restFul.get("/accountledger/v1/" + hash, null);
        if (result.isFailed()) {
            return result;
        }
        Map<String, Object> map = (Map) result.getData();
        //重新组装input
        List<Map<String, Object>> inputMaps = (List<Map<String, Object>>) map.get("inputs");
        List<Input> inputs = new ArrayList<>();
        for (Map<String, Object> inputMap : inputMaps) {
            Input inputDto = new Input(inputMap);
            inputs.add(inputDto);
        }
        map.put("inputs", inputs);

        //重新组装output
        List<Map<String, Object>> outputMaps = (List<Map<String, Object>>) map.get("outputs");
        List<Output> outputs = new ArrayList<>();
        for (Map<String, Object> outputMap : outputMaps) {
            Output outputDto = new Output(outputMap);
            outputs.add(outputDto);
        }
        map.put("outputs", outputs);
        Transaction transactionDto = new Transaction(map);
        result.setData(transactionDto);
        return result;
    }

    @Override
    public Result transfer(String address, String toAddress, String password, long amount, String remark) {
        if (!AddressTool.validAddress(address) || !AddressTool.validAddress(toAddress)) {
            return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
        }
        if (StringUtils.isNotBlank(password) && !StringUtils.validPassword(password)) {
            return Result.getFailed(AccountErrorCode.PASSWORD_IS_WRONG);
        }
        if (!validTxRemark(remark)) {
            return Result.getFailed(AccountErrorCode.PARAMETER_ERROR);
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("address", address);
        parameters.put("toAddress", toAddress);
        parameters.put("password", password);
        parameters.put("amount", amount);
        parameters.put("remark", remark);
        Result result = restFul.post("/accountledger/transfer", parameters);
        return result;
    }

    @Override
    public Result transfer(String address, String toAddress, long amount, String remark) {
        return transfer(address, toAddress, null, amount, remark);
    }

    private boolean validTxRemark(String remark) {
        if (StringUtils.isBlank(remark)) {
            return true;
        }
        try {
            byte[] bytes = remark.getBytes(SDKConstant.DEFAULT_ENCODING);
            if (bytes.length > 100) {
                return false;
            }
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    @Override
    public Result getBalance(String address) {
        if (!AddressTool.validAddress(address)) {
            return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
        }
        Result result = restFul.get("/accountledger/balance/" + address, null);
        if (result.isFailed()) {
            return result;
        }
        Map<String, Object> map = (Map) result.getData();
        map.put("balance", ((Map) map.get("balance")).get("value"));
        map.put("usable", ((Map) map.get("usable")).get("value"));
        map.put("locked", ((Map) map.get("locked")).get("value"));
        BalanceInfo balanceDto = new BalanceInfo(map);
        return result.setData(balanceDto);
    }

    @Override
    public Result broadcastTransaction(String txHex) {
        Map<String, Object> map = new HashMap<>();
        map.put("txHex", txHex);
        Result result = restFul.post("/accountledger/transaction/broadcast", map);
        return result;
    }

    @Override
    public Result validateTransaction(String txHex) {
        Map<String, Object> map = new HashMap<>();
        map.put("txHex", txHex);
        Result result = restFul.post("/accountledger/transaction/valiTransaction", map);
        return result;
    }

    @Override
    public Result createTransaction(List<Input> inputs, List<Output> outputs, String remark) {
        if (inputs == null || inputs.isEmpty()) {
            return Result.getFailed("inputs error");
        }
        if (outputs == null || outputs.isEmpty()) {
            return Result.getFailed("outputs error");
        }

        byte[] remarkBytes = null;
        if (!StringUtils.isBlank(remark)) {
            try {
                remarkBytes = remark.getBytes(SDKConstant.DEFAULT_ENCODING);
            } catch (UnsupportedEncodingException e) {
                return Result.getFailed("remark error");
            }
        }

        List<Coin> outputList = new ArrayList<>();
        for (int i = 0; i < outputs.size(); i++) {
            Output outputDto = outputs.get(i);
            Coin to = new Coin();
            try {
                if (!AddressTool.validAddress(outputDto.getAddress())) {
                    return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
                }
                byte[] owner = AddressTool.getAddress(outputDto.getAddress());
                if (owner[2] == 3) {
                    Script scriptPubkey = SignatureUtil.createOutputScript(to.getAddress());
                    to.setOwner(scriptPubkey.getProgram());
                } else {
                    to.setOwner(AddressTool.getAddress(outputDto.getAddress()));

                }
            } catch (Exception e) {
                return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
            }

            try {
                to.setNa(Na.valueOf(outputDto.getValue()));
            } catch (Exception e) {
                return Result.getFailed(AccountErrorCode.DATA_PARSE_ERROR);
            }
            if (outputDto.getLockTime() < 0) {
                return Result.getFailed("lockTime error");
            }

            to.setLockTime(outputDto.getLockTime());
            outputList.add(to);
        }

        List<Coin> inputsList = new ArrayList<>();
        String address = null;

        for (int i = 0; i < inputs.size(); i++) {
            Input inputDto = inputs.get(i);
            if (inputDto.getAddress() == null) {
                return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
            }
            if (i == 0) {
                address = inputDto.getAddress();
            } else {
                if (!address.equals(inputDto.getAddress())) {
                    return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
                }
            }
            byte[] key = Arrays.concatenate(Hex.decode(inputDto.getFromHash()), new VarInt(inputDto.getFromIndex()).encode());
            Coin coin = new Coin();
            coin.setOwner(key);
            coin.setNa(Na.valueOf(inputDto.getValue()));
            coin.setLockTime(inputDto.getLockTime());
            inputsList.add(coin);
        }

        io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.createTransferTx(inputsList, outputList, remarkBytes);
        if (!TransactionTool.isFeeEnough(tx, 1)) {
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
    public Result signTransaction(String txHex, String priKey, String address, String password) {
        if (StringUtils.isBlank(priKey)) {
            return Result.getFailed("priKey error");
        }
        if (StringUtils.isBlank(txHex)) {
            return Result.getFailed("txHex error");
        }
        if (!AddressTool.validAddress(address)) {
            return Result.getFailed("address error");
        }

        if (StringUtils.isNotBlank(password)) {
            if (StringUtils.validPassword(password)) {
                //decrypt
                byte[] privateKeyBytes = null;
                try {
                    privateKeyBytes = AESEncrypt.decrypt(Hex.decode(priKey), password);
                } catch (Exception e) {
                    return Result.getFailed(AccountErrorCode.PASSWORD_IS_WRONG);
                }
                priKey = Hex.encode(privateKeyBytes);
            } else {
                return Result.getFailed(AccountErrorCode.PASSWORD_IS_WRONG);
            }
        }
        if (!ECKey.isValidPrivteHex(priKey)) {
            return Result.getFailed(AccountErrorCode.PARAMETER_ERROR, "priKey error");
        }

        ECKey key = ECKey.fromPrivate(new BigInteger(Hex.decode(priKey)));
        try {
            String newAddress = AccountTool.newAddress(key).getBase58();
            if (!newAddress.equals(address)) {
                return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
            }
        } catch (NulsException e) {
            return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
        }

        try {
            byte[] data = Hex.decode(txHex);
            io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.getInstance(new NulsByteBuffer(data));
            tx = TransactionTool.signTransaction(tx, key);

            txHex = Hex.encode(tx.serialize());
            Map<String, String> map = new HashMap<>();
            map.put("value", txHex);
            return Result.getSuccess().setData(map);
        } catch (Exception e) {
            Log.error(e);
            return Result.getFailed(AccountErrorCode.DATA_PARSE_ERROR);
        }
    }

    @Override
    public Result signMultipleTransaction(String txHex, List<String> privKeys, String password) {
        if (StringUtils.isBlank(txHex)) {
            return Result.getFailed("txHex error");
        }
        if (privKeys == null || privKeys.size() == 0) {
            return Result.getFailed("privKeys error");
        }

        // decode private key
        if (StringUtils.isNotBlank(password)) {
            if (!StringUtils.validPassword(password)) {
                return Result.getFailed(AccountErrorCode.PASSWORD_IS_WRONG);
            }

            privKeys = privKeys.stream()
                    .map(p -> {
                        byte[] privateKeyBytes = null;
                        try {
                            privateKeyBytes = AESEncrypt.decrypt(Hex.decode(p), password);
                        } catch (Exception e) {
                            return null;
                        }
                        return Hex.encode(privateKeyBytes);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        List<ECKey> keys = privKeys.stream()
                .map(p -> ECKey.fromPrivate(new BigInteger(Hex.decode(p))))
                .collect(Collectors.toList());

        // sign the transaction
        try {
            byte[] data = Hex.decode(txHex);
            io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.getInstance(new NulsByteBuffer(data));
            List<P2PHKSignature> p2PHKSignatures = SignatureUtil.createSignaturesByEckey(tx, keys);
            TransactionSignature transactionSignature = new TransactionSignature();
            transactionSignature.setP2PHKSignatures(p2PHKSignatures);
            txHex = Hex.encode(tx.serialize());
            Map<String, String> map = new HashMap<>();
            map.put("value", txHex);
            return Result.getSuccess().setData(map);
        } catch (Exception e) {
            Log.error(e);
            return Result.getFailed(AccountErrorCode.DATA_PARSE_ERROR);
        }
    }

    @Override
    public Result createMSAccountTransferTransaction(List<Input> inputs, List<Output> outputs, String remark) {

        return null;
    }

    @Override
    public Result createChangeCoinTransaction(List<Input> inputs, String address) {
        try {
            if (inputs == null || inputs.isEmpty()) {
                return Result.getFailed("inputs error");
            }
            if(StringUtils.isBlank(address) || !AddressTool.validAddress(address)){
                return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
            }
            Collections.sort(inputs, InputCompare.getInstance());
            int targetSize = 0;
            int size = 0;
            List<String> transactionList = new ArrayList<>();
            Na amount = Na.ZERO;
            boolean newTransaction = true;
            TransferTransaction tx = null;
            CoinData coinData = null;
            List <String> ownerHexList = null;
            for(int i = 0;i<inputs.size();i++){
                Input input = inputs.get(i);
                if (input.getAddress() == null || !input.getAddress().equals(address)) {
                    return Result.getFailed(AccountErrorCode.ADDRESS_ERROR);
                }
                //判断是否需创建新交易
                if(newTransaction){
                    tx = new TransferTransaction();
                    tx.setTime(TimeService.currentTimeMillis());
                    size = tx.getSize() + 38;
                    coinData = new CoinData();
                    targetSize = TransactionTool.MAX_TX_SIZE - size - P2PHKSignature.SERIALIZE_LENGTH;
                    amount = Na.ZERO;
                    ownerHexList = new ArrayList<>();
                    newTransaction = false;
                }
                byte[] key = Arrays.concatenate(Hex.decode(input.getFromHash()), new VarInt(input.getFromIndex()).encode());
                Coin coin = new Coin();
                coin.setOwner(key);
                coin.setNa(Na.valueOf(input.getValue()));
                coin.setLockTime(input.getLockTime());
                size += coin.size();
                ownerHexList.add(Hex.encode(key));
                //判断当前交易中的UTXO是否存在脚本签名的交易
                if(size > targetSize - P2PHKSignature.SERIALIZE_LENGTH){
                    Map<String, Object> map = new HashMap<>();
                    map.put("utxoList", ownerHexList);
                    Result result = restFul.post("/accountledger/multiAccount/getSignType", map);
                    Map<String, Object> resultMap = (Map) result.getData();
                    int signType = Integer.parseInt((String)resultMap.get("signType"));
                    //如果两种签名都存在
                    if((signType & 0x01) == 0x01 && (signType & 0x02) == 0x02){
                        size+=P2PHKSignature.SERIALIZE_LENGTH;
                        Na fee = TransactionFeeCalculator.getFee(size, TransactionFeeCalculator.MIN_PRECE_PRE_1024_BYTES);
                        amount = amount.subtract(fee);
                        tx.setCoinData(coinData);
                        Coin toCoin = new Coin(AddressTool.getAddress(address), amount);
                        coinData.getTo().add(toCoin);
                        tx.setHash(NulsDigestData.calcDigestData(tx.serializeForHash()));
                        transactionList.add(Hex.encode(tx.serialize()));
                        i--;
                        newTransaction = true;
                        continue;
                    }
                }
                if (i == 127) {
                    size += 1;
                }
                if (size > targetSize) {
                    Na fee = TransactionFeeCalculator.getFee(size, TransactionFeeCalculator.MIN_PRECE_PRE_1024_BYTES);
                    amount = amount.subtract(fee);
                    Coin toCoin = new Coin(AddressTool.getAddress(address), amount);
                    coinData.getTo().add(toCoin);
                    tx.setCoinData(coinData);
                    tx.setHash(NulsDigestData.calcDigestData(tx.serializeForHash()));
                    transactionList.add(Hex.encode(tx.serialize()));
                    i--;
                    newTransaction = true;
                    continue;
                }
                coinData.getFrom().add(coin);
                tx.setCoinData(coinData);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("value", transactionList);
            return Result.getSuccess().setData(map);
        }catch (IOException e){
            Log.error(e);
            return Result.getFailed(AccountErrorCode.DATA_PARSE_ERROR);
        }
    }

    @Override
    public Result signMultiTransaction(String txHex, List<String> privKeys, List<String> passwords) {
        try {
            if(StringUtils.isBlank(txHex)){
                return Result.getFailed("txHex can not be null!");
            }
            if(privKeys == null || privKeys.size() == 0){
                return Result.getFailed("The privKeys list can not be null!");
            }
            if(passwords == null || passwords.size() == 0){
                return Result.getFailed("The passwords list can not be null!");
            }
            if(passwords.size() != privKeys.size()){
                return Result.getFailed("privKeys length and passwords length are not equal,If there is no password in the account, please empty the string.");
            }
            io.nuls.sdk.core.model.transaction.Transaction tx = TransactionTool.getInstance(new NulsByteBuffer(Hex.decode(txHex)));
            for(int i=0;i<privKeys.size();i++){
                String priKey = privKeys.get(i);
                String password = passwords.get(i);
                priKey = getPrikey(priKey,password);
                if (!ECKey.isValidPrivteHex(priKey)) {
                    return Result.getFailed(AccountErrorCode.PARAMETER_ERROR, "priKey error");
                }
                ECKey key = ECKey.fromPrivate(new BigInteger(Hex.decode(priKey)));
            }
        }catch (Exception e){
            Log.error(e);
            return Result.getFailed(AccountErrorCode.DATA_PARSE_ERROR);
        }
        return null;
    }

    public String getPrikey(String prikey,String password){
        if (StringUtils.isNotBlank(password)) {
            if (StringUtils.validPassword(password)) {
                //decrypt
                byte[] privateKeyBytes = null;
                try {
                    privateKeyBytes = AESEncrypt.decrypt(Hex.decode(prikey), password);
                } catch (Exception e) {
                    return "";
                }
                prikey = Hex.encode(privateKeyBytes);
            } else {
                return "";
            }
        }
        return prikey;
    }
}
