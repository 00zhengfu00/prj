package com.physicmaster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/20 16:44
 * 功能说明 :  快速生成适配工具类
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class DimenTool {

    public static void gen() {
        //以此文件夹下的dimens.xml文件内容为初始值参照
        File file = new File("./app/src/main/res/values/dimens.xml");

        BufferedReader reader = null;
        StringBuilder sw180 = new StringBuilder();
        StringBuilder sw270 = new StringBuilder();
        StringBuilder sw360 = new StringBuilder();
        StringBuilder sw450 = new StringBuilder();
        StringBuilder sw540 = new StringBuilder();
        StringBuilder sw630 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();

        try {

            System.out.println("生成不同分辨率：");

            reader = new BufferedReader(new FileReader(file));

            String tempString;

            int line = 1;

            // 一次读入一行，直到读入null为文件结束

            while ((tempString = reader.readLine()) != null) {


                if (tempString.contains("</dimen>")) {

                    //tempString = tempString.replaceAll(" ", "");

                    String start = tempString.substring(0, tempString.indexOf(">") + 1);

                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    //截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字
                    Double num = Double.parseDouble
                            (tempString.substring(tempString.indexOf(">") + 1,
                                    tempString.indexOf("</dimen>") - 2));

                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                    sw180.append(start).append(num * 0.5).append(end).append("\r\n");

                    sw270.append(start).append(num * 0.75).append(end).append("\r\n");

                    sw360.append(start).append(num * 1.0).append(end).append("\r\n");

                    sw450.append(start).append(num * 1.25).append(end).append("\r\n");

                    sw540.append(start).append(num * 1.5).append(end).append("\r\n");

                    sw630.append(start).append(num * 1.75).append(end).append("\r\n");

                    sw720.append(start).append(num * 2.0).append(end).append("\r\n");


                } else {
                    sw180.append(tempString).append("");

                    sw270.append(tempString).append("");

                    sw360.append(tempString).append("");

                    sw450.append(tempString).append("");

                    sw540.append(tempString).append("");

                    sw630.append(tempString).append("");
                    sw720.append(tempString).append("");

                }

                line++;

            }

            reader.close();
            System.out.println("<!--  sw180 -->");

            System.out.println(sw180);

            System.out.println("<!--  sw270 -->");

            System.out.println(sw270);

            System.out.println("<!--  sw360 -->");

            System.out.println(sw360);

            System.out.println("<!--  sw450 -->");

            System.out.println(sw450);

            System.out.println("<!--  sw540 -->");

            System.out.println(sw540);

            System.out.println("<!--  sw630 -->");

            System.out.println(sw630);

            System.out.println("<!--  sw720 -->");

            System.out.println(sw720);

            String sw180file = "./app/src/main/res/values-sw180dp/dimens.xml";

            String sw270file = "./app/src/main/res/values-sw270dp/dimens.xml";

            String sw360file = "./app/src/main/res/values-sw360dp/dimens.xml";

            String sw450file = "./app/src/main/res/values-sw450dp/dimens.xml";

            String sw540file = "./app/src/main/res/values-sw540dp/dimens.xml";

            String sw630file = "./app/src/main/res/values-sw630dp/dimens.xml";

            String sw720file = "./app/src/main/res/values-sw720dp/dimens.xml";
            //将新的内容，写入到指定的文件中去
            writeFile(sw180file, sw180.toString());

            writeFile(sw270file, sw270.toString());

            writeFile(sw360file, sw360.toString());

            writeFile(sw450file, sw450.toString());

            writeFile(sw540file, sw540.toString());

            writeFile(sw630file, sw630.toString());
            writeFile(sw720file, sw720.toString());

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            if (reader != null) {

                try {

                    reader.close();

                } catch (IOException e1) {

                    e1.printStackTrace();

                }

            }

        }

    }


    /**
     * 写入方法
     */

    public static void writeFile(String file, String text) {

        PrintWriter out = null;

        try {

            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            out.println(text);

        } catch (IOException e) {

            e.printStackTrace();

        }


        out.close();

    }

    public static void main(String[] args) {

        gen();
    }

}