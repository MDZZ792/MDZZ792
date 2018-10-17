package com.test.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName JsonPrometheus
 * @Atuhor MDZZ_792 (｢･ω･)｢ 嘿
 * @Time 下午6:07 18-10-15
 */
//{"status":"success",
// "data":{"resultType":"matrix",
//         "result":[{"metric":{"Host_IP":"192.168.159.168","__name__":"Jvm_Thread_CpuUsage","instance":"localhost:9100","job":"test_save"},"values":[[1539568005,"58"],[1539568020,"64"]]}]}}
public class JsonPrometheus {
    public static List<String> MatrixJson(String string){
        JSONObject jsonObject = JSONObject.fromObject(string);
        String data = jsonObject.getString("data");
        JSONObject jsonObject1 = JSONObject.fromObject(data);
        JSONArray result = jsonObject1.getJSONArray("result");
        List<String> list = new ArrayList<String>();
        for(int i=0;i<result.size();i++){
            JSONObject jsonObject2 = JSONObject.fromObject(result.get(i));
            String metric = jsonObject2.getString("metric");
            String time = "";
            String StringValue = "";
            JSONArray values = jsonObject2.getJSONArray("values");
            for(int j=0;j<values.size();j++){
                JSONArray array = (JSONArray) values.get(j);
                String a = array.get(0).toString();
                time = time + a + ",";
                String value = array.get(1).toString();
                StringValue = StringValue + value + ",";
            }
            if(time.length()>0&&StringValue.length()>0){
                time = time.substring(0,time.length()-1);
                StringValue = StringValue.substring(0,StringValue.length()-1);
            }
            if(metric.length()>0){
                metric = metric.substring(1,metric.length()-1).replaceAll("\"","");
            }
            list.add(metric+"?"+StringValue+"?"+time);
        }
        List<String> strings = TimeCompared(list);
        return strings;
    }
    public static String VectorJson(){

        return "";
    }

    public static List<String> TimeCompared(List<String> list){
        List<String> NewList = new ArrayList<String>();
        String time="";
        Boolean status = false;
        f1:
            for(int i=0;i<list.size();i++){
                String[] split = list.get(i).split("[?]");
                if(null == time || time.equals("")){
                    time = split[2];
                }
                if(time.equals(split[2])){
                    status = true;
                }else{
                    status = false;
                    break f1;
                }
            }
        if(status){
            for(int i=0;i<list.size();i++){
                String[] split = list.get(i).split("[?]");
                String temp = split[0]+"?"+split[1];
                NewList.add(temp);
            }
            NewList.add(time);
        }else{
           return list;
        }
        return NewList;
    }
}
