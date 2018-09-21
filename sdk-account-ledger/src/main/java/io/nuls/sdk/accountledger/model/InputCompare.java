package io.nuls.sdk.accountledger.model;
import java.util.Comparator;
public class InputCompare implements Comparator<Input> {
    private static InputCompare instance = new InputCompare();

    private InputCompare() {

    }

    public static InputCompare getInstance() {
        return instance;
    }

    @Override
    public int compare(Input o1, Input o2) {
        if(o1 == null) {
            return -1;
        }
        if(o2 == null) {
            return 1;
        }
        if(o1.getValue() > o2.getValue()) {
            return -1;
        }else{
            return 1;
        }
    }
}
