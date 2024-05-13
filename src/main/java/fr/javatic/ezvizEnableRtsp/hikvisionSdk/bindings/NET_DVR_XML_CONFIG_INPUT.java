package fr.javatic.ezvizEnableRtsp.hikvisionSdk.bindings;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.List;

public class NET_DVR_XML_CONFIG_INPUT extends Structure {
    public int dwSize;
    public Pointer lpRequestUrl;
    public int dwRequestUrlLen;
    public Pointer lpInBuffer;
    public int dwInBufferSize;
    public int dwRecvTimeOut;
    public byte byForceEncrpt;
    public byte byNumOfMultiPart;
    public byte byMIMEType;
    public byte byRes1;
    public int dwSendTimeOut;
    public byte[] byRes = new byte[24];

    @Override
    public List<String> getFieldOrder() {
        return List.of(
                "dwSize",
                "lpRequestUrl",
                "dwRequestUrlLen",
                "lpInBuffer",
                "dwInBufferSize",
                "dwRecvTimeOut",
                "byForceEncrpt",
                "byNumOfMultiPart",
                "byMIMEType",
                "byRes1",
                "dwSendTimeOut",
                "byRes");
    }
}
