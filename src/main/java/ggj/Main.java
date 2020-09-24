package ggj;

import ggj.utils.ConvertUtil;
import ggj.utils.CosineSimilarity;

import java.io.File;

import static java.lang.System.*;

/**
 * 参考原文：
 * https://github.com/DMingOu/Software-Engineering-Project
 *      /tree/master/3118005422/PaperCheckDemo
 */

public class Main {
    public static void main( String[] args ) {
        process(args[0],args[1],args[2]);
        exit(0);
    }

    public static void process(String orgTextPath,String newTextPath,String ansFilePath){
        File originFile = new File(orgTextPath);
        File newFile = new File(newTextPath);
        if(! originFile.exists() || ! newFile.exists()){
            out.println("The file path is invalid, please check the file path parameter !\n");
            return;
        }
        String orgString = ConvertUtil.txtToString(orgTextPath);
        String newString = ConvertUtil.txtToString(newTextPath);
        //执行计算
        double ans = CosineSimilarity.getSimilarity(orgString , newString);
        //将结果字符串输出到 指定的文件
        String ansString = String.valueOf(ans);
        ConvertUtil.convertString2File(ansFilePath,orgTextPath+"\n"+newTextPath +"\n"
                +"Similar Score ：" + ansString + "%");
        //控制台输出
        out.println("Similar Score  : "+ ansString + "%\n");
    }



}