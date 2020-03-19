package com.lemon.api.auto10.pojo;

/**
 * 回写数据对象
 */

public class WriteBackData {
    //因为在testcase中遍历回写数据，每一条测试用例都回写一次，造成了很大的读写资源开销
    //所以在回写excel之前，先将excel数据缓存到一个对象中，最后循环遍历该对象，一次写入就可以。
    //所以新建这个会写数据的类，之前caseId是用例表单的唯一标识，但是不是所有表单回写的行的唯一标识，所以这里要改这个变量的名字
    private String sheetName;
    //行标识的字段名称
//    private  String caseId;
    private  String rowIdentify;

    private String cellName;
    private  String result;

    public WriteBackData(String sheetName, String rowIdentify, String cellName, String result) {
        this.sheetName = sheetName;
//        this.caseId = caseId;
        this.rowIdentify=rowIdentify;
        this.cellName = cellName;
        this.result = result;
    }

    public WriteBackData() {
    }



    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

//    public String getCaseId() {
//        return caseId;
//    }

//    public void setCaseId(String caseId) {
//        this.caseId = caseId;
//    }


    public String getRowIdentify() {
        return rowIdentify;
    }

    public void setRowIdentify(String rowIdentify) {
        this.rowIdentify = rowIdentify;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


}
