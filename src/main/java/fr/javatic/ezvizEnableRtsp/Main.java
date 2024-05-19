package fr.javatic.ezvizEnableRtsp;

import fr.javatic.ezvizEnableRtsp.hikvisionSdk.Hikvision;
import fr.javatic.ezvizEnableRtsp.hikvisionSdk.HikvisionCallFailure;
import fr.javatic.ezvizEnableRtsp.hikvisionSdk.ServiceSwitchConfig;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        Config config;
        try {
            config = Config.parse(args);
        } catch (InvalidCommandLineException e) {
            System.err.println("Error :" + e.getMessage());
            System.err.println();
            printHelp();
            return;
        }

        try (var hikvision = new Hikvision()) {
            if(config.intervalInSeconds()==null){
                enableRtsp(hikvision, config);
                executorService.close();
            }else{
                System.out.println("Launch scheduled check (" + config.intervalInSeconds() + " seconds)");
                executorService.scheduleWithFixedDelay(
                        ()-> enableRtspIfPortIsUnavailable(config, hikvision),
                        0,
                        config.intervalInSeconds(),
                        TimeUnit.SECONDS
                );
                Thread.currentThread().join();
            }
        } catch (Exception e) {
            System.out.println("Unexpected failure, exit");
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static void enableRtspIfPortIsUnavailable(Config config, Hikvision hikvision) {
        if (rtspPortIsAvailable(config.host())) {
            //System.out.println("RTSP port is available");
        }else{
            enableRtsp(hikvision, config);
        }
    }

    private static void enableRtsp(Hikvision hikvision, Config config) {
        try {
            System.out.println("Enable RTSP");
            hikvision.login(config.host(), config.port(), config.username(), config.password());
            System.out.println(hikvision.setServiceSwitch(new ServiceSwitchConfig(1, 1, 1, 1)));
        } catch (HikvisionCallFailure e) {
            System.err.println("Failure : " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private static boolean rtspPortIsAvailable(String hostname) {
        try (var socket = new Socket()) {
            socket.setReuseAddress(true);
            SocketAddress sa = new InetSocketAddress(hostname, 554);
            socket.connect(sa, 1000);
            return socket.isConnected();
        } catch (IOException e) {
            return false;
        }
    }

    private static void printHelp() {
        System.out.println("Usage: docker run --rm ezviz-enable-rtsp --host=hostname [--port=8000] --username=admin --password=foobar [--interval=sec]");
        System.out.println("If `--interval` is defined, then the program won't exit ; at defined interval it will check if port 554 is available and if not, will try to enable rtsp");
    }
}
