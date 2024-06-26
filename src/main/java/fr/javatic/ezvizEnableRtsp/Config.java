package fr.javatic.ezvizEnableRtsp;

import java.util.Arrays;

record Config(String host, short port, String username, String password, Integer intervalInSeconds) {
    public static Config parse(String[] args){
        var host = Arrays.stream(args).filter(it -> it.startsWith("--host=")).findFirst()
                .map(it->it.substring(it.indexOf('=')+1))
                .orElseThrow(()->new InvalidCommandLineException("Missing required argument : --host"));

        var portAsString = Arrays.stream(args).filter(it -> it.startsWith("--port=")).findFirst()
                .map(it->it.substring(it.indexOf('=')+1))
                .orElse("8000");

        short port;
        try{
            port = Short.parseShort(portAsString);
        }catch (NumberFormatException e){
            throw new InvalidCommandLineException(String.format("Failed to convert port value '%s' to number", portAsString));
        }

        var username = Arrays.stream(args).filter(it -> it.startsWith("--username=")).findFirst()
                .map(it->it.substring(it.indexOf('=')+1))
                .orElseThrow(()->new InvalidCommandLineException("Missing required argument : --username"));

        var password = Arrays.stream(args).filter(it -> it.startsWith("--password=")).findFirst()
                .map(it->it.substring(it.indexOf('=')+1))
                .orElseThrow(()->new InvalidCommandLineException("Missing required argument : --password"));

        var intervalAsString = Arrays.stream(args).filter(it -> it.startsWith("--interval=")).findFirst()
                .map(it->it.substring(it.indexOf('=')+1))
                .orElse(null);

        Integer intervalInSeconds = null;
        if(intervalAsString != null) {
            try {
                intervalInSeconds = Integer.parseInt(intervalAsString);
            } catch (NumberFormatException e) {
                throw new InvalidCommandLineException(String.format("Failed to convert interval value '%s' to number", intervalAsString));
            }
        }

        return new Config(host, port, username, password, intervalInSeconds);
    }
}
