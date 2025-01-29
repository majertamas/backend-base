package hu.mikrum.backendbase.teszt.util;

public class Util {

    private Util() {
        throw new IllegalStateException("Utility class");
    }

    public static final String LANGUAGE_ACCESS_PATH = "$.Language.";
    public static final String PREFIX_FOR_MISSING_LANG_VALUE = "$.*.";

}
