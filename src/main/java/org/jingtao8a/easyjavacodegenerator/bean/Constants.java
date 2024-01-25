package org.jingtao8a.easyjavacodegenerator.bean;

import org.jingtao8a.easyjavacodegenerator.utils.PropertiesUtils;

public class Constants {
    public static Boolean IGNORE_TABLE_PREFIX;
    public static String SUFFIX_BEAN_PARAM;
    public static String PATH_JAVA = "java";
    public static String PATH_RESOURCES = "resources";
    public static String PATH_BASE;
    public static String PATH_PO;
    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PACKAGE_PARAM;

    static {
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));
        SUFFIX_BEAN_PARAM = PropertiesUtils.getString("suffix.bean.param");

        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        PACKAGE_PO = PropertiesUtils.getString("package.po");

        PATH_BASE = PropertiesUtils.getString("path.base");
        PATH_BASE = PATH_BASE + PATH_JAVA  + "/" + PACKAGE_BASE.replace(".", "/");

        PATH_PO = PATH_BASE + "/" + PACKAGE_PO.replace(".", "/");
    }

    public static final String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
    public static final String[] SQL_DATE_TYPES = new String[]{"date"};
    public static final String[] SQL_DECIMAL_TYPE = new String[]{"decimal", "double", "float"};
    public static final String[] SQL_STRING_TYPE = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
    //Integer
    public static final String[] SQL_INTEGER_TYPE = new String[]{"int", "tinyint"};
    //Long
    public static final String[] SQL_LONG_TYPE = new String[]{"bigint"};
}
