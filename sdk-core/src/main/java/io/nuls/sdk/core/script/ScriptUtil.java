package io.nuls.sdk.core.script;

import java.util.List;

public class ScriptUtil {

    /**
     * 根据交易签名和公钥生成解锁脚本 （P2PSH）
     *
     * @param sigByte    交易签名
     * @param pubkeyByte 公钥
     * @return Script      生成的解锁脚本
     */
    public static Script createP2PKHInputScript(byte[] sigByte, byte[] pubkeyByte) {
        return ScriptBuilder.createNulsInputScript(sigByte, pubkeyByte);
    }

    /**
     * 根据输出地址生成锁定脚本
     *
     * @param address 输出地址
     * @return Script  生成的锁定脚本
     */
    public static Script createP2PKHOutputScript(byte[] address) {
        return ScriptBuilder.createOutputScript(address, 1);
    }


    /**
     * M-N多重签名模式下根据多个公钥和M-N生成赎回脚本
     *
     * @param pub_keys 公钥列表
     * @param m        表示至少需要多少个签名验证通过
     * @return Script  生成的锁定脚本
     */
    public static Script creatRredeemScript(List<String> pub_keys, int m) {
        return ScriptBuilder.createNulsRedeemScript(m, pub_keys);
    }

    /**
     * M-N多重签名模式下根据多个公钥和M-N生成解锁脚本（N就是公钥列表长度）
     *
     * @param signatures      签名列表
     * @param multisigProgram 当交易为P2SH时，表示的就是赎回脚本
     * @return Script     生成的解鎖脚本
     */
    public static Script createP2SHInputScript(List<byte[]> signatures, Script multisigProgram) {
        return ScriptBuilder.createNulsP2SHMultiSigInputScript(signatures, multisigProgram);
    }

    /**
     * M-N多重签名模式下根据多个公钥和M-N生成锁定脚本（N就是公钥列表长度）
     *
     * @param redeemScript 贖回腳本
     * @return Script  生成的锁定脚本
     */
    public static Script createP2SHOutputScript(Script redeemScript) {
        return ScriptBuilder.createP2SHOutputScript(redeemScript);
    }

    /**
     * M-N多重签名模式下，根据输出地址生成锁定脚本
     *
     * @param address 输出地址
     * @return Script  生成的锁定脚本
     */
    public static Script createP2SHOutputScript(byte[] address) {
        return ScriptBuilder.createOutputScript(address, 0);
    }
}
