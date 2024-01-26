package org.jingtao8a.easyjavacodegenerator.builder;


import org.apache.commons.lang3.ArrayUtils;
import org.jingtao8a.easyjavacodegenerator.bean.Constants;
import org.jingtao8a.easyjavacodegenerator.bean.FieldInfo;
import org.jingtao8a.easyjavacodegenerator.bean.TableInfo;
import org.jingtao8a.easyjavacodegenerator.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BuildQuery {
    private static final Logger logger = LoggerFactory.getLogger(BuildQuery.class);
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_QUERY);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanParamName();
        File poFile = new File(folder, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(poFile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);
            bw.write("package " + Constants.PACKAGE_QUERY + ";");
            bw.newLine();
            bw.write("import lombok.Data;");
            bw.newLine();
            bw.write("import lombok.ToString;");
            if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
                bw.newLine();
                bw.write("import java.util.Date;");
            }
            if (tableInfo.getHaveBigDecimal()) {
                bw.newLine();
                bw.write("import java.math.BigDecimal;");
            }


            bw.newLine();
            BuildComment.createClassComment(bw, tableInfo.getComment());
            bw.write("@Data");
            bw.newLine();
            bw.write("@ToString");
            bw.newLine();
            bw.write("public class " + className + " extends BaseQuery {");
            bw.newLine();

            List<FieldInfo> extendList = new ArrayList<>();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY + ";");
                    bw.newLine();
                    bw.newLine();
                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setJavaType(fieldInfo.getJavaType());
                    fuzzyField.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY);
                    fuzzyField.setFieldName(fieldInfo.getFieldName());
                    fuzzyField.setSqlType(fieldInfo.getSqlType());
                    extendList.add(fuzzyField);
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START + ";");
                    bw.newLine();
                    bw.write("\tprivate String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END + ";");
                    bw.newLine();
                    bw.newLine();

                    FieldInfo timeStartField = new FieldInfo();
                    timeStartField.setJavaType("String");
                    timeStartField.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START);
                    timeStartField.setFieldName(fieldInfo.getFieldName());
                    timeStartField.setSqlType(fieldInfo.getSqlType());
                    extendList.add(timeStartField);

                    FieldInfo timeEndField = new FieldInfo();
                    timeEndField.setJavaType("String");
                    timeEndField.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END);
                    timeEndField.setFieldName(fieldInfo.getFieldName());
                    timeEndField.setSqlType(fieldInfo.getSqlType());
                    extendList.add(timeEndField);
                }
            }
            tableInfo.setExtendFieldInfoList(extendList);
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建po失败");
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outw != null) {
                try {
                    outw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
