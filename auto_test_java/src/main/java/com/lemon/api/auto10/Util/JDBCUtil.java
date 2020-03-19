package com.lemon.api.auto10.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class JDBCUtil {

   public static Properties properties =new Properties();

   static {
       System.out.println("静态代码块解析properities文件数据");
       InputStream inputStream = null;
       try {
           inputStream=new FileInputStream(new File("src/main/resources/jdbc.properties"));
           properties.load(inputStream);
       } catch (Exception e) {
           System.out.println("发生了异常");
           e.printStackTrace();
       }
   }


    /**这里的sql不论查了多少个字段，字段值保存到map里面，其中key值保存字段名，value值保存对应字段的数据
     * select 字段1，字段2，字段…… from tablename
     * @param sql
     * @param values
     * @return
     */
   public static Map<String,Object> query(String sql,Object ... values){//values是一个可变参数,可以传，也可以不传
       Map<String,Object> columnLabelAndValues=null;
       //1.根据链接信息，获取数据库连接
        Connection connection=getConnection();
       try {
           //2.获取PreparedStatement对象(该对象有提供数据库的操作方法）
           PreparedStatement preparedStatement=connection.prepareStatement(sql);

           //3.如果传入的sql有占位符？，则设置where条件字段的值preparedStatement.setObject（实时绑定），不传values代表
           //where 条件中没有？，是传的完整的sql
           for (int i = 0; i <values.length ; i++) {
               preparedStatement.setObject(i + 1, values[i]);
           }
           //4.调用查询方法，执行查询，返回ResultSet（结果集）
               ResultSet resultSet=preparedStatement.executeQuery();
           //获取sql中查询的字段名
               ResultSetMetaData metaData=resultSet.getMetaData();
           //得到查询字段的数目
               int columnCount=metaData.getColumnCount();

           //5.从结果集取查询数据的字段以及值,放到map中
               columnLabelAndValues=new HashMap<String, Object>();
               while(resultSet.next()){
                   for (int i = 1; i <= columnCount; i++) {
                       String columnLabel=metaData.getColumnLabel(i);
                       String columnValue=resultSet.getObject(columnLabel).toString();
                       columnLabelAndValues.put(columnLabel,columnValue);

                   }

               }


       } catch (SQLException e) {
           e.printStackTrace();
       }
       return  columnLabelAndValues;

   }

    /**获取数据库连接
     * @return
     */
    private static Connection getConnection() {
       //从properities中获取url
        String url=properties.getProperty("jdbc.url");
        //从properities中获取user
        String user=properties.getProperty("jdbc.user");
        //从properities中获取password
        String password=properties.getProperty("jdbc.password");
        Connection connection=null;
        try {
            connection=DriverManager.getConnection(url,user,password);
            System.out.println("连接数据库成功");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args){
        String sql="select id,leaveamount from member where mobilephone=15511447879";

        Map<String,Object> columnLabelAndValues=query(sql);

        Set<String> keys=columnLabelAndValues.keySet();


        for (String key: keys) {
          Object value=columnLabelAndValues.get(key);
          System.out.println(key+"="+value);

        }



    }
}
