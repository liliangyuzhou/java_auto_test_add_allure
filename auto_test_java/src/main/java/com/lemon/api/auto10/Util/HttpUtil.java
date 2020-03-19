package com.lemon.api.auto10.Util;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.*;

public class HttpUtil {

    public static Map<String,String> cookies= new HashMap<String, String>();

    //以post方式调用接口的工具方法
    public static String doPost(String url, Map<String,String> params) {


        //2.接口的请求方式
        HttpPost post=new HttpPost(url);


        //3.准备测试数据


        List<BasicNameValuePair> param =new ArrayList<BasicNameValuePair>();
        Set<String> keys=params.keySet();
        for (String paramkey:keys) {
            String paramvalue=params.get(paramkey);
            param.add(new BasicNameValuePair(paramkey,paramvalue));

        }
        String result="";

        try {
            post.setEntity(new UrlEncodedFormEntity(param, "utf-8"));
            //4.准备请求头数据（如有必要，比如cookie，content-type等）

            addCookieInRequestHeaderBeforeRequest(post);

            //5.发起请求，获取接口响应信息（状态码，响应报文，或者某些特殊的响应头数据）
            HttpClient client = HttpClients.createDefault();
            HttpResponse response = client.execute(post);

            //从响应头中获取cookie
            getAndStoreCookiesFromResponseHeader(response);


            //获取状态码
            int code = response.getStatusLine().getStatusCode();
            System.out.println(code);
            //获取响应结果
            result = EntityUtils.toString(response.getEntity());
        }catch(Exception e){

            e.printStackTrace();

        }
        return result;
    }

    private static void addCookieInRequestHeaderBeforeRequest(HttpRequest request) {
        //这里的Cookie是此项目固定的字段名，从浏览器中看出
        String jessionIdcookie=cookies.get("JESSIONID");
        if(jessionIdcookie != null){
           request.addHeader("Cookie",jessionIdcookie);

        }


    }

    private static void getAndStoreCookiesFromResponseHeader(HttpResponse response) {
        //从响应头中获取key为"Set-cookie"的响应头
        Header setCookieHeader=response.getFirstHeader("Set-cookie");

        //如果响应头不为空
        if( setCookieHeader != null){
            //取出该响应头的值
            String cookiePairsString=setCookieHeader.getValue();
            //把取到的值如果不为空，使用；进行分割
            if(cookiePairsString !=null && cookiePairsString.trim().length()>0){
                String[] cookiepairs=cookiePairsString.split(";");
                if(cookiepairs !=null) {
                    for (String cookiepair:cookiepairs) {
                        if(cookiepair.contains("JSESSIONID")){
                            //保存到map
                            cookies.put("JESSIONID",cookiepair);

                        }

                    }

                }



            }
        }
    }


    //以get方式调用接口的工具方法
    public static String doGet(String url, Map<String,String> params){
        Set<String> keys=params.keySet();
        int mark=1;
        for (String paramkey:keys) {

            String paramvalue = params.get(paramkey);
            if (mark==1){
                url+=("?"+paramkey+"="+paramvalue);
            }else {
                url+=("&"+paramkey+"="+paramvalue);

            }
            mark++;
        }
        System.out.println(url);

        //2.获取接口的请求方式
        HttpGet get=new HttpGet(url);
        String result="";
        try {

            addCookieInRequestHeaderBeforeRequest(get);

            CloseableHttpClient client = HttpClients.createDefault();
            HttpResponse httpResponse = client.execute(get);

            getAndStoreCookiesFromResponseHeader(httpResponse);

            //4.准备接口的请求头信息，如果有必要的话


            //5.获取接口的状态码，响应结果等。

            System.out.println(httpResponse.getStatusLine().getStatusCode());
            result = EntityUtils.toString(httpResponse.getEntity());
        }catch(Exception e){
            e.printStackTrace();
        }


        return result;

    }


    /** 根据判断type来确认接口请求的方式
     * @param type 接口请求的方式
     * @param url 接口的请求地址
     * @param params 接口请求的参数
     * @return
     */
    public static String doService(String type,String url,Map<String,String> params){
        String result=null;
        if (type.equalsIgnoreCase("post")){

            result= HttpUtil.doPost(url,params);
        }else if(type.equalsIgnoreCase("get")){

            result= HttpUtil.doGet(url,params);
        }
        return result;
    }

}
