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
import io.nuls.sdk.core.crypto.Hex;
import io.nuls.sdk.core.model.Coin;
import io.nuls.sdk.core.model.Na;
import io.nuls.sdk.core.utils.AddressTool;
import io.nuls.sdk.core.utils.ArraysTool;
import io.nuls.sdk.core.utils.VarInt;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: PierreLuo
 */
public class ConvertCoinTool {

    public static List<Coin> convertCoinList(List<Input> utxos) {
        if(utxos == null || utxos.size() == 0) {
            return null;
        }
        List<Coin> coinList = new ArrayList<>(utxos.size());
        for(Input utxo : utxos) {
            coinList.add(convertCoin(utxo));
        }
        return coinList;
    }

    public static Coin convertCoin(Input utxo) {
        Coin coin = new Coin();
        byte[] txHashBytes = Hex.decode(utxo.getFromHash());
        coin.setOwner(ArraysTool.concatenate(txHashBytes, new VarInt(utxo.getFromIndex()).encode()));
        coin.setLockTime(utxo.getLockTime());
        coin.setNa(Na.valueOf(utxo.getValue()));
        coin.setTempOwner(AddressTool.getAddress(utxo.getAddress()));
        return coin;
    }

    public static List<Input> convertInputList(List<Coin> froms) {
        if(froms == null || froms.size() == 0) {
            return null;
        }
        List<Input> inputs = new ArrayList<>(froms.size());
        for(Coin coin : froms) {
            inputs.add(convertInput(coin));
        }
        return inputs;
    }

    public static Input convertInput(Coin coin) {
        Input input = new Input();
        input.setAddress(AddressTool.getStringAddressByBytes(coin.getTempOwner()));
        input.setLockTime(coin.getLockTime());
        input.setValue(coin.getNa().getValue());
        input.setFromHash(LedgerUtil.getTxHash(coin.getOwner()));
        input.setFromIndex(LedgerUtil.getIndex(coin.getOwner()));
        return input;
    }
}
