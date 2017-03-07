package com.TNF;

import java.io.File;
/**
 * Created by Brayden on 8/3/2017.
 */
public class Data {

    public static String OPERATING_SYSTEM = System.getProperty("os.name").toLowerCase();
    public static String BaseUrl = "https://TheNightForum.github.io/";
    public static String location = "";
    public static String locationRes = "";
    public static String locationConfigs = "";
    public static String locationGames = "";
    public static String locationTemp = "";
    public static String locationBin = "";
    public static void main() {
            if (OPERATING_SYSTEM.indexOf("win") >= 0) {
                location = System.getenv("APPDATA") + "\\TNFLauncher\\";
                locationRes = location + "res\\";
                locationConfigs = location + "configs\\";
                locationGames = location + "games\\";
                locationTemp = location + "temp\\";
                locationBin = location + "bin\\";
            } else if (OPERATING_SYSTEM.indexOf("mac") >= 0) {
                location = System.getProperty("user.home") + "/TNFLauncher/";
                locationRes = location + "res/";
                locationConfigs = location + "configs/";
                locationGames = location + "games/";
                locationTemp = location + "temp/";
                locationBin = location + "bin/";
            } else if (OPERATING_SYSTEM.indexOf("nix") >= 0 || OPERATING_SYSTEM.indexOf("nux") >= 0 || OPERATING_SYSTEM.indexOf("aix") > 0) {
                location = System.getProperty("user.home") + "/TNFLauncher/";
                locationRes = location + "res/";
                locationConfigs = location + "configs/";
                locationGames = location + "games/";
                locationTemp = location + "temp/";
                locationBin = location + "bin/";
            } else if (OPERATING_SYSTEM.indexOf("sunos") >= 0) {
                location = System.getProperty("user.home") + "/TNFLauncher/";
                locationRes = location + "res/";
                locationConfigs = location + "configs/";
                locationGames = location + "games/";
                locationTemp = location + "temp/";
                locationBin = location + "bin/";
            } else {
                location = System.getProperty("user.home") + "/TNFLauncher/";
                locationRes = location + "res/";
                locationConfigs = location + "configs/";
                locationGames = location + "games/";
                locationTemp = location + "temp/";
                locationBin = location + "bin/";
            }
            File file = new File(location);
            file.mkdirs();
            File bin = new File(locationBin);
            bin.mkdirs();
            File temp = new File(locationTemp);
            temp.mkdirs();
            File res = new File(locationRes);
            res.mkdirs();
            File configs = new File(locationConfigs);
            configs.mkdirs();
            File games = new File(locationGames);
            games.mkdirs();

    }

}