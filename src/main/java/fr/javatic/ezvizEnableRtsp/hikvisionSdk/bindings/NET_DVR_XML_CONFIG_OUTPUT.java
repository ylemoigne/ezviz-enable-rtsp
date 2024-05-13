package fr.javatic.ezvizEnableRtsp.hikvisionSdk.bindings;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.List;

public class NET_DVR_XML_CONFIG_OUTPUT extends Structure {
    public int dwSize;
    public Pointer lpOutBuffer;
    public int dwOutBufferSize;
    public int dwReturnedXMLSize;
    public Pointer lpStatusBuffer;
    public int dwStatusSize;
    public Pointer lpDataBuffer;
    public byte byNumOfMultiPart;
    public byte[] byRes = new byte[23];

    @Override
    public List<String> getFieldOrder() {
        return List.of(
                "dwSize",
                "lpOutBuffer",
                "dwOutBufferSize",
                "dwReturnedXMLSize",
                "lpStatusBuffer",
                "dwStatusSize",
                "lpDataBuffer",
                "byNumOfMultiPart",
                "byRes");
    }
}
