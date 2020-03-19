package com.lemon.api.auto10.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties properties =new Properties();
    static {
        InputStream inputStream = null;
        try {
            inputStream=new FileInputStream(new File("src/main/resources/config.properties"));
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static  String getExcelPath(){

        return properties.getProperty("excel.path");
    }
}
