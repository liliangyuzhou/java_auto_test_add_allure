package com.lemon.api.auto10.variableGenerate;

import com.lemon.api.auto10.Util.JDBCUtil;

import java.util.HashMap;
import java.util.Map;

public class MobileGenerator {

    /**生成用来注册的手机号码，使用min或者max都可以，不过max为+1，min为-1
     * @return
     */
    public String generateToRegisterMobilePhone(){
//        String sql="select concat(max(mobilephone)+1,'') as toRegisterMobilePhone from member";
        String sql="select cast((max(mobilephone)+10) as char) as toRegisterMobilePhone from member";
//        String sql="select cast((min(mobilephone)-9) as char) as toRegisterMobilePhone from member";

        Map<String,Object> columnLabelsAndValues=JDBCUtil.query(sql);

        //因为调用get方法后返回的是object类型，所以调用toString()方法转换为String类型
        return columnLabelsAndValues.get("toRegisterMobilePhone").toString();

    }

    /**生成系统数据库中没有的手机号码
     * @return
     */
    public String generateSystemNotExistMobilePhone(){
//        concat(max(mobilephone)+1,''),拼接字符串，转换为字符串格式，不然出来是科学计数法
//        String sql="select max(mobilephone)+2 as systemNotExistMobilePhone from member";
//但是使用了concat(max(mobilephone)+1,'')，视频没有报错，自己一直返回一个对象
//        String sql="select concat(max(mobilephone)+2,'') as systemNotExistMobilePhone from member";
        //所以sql使用cast转换为char（字符转）类型
        String sql="select cast((max(mobilephone)+11) as char) as systemNotExistMobilePhone from member";
//        String sql="select cast((min(mobilephone)-10) as char) as systemNotExistMobilePhone from member";

        Map<String,Object> columnLabelsAndValues=JDBCUtil.query(sql);

        //因为调用get方法后返回的是object类型，所以调用toString()方法转换为String类型
        return columnLabelsAndValues.get("systemNotExistMobilePhone").toString();

    }

    public static void main(String[] args){

        MobileGenerator mo=new MobileGenerator();
        String mobilephone=mo.generateSystemNotExistMobilePhone();
        System.out.println(mobilephone);


    }
}
