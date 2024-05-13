package fr.javatic.ezvizEnableRtsp.hikvisionSdk.bindings;

import com.sun.jna.*;

public interface HCNetSDKNative extends Library {
    HCNetSDKNative INSTANCE = Native.load("hcnetsdk", HCNetSDKNative.class);

    boolean NET_DVR_Init();
    boolean NET_DVR_Cleanup();

    boolean NET_DVR_SetConnectTime(int dwWaitTime, int dwTryTimes);
    boolean NET_DVR_SetReconnect(int dwInterval, boolean bEnableRecon);

    NativeLong NET_DVR_Login_V40(Pointer pLoginInfo, Pointer lpDeviceInfo);
    boolean NET_DVR_Logout(NativeLong lUserID);

    boolean NET_DVR_STDXMLConfig(NativeLong lUserID, NET_DVR_XML_CONFIG_INPUT net_dvr_xml_config_input, NET_DVR_XML_CONFIG_OUTPUT net_dvr_xml_config_output);

    int NET_DVR_GetLastError();
}
