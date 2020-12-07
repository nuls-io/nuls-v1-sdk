package io.nuls.contract.sdk;

/**
 * 合约接口，合约类实现这个接口
 */
public interface Contract {

    /**
     * 直接向合约转账NULS，会触发这个方法，默认不做任何操作
     * 前提: 若合约地址支持直接转账NULS，需重载这个方法，并且标记`@Payable`注解
     */
    default void _payable() {
    }

    /**
     * 直接向合约转账其他资产，会触发这个方法，默认不做任何操作
     * 前提: 若合约地址支持直接转账其他资产，需重载这个方法，并且标记`@PayableMultyAsset`注解
     */
    default void _payableMultyAsset() {
    }

    /**
     * 1. 当共识节点奖励地址是合约地址时，会触发这个方法，参数是区块奖励地址明细 eg. [[address, amount], [address, amount], ...]
     * 2. 当委托节点地址是合约地址时，会触发这个方法，参数是合约地址和奖励金额 eg. [[address, amount]]
     * 前提: 需重载这个方法，并且标记`@Payable`注解
     */
    default void _payable(String[][] args) {
    }
}
