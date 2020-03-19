package com.lemon.api.auto10.Util;

import com.lemon.api.auto10.pojo.Case;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Case用例工具类
 */
public class CaseUtil {

    //保存所有的用例对象(作为共享的数据，取一次就可以，放到内存里面，所以使用static修饰)，excel中每一行代表一条测试用例
    public static List<Case> cases=new ArrayList<Case>();

    static {
        //将excel所有的数据解析到cases对象中
//        List<Case> list=ExcelUtil.load("src/main/resources/testcase_V8.xlsx","register", Case.class);
        List<Case> list= ExcelUtil.load(PropertiesUtil.getExcelPath(),"register", Case.class);

        cases.addAll(list);
    }

    /**根据接口的编号拿对应接口的测试用例
     * @param apiId excel文档中指定接口的编号
     * @param cellNames 要获取excel用例数据中对应的列名
     * @return
     */
    public static Object [][] getCaseDatasByApiId(String apiId,String [] cellNames){

        Class<Case> clazz= Case.class;

        //csList用来存放筛选需要的测试用例的对象集合
        ArrayList<Case> csList=new ArrayList<Case>();

        //通过循环找到指定编号对应的测试用例数据

        for (Case cs:cases) {
            if(cs.getApiId().equals(apiId)){
                csList.add(cs);

            }

        }
        Object [][] datas=new Object[csList.size()][cellNames.length];


//这里要往二位数组中放数据，不写增强for循环，就写一般的for循环就好
        for (int i = 0; i <csList.size() ; i++) {
            Case cs=csList.get(i);
            for (int j = 0; j <cellNames.length ; j++) {
//                String cellName=cellNames[j];
//                String value="";
//                //强耦合，万一Case设计变化，或者excel新增列，都要修改此处代码，所以不建议这种写法，使用反射不会出现这些字段名
//                if(cellName.equals("CaseId")){
//                    value=cs.getCaseId();
//
//                }else if(cellName.equals("ApiId")){
//                    value=cs.getApiId();
//
//                }else if(cellName.equals("Desc")){
//                    value=cs.getDesc();
//                }else if(cellName.equals("Params")){
//                    value=cs.getParams();
//                }
//                datas[i][j]=value;
                //要反射的方法名
                String methodName="get"+cellNames[j];

                try {
                    //获取要反射的方法
                    Method method=clazz.getMethod(methodName);
                    //完成get方法的反射调用
                    String value = (String) method.invoke(cs);
                    datas[i][j]=value;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            
        }
        return  datas;
    }

    public static void main(String[] args){

//验证excel里面的数据是不是以Case对象的方式在静态代码块中加载到内存中
//        for (Case cs:CaseUtil.cases) {
//            System.out.println(cs);
//
//        }

        String [] cellNames={"ApiId","Params"};
        Object [][] datas=getCaseDatasByApiId("1",cellNames);
        for (Object[] objects:datas) {

            for (Object data:objects) {
                System.out.println(data);
            }
        }

    }
}
