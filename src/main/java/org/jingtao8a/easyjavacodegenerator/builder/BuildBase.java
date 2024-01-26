package org.jingtao8a.easyjavacodegenerator.builder;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.jingtao8a.easyjavacodegenerator.bean.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildBase {
    private static Logger logger = LoggerFactory.getLogger(BuildBase.class);
    public static void execute() {
        List<String> headerInfoList = new ArrayList<>();
        headerInfoList.add("package " + Constants.PACKAGE_UTILS + ";");
        build(headerInfoList, "DateUtils", Constants.PATH_UTILS);
        build(headerInfoList, "JsonUtils", Constants.PATH_UTILS);
        //生成BaseMapper
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_MAPPER + ";");
        build(headerInfoList, "BaseMapper", Constants.PATH_MAPPER);
        //生成PageSize枚举
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_ENUM + ";");
        build(headerInfoList, "PageSize", Constants.PATH_ENUM);

        //生成SimplePage
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_QUERY + ";");
        headerInfoList.add("import " + Constants.PACKAGE_ENUM + "." + "PageSize;");
        build(headerInfoList, "SimplePage", Constants.PATH_QUERY);

        //生成BaseQuery
        headerInfoList.clear();
        headerInfoList.add("package " + Constants.PACKAGE_QUERY + ";");
        build(headerInfoList, "BaseQuery", Constants.PATH_QUERY);
    }

    private static void build(List<String> headerInfoList, String fileName, String outPutPath) {
        File folder = new File(outPutPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File javaFile = new File(outPutPath, fileName + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        InputStream in = null;
        InputStreamReader inr = null;
        BufferedReader br = null;
        try {
            out = new FileOutputStream(javaFile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);
            String templatePath = BuildBase.class.getClassLoader().getResource("template/" + fileName +".txt").getPath();
            in = new FileInputStream(templatePath);
            inr = new InputStreamReader(in, "utf-8");
            br = new BufferedReader(inr);

            for (String head : headerInfoList) {
                bw.write(head);
                bw.newLine();
            }
            String line = null;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {
            logger.error("生成基础类失败", fileName, e);
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
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inr != null) {
                try {
                    inr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
