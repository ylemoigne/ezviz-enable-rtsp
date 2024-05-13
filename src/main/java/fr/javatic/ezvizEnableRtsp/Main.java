package fr.javatic.ezvizEnableRtsp;

import fr.javatic.ezvizEnableRtsp.hikvisionSdk.Hikvision;
import fr.javatic.ezvizEnableRtsp.hikvisionSdk.ServiceSwitchConfig;

public class Main {
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
            hikvision.login(config.host(), config.port(), config.username(), config.password());
            System.out.println(hikvision.setServiceSwitch(new ServiceSwitchConfig(1, 1, 1,1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printHelp() {
        System.out.println("Usage: docker run --rm ezviz-enable-rtsp --host=hostname [--port=8000] --username=admin --password=foobar");
    }
}
