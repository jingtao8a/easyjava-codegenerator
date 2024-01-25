package org.jingtao8a.easyjavacodegenerator.builder;


import org.jingtao8a.easyjavacodegenerator.bean.Constants;
import org.jingtao8a.easyjavacodegenerator.bean.FieldInfo;
import org.jingtao8a.easyjavacodegenerator.bean.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BuildPo {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File poFile = new File(folder, tableInfo.getBeanName() + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(poFile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);
            bw.write("package " + Constants.PACKAGE_BASE + "." + Constants.PACKAGE_PO + ";");
            bw.newLine();
            bw.write("import java.io.Serializable");
            if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
                bw.newLine();
                bw.write("import java.util.Date");
            }
            if (tableInfo.getHaveBigDecimal()) {
                bw.newLine();
                bw.write("import java.math.BigDecimal");
            }
            bw.newLine();
            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
            }
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
        }
    }
}
