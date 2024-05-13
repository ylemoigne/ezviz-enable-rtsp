package fr.javatic.ezvizEnableRtsp.hikvisionSdk.bindings;

import com.sun.jna.Structure;

import java.util.List;

public class BYTE_ARRAY extends Structure {
    public byte[] byValue;

    public BYTE_ARRAY(int i) {
        this.byValue = new byte[i];
    }

    @Override
    public List<String> getFieldOrder() {
        return List.of("byValue");
    }
}
