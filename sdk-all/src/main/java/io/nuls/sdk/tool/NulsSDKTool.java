package io.nuls.sdk.tool;

import io.nuls.sdk.account.service.AccountService;
import io.nuls.sdk.account.service.impl.AccountServiceImpl;
import io.nuls.sdk.accountledger.model.*;
import io.nuls.sdk.accountledger.service.AccountLedgerService;
import io.nuls.sdk.accountledger.service.impl.AccountLedgerServiceImpl;
import io.nuls.sdk.consensus.model.AgentInfo;
import io.nuls.sdk.consensus.model.DepositInfo;
import io.nuls.sdk.consensus.service.ConsensusService;
import io.nuls.sdk.consensus.service.impl.ConsensusServiceImpl;
import io.nuls.sdk.contract.service.ContractService;
import io.nuls.sdk.contract.service.impl.ContractServiceImpl;
import io.nuls.sdk.core.model.JsonRPCResult;
import io.nuls.sdk.core.model.Na;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.utils.TransactionTool;
import io.nuls.sdk.protocol.service.BlockService;
import io.nuls.sdk.protocol.service.impl.BlockServiceImpl;

import java.io.FileReader;
import java.util.List;

public class NulsSDKTool {

    static {
        TransactionTool.init();
    }

    private static AccountService accountService = AccountServiceImpl.getInstance();

    private static AccountLedgerService accountLedgerService = AccountLedgerServiceImpl.getInstance();

    private static BlockService blockService = BlockServiceImpl.getInstance();

    private static ConsensusService consensusService = ConsensusServiceImpl.getInstance();

    private static ContractService contractService = ContractServiceImpl.getInstance();

    public static Result createAccount() {
        return accountService.createAccount();
    }

    public static Result createAccount(String password) {
        return accountService.createAccount(password);
    }

    public static Result createAccount(int count) {
        return accountService.createAccount(count);
    }

    public static Result createAccount(int count, String password) {
        return accountService.createAccount(count, password);
    }

    public static Result createOfflineAccount() {
        return accountService.createOfflineAccount();
    }

    public static Result createOfflineAccount(String password) {
        return accountService.createOfflineAccount(password);
    }

    public static Result createOfflineAccount(int count) {
        return accountService.createOfflineAccount(count);
    }

    public static Result createOfflineAccount(int count, String password) {
        return accountService.createOfflineAccount(count, password);
    }

//    public static Result backupAccount(String address, String password) {
//        return accountService.backupAccount(address, "", password);
//    }

    public static Result backupAccount(String address, String password) {
        return accountService.backupAccount(address, "", password);
    }

    public static Result getAliasFee(String address, String alias) {
        return accountService.getAliasFee(address, alias);
    }

    public static Result getAccount(String address) {
        return accountService.getAccount(address);
    }

    public static Result getAccountList(int pageNumber, int pageSize) {
        return accountService.getAccountList(pageNumber, pageSize);
    }

    public static Result getAssets(String address) {
        return accountService.getAssets(address);
    }

    public static Result getAddressByAlias(String alias) {
        return accountService.getAddressByAlias(alias);
    }

    public static Result getPrikey(String address, String password) {
        return accountService.getPrikey(address, password);
    }

    public static Result getPrikey(String address) {
        return accountService.getPrikey(address);
    }

    public static Result getPrikeyOffline(String encryptedPriKey, String password) {
        return accountService.getPrikeyOffline(encryptedPriKey, password);
    }

    public static Result getWalletTotalBalance() {
        return accountService.getWalletTotalBalance();
    }

    public static Result isAliasUsable(String alias) {
        return accountService.isAliasUsable(alias);
    }

    public static Result importAccountByKeystore(String path, String password, boolean overwrite) {
        return accountService.importAccountByKeystore(path, password, overwrite);
    }

    public static Result importAccountByKeystore(String path, boolean overwrite) {
        return accountService.importAccountByKeystore(path, overwrite);
    }

    public static Result importAccountByKeystore(FileReader fileReader, String password, boolean overwrite) {
        return accountService.importAccountByKeystore(fileReader, password, overwrite);
    }


    public static Result importAccountByKeystore(FileReader fileReader, boolean overwrite) {
        return accountService.importAccountByKeystore(fileReader, overwrite);
    }

