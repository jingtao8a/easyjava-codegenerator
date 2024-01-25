package org.jingtao8a.easyjavacodegenerator;

import org.jingtao8a.easyjavacodegenerator.bean.Constants;
import org.jingtao8a.easyjavacodegenerator.bean.TableInfo;
import org.jingtao8a.easyjavacodegenerator.builder.BuildBase;
import org.jingtao8a.easyjavacodegenerator.builder.BuildPo;
import org.jingtao8a.easyjavacodegenerator.builder.BuildQuery;
import org.jingtao8a.easyjavacodegenerator.builder.BuildTable;
import org.jingtao8a.easyjavacodegenerator.utils.DateUtils;
import org.jingtao8a.easyjavacodegenerator.utils.JsonUtils;

import java.util.Date;
import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tableInfoList = BuildTable.getTable();
        BuildBase.execute();
        for (TableInfo tableInfo : tableInfoList) {
//            System.out.println(JsonUtils.convertObj2Json(tableInfo));
            BuildPo.execute(tableInfo);
            BuildQuery.execute(tableInfo);
        }
//        System.out.println(Constants.PATH_PO);
//        System.out.println(Constants.PATH_BASE);
//        System.out.println(DateUtils.format(new Date(), DateUtils.YYYYMMDD));

    }
}
