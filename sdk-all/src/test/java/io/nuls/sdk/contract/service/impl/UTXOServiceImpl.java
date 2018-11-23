package io.nuls.sdk.contract.service.impl;

import io.nuls.sdk.accountledger.model.Input;
import io.nuls.sdk.contract.service.UTXOService;
import io.nuls.sdk.core.model.Result;
import io.nuls.sdk.core.utils.RestFulUtils;
import io.nuls.sdk.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * for test add get utxos
 * Created by wangkun23 on 2018/10/9.
 */
public class UTXOServiceImpl implements UTXOService {

    final static Logger logger = LoggerFactory.getLogger(UTXOServiceImpl.class);

    private static UTXOService instance = new UTXOServiceImpl();

    private RestFulUtils restFulUtils = RestFulUtils.getInstance();

    /**
     * instance
     *
     * @return
     */
    public static UTXOService getInstance() {
        return instance;
    }

    /**
     * get utxos by address and amount
     *
     * @param address
     * @param amount
     * @return
     */
    @Override
    public List<Input> getUTXOs(String address, Long amount) {
        if (StringUtils.isBlank(address)) {
            throw new IllegalArgumentException("address not be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount not be null");
        }
        String url = "/utxo/amount/" + address + "/" + amount;
        Result result = restFulUtils.get(url, new HashMap());

        if (result.isFailed()) {
            return Collections.EMPTY_LIST;
        }
        List<Input> inputs = new ArrayList<>();
        Map data = (Map) result.getData();
        List<Map<String, Object>> utxos = (List<Map<String, Object>>) data.get("utxoDtoList");
        for (Map<String, Object> utxo : utxos) {
            Input input = new Input();
            input.setAddress(address);
            input.setFromHash(utxo.get("txHash").toString());
            input.setFromIndex((int) utxo.get("txIndex"));
            input.setValue(Long.parseLong(utxo.get("value").toString()));
            input.setLockTime((Integer) utxo.get("lockTime"));
            inputs.add(input);
        }
        return inputs;
    }
}
