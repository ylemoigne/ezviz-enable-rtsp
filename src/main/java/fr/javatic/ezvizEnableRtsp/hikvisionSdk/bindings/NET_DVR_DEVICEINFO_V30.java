package fr.javatic.ezvizEnableRtsp.hikvisionSdk.bindings;

import com.sun.jna.Structure;

import java.util.List;

public class NET_DVR_DEVICEINFO_V30 extends Structure {
    public byte[] sSerialNumber = new byte[48];
    public byte byAlarmInPortNum;
    public byte byAlarmOutPortNum;
    public byte byDiskNum;
    public byte byDVRType;
    public byte byChanNum;
    public byte byStartChan;
    public byte byAudioChanNum;
    public byte byIPChanNum;
    public byte[] byRes1 = new byte[24];

    @Override
    public List<String> getFieldOrder() {
        return List.of("sSerialNumber",
                "byAlarmInPortNum",
                "byAlarmOutPortNum",
                "byDiskNum",
                "byDVRType",
                "byChanNum",
                "byStartChan",
                "byAudioChanNum",
                "byIPChanNum",
                "byRes1"
        );
    }
}
