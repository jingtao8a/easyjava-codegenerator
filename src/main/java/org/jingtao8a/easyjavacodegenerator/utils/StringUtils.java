package org.jingtao8a.easyjavacodegenerator.utils;

public class StringUtils {
    public static String uperCaseFirstLetter(String field) {
        if (field.isEmpty()) {
            return field;
        }
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    public static String lowCaseFirstLetter(String field) {
        if (field.isEmpty()) {
            return field;
        }
        return field.substring(0, 1).toLowerCase() + field.substring(1);
    }
    public static void main(String[] args) {
        System.out.println(lowCaseFirstLetter("JFLJFL"));
    }
}
