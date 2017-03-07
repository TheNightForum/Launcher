package com.TNF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Launch {

    public static void main(){

        File game = new File(Data.locationBin + File.separator);
        final List<String> arguments = new ArrayList<>();
        arguments.add(getJavaProgramFile());
        arguments.add("-jar");
        arguments.add("Launcher.jar");
        try {
            final ProcessBuilder pb = new ProcessBuilder();
            pb.command(arguments);
            pb.directory(game);
            pb.start();
            System.exit(0);
        } catch (IOException | RuntimeException e) {
            System.exit(1);
        }

    }

    private static String getJavaProgramFile() {
        return System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
    }
}