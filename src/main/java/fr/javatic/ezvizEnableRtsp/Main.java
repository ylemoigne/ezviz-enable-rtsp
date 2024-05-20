package fr.javatic.ezvizEnableRtsp;

import fr.javatic.ezvizEnableRtsp.hikvisionSdk.Hikvision;
import fr.javatic.ezvizEnableRtsp.hikvisionSdk.HikvisionCallFailure;
import fr.javatic.ezvizEnableRtsp.hikvisionSdk.ServiceSwitchConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class.getSimpleName());
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
            if (config.intervalInSeconds() == null) {
                enableRtsp(hikvision, config);
                executorService.close();
            } else {
                LOGGER.info("Launch scheduled check (" + config.intervalInSeconds() + " seconds)");
                executorService.scheduleWithFixedDelay(
                        () -> enableRtspIfPortIsUnavailable(config, hikvision),
                        0,
                        config.intervalInSeconds(),
                        TimeUnit.SECONDS
                );
                Thread.currentThread().join();
            }
        } catch (Exception e) {
            LOGGER.error("Unexpected failure, exit", e);
            System.exit(1);
        }
    }

    private static void enableRtspIfPortIsUnavailable(Config config, Hikvision hikvision) {
        if (rtspPortIsAvailable(config.host())) {
            LOGGER.debug("RTSP port is available");
        } else {
            enableRtsp(hikvision, config);
        }
    }

    private static void enableRtsp(Hikvision hikvision, Config config) {
        try {
            LOGGER.info("Enable RTSP");
            hikvision.login(config.host(), config.port(), config.username(), config.password());
            var response = hikvision.setServiceSwitch(new ServiceSwitchConfig(1, 1, 1, 1));
            LOGGER.info("Response: {}", response);
        } catch (HikvisionCallFailure e) {
            LOGGER.error("Enabling RTSP Failure", e);
        }finally {
            hikvision.logout();
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
