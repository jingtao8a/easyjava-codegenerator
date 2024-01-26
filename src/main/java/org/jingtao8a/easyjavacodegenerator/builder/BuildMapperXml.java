package org.jingtao8a.easyjavacodegenerator.builder;

import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import org.apache.commons.lang3.ArrayUtils;
import org.jingtao8a.easyjavacodegenerator.bean.Constants;
import org.jingtao8a.easyjavacodegenerator.bean.FieldInfo;
import org.jingtao8a.easyjavacodegenerator.bean.TableInfo;
import org.jingtao8a.easyjavacodegenerator.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class BuildMapperXml {
    private static Logger logger = LoggerFactory.getLogger(BuildMapperXml.class);
    private static String BASE_COLUMN_LIST = "base_column_list";
    private static String BASE_QUERY_CONDITION = "base_query_condition";
    private static String BASE_QUERY_CONDITION_EXTEND = "base_query_condition_extend";
    private static String QUERY_CONDITION = "query_condition";
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPER_XML);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPER;
        File poFile = new File(folder, className + ".xml");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(poFile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);

            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            bw.newLine();
            bw.write("\t\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bw.newLine();
            bw.write(String.format("<mapper namespace=\"%s\">", Constants.PACKAGE_MAPPER + "." + className));
            bw.newLine();
            bw.newLine();
            bw.write("\t<!-- 实体映射 -->");
            bw.newLine();
            String poClass = Constants.PACKAGE_PO + "." + tableInfo.getBeanName();
            bw.write("\t<resultMap id=\"base_result_map\" type=\""+ poClass + "\">");
            bw.newLine();
            FieldInfo idField = null;
            List<FieldInfo> fieldInfoList = tableInfo.getKeyIndexMap().get("PRIMARY");
            if (fieldInfoList.size() == 1) {
                idField = fieldInfoList.get(0);
            }
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                bw.write("\t<!-- " + fieldInfo.getComment() + "-->");
                bw.newLine();
                String key = null;
                if (idField != null && idField.getPropertyName().equals(fieldInfo.getPropertyName())) {
                    key = "id";
                } else {
                    key = "result";
                }
                bw.write("\t\t<" + key + " column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                bw.newLine();
            }
            bw.write("\t</resultMap>");
            bw.newLine();
            //通用查询列
            bw.write("\t<!-- 通用查询结果列 -->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_COLUMN_LIST + "\">");
            bw.newLine();
            StringBuffer stringBuffer = new StringBuffer();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                stringBuffer.append(fieldInfo.getFieldName()).append(" ,");
            }
            String baseColumnStr = stringBuffer.substring(0, stringBuffer.lastIndexOf(","));
            bw.write("\t\t" + baseColumnStr);
            bw.newLine();
            bw.write("\t</sql>");
            //基础查询条件
            bw.newLine();
            bw.write("\t<!-- 基础查询条件-->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION + "\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                String stringQuery = new String();
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    stringQuery = " and query." + fieldInfo.getPropertyName() + " !=''";
                }
                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null" + stringQuery +"\">");
                bw.newLine();
                bw.write("\t\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName() +"}");
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();
            }
            bw.write("\t</sql>");
            bw.newLine();
            bw.newLine();
            bw.write("\t<!-- 扩展的查询条件 -->");
            bw.newLine();
            bw.write("\t<sql id=\"" + BASE_QUERY_CONDITION_EXTEND + "\">");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getExtendFieldInfoList()) {
                String andWhere = "";
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    andWhere = " and " + fieldInfo.getFieldName() + " like concat('%', #{query." + fieldInfo.getPropertyName() + "}, '%')";
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    String op = "";
                    if (fieldInfo.getPropertyName().endsWith(Constants.SUFFIX_BEAN_QUERY_TIME_START)) {
                        andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " >= str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d')]]>";
                    } else {
                        andWhere = "<![CDATA[ and " + fieldInfo.getFieldName() + " < date_sub(str_to_date(#{query." + fieldInfo.getPropertyName() + "}, '%Y-%m-%d'), interval - 1 day)]]>";
                    }
                }
                bw.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() +" != ''\">");
                bw.newLine();
                bw.write("\t\t\t" + andWhere);
                bw.newLine();
                bw.write("\t\t</if>");
                bw.newLine();
            }
            bw.write("\t</sql>");
            bw.newLine();
            //通用查询条件
            bw.newLine();
            bw.write("\t<!-- 通用查询条件 -->");
            bw.newLine();
            bw.write("\t<sql id=\"" + QUERY_CONDITION + "\">");
            bw.newLine();
            bw.write("\t\t<where>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION + "\"/>");
            bw.newLine();
            bw.write("\t\t\t<include refid=\"" + BASE_QUERY_CONDITION_EXTEND + "\"/>");
            bw.newLine();
            bw.write("\t\t</where>");
            bw.newLine();
            bw.write("\t</sql>");
            bw.newLine();

            //查询列表
            bw.newLine();
            bw.write("\t<!-- 查询列表 -->");
            bw.newLine();
            bw.write("\t<select id=\"selectList\" resultMap=\"base_result_map\">");
            bw.newLine();
            bw.write("\t\tSELECT <include refid=\"base_column_list\"/> FROM " + tableInfo.getTableName() + " <include refid=\"query_condition\"/>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.orderBy!=null\">");
            bw.newLine();
            bw.write("\t\t\torder by ${query.orderBy}");
            bw.newLine();
            bw.write("\t\t</if>");
            bw.newLine();
            bw.write("\t\t<if test=\"query.simplePage!=null\">");
            bw.newLine();
            bw.write("\t\t\tlimit #{query.simplePage.start}, #{query.simplePage.end}");
            bw.newLine();
            bw.write("\t\t</if>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();

            //查询数量
            bw.newLine();
            bw.write("\t<!-- 查询数量 -->");
            bw.newLine();
            bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Long\">");
            bw.newLine();
            bw.write("\t\tSELECT count(1) FROM " + tableInfo.getTableName() + " <include refid=\"query_condition\"/>");
            bw.newLine();
            bw.write("\t</select>");
            bw.newLine();

            //单条插入
            bw.newLine();
            bw.write("\t<!-- 单条插入 -->");
            bw.newLine();
            bw.write("\t<insert id=\"insert\" parameterType=\"" + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + "\">");
            bw.newLine();
            FieldInfo autoIncrementField = null;
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                if (fieldInfo.getIsAutoIncrement()) {
                    autoIncrementField = fieldInfo;
                    break;
                }
            }
            if (autoIncrementField != null) {
                bw.write("\t\t<selectKey keyProperty=\"bean." + autoIncrementField.getPropertyName() +
                        "\" resultType=\"" + autoIncrementField.getJavaType() + "\" order=\"AFTER\"");
                bw.newLine();
                bw.write("\t\t\tSELECT LAST_INSERT_ID()");
                bw.newLine();
                bw.write("\t\t</selectKey>");
            }
            bw.newLine();
            bw.write("\t\tINSERT INTO " + tableInfo.getTableName());
            bw.newLine();
            bw.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo: tableInfo.getFieldInfoList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t\t<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\",\">");
            bw.newLine();
            for (FieldInfo fieldInfo: tableInfo.getFieldInfoList()) {
                bw.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + " != null\">");
                bw.newLine();
                bw.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
                bw.newLine();
                bw.write("\t\t\t</if>");
                bw.newLine();
            }
            bw.write("\t\t</trim>");
            bw.newLine();
            bw.write("\t</insert>");
            bw.newLine();

            bw.write("</mapper>");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建MapperXML失败");
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
