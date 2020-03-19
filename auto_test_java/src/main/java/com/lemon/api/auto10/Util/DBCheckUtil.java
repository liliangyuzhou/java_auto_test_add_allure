package com.lemon.api.auto10.Util;

import com.alibaba.fastjson.JSONObject;
import com.lemon.api.auto10.pojo.DBChecker;
import com.lemon.api.auto10.pojo.DBQueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBCheckUtil {

    /**根据脚本查询并返回对应结果
     * @param validateSql
     * @return
     */
    public static String doQuery(String validateSql) {
        //将脚本字符串封装成对象
        List<DBChecker> dbCheckers=JSONObject.parseArray(validateSql, DBChecker.class);

        List<DBQueryResult> dbQueryResultList =new ArrayList<DBQueryResult>();
        //循环遍历，取出要执行的所有sql脚本
        for (DBChecker dbChecker:dbCheckers) {
            //拿到sql的编号
            String no=dbChecker.getNo();
            //拿到sql脚本
            String sql=dbChecker.getSql();
            System.out.println(sql);

            Map<String,Object> columnLabelAndValues= JDBCUtil.query(sql);


            //把拿到的数据封装到 DBQueryResult中
            DBQueryResult dbQueryResult =new DBQueryResult();
            dbQueryResult.setNo(no);
            dbQueryResult.setColumnLabelAndValues(columnLabelAndValues);

            dbQueryResultList.add(dbQueryResult);

        }


        return JSONObject.toJSONString(dbQueryResultList);

    }
    public static void main(String[] args){

        String validateSql="[{\"no\":\"1\",\"sql\":\"select leaveamout from memeber where mobilephone='15511447879'\"},{\"no\":\"2\",\"sql\":\"select count(*) as totalNum from financelog where mobilephone='15511447879'\"}]";
        List<DBChecker> dbCheckers=JSONObject.parseArray(validateSql, DBChecker.class);

        for (DBChecker dbChecker: dbCheckers)
            System.out.println(dbChecker);



    }
}
