package fr.javatic.ezvizEnableRtsp.hikvisionSdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.NativeLong;
import fr.javatic.ezvizEnableRtsp.Main;
import fr.javatic.ezvizEnableRtsp.hikvisionSdk.bindings.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Hikvision implements AutoCloseable {
    private static final HCNetSDKNative hCNetSDK = HCNetSDKNative.INSTANCE;

    private final ObjectMapper objectMapper;
    private NativeLong currentUser = null;

    public Hikvision() {
        System.out.println("[Hikvision] Initialize SDK");
        objectMapper = new ObjectMapper();

        hCNetSDK.NET_DVR_Init();
        //hCNetSDK.NET_DVR_SetLogToFile(true, null, false);
        if (!hCNetSDK.NET_DVR_SetConnectTime(5000, 4)) {
            System.err.println("Call to NET_DVR_SetConnectTime failed");
        }
        hCNetSDK.NET_DVR_SetReconnect(10000, true);
    }

    public void login(String cameraHost, short cameraPort, String username, String password) {
        System.out.println("[Hikvision] Perform Login");
        if(currentUser!=null){
            throw new IllegalStateException("User is already logged in");
        }

        var loginInfo = new NET_DVR_USER_LOGIN_INFO();
        var deviceInfo = new NET_DVR_DEVICEINFO_V40();

        copyStringTo(cameraHost, loginInfo.sDeviceAddress);
        copyStringTo(username, loginInfo.sUserName);
        copyStringTo(password, loginInfo.sPassword);
        loginInfo.wPort = cameraPort;
        loginInfo.write();

        NativeLong userId = hCNetSDK.NET_DVR_Login_V40(loginInfo.getPointer(), deviceInfo.getPointer());
        if (userId.longValue() == -1) {
            throw new HikvisionCallFailure("Login failed : " + hCNetSDK.NET_DVR_GetLastError());
        }

        this.currentUser = userId;
    }

    public NetDvrXmlConfigOutput setServiceSwitch(ServiceSwitchConfig config){
        System.out.println("[Hikvision] Set service switch "+config);
        String body;
        try {
            body = objectMapper.writeValueAsString(new ServiceSwitchCommand(config));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var content = String.format("PUT /ISAPI/EZVIZ/IPC/System/servicesSwitch?format=json\r\n%s\r\n", body);
        var contentSp = new StringPointer(content);

        var xmlConfigInput = new NET_DVR_XML_CONFIG_INPUT();
        xmlConfigInput.dwSize = xmlConfigInput.size();
        xmlConfigInput.lpRequestUrl = contentSp.pointer;
        xmlConfigInput.dwRequestUrlLen = contentSp.size;
        xmlConfigInput.lpInBuffer = null;
        xmlConfigInput.dwInBufferSize = 0;
        xmlConfigInput.write();

        var xmlConfigOutput = new NET_DVR_XML_CONFIG_OUTPUT();
        xmlConfigOutput.dwSize = xmlConfigOutput.size();
        xmlConfigOutput.lpOutBuffer = new BYTE_ARRAY(10485760).getPointer();
        xmlConfigOutput.dwOutBufferSize = 10485760;
        xmlConfigOutput.lpStatusBuffer = new BYTE_ARRAY(16384).getPointer();
        xmlConfigOutput.dwStatusSize = 16384;
        xmlConfigOutput.write();

        if(!hCNetSDK.NET_DVR_STDXMLConfig(this.currentUser, xmlConfigInput, xmlConfigOutput)){
            throw new HikvisionCallFailure("Set Service Switch failed : " + hCNetSDK.NET_DVR_GetLastError());
        }

        NetDvrXmlConfigOutput output;
        try {
            output = objectMapper.readValue(
                    xmlConfigOutput.lpOutBuffer.getByteArray(0L, xmlConfigOutput.dwReturnedXMLSize),
                    NetDvrXmlConfigOutput.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(output.statusCode()!=1){
            throw new HikvisionCallFailure("Set Service Switch failed : " + output);
        }

        return output;
    }

    public void logout(){
        System.out.println("[Hikvision] Logout");
//        if(!hCNetSDK.NET_DVR_Logout(this.currentUser)){
//            throw new HikvisionCallFailure("Logout failed : " + hCNetSDK.NET_DVR_GetLastError());
//        }
        currentUser = null;
    }

    @Override
    public void close() throws Exception {
        System.out.println("[Hikvision] Closing SDK");
        if(currentUser!=null){
            logout();
        }
//        hCNetSDK.NET_DVR_Cleanup();
    }

    private static void copyStringTo(String data, byte[] target) {
        var dataBytes = data.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(dataBytes, 0, target, 0, dataBytes.length);
    }
}
