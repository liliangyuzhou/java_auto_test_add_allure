package com.lemon.api.auto10.pojo;

import java.util.Map;

public class DBQueryResult {



    //sql脚本的编号
    private String no;
    //脚本执行查到的数据，保存在map中（key保存的是查询的字段名，value保存的是查询对应字段对应的值）
    private Map<String,Object> columnLabelAndValues;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Map<String, Object> getColumnLabelAndValues() {
        return columnLabelAndValues;
    }

    public void setColumnLabelAndValues(Map<String, Object> columnLabelAndValues) {
        this.columnLabelAndValues = columnLabelAndValues;
    }

    @Override
    public String toString() {
        return "no="+this.no+",columnLabelAndValues="+this.columnLabelAndValues;
    }
}
