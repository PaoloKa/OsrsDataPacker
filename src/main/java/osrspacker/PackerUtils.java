package osrspacker;

public class PackerUtils {

    public static String stripDat(String name) {
        return name.substring(0, name.length() - ".dat".length());
    }

    private static int stripId(String name) {
        return Integer.parseInt(name.substring(0, name.length() - ".dat".length()));
    }
}
