package utils;

public class StringUtils {

    public static String emptyToNull(String s) {
        return s != null && !s.isEmpty() ? s : null;
    }

}
