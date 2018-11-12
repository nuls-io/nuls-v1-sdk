/**
 * MIT License
 * <p>
 * Copyright (c) 2017-2018 nuls.io
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.nuls.sdk.accountledger.utils;

import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.accountledger.model.TransactionCreatedReturnInfo;
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.model.NulsDigestData;
import io.nuls.sdk.core.model.transaction.Transaction;
import io.nuls.sdk.core.utils.AssertUtil;
import io.nuls.sdk.core.utils.VarInt;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * @desription:
 * @author: PierreLuo
 */
public class LedgerUtil {

    private final static int TX_HASH_LENGTH = NulsDigestData.HASH_LENGTH;


    public static byte[] getTxHashBytes(byte[] fromBytes) {
        if(fromBytes == null || fromBytes.length < TX_HASH_LENGTH) {
            return null;
        }
        byte[] txBytes = new byte[TX_HASH_LENGTH];
        System.arraycopy(fromBytes, 0, txBytes, 0, TX_HASH_LENGTH);
        return txBytes;
    }

    public static String getTxHash(byte[] fromBytes) {
        byte[] txBytes = getTxHashBytes(fromBytes);
        if(txBytes != null) {
            return Hex.encode(txBytes);
        }
        return null;
    }

    public static byte[] getIndexBytes(byte[] fromBytes) {
        if(fromBytes == null || fromBytes.length < TX_HASH_LENGTH) {
            return null;
        }
        int length = fromBytes.length - TX_HASH_LENGTH;
        byte[] indexBytes = new byte[length];
        System.arraycopy(fromBytes, TX_HASH_LENGTH, indexBytes, 0, length);
        return indexBytes;
    }

    public static Integer getIndex(byte[] fromBytes) {
        byte[] indexBytes = getIndexBytes(fromBytes);
        if(indexBytes != null) {
            VarInt varInt = new VarInt(indexBytes, 0);
            return Math.toIntExact(varInt.value);
        }
        return null;
    }

    public static String asString(byte[] bytes) {
        AssertUtil.canNotEmpty(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] asBytes(String string) {
        AssertUtil.canNotEmpty(string);
        return Base64.getDecoder().decode(string);
    }

    public static TransactionCreatedReturnInfo makeReturnInfo(Transaction tx) throws IOException {
        String hash = NulsDigestData.calcDigestData(tx.serializeForHash()).getDigestHex();
        String txHex = Hex.encode(tx.serialize());
        List<Input> inputs = ConvertCoinTool.convertInputList(tx.getCoinData().getFrom());
        TransactionCreatedReturnInfo returnInfo = new TransactionCreatedReturnInfo(hash, txHex, inputs);
        return returnInfo;
    }
}
