package io.nuls.contract.sdk.token;

import io.nuls.contract.sdk.Address;

import java.math.BigInteger;

public interface Token {

    String name();

    String symbol();

    int decimals();

    BigInteger totalSupply();

    BigInteger balanceOf(Address owner);

    BigInteger allowance(Address owner, Address spender);

    boolean transfer(Address to, BigInteger value);

    boolean transferLocked(Address to, BigInteger value, long lockedTime);

    boolean transferFrom(Address from, Address to, BigInteger value);

    boolean approve(Address spender, BigInteger value);


}