    public static Result importAccountByPriKey(String privateKey, String password, boolean overwrite) {
        return accountService.importAccountByPriKey(privateKey, password, overwrite);
    }

    public static Result importAccountByPriKey(String privateKey, boolean overwrite) {
        return accountService.importAccountByPriKey(privateKey, overwrite);
    }

    public static Result isEncrypted(String address) {
        return accountService.isEncrypted(address);
    }

    public static Result lockAccount(String address) {
        return accountService.lockAccount(address);
    }

    public static Result unlockAccount(String address, String password, int unlockTime) {
        return accountService.unlockAccount(address, password, unlockTime);
    }

    public static Result removeAccount(String address, String password) {
        return accountService.removeAccount(address, password);
    }

    public static Result removeAccount(String address) {
        return accountService.removeAccount(address);
    }

    public static Result setPassword(String address, String password) {
        return accountService.setPassword(address, password);
    }

    public static Result resetPassword(String address, String password, String newPassword) {
        return accountService.resetPassword(address, password, newPassword);
    }

    public static Result setPasswordOffline(String address, String priKey, String password) {
        return accountService.setPasswordOffline(address, priKey, password);
    }

    public static Result resetPasswordOffline(String address, String encryptedPriKey, String password, String newPassword) {
        return accountService.resetPasswordOffline(address, encryptedPriKey, password, newPassword);
    }

    public static Result updatePasswordByKeystore(FileReader fileReader, String password) {
        return accountService.updatePasswordByKeystore(fileReader, password);
    }

    public static Result setAlias(String address, String alias, String password) {
        return accountService.setAlias(address, alias, password);
    }

    public static Result setAlias(String address, String alias) {
        return accountService.setAlias(address, alias);
    }

    public static Result getAddressByPriKey(String priKey) {
        return accountService.getAddressByPriKey(priKey);
    }

    public static Result getAddressByEncryptedPriKey(String encryptedPriKey, String password) {
        return accountService.getAddressByEncryptedPriKey(encryptedPriKey, password);
    }

    public static Result validateAddress(String address) {
        return accountService.validateAddress(address);
    }

    public static Result createMSAccount(List<String> pubKeys, Integer threshold) {
        return accountService.createMSAccount(pubKeys, threshold);
    }

    public static Result getTxByHash(String hash) {
        return accountLedgerService.getTxByHash(hash);
    }

    public static Result transfer(String address, String toAddress, String password, long amount, String remark) {
        return accountLedgerService.transfer(address, toAddress, password, amount, remark);
    }

    public static Result transfer(String address, String toAddress, long amount, String remark) {
        return accountLedgerService.transfer(address, toAddress, amount, remark);
    }

    public static Result sendToAddress(String address, String toAddress, String password, long amount, String remark) {
        return accountLedgerService.sendToAddress(address, toAddress, password, amount, remark);
    }

    public static Result sendToAddress(String address, String toAddress, long amount, String remark) {
        return accountLedgerService.sendToAddress(address, toAddress, amount, remark);
    }

    public static Result multipleAddressTransfer(List<TransferFrom> inputs, List<TransferTo> outputs, String remark) {
        return accountLedgerService.multipleAddressTransfer(inputs, outputs, remark);
    }

    public static Result getBalance(String address) {
        return accountLedgerService.getBalance(address);
    }

    public static Result getAccountBalance(String address) {
        return accountService.getAccountBalance(address);
    }

    public static JsonRPCResult getUtxo(String address, long amount) {
        return accountLedgerService.getUTXO(address, amount);
    }

    public static Result createTransaction(List<Input> inputs, List<Output> outputs, String remark) {
        return accountLedgerService.createTransaction(inputs, outputs, remark);
    }

    public static Result createTransaction(String address, String toAddress, long amount, String remark, List<Input> inputs) {
        return accountLedgerService.createTransaction(address, toAddress, amount, remark, inputs);
    }

    public static Result signTransaction(String txHex, String priKey, String address, String password) {
        return accountLedgerService.signTransaction(txHex, priKey, address, password);
    }

    public static Result broadcastTransaction(String txHex) {
        return accountLedgerService.broadcastTransaction(txHex);
    }

    public static Result validateTransaction(String txHex) {
        return accountLedgerService.validateTransaction(txHex);
    }

