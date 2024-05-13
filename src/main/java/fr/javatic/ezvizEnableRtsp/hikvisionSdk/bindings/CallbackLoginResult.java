package fr.javatic.ezvizEnableRtsp.hikvisionSdk.bindings;

import com.sun.jna.Callback;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public interface CallbackLoginResult extends Callback {
    public int invoke(NativeLong lUserID, int dwResult, Pointer lpDeviceinfo, Pointer pUser);
}
