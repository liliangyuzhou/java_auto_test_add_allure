package com.lemon.api.auto10.TestCases;

import com.lemon.api.auto10.Util.CaseUtil;
import org.testng.annotations.DataProvider;

//相比于RegisterTestCase_v7，增加断言后，去掉断言，备课讲的是actual结果回写excel，人眼比对
public class RegisterTestCase_v9 extends BaseProcessor {



    //testng里面的数据提供者,一般不写name属性的话，在测试用例中使用数据提供者是，name默认为数据提供者的方法名
    @DataProvider(name="datasource")
    public Object[][] datas(){

        //        去掉此定义，因为多个测试类都需要修改，抽到父类中去，子类继承就好
        //这里的cellName必须与excel的字段名保持一致，否则会出现属性的set，get方法出错
        //String [] cellNames ={"CaseId","ApiId","Params","ExpectedResponseData","PrevalidateSql","AftervalidateSql"};

        Object[][] datas= CaseUtil.getCaseDatasByApiId("1",cellNames);
        return datas;

    }

    public static void main(String[] args){

//json解析
//        String paramter="{\"status\":\"0\",\"code\":\"20103\",\"data\":null,\"msg\":\"密码不能为空\"}";
//        Map<String,String> params= (Map<String, String>) JSONObject.parse(paramter);
//        Set<String> keys=params.keySet();
//        for (String key:keys) {
//            String value=params.get(key);
//
//            System.out.println("key="+key+",value="+value);
//
//        }



    }

}
