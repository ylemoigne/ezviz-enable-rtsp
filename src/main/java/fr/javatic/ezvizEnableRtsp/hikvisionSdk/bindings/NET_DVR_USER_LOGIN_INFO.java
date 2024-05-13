package fr.javatic.ezvizEnableRtsp.hikvisionSdk.bindings;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.List;

public class NET_DVR_USER_LOGIN_INFO extends Structure {
    public byte[] sDeviceAddress = new byte[129];
    public byte byUseTransport;
    public short wPort;
    public byte[] sUserName = new byte[64];
    public byte[] sPassword = new byte[64];
    public CallbackLoginResult cbLoginResult;
    Pointer pUser;
    public int bUseAsynLogin;
    public byte[] byRes2 = new byte[128];

    @Override
    public List<String> getFieldOrder() {
        return List.of(
                "sDeviceAddress",
                "byUseTransport",
                "wPort",
                "sUserName",
                "sPassword",
                "cbLoginResult",
                "bUseAsynLogin",
                "byRes2"
        );
    }
}
