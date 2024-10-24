package org.sture.java.budgeting.utils;

public class Utils {
    public static Double parseTextAsDoubleOrNull(String textToParse)
    {
        try {
            return Double.parseDouble(textToParse);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

