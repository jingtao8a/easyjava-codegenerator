package org.jingtao8a.easyjavacodegenerator.bean;

import lombok.Data;

@Data
public class FieldInfo {
//    字段名称
    private String FieldName;
//    bean属性名称
    private String propertyName;
//    sql属性类型
    private String sqlType;
//    字段类型
    private String javaType;
//    字段注释
    private String comment;
//    字段是否是自增长
    private Boolean isAutoIncrement = false;
}

