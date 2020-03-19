package com.lemon.api.auto10.Util;

import com.lemon.api.auto10.pojo.ParamVariable;
import com.lemon.api.auto10.pojo.WriteBackData;
import jdk.nashorn.internal.runtime.logging.Logger;

import java.lang.reflect.Method;
import java.util.*;

/**
 *参数化工具类
 */

public class VariableUtil {
    public static void main(String[] args){

        Set<String> keys=VariableNamesAndValuesMap.keySet();
        for (String key: keys ) {
            String s=VariableNamesAndValuesMap.get(key);
            System.out.println(s);


        }


    }

    public static Map<String,String> VariableNamesAndValuesMap=new HashMap<String, String>();

    public static List<ParamVariable> paramVariableList=new ArrayList<ParamVariable>();

    static{
        //1.加载表单里面的每一行数据，封装成一个ParemVariable对象，然后统一放到集合中
//        List<ParamVariable> list=ExcelUtil.load("src/main/resources/testcase_V8.xlsx","paramVariable",ParamVariable.class);
        List<ParamVariable> list= ExcelUtil.load(PropertiesUtil.getExcelPath(),"paramVariable", ParamVariable.class);

        paramVariableList.addAll(list);
        //将行对象里面的key和value放到map中
        loadVariablesToMap();
        //获取该表单的行👌和对应得索引，列名和对应列索引，因为测试套件在批量回写的时候，需要用到
        //cellNameCellNumMapping和rowidentifyRowNumMapping这两个集合，所以需要调用这个方法要先加载到内存中
        ExcelUtil.loadRownumAndCellName(PropertiesUtil.getExcelPath(),"paramVariable");
    }
    /**替换参数里面的变量
     * @param parameter
     * @return
     */
    public static String replaceVariable(String parameter) {

        Set<String> paramVariableNames=VariableNamesAndValuesMap.keySet();
        for (String paramVariableName:paramVariableNames) {
            if(parameter.contains(paramVariableName)){
                parameter=parameter.replace(paramVariableName, VariableNamesAndValuesMap.get(paramVariableName));
                System.out.println(parameter);
            }

        }


        return parameter;
    }

    /**
     *  遍历对应集合，将key和对应的value保存到map中
     */
    private static void loadVariablesToMap() {
        for (ParamVariable paramVariable:paramVariableList) {
            //获取变量名
            String paramVariableName=paramVariable.getName();
            //获取变量值
            String paramVariableValue=paramVariable.getValue();

            //如果变量值为空
            if(paramVariableValue ==null || paramVariableValue.trim().length()==0){
                //从excel读取出的数据中paramVariable表单中获取反射的类路径
                String reflectClassPath=paramVariable.getReflectClass();
                //从excel读取出的数据中paramVariable表单中获取反射的类方法名
                String reflectMethodName=paramVariable.getReflectMethod();

                // 第二种获取字节码的方式，使用forName方法,jdk提供
                try {
                    Class clazz=Class.forName(reflectClassPath);
                    //创建一个该需要反射类的Object对象，调用是方法的时候使用
                    Object object=clazz.newInstance();
                    //获取方法对象,因为该方法没有设计需要传参，所以第二个参数不需要写
                    Method method=clazz.getMethod(reflectMethodName);

                    //通过方法对象，调用invoke方法，传入该类的对象，调用该方法。因为需要用到调用方法的返回值
                    // ，因为返回的的是object对象，转为String需要强制类型转换，并且不需要定义新变量了
                    //直接使用paramVariableValue接受就好，方便后面放入map
                    paramVariableValue= (String) method.invoke(object);


                    //保存数据到要回写的集合,这个表单的唯一标识不是用例id（case_id），是变量名Name
                    ExcelUtil.writeBackDatas.add(new WriteBackData("paramVariable",paramVariableName,"ReflectValue",paramVariableValue));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //放入到map中
            VariableNamesAndValuesMap.put(paramVariableName,paramVariableValue);

        }
    }
}
