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
import io.nuls.contract.sdk.Block;

import java.math.BigInteger;

import static io.nuls.contract.sdk.Utils.require;

/**
 * @author: PierreLuo
 * @date: 2021/8/31
 */
public class NRC20Wrapper implements Token {

    private Address nrc20Token;

    public NRC20Wrapper(Address nrc20Token) {
        require(nrc20Token.isContract(), "not contract address");
        this.nrc20Token = nrc20Token;
    }

    @Override
    public String name() {
        String result = nrc20Token.callWithReturnValue("name", "", null, BigInteger.ZERO);
        if (result != null && result.length() > 0) {
            return result;
        }
        return null;
    }

    @Override
    public String symbol() {
        String result = nrc20Token.callWithReturnValue("symbol", "", null, BigInteger.ZERO);
        if (result != null && result.length() > 0) {
            return result;
        }
        return null;
    }

    @Override
    public int decimals() {
        String result = nrc20Token.callWithReturnValue("decimals", "", null, BigInteger.ZERO);
        if (result != null && result.length() > 0) {
            return Integer.parseInt(result);
        }
        return 0;
    }

    @Override
    public BigInteger totalSupply() {
        String result = nrc20Token.callWithReturnValue("totalSupply", "", null, BigInteger.ZERO);
        if (result != null && result.length() > 0) {
            return new BigInteger(result);
        }
        return BigInteger.ZERO;
    }

    @Override
    public BigInteger balanceOf(Address owner) {
        String[][] args = new String[1][];
        args[0] = new String[]{owner.toString()};
        String result = nrc20Token.callWithReturnValue("balanceOf", "", args, BigInteger.ZERO);
        if (result != null && result.length() > 0) {
            return new BigInteger(result);
        }
        return BigInteger.ZERO;
    }

    @Override
    public BigInteger allowance(Address owner, Address spender) {
        String[][] args = new String[2][];
        args[0] = new String[]{owner.toString()};
        args[1] = new String[]{spender.toString()};
        String result = nrc20Token.callWithReturnValue("allowance", "", args, BigInteger.ZERO);
        if (result != null && result.length() > 0) {
            return new BigInteger(result);
        }
        return null;
    }

    @Override
    public boolean transfer(Address to, BigInteger value) {
        String[][] args = new String[2][];
        args[0] = new String[]{to.toString()};
        args[1] = new String[]{value.toString()};
        String result = nrc20Token.callWithReturnValue("transfer", "", args, BigInteger.ZERO);
        if (result != null && result.length() > 0) {
            return Boolean.parseBoolean(result);
        }
        return false;
    }

    @Override
    public boolean transferLocked(Address to, BigInteger value, long lockedTime) {
        // Check whether the filled lock time is incremental or natural time
        if (lockedTime < 1640995200) {
            // if the time is less than 2022-01-01, it is regarded as an increment, and the current block timestamp is added.
            lockedTime += Block.timestamp();
        }
        String[][] args = new String[3][];
        args[0] = new String[]{to.toString()};
        args[1] = new String[]{value.toString()};
        args[2] = new String[]{String.valueOf(lockedTime)};
        String result = nrc20Token.callWithReturnValue("transferLocked", "", args, BigInteger.ZERO);
        if (result != null && result.length() > 0) {
            return Boolean.parseBoolean(result);
        }
        return false;
    }

    @Override
    public boolean transferFrom(Address from, Address to, BigInteger value) {
        String[][] args = new String[3][];
        args[0] = new String[]{from.toString()};
        args[1] = new String[]{to.toString()};
        args[2] = new String[]{value.toString()};
        String result = nrc20Token.callWithReturnValue("transferFrom", "", args, BigInteger.ZERO);
        if (result != null && result.length() > 0) {
            return Boolean.parseBoolean(result);
        }
        return false;
    }

    @Override
    public boolean approve(Address spender, BigInteger value) {
        String[][] args = new String[2][];
        args[0] = new String[]{spender.toString()};
        args[1] = new String[]{value.toString()};
        String result = nrc20Token.callWithReturnValue("approve", "", args, BigInteger.ZERO);
        if (result != null && result.length() > 0) {
            return Boolean.parseBoolean(result);
        }
        return false;
    }

    public Address getNrc20Token() {
        return nrc20Token;
    }
}
