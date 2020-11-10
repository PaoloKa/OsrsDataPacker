package datapacker;

import java.text.DecimalFormat;

public class PackerUtils {

    public static final DecimalFormat format = new DecimalFormat("#.##");

    public static String stripDat(String name) {
        return name.substring(0, name.length() - ".dat".length());
    }

    private static int stripId(String name) {
        return Integer.parseInt(name.substring(0, name.length() - ".dat".length()));
    }
}
