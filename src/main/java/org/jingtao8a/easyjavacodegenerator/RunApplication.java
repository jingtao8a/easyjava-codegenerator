package org.jingtao8a.easyjavacodegenerator;

import org.jingtao8a.easyjavacodegenerator.bean.Constants;
import org.jingtao8a.easyjavacodegenerator.bean.TableInfo;
import org.jingtao8a.easyjavacodegenerator.builder.BuildPo;
import org.jingtao8a.easyjavacodegenerator.builder.BuildTable;
import org.jingtao8a.easyjavacodegenerator.utils.JsonUtils;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tableInfoList = BuildTable.getTable();
        for (TableInfo tableInfo : tableInfoList) {
//            System.out.println(JsonUtils.convertObj2Json(tableInfo));
            BuildPo.execute(tableInfo);
        }
//        System.out.println(Constants.PATH_PO);
//        System.out.println(Constants.PATH_BASE);
    }
}
