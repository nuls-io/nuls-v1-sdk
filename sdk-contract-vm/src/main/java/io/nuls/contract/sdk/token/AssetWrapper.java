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
package io.nuls.contract.sdk.token;

import io.nuls.contract.sdk.Address;

import java.math.BigInteger;

import static io.nuls.contract.sdk.Utils.revert;

/**
 * @author: PierreLuo
 * @date: 2021/8/31
 */
public class AssetWrapper implements Token {

    private int assetChainId;
    private int assetId;

    public AssetWrapper(int assetChainId, int assetId) {
        this.assetChainId = assetChainId;
        this.assetId = assetId;
    }

    @Override
    public String name() {
        revert("Not Support");
        return null;
    }

    @Override
    public String symbol() {
        revert("Not Support");
        return null;
    }

    @Override
    public int decimals() {
        revert("Not Support");
        return 0;
    }

    @Override
    public BigInteger totalSupply() {
        revert("Not Support");
        return BigInteger.ZERO;
    }

    @Override
    public BigInteger balanceOf(Address owner) {
        return owner.balance(assetChainId, assetId);
    }

    @Override
    public BigInteger allowance(Address owner, Address spender) {
        revert("Not Support");
        return BigInteger.ZERO;
    }

    @Override
    public boolean transfer(Address to, BigInteger value) {
        to.transfer(value, assetChainId, assetId);
        return true;
    }

    @Override
    public boolean transferLocked(Address to, BigInteger value, long lockedTime) {
        to.transferLocked(value, assetChainId, assetId, lockedTime);
        return true;
    }

    @Override
    public boolean transferFrom(Address from, Address to, BigInteger value) {
        revert("Not Support");
        return false;
    }

    @Override
    public boolean approve(Address spender, BigInteger value) {
        revert("Not Support");
        return false;
    }

    public int getAssetChainId() {
        return assetChainId;
    }

    public int getAssetId() {
        return assetId;
    }
}
