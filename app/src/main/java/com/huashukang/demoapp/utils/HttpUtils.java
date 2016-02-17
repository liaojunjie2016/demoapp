package com.huashukang.demoapp.utils;

import android.util.Log;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * Created by SUCHU on 2016/2/16.
 */
public class HttpUtils {
    private static final String URL="http://192.168.2.22:8080/MyFileServer/ImgUpload";
    public static void upload(File file){
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        try {
            URL url = new URL(URL);
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Charset","UTF-8");
                connection.setRequestProperty("connection","keep-alive");
                connection.setRequestProperty("Content-Type",CONTENT_TYPE+";boundary="+BOUNDARY);
                if(null!=file){
                    Log.i("HttpUtils",file.getAbsolutePath());
                    DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(PREFIX);
                    stringBuffer.append(BOUNDARY);
                    stringBuffer.append(LINE_END);
                    stringBuffer.append("Content-Disposition:form-data; name=\"uploadfile\";filename=\""+file.getName()+"\""+LINE_END);
                    stringBuffer.append("Content-Type:application/octet-stream; charset=UTF-8"+LINE_END);
                    stringBuffer.append(LINE_END);
                    output.write(stringBuffer.toString().getBytes());
                    InputStream inputStream = new FileInputStream(file);
                    byte [ ] bytes = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(bytes))!=-1){
                        output.write(bytes,0,len);
                    }
                    inputStream.close();
                    output.write(LINE_END.getBytes());
                    byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                    output.write(end_data);
                    output.flush();
                    int res = connection.getResponseCode();
                    Log.i("HttpUtils","response code:"+res);
                    if(res ==200){
                        Log.i("SUCCESS","OK");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}