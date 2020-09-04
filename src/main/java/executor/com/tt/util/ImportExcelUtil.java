package executor.com.tt.util;

import executor.com.tt.base.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.buf.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  导入Excel 工具类
 */
public class ImportExcelUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String EXCEL_XLS = ".xls";
    private static final String EXCEL_XLSX = ".xlsx";


    /*
     * workbook:工作簿,就是整个Excel文档
     * sheet:工作表
     * row:行
     * cell:单元格
     */
    public  List<List<String>> readExcelInfo(String url) throws Exception{
        File excelFile = new File(url);
        InputStream is = new FileInputStream(excelFile);
        checkExcelVaild(excelFile);
        Workbook workbook = getWorkBook(is, excelFile);
        //获取Sheet数量
        int sheetNum = workbook.getNumberOfSheets();

        //创建二维数组保存所有读取到的行列数据，外层存行数据，内层存单元格数据
        List<List<String>> dataList = new ArrayList<List<String>>();
        //遍历工作簿中的sheet,第一层循环所有sheet表
        for(int index = 0;index<sheetNum;index++){
            Sheet sheet = workbook.getSheetAt(index);
            if(sheet==null){
                continue;
            }
            System.out.println("表单行数："+sheet.getLastRowNum());

            //如果当前行没有数据跳出循环，第二层循环单sheet表中所有行
            for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){
                Row row = sheet.getRow(rowIndex);
                if(row==null) {
                    continue;
                }
                Row rowFirst=sheet.getRow(0);
                List<String> cellList = new ArrayList<String>();
                //遍历第一行数据，表头
                if (rowIndex == 0){
                    for(int cellIndex=0;cellIndex<rowFirst.getLastCellNum();cellIndex++){
                        Cell cell = row.getCell(cellIndex);
                        cellList.add(cell.toString().trim());
                    }
                }else {
                    //遍历第二行的每一列，第三层循环行中所有单元格
                    for (int cellIndex = 0; cellIndex < rowFirst.getLastCellNum(); cellIndex++) {
                        Cell cell = row.getCell(cellIndex);
                        System.out.println("cell"+cell);
                        cellList.add(getCellValue(cell));
                    }
                }
                dataList.add(cellList);
            }

        }
        is.close();
        return dataList;
    }

    /**
     * 获取单元格的数据,校验数据格式
     * @param cell
     * @return
     */
    public  String getCellValue(Cell cell){
        String cellValue = "";
        if(cell==null || cell.toString().trim().equals("")){
            return null;
        }
        CellType cellType = cell.getCellTypeEnum();
        if(cellType==CellType.STRING){
            cellValue = cell.getStringCellValue().trim();
            String str = "\'" + cellValue + "\'" ;
            return str;
        }
        if(cellType==CellType.NUMERIC){
            if (HSSFDateUtil.isCellDateFormatted(cell)) {  //判断日期类型
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cellValue=df.format(cell.getDateCellValue().getTime());
            } else {  //否
                cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
            }
            return cellValue;
        }
        if(cellType==CellType.BOOLEAN){
            cellValue = String.valueOf(cell.getBooleanCellValue());
            return cellValue;
        }
        return null;

    }
    /**
     *判断excel的版本，并根据文件流数据获取workbook
     * @throws IOException
     *
     */
    public  Workbook getWorkBook(InputStream is,File file) throws Exception{

        Workbook workbook = null;
        if(file.getName().endsWith(EXCEL_XLS)){
            workbook = new HSSFWorkbook(is);
        }else if(file.getName().endsWith(EXCEL_XLSX)) {
            workbook = new XSSFWorkbook(is);
        }
        return workbook;
    }

    /**
     *校验文件是否为Excel
     * @throws Exception
     */
    public  void checkExcelVaild(File file) throws Exception {
        if(!file.exists()){
            throw new Exception("文件不存在！");
        }
        if(!file.isFile()||((!file.getName().endsWith(EXCEL_XLS)&&!file.getName().endsWith(EXCEL_XLSX)))){
            System.out.println(file.isFile()+"==="+file.getName().endsWith(EXCEL_XLS)+"==="+file.getName().endsWith(EXCEL_XLSX));
            System.out.println(file.getName());
            throw new Exception("文件不是Excel");
        }
    }

    /**
     *
     * @param param
     * @throws Exception
     */
    public  void importData(Map param) throws Exception {

        String fileName= (String) param.get("file_name");

        //TODO 1.解析excel获得数据
        List<List<String>> lists=readExcelInfo(fileName);

        //TODO 2.导入
        //获得第一行数据，数据库表字段
        String list=StringUtils.join(lists.get(0));

        if(!list.isEmpty()){
            param.put("list",list);
        }

        //TODO 插入数据库
        SqlSession session = MyBatisUtil.getSqlSession();
        String statement = "executor.com.tt.mapper.UserMapper.insertTable";
        for (int i = 1; i < lists.size(); i++) {
            String data=StringUtils.join(lists.get(i));
            System.out.println(data);
            if (!data.isEmpty()) {
                param.put("data", data);
                int insert = session.insert(statement, param);
                if(insert==1) {
                    session.commit();
                }
            }
        }
        session.close();

    }


    @Test
    public void testTwo() throws Exception {
        Map param=new HashMap();
        String fileName="accountList6.xlsx";
        String tableName="dim_gift_info_map";
        param.put("file_name",fileName);
        param.put("table_name",tableName);
        importData(param);
    }
}