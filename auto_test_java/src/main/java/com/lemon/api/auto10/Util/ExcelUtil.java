package com.lemon.api.auto10.Util;

import com.lemon.api.auto10.pojo.Case;
import com.lemon.api.auto10.pojo.WriteBackData;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    public static void main(String[] args){

//        load("src/main/resources/testcase_V8.xlsx","register");
        load(PropertiesUtil.getExcelPath(),"register");


    }

    public static Map<String,Integer> rowidentifyRowNumMapping =new HashMap<String, Integer>();

    public static Map<String,Integer> cellNameCellNumMapping =new HashMap<String, Integer>();

    public static List<WriteBackData> writeBackDatas =new ArrayList<WriteBackData>();


    static {
//        loadRownumAndCellName("src/main/resources/testcase_V8.xlsx","register");
        loadRownumAndCellName(PropertiesUtil.getExcelPath(),"register");

    }

    /**获取caseId（用例序号）以及它对应的行索引
     * 获取cellName（首行单个字段的名称）以及它对应的列索引
     *
     * @param excelpath
     * @param sheetname
     */
    public static void loadRownumAndCellName(String excelpath, String sheetname) {
        //加载excel文件，这里使用第二种方式，不传一个file对象，传入一个输入流对象
        //因为，file对象不好控制excel文件的关闭，流对象帮我我们更好的控制已经打开excel的关闭，事实上
        //file对象底层也是封装的流对象。

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(excelpath));
            Workbook workbook=WorkbookFactory.create(inputStream) ;
            Sheet sheet=workbook.getSheet(sheetname);
            //获取sheet的首行
            Row titleRow=sheet.getRow(0);

            if(titleRow !=null && !isEmptyRow(titleRow)){
                //getLastCellNum()获取的值比索引大1，所以下面for循环不用取到等于
               int lastCellnum= titleRow.getLastCellNum();
                for (int i = 0; i <lastCellnum ; i++) {
                    Cell cell=titleRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(CellType.STRING);
                    String title=cell.getStringCellValue();
                    title=title.substring(0,title.indexOf("("));

                    //获取该单元格cell的索引（在for循环下面的cell，这样理解）
                    int cellnum=cell.getAddress().getColumn();

                    cellNameCellNumMapping.put(title,cellnum);

                }

            }
            //从第二行开始，获取所有的数据行,getLastRowNum()就是获取的最大的索引值，所以i需要=
            int lastRowNum=sheet.getLastRowNum();

            //循环拿到每个数据行
            for (int i = 1; i <=lastRowNum ; i++) {
                Row dataRow=sheet.getRow(i);

                //获取行上面的第一列，也就是测试用例的序号
                Cell firstCellofRow=dataRow.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                firstCellofRow.setCellType(CellType.STRING);
                String caseId=firstCellofRow.getStringCellValue();
                //获取行的索引,这里的索引值和i相等，但不建议使用，使用下面的方法拿到
                int rownum=dataRow.getRowNum();

                rowidentifyRowNumMapping.put(caseId,rownum);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(inputStream !=null){//这里一定要判断这个流对象是否为空，为空对象调用close会报空指针异常
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    /**
     * @param excelpath execl文件的路径
     * @param startrow 开始的行数
     * @param endrow 结束的行数
     * @param startcell 开始的列数
     * @param endcell 结束的列数
     * @return
     */
    public static Object [][] datas(String excelpath,int startrow,int endrow,int startcell,int endcell){
        /*
        这里的startrow,endrow,startcell,endcell都是指的行号和列号，而不是索引
         */
        Object [][] datas=null;
        try {
        //1.获取workbook对象
            Workbook workbook=WorkbookFactory.create(new File(excelpath));
        //2.获取sheet对象
            Sheet sheet=workbook.getSheet("register");

            datas=new Object[endrow-startrow+1][endcell-startcell+1];

            for (int i = startrow; i <=endrow ; i++) {
                //3.获取行
                Row row=sheet.getRow(i-1);
                for (int j = startcell; j <=endcell; j++) {
                    //4.获取列
                    Cell cell=row.getCell(j-1);
                    //这里设置单元格的值的类型为string，方便后面处理数据
                    cell.setCellType(CellType.STRING);
                    String value=cell.getStringCellValue();
//                    System.out.println(value);
                    datas[i-startrow][j-startcell]=value;


                }
        //4.获取列
//            Iterator<Cell> cells=row.cellIterator();
//            for (Iterator<Cell> it = cells; it.hasNext(); ) {
//                Cell cell = it.next();
//                System.out.println(cell);
//            }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return datas;
    }


    /**可以在数组中输入不连续的行和列
     * @param excelpath excel的路径
     * @param rows 行数的数组
     * @param cells 列数的数组
     * @return
     */
    public static Object [][] datas(String excelpath,String sheetname,int[] rows,int[] cells){
        /*
        这里的startrow,endrow,startcell,endcell都是指的行号和列号，而不是索引
         */
        Object [][] datas=null;
        try {
            //1.获取workbook对象
            Workbook workbook=WorkbookFactory.create(new File(excelpath));
            //2.获取sheet对象
            Sheet sheet=workbook.getSheet(sheetname);

            datas=new Object[rows.length][cells.length];

            for (int i = 0; i <rows.length ; i++) {
                //3.根据索引获取行
                Row row=sheet.getRow(rows[i]-1);
                for (int j = 0; j <cells.length; j++) {
                    //4.根据索引获取列
                    Cell cell=row.getCell(cells[j]-1);
                    //这里设置单元格的值的类型为string，方便后面处理数据
                    cell.setCellType(CellType.STRING);
                    String value=cell.getStringCellValue();
//                    System.out.println(value);
                    //根据索引获取行跟列
                    datas[i][j]=value;


                }
                //4.获取列
//            Iterator<Cell> cells=row.cellIterator();
//            for (Iterator<Cell> it = cells; it.hasNext(); ) {
//                Cell cell = it.next();
//                System.out.println(cell);
//            }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return datas;
    }

    /**解析指定excel表单Case类的数据，封装每行数据为对象
     * @param excelpath excel文件的相对路径
     * @param sheetname excel的表单名

     */
    public static void load(String excelpath, String sheetname) {
        //获取Case类的字节码，进行此类的反射
        Class<Case> clazz= Case.class;
        //创建workbook对象
        try {
            Workbook workbook=WorkbookFactory.create(new File(excelpath));

            //创建sheet对象
            Sheet sheet=workbook.getSheet(sheetname);

            //通过excel的索引获取首行字段名
            Row titlerow=sheet.getRow(0);

            //获取最大的列数
            int lastcellnumber=titlerow.getLastCellNum();
            String [] fields=new String[lastcellnumber];

            //遍历所有的列，取出每一列的字段名，并保存在数组中
            for (int i = 0; i <lastcellnumber ; i++) {

                //根据列索引获取cell单元格对象，这里的MissingCellPolicy是个枚举类
                Cell cell=titlerow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                //设置列的类型为字符串
                cell.setCellType(CellType.STRING);
                //拿到单元格的值，也就是我们下面要用的标题
                String title=cell.getStringCellValue();
                //使用字符串截取出纯英文的title名
                title=title.substring(0,title.indexOf("("));
                fields[i]=title;


            }

            int lastRowIndex=sheet.getLastRowNum();
            // 循环处理每一个数据行
            for (int i = 1; i <=lastRowIndex ; i++) {
                Case cs= (Case) clazz.newInstance();
                //拿到一个数据行
                Row dataRow=sheet.getRow(i);
                //判断excel多余的空行
                if(dataRow==null||isEmptyRow(dataRow)){
                    continue;
                }
                //遍历拿到的数据行中所有的列，将数据封装到cs对象中
                for (int j = 0; j <lastcellnumber ; j++) {
                    Cell cell=dataRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    cell.setCellType(CellType.STRING);
                    String value=cell.getStringCellValue();

                    //获取要反射的方法名
                    String methodName="set"+fields[j];
                    //获取要反射的方法对象
                    Method method=clazz.getMethod(methodName,String.class);
                    //完成反射调用
                    method.invoke(cs,value);


                }
                //
                CaseUtil.cases.add(cs);

            }



            //对象数据的封装：1。正向封装，从excel取出对应的字段名，进行判断，调用Case里面属性的set，get方法
            //但是这么做，字段名写死在代码里面。万一excel字段名发生改变，代码需要跟着变。这样耦合度很高，不建议
            //使用这种方法。 2。通过反射封装数据到对象中去，通过反射来解耦合。

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**重构load方法，多传入一个clazz字节码参数，可以使用成通用的方法，不仅仅局限于Case类
     * 这里因为下面做了判断，仅仅适用于Case和Rest两个类
     * 解析指定excel表单的数据，封装每行数据为对象
     * @param excelpath excel文件的相对路径
     * @param sheetname excel的表单名
     * @param clazz 不同类的字节码
     * T是范型，可以传入任意类型，需要在方法签名前面也加入<T>
     */
    public static <T> List<T> load(String excelpath, String sheetname, Class<T> clazz) {
        List<T> list =new ArrayList<T>();

        //获取Case类的字节码，进行此类的反射

        //创建workbook对象
        try {
            Workbook workbook=WorkbookFactory.create(new File(excelpath));

        //创建sheet对象
            Sheet sheet=workbook.getSheet(sheetname);

        //通过excel的索引获取首行字段名
            Row titlerow=sheet.getRow(0);

        //获取最大的列数
            int lastcellnumber=titlerow.getLastCellNum();
            String [] fields=new String[lastcellnumber];

        //遍历所有的列，取出每一列的字段名，并保存在数组中
            for (int i = 0; i <lastcellnumber ; i++) {

                //根据列索引获取cell单元格对象，这里的MissingCellPolicy是个枚举类
                Cell cell=titlerow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                //设置列的类型为字符串
                cell.setCellType(CellType.STRING);
                //拿到单元格的值，也就是我们下面要用的标题
                String title=cell.getStringCellValue();
                //使用字符串截取出纯英文的title名
                title=title.substring(0,title.indexOf("("));
                fields[i]=title;


            }

            int lastRowIndex=sheet.getLastRowNum();
            // 循环处理每一个数据行
            for (int i = 1; i <=lastRowIndex ; i++) {
                T obj= clazz.newInstance();
                //拿到一个数据行
                Row dataRow=sheet.getRow(i);
                //判断excel多余的空行
                if(dataRow==null||isEmptyRow(dataRow)){
                    continue;
                }
                //遍历拿到的数据行中所有的列，将数据封装到cs对象中
                for (int j = 0; j <lastcellnumber ; j++) {
                    Cell cell=dataRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    cell.setCellType(CellType.STRING);
                    String value=cell.getStringCellValue();

                    //获取要反射的方法名
                    String methodName="set"+fields[j];
                    //获取要反射的方法对象
                    Method method=clazz.getMethod(methodName,String.class);
                    //完成反射调用
                    method.invoke(obj,value);


                }
                //把每列的值放到行中，一整个list相当于一个行对象，在对应的CaseUtil,RestUtil,VariableUtil
                //的静态代码块中需要将这些行对象放到一个集合中，供后面代码调用
                list.add(obj);

//                //判断对象是哪一个类的对象，并通过判断放入到对应的List对象中
//                if(obj instanceof Case){//instanceof  是判断对象类型的语法
//                    Case cs= (Case) obj;
//                    CaseUtil.cases.add(cs);
//
//                }else if (obj instanceof Rest){
//                    Rest rest= (Rest) obj;
//                    RestUtil.rests.add(rest);
//
//                }else if(obj instanceof ParamVariable){
//                    ParamVariable paramVariable= (ParamVariable) obj;
//                    VariableUtil.paramVariableList.add(paramVariable);
//
//                }

            }



        //对象数据的封装：1。正向封装，从excel取出对应的字段名，进行判断，调用Case里面属性的set，get方法
            //但是这么做，字段名写死在代码里面。万一excel字段名发生改变，代码需要跟着变。这样耦合度很高，不建议
            //使用这种方法。 2。通过反射封装数据到对象中去，通过反射来解耦合。

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private static boolean isEmptyRow(Row dataRow) {
        int lastCellNum=dataRow.getLastCellNum();
        for (int i = 0; i <lastCellNum ; i++) {
            Cell cell=dataRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);
            String value=cell.getStringCellValue();
            if(value !=null ||value.trim().length()>0){
                return  false;

            }
            
        }

        return true;

    }

    /**回写测试报文
     * @param excelpath 回写的excel文件路径
     * @param sheetname 回写数据的表单名
     * @param caseId 测试用例的id
     * @param cellName 需要回写列单元格的名称
     * @param result 回写的数据
     */
    public static void writeBackData(String excelpath,String sheetname,String caseId, String cellName, String result) {

        System.out.println("读写excel");
        InputStream inputStream=null;
        OutputStream outputStream=null;
        try {

            inputStream = new FileInputStream(new File(excelpath));
            Workbook workbook=WorkbookFactory.create(inputStream);
            Sheet sheet =workbook.getSheet(sheetname);
            //sheet.getRow(rownum)===>row
            int rownum=rowidentifyRowNumMapping.get(caseId);
            Row row=sheet.getRow(rownum);
            //row.getCell(cellName)===>cell
            int cellnum=cellNameCellNumMapping.get(cellName);
            //cell.setCellValue(result)==>excel
            Cell cell=row.getCell(cellnum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(result);
            outputStream=new FileOutputStream(new File(excelpath));
            workbook.write(outputStream);

        }catch(Exception e) {
            e.printStackTrace();
        }finally{
                try {
                if(outputStream !=null){
                    outputStream.close();
                }
                if (inputStream !=null){
                    inputStream.close();
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }



    }

    /**批量回写数据到excel中的方法
     * @param excelpath excel文件路径
     */
    public static void batchWriteBackDatas(String excelpath) {


        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream= new FileInputStream(new File(excelpath));
             Workbook workbook=WorkbookFactory.create(inputStream);
            for (WriteBackData writeBackData:writeBackDatas) {

                //获取sheetName,因为sheetName这几个属性已经封装到writeBackDatas里面了
                String sheetName=writeBackData.getSheetName();
                Sheet sheet=workbook.getSheet(sheetName);

                //获取行索引
//               String caseId=writeBackData.getCaseId();
//                int rownum=rowidentifyRowNumMapping.get(caseId);
               String rowIdentify=writeBackData.getRowIdentify();
               int rownum=rowidentifyRowNumMapping.get(rowIdentify);
               Row row=sheet.getRow(rownum);

               //获取列索引
                String cellName=writeBackData.getCellName();
                int cellnum=cellNameCellNumMapping.get(cellName);
                Cell cell=row.getCell(cellnum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(CellType.STRING);
                //获取要放入excel的数据
                String result=writeBackData.getResult();
                cell.setCellValue(result);




            }
                //将cell中放的数据写入到流中写入到excel中
                outputStream =new FileOutputStream(new File(excelpath));
                workbook.write(outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(outputStream !=null){
                    outputStream.close();
                }
                if (inputStream !=null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}