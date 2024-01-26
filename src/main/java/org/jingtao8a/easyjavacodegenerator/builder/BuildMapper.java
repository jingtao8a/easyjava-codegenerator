package org.jingtao8a.easyjavacodegenerator.builder;

import org.jingtao8a.easyjavacodegenerator.bean.Constants;
import org.jingtao8a.easyjavacodegenerator.bean.FieldInfo;
import org.jingtao8a.easyjavacodegenerator.bean.TableInfo;
import org.jingtao8a.easyjavacodegenerator.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildMapper {
    private static Logger logger = LoggerFactory.getLogger(BuildMapper.class);
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_MAPPER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + Constants.SUFFIX_MAPPER;
        File poFile = new File(folder, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(poFile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);
            bw.write("package " + Constants.PACKAGE_MAPPER + ";");
            bw.newLine();
            bw.write("import org.apache.ibatis.annotations.Param;");
            bw.newLine();
            //构建类注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + "Mapper");
            bw.newLine();
            bw.write("public interface " + className + "<T,P> extends BaseMapper {");
            bw.newLine();
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldInfoList = entry.getValue();
                int index = 0;
                StringBuffer methodName = new StringBuffer();
                StringBuffer methodParams = new StringBuffer();

                for (FieldInfo fieldInfo : keyFieldInfoList) {
                    if (index != 0 ) {
                        methodName.append("And");
                        methodParams.append(", ");
                    }
                    methodName.append(StringUtils.uperCaseFirstLetter(fieldInfo.getPropertyName()));
                    methodParams.append("@Param(\"" + fieldInfo.getPropertyName() +  "\") " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName());
                    index++;
                }
                BuildComment.createFieldComment(bw, "根据" + methodName + "查询");
                bw.write("\t T selectBy" + methodName + "(" + methodParams + ");");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw, "根据" + methodName + "更新");
                bw.write("\t Integer updateBy" + methodName + "(@Param(\"bean\") T t, " + methodParams + ");");
                bw.newLine();
                bw.newLine();

                BuildComment.createFieldComment(bw, "根据" + methodName + "删除");
                bw.write("\t Integer deleteBy" + methodName + "(" + methodParams + ");");
                bw.newLine();
                bw.newLine();
            }
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("创建Mapper失败");
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
