package com.bishe.visualizationsystem.admin.test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bishe.visualizationsystem.admin.bean.Wave;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Joseph
 * @date 2022/1/3
 * @apiNote
 */



public class TxtToJson {

    public void txtToJson(String dataPath) throws IOException {
        InputStreamReader ins = new InputStreamReader(new FileInputStream(dataPath));
        BufferedReader br = new BufferedReader(ins);
        //存放bean对象
        List<Wave> tlist = new ArrayList<>();

        //读取txt
        String line = null;
        List<String> list = new ArrayList<String>();
        while((line = br.readLine()) != null) {
            list.add(line);
        }
        br.close();
        int i = 0;
        //txt的每一行相当于一条数据，split按空格作分隔符进行拆分。\\s+是正则表达式。
        for (String str : list) {
            if (str.length() < 3){
                Wave wave = new Wave();
                wave.setTimes(i++);
                wave.setValue(Double.parseDouble(str));
                tlist.add(wave);
            }
            else if(str.charAt(str.length()-3) != '-'){
                Wave wave = new Wave();
                wave.setTimes(i++);
                wave.setValue(Double.parseDouble(str));
                tlist.add(wave);
            }
        }
        //JSON.toJSONString()方法：将对象数组（JSON格式的字符串也可以）转换成JSON数据。
        // 输出结果[{"times":0,"value":4.03822E-4},{"times":1,"value":0.00201702}]
//        String json = JSON.toJSONString(tlist);
        // 输出结果[[0,4.03822E-4],[1,0.00201702]]
        String json = JSON.toJSONString(tlist,SerializerFeature.BeanToArray);

//        System.out.println(json);

        //创建新文件
        String jsonPath = dataPath.substring(0,dataPath.length()-3) + "json";
        File txtToJson = new File(jsonPath);
        txtToJson.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(txtToJson));
        out.write(json);
        out.flush(); // 把缓存区内容压入文件
        out.close(); // 最后记得关闭文件
    }

//    public static void main(String[] args) throws IOException {
//        InputStreamReader ins = new InputStreamReader(new FileInputStream("D:\\test.txt"));
//        BufferedReader br = new BufferedReader(ins);
//        //存放bean对象
//        List<Wave> tlist = new ArrayList<>();
//
//        //读取txt
//        String line = null;
//        List<String> list = new ArrayList<String>();
//        while((line = br.readLine()) != null) {
//            list.add(line);
//        }
//        br.close();
//        int i = 0;
//        //txt的每一行相当于一条数据，split按空格作分隔符进行拆分。\\s+是正则表达式。
//        for (String str : list) {
//            if (str.length() < 3){
//                Wave wave = new Wave();
//                wave.setTimes(i++);
//                wave.setValue(Double.parseDouble(str));
//                tlist.add(wave);
//            }
//            else if(str.charAt(str.length()-3) != '-'){
//                Wave wave = new Wave();
//                wave.setTimes(i++);
//                wave.setValue(Double.parseDouble(str));
//                tlist.add(wave);
//            }
//        }
//        //JSON.toJSONString()方法：将对象数组（JSON格式的字符串也可以）转换成JSON数据。
//        // 输出结果[{"times":0,"value":4.03822E-4},{"times":1,"value":0.00201702}]
////        String json = JSON.toJSONString(tlist);
//        // 输出结果[[0,4.03822E-4],[1,0.00201702]]
//        String json = JSON.toJSONString(tlist,SerializerFeature.BeanToArray);
//
////        System.out.println(json);
//
//        //创建新文件
//        File txtToJson = new File("D:\\json2.json");
//        txtToJson.createNewFile();
//        BufferedWriter out = new BufferedWriter(new FileWriter(txtToJson));
//        out.write(json);
//        out.flush(); // 把缓存区内容压入文件
//        out.close(); // 最后记得关闭文件
//    }
}
