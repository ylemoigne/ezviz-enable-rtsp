package fr.javatic.ezvizEnableRtsp.hikvisionSdk;

import com.sun.jna.Pointer;
import fr.javatic.ezvizEnableRtsp.hikvisionSdk.bindings.BYTE_ARRAY;

import java.nio.charset.StandardCharsets;

class StringPointer {
    public final Pointer pointer;
    public final int size;

    public StringPointer(String data) {
        var dataBytes = data.getBytes(StandardCharsets.UTF_8);

        var nativeByteArray = new BYTE_ARRAY(dataBytes.length);
        System.arraycopy(dataBytes, 0, nativeByteArray.byValue, 0, dataBytes.length);
        nativeByteArray.write();
        this.pointer = nativeByteArray.getPointer();
        this.size = dataBytes.length;
    }
}
