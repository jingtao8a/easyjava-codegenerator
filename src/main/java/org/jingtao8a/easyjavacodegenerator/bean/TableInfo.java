package org.jingtao8a.easyjavacodegenerator.bean;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class TableInfo {
//    表名
    private String tableName;
//    bean名称
    private String beanName;
//    参数名称
    private String beanParamName;
//    表注释
    private String comment;
//    字段信息
    private List<FieldInfo> fieldInfoList;
//    唯一索引集合
    private Map<String , List<FieldInfo>> keyIndexMap = new LinkedHashMap<>();
//    是否有date类型
    private Boolean haveDate = false;
//    是否有时间类型
    private Boolean haveDateTime = false;
//    是否有bigdecimal类型
    private Boolean haveBigDecimal = false;
}
