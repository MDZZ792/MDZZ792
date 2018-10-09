package com.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    //读取propertes配置文件
    public static Properties loadProperties(String propertyFile) {
        Properties properties = new Properties();
        try {
            InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertyFile);
            if(is == null){
                is = PropertiesUtil.class.getClassLoader().getResourceAsStream("properties/" + propertyFile);
            }
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
