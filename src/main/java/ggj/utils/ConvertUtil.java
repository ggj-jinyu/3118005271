package ggj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.lang.System.*;

public class ConvertUtil {
    //ConvertUtil Should not be instantiated，将ConverUtil实例化方法private
    private ConvertUtil(){
    }

    /**
     * 将文件路径对应的 txt文本转化为内存中的字符串
     * @param filepath 为文件路径
     * @return 将txt文件转化成的字符串
     */
    public static String txtToString(String filepath){
        String str = "";
        File file = new File(filepath);
        if(! file.exists()){
            out.println(filepath + "  文件不存在 !");
            return "";
        }
        try {
            FileInputStream in = new FileInputStream(file);
            int size = in.available();
            byte[] buffer=new byte[size];
            int bufferCount = in.read(buffer);
            if(bufferCount == 0){
                out.println(filepath+" 为空文本");
            }
            in.close();
            //UTF-8 显示
            str = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 输出文件
     * @param outputFilePath 输出文件的路径
     * @param result 字符串形式传入结果
     */
    public static void convertString2File(String outputFilePath,String result){
        //创建输出路径的文件
        File outputFile = new File(outputFilePath);
        //写入
        try (FileWriter fr = new FileWriter(outputFile)) {
            char[] cs = result.toCharArray();
            fr.write(cs);
            fr.flush();
            out.println("The result has been written into " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
