//package com.leesky.ezframework.tools.utils;
//
//
//import com.google.common.collect.Lists;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.time.DateFormatUtils;
//
//import java.io.*;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
//public class BuildJavaCode {
//    //输出文件路径
//    private static final String output_path = "/home/leesky/code/";
//    //根据实际修改： xxxModel.java 文件位置
//    private static final String input_path = "/home/leesky/model";
//    //根据实际修改： 涉及的 model 所在包名称前缀
//    private static final String modelBasePack = "com.leesky.ezframework.backend.model";
//
//    private static final Iterator<File> list = FileUtils.iterateFiles(new File(input_path), new String[]{"java"}, true);
//
//    public static void main(String[] args) throws IOException {
//
//        while (list.hasNext()) {
//            File file = list.next();
//            if (StringUtils.endsWith(file.getName(), "Model.java")) {
//                List<String> mapperList = Lists.newArrayList();
//                List<String> serviceList = Lists.newArrayList();
//                List<String> serviceImplList = Lists.newArrayList();
//
//                String absPath = getModelPath(file);
//                String modelName = file.getName().replace(".java", "");
//                String mapperName = modelName.replace("Model", "") + "Mapper";
//                String serviceName = modelName.replace("Model", "") + "Service";
//                String serviceImplName = modelName.replace("Model", "") + "ServiceImpl";
//
//
//                mapperName = "I" + StringUtils.uncapitalize(mapperName);
//                serviceName = "I" + StringUtils.uncapitalize(serviceName);
//
//                //构建mapper文件
//                mapperList.addAll(zhushi());
//                mapperList.add("package " + absPath.replace("model", "mapper") + ";");
//                mapperList.add("");
//                mapperList.add("import com.leesky.ezframework.join.mapper.LeeskyMapper;");
//                mapperList.add("import  " + absPath + "." + modelName + ";");
//                mapperList.add("");
//                mapperList.add("public interface " + mapperName + " extends LeeskyMapper<" + modelName + "> {");
//                mapperList.add("}");
//                FileUtils.writeLines(new File(getPath(absPath, "/mapper") + File.separatorChar + mapperName + ".java"), "UTF-8", mapperList, false);
//
//                //构建servcie层文件
//                serviceList.addAll(zhushi());
//                serviceList.add("package " + absPath.replace("model", "service") + ";");
//                serviceList.add("");
//                serviceList.add("import com.leesky.ezframework.service.IbaseService;");
//                serviceList.add("import  " + absPath + "." + modelName + ";");
//                serviceList.add("");
//                serviceList.add("public interface " + serviceName + " extends IbaseService<" + modelName + "> {");
//                serviceList.add("}");
//                FileUtils.writeLines(new File(getPath(absPath, "/service") + File.separatorChar + serviceName + ".java"), "UTF-8", serviceList, false);
//
//                //构建serviceImpl层文件
//                serviceImplList.addAll(zhushi());
//                serviceImplList.add("package " + absPath.replace("model", "service") + ".impl;");
//                serviceImplList.add("");
//                serviceImplList.add("import org.springframework.stereotype.Service;");
//                serviceImplList.add("import com.leesky.ezframework.service.impl.BaseServiceImpl;");
//                serviceImplList.add("import " + absPath + "." + modelName + ";");
//                serviceImplList.add("import " + absPath.replace("model", "service") + "." + serviceName + ";");
//                serviceImplList.add("import " + absPath.replace("model", "mapper") + "." + mapperName + ";");
//                serviceImplList.add("");
//                serviceImplList.add("@Service");
//                serviceImplList.add("public class " + serviceImplName + " extends BaseServiceImpl<" + mapperName + "," + modelName + ">implements " + serviceName + " {");
//                serviceImplList.add("}");
//                FileUtils.writeLines(new File(getPath(absPath, "/service") + "/impl" + File.separatorChar + serviceImplName + ".java"), "UTF-8", serviceImplList, false);
//
//            }
//        }
//    }
//
//
//    private static String getModelPath(File f) {
//        String str = null;
//        try (
//                FileInputStream inputStream = new FileInputStream(f);
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
//        ) {
//            while ((str = bufferedReader.readLine()) != null) {
//                if (StringUtils.contains(str, "package "))
//                    return str.replace("package", "").replace(";", "").trim();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return str;
//    }
//
//    private static String getPath(String modelPackName, String codeType) {
//        return output_path + File.separatorChar + codeType + modelPackName.replace(modelBasePack, "").replace(".", File.separator);
//    }
//
//    private static List<String> zhushi() {
//        List<String> list = Lists.newArrayList();
//        list.add("/*");
//        list.add(" * @作者: 魏来");
//        list.add(" * @日期: " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//        list.add(" * @组织: 森麒麟轮胎股份有限公司");
//        list.add(" * @部门: 国内市场替换部IT组");
//        list.add(" * @Desc: ");
//        list.add(" */");
//        return list;
//    }
//}
