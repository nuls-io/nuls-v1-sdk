package io.nuls.sdk.core.model;

import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.utils.NulsByteBuffer;
import io.nuls.sdk.core.utils.NulsOutputStreamBuffer;
import io.nuls.sdk.core.utils.SerializeUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * contract call tx data
 * Created by wangkun23 on 2018/10/8.
 */
public class CallContractData extends TransactionLogicData implements ContractData {
    private byte[] sender;
    private byte[] contractAddress;
    private long value;
    private long gasLimit;
    private long price;
    private String methodName;
    private String methodDesc;
    private byte argsCount;
    private String[][] args;

    @Override
    public int size() {
        int size = 0;
        size += Address.ADDRESS_LENGTH;
        size += Address.ADDRESS_LENGTH;
        size += SerializeUtils.sizeOfInt64();
        size += SerializeUtils.sizeOfInt64();
        size += SerializeUtils.sizeOfInt64();

        size += SerializeUtils.sizeOfString(methodName);
        size += SerializeUtils.sizeOfString(methodDesc);
        size += 1;
        if (args != null) {
            for (String[] arg : args) {
                if (arg == null) {
                    size += 1;
                } else {
                    size += 1;
                    for (String str : arg) {
                        size += SerializeUtils.sizeOfString(str);
                    }
                }
            }
        }
        return size;
    }

    @Override
    protected void serializeToStream(NulsOutputStreamBuffer stream) throws IOException {
        stream.write(sender);
        stream.write(contractAddress);
        stream.writeInt64(value);
        stream.writeInt64(gasLimit);
        stream.writeInt64(price);

        stream.writeString(methodName);
        stream.writeString(methodDesc);
        stream.write(argsCount);
        if (args != null) {
            for (String[] arg : args) {
                if (arg == null) {
                    stream.write((byte) 0);
                } else {
                    stream.write((byte) arg.length);
                    for (String str : arg) {
                        stream.writeString(str);
                    }
                }
            }
        }
    }

    @Override
    public void parse(NulsByteBuffer byteBuffer) throws NulsException {
        this.sender = byteBuffer.readBytes(Address.ADDRESS_LENGTH);
        this.contractAddress = byteBuffer.readBytes(Address.ADDRESS_LENGTH);
        this.value = byteBuffer.readInt64();
        this.gasLimit = byteBuffer.readInt64();
        this.price = byteBuffer.readInt64();

        this.methodName = byteBuffer.readString();
        this.methodDesc = byteBuffer.readString();
        this.argsCount = byteBuffer.readByte();
        byte length = this.argsCount;
        this.args = new String[length][];
        for (byte i = 0; i < length; i++) {
            byte argCount = byteBuffer.readByte();
            if (argCount == 0) {
                args[i] = new String[0];
            } else {
                String[] arg = new String[argCount];
                for (byte k = 0; k < argCount; k++) {
                    arg[k] = byteBuffer.readString();
                }
                args[i] = arg;
            }
        }
    }

    @Override
    public byte[] getSender() {
        return sender;
    }

    public void setSender(byte[] sender) {
        this.sender = sender;
    }

    @Override
    public byte[] getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(byte[] contractAddress) {
        this.contractAddress = contractAddress;
    }

    @Override
    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(long gasLimit) {
        this.gasLimit = gasLimit;
    }

    @Override
    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public byte getArgsCount() {
        return argsCount;
    }

    public void setArgsCount(byte argsCount) {
        this.argsCount = argsCount;
    }

    public String[][] getArgs() {
        return args;
    }

    public void setArgs(String[][] args) {
        this.args = args;
    }

    @Override
    public Set<byte[]> getAddresses() {
        Set<byte[]> addressSet = new HashSet<>();
        addressSet.add(contractAddress);
        return addressSet;
    }
}
