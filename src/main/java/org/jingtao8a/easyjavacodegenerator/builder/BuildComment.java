package org.jingtao8a.easyjavacodegenerator.builder;

import org.jingtao8a.easyjavacodegenerator.utils.DateUtils;

import java.io.BufferedWriter;
import java.util.Date;

public class BuildComment {
    public static void createClassComment(BufferedWriter bw, String classComment) throws Exception {
        bw.write("/**");
        bw.newLine();
        bw.write("@Description:" + classComment);
        bw.newLine();
        bw.write("@Date:" + DateUtils.format(new Date(), DateUtils.YYYY_MM_DD));
        bw.newLine();
        bw.write("*/");
        bw.newLine();
    }

    public static void createFieldComment(BufferedWriter bw, String fieldComment) throws Exception {
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + fieldComment);
        bw.newLine();
        bw.write("\t*/");
        bw.newLine();
    }
}
