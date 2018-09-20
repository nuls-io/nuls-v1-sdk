package io.nuls.sdk.core.script;

import io.nuls.sdk.core.contast.SDKConstant;
import io.nuls.sdk.core.exception.NulsException;
import io.nuls.sdk.core.model.BaseNulsData;
import io.nuls.sdk.core.utils.NulsByteBuffer;
import io.nuls.sdk.core.utils.NulsOutputStreamBuffer;
import io.nuls.sdk.core.utils.SerializeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScriptSign extends BaseNulsData {

    private List<Script> scripts;

    @Override
    protected void serializeToStream(NulsOutputStreamBuffer stream) throws IOException {
        if (scripts != null && scripts.size() > 0) {
            for (Script script : scripts) {
                stream.writeBytesWithLength(script.getProgram());
            }
        } else {
            stream.write(SDKConstant.PLACE_HOLDER);
        }
    }

    @Override
    public void parse(NulsByteBuffer byteBuffer) throws NulsException {
        List<Script> scripts = new ArrayList<>();
        while (!byteBuffer.isFinished()) {
            scripts.add(new Script(byteBuffer.readByLengthByte()));
        }
        this.scripts = scripts;
    }

    public static ScriptSign createFromBytes(byte[] bytes) throws NulsException {
        ScriptSign sig = new ScriptSign();
        sig.parse(bytes, 0);
        return sig;
    }

    public List<Script> getScripts() {
        return scripts;
    }

    public void setScripts(List<Script> scripts) {
        this.scripts = scripts;
    }

    @Override
    public int size() {
        int size = 0;
        if (scripts != null && scripts.size() > 0) {
            for (Script script : scripts) {
                size += SerializeUtils.sizeOfBytes(script.getProgram());
            }
        } else {
            size = 4;
        }
        return size;
    }

}
