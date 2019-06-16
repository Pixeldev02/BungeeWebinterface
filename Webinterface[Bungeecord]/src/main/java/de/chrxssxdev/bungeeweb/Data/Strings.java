package de.chrxssxdev.bungeeweb.Data;

public class Strings {

    private static String prefix = "§7Webinterface §8× §7";

    private static String noPerm = prefix + "§cDazu hast du keine Rechte!";

    public static String getPrefix() {
        return prefix;
    }

    public static String getNoPerm() {
        return noPerm;
    }
}
