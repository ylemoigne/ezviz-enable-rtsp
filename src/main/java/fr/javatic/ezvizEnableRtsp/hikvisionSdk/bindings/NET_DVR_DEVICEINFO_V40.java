package fr.javatic.ezvizEnableRtsp.hikvisionSdk.bindings;

import com.sun.jna.Structure;

import java.util.List;

public class NET_DVR_DEVICEINFO_V40 extends Structure {
    public NET_DVR_DEVICEINFO_V30 struDeviceV30 = new NET_DVR_DEVICEINFO_V30();
    public byte bySupportLock;
    public byte byRetryLoginTime;
    public byte byPasswordLevel;
    public byte byRes1;
    public int dwSurplusLockTime;
    public byte[] byRes2 = new byte[256];

    @Override
    public List<String> getFieldOrder() {
        return List.of(
                "struDeviceV30",
                "bySupportLock",
                "byRetryLoginTime",
                "byPasswordLevel",
                "byRes1",
                "dwSurplusLockTime",
                "byRes2"
        );
    }
}
