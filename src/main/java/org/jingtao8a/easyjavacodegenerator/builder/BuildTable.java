package org.jingtao8a.easyjavacodegenerator.builder;

import com.mysql.cj.xdevapi.Table;
import org.apache.commons.lang3.ArrayUtils;
import org.jingtao8a.easyjavacodegenerator.bean.Constants;
import org.jingtao8a.easyjavacodegenerator.bean.FieldInfo;
import org.jingtao8a.easyjavacodegenerator.bean.TableInfo;
import org.jingtao8a.easyjavacodegenerator.utils.JsonUtils;
import org.jingtao8a.easyjavacodegenerator.utils.PropertiesUtils;
import org.jingtao8a.easyjavacodegenerator.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuildTable {
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static Connection conn = null;
    private static String SQL_SHOW_TABLE_STATUS = "show table status";
    private static String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";
    private static String SQL_SHOW_TABLE_INDEX = "show index from %s";

    static {
        String driverName = PropertiesUtils.getString("db.driver.name");
        String url = PropertiesUtils.getString("db.url");
        String user = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.error("数据库连接失败", e);
        }
    }

    public static void getTable()  {
        PreparedStatement ps = null;
        ResultSet tableResult = null;

        List<TableInfo> tableInfoList = new ArrayList<>();
        try {
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = ps.executeQuery();
            while (tableResult.next()) {
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");
//                logger.info("tableName:{}, comment:{}", tableName, comment);

                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PREFIX) {
                    beanName = tableName.substring(beanName.indexOf("_") + 1);
                }
                beanName = processFiled(beanName, true);

                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_PARAM);

                List<FieldInfo> fieldInfoList = readFieldInfo(tableInfo);

                tableInfo.setFieldInfoList(fieldInfoList);
                getKeyIndexInfo(tableInfo);

                tableInfoList.add(tableInfo);
                logger.info("表{} ", JsonUtils.convertObj2Json(tableInfo));
            }
        } catch (Exception e) {
            logger.error("读取表失败");
        } finally {
            if (tableResult != null) {
                try {
                    tableResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static List<FieldInfo> readFieldInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet fieldResult = null;

        List<FieldInfo> fieldInfoList = new ArrayList<>();
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while (fieldResult.next()) {
                String field = fieldResult.getString("field");
                String type = fieldResult.getString("type");
                String extra = fieldResult.getString("extra");
                String comment = fieldResult.getString("comment");
                String propertyName = processFiled(field, false);
                if (type.indexOf("(") > 0) {
                    type = type.substring(0, type.indexOf("("));
                }

                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setComment(comment);
                fieldInfo.setFieldName(field);
                fieldInfo.setSqlType(type);
                fieldInfo.setIsAutoIncrement("auto_increment".equals(extra) ? true : false);
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setJavaType(processJavaType(type));
                fieldInfoList.add(fieldInfo);

                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
                    tableInfo.setHaveDateTime(true);
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)) {
                    tableInfo.setHaveDate(true);
                }
                if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
                    tableInfo.setHaveBigDecimal(true);
                }
//                logger.info("field: {}, type: {}, extra: {},comment: {}", field, processJavaType(type), extra, comment);
            }
        } catch (Exception e) {
            logger.error("readFieldInfo 失败");
        } finally {
            if (fieldResult != null) {
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return fieldInfoList;
    }

    private static void getKeyIndexInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet fieldResult = null;
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX, tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while (fieldResult.next()) {
                String keyName = fieldResult.getString("key_name");
                Integer nonUnique = fieldResult.getInt("non_unique");
                String columnName = fieldResult.getString("column_name");
                if (nonUnique == 1) {
                    continue;
                }
                List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
                if (null == keyFieldList) {
                    keyFieldList = new ArrayList<FieldInfo>();
                    tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
                }
                for (FieldInfo fieldInfo :tableInfo.getFieldInfoList()) {
                    if (fieldInfo.getFieldName().equals((columnName))) {
                        keyFieldList.add(fieldInfo);
                    }
                }

            }
        } catch (Exception e) {
            logger.error("getKeyIndexInfo 失败");
        } finally {
            if (fieldResult != null) {
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static String processFiled(String field, Boolean upperCaseFirstLetter) {
        StringBuffer stringBuffer = new StringBuffer();
        String[] fields = field.split("_");
        stringBuffer.append(upperCaseFirstLetter ? StringUtils.uperCaseFirstLetter(fields[0]) : fields[0]);
        for (int i = 1; i < fields.length; ++i) {
            stringBuffer.append(StringUtils.uperCaseFirstLetter(fields[i]));
        }
        return stringBuffer.toString();
    }

    private static String processJavaType(String type) {
        if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPE, type)) {
            return "Integer";
        }
        if (ArrayUtils.contains(Constants.SQL_LONG_TYPE, type)) {
            return "Long";
        }
        if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, type)) {
            return "String";
        }
        if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type)) {
            return "Date";
        }
        if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)) {
            return "BigDecimal";
        }
        throw new RuntimeException("无法识别的类型:" + type);
    }
}