    public static Result getNewestBlockHeight() {
        return blockService.getNewestBlockHeight();
    }

    public static Result getNewestBlockHash() {
        return blockService.getNewestBlockHash();
    }

    public static Result getNewestBlockHeader() {
        return blockService.getNewestBlockHeader();
    }

    public static Result getBlockHeader(int height) {
        return blockService.getBlockHeader(height);
    }

    public static Result getBlockHeader(String hash) {
        return blockService.getBlockHeader(hash);
    }

    public static Result getBlock(int height) {
        return blockService.getBlock(height);
    }

    public static Result getBlock(String hash) {
        return blockService.getBlock(hash);
    }

    public static Result getBlockWithBytes(String hash) {
        return blockService.getBlockWithBytes(hash);
    }

    public static Result getBlockWithBytes(long height) {
        return blockService.getBlockWithBytes(height);
    }

    public static Result createAgentTransaction(AgentInfo agentInfo, List<Input> inputs, Na fee) {
        return consensusService.createAgentTransaction(agentInfo, inputs, fee);
    }

    public static Result createDepositTransaction(DepositInfo depositInfo, List<Input> inputs, Na fee) {
        return consensusService.createDepositTransaction(depositInfo, inputs, fee);
    }

    public static Result createCancelDepositTransaction(Output output) {
        return consensusService.createCancelDepositTransaction(output);
    }

    public static Result createStopAgentTransaction(Output output) {
        return consensusService.createStopAgentTransaction(output);
    }

    public static Result getDeposits(String address, int pageNumber, int pageSize) {
        return consensusService.getDeposits(address, pageNumber, pageSize);
    }

    public static Result getAgentDeposits(String agentHash, int pageNumber, int pageSize) {
        return consensusService.getAgentDeposits(agentHash, pageNumber, pageSize);
    }

    public static Result createMultipleInputAddressTransaction(List<Input> inputs, int nInputAccount, List<Output> outputs, String remark) {
        return accountLedgerService.createMultipleInputAddressTransaction(inputs, nInputAccount, outputs, remark);
    }

    public static Result createMSAccountTransferTransaction(MSAccount account, List<Input> inputs, List<Output> outputs, String remark) {
        return accountLedgerService.createMSAccountTransferTransaction(account, inputs, outputs, remark);
    }

    public static Result signMultiTransaction(String txHex, List<String> privKeys, List<String> passwords) {
        return accountLedgerService.signMultipleAddressTransaction(txHex, privKeys, passwords);
    }

    public static Result createChangeCoinTransaction(List<Input> inputs, String address) {
        return accountLedgerService.createChangeCoinTransaction(inputs, address);
    }

    public static Result createMSAgentTransaction(AgentInfo agentInfo, List<Input> inputs, Na fee) {
        return consensusService.createMSAgentTransaction(agentInfo, inputs, fee);
    }

    public static Result createStopMSAgentTransaction(Output output) {
        return consensusService.createStopMSAgentTransaction(output);
    }

    public static Result createMSAccountDepositTransaction(DepositInfo info, List<Input> inputs, Na fee) {
        return consensusService.createMSAccountDepositTransaction(info, inputs, fee);
    }

    public static Result createMSAccountCancelDepositTransaction(Output output) {
        return consensusService.createMSAccountCancelDepositTransaction(output);
    }

    public static Result createContractTransaction(String sender, Long gasLimit, Long price, String contractCodeHex, Object[] args, String remark, List<Input> utxos) {
        return contractService.createContractTransaction(sender, gasLimit, price, contractCodeHex, args, remark, utxos);
    }

    public static Result callContractTransaction(String sender, Long value, Long gasLimit, Long price, String contractAddress, String methodName, String methodDesc, Object[] args, String remark, List<Input> utxos) {
        return contractService.callContractTransaction(sender, value, gasLimit, price, contractAddress, methodName, methodDesc, args, remark, utxos);
    }

    public static Result deleteContractTransaction(String sender, String contractAddress, String remark, List<Input> utxos) {
        return contractService.deleteContractTransaction(sender, contractAddress, remark, utxos);
    }

    public static Result getTxWithBytes(String hash) {
        return accountLedgerService.getTxWithBytesByHash(hash);
    }
}
