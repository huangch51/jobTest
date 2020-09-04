package executor.com.tt.one;

import executor.com.tt.base.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.tomcat.util.buf.StringUtils;
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

public class ImportExcelUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String EXCEL_XLS = ".xls";
    private static final String EXCEL_XLSX = ".xlsx";

    /**
     *读取excel数据
     * @throws Exception
     *
     */
    public static List<List<String>> readExcelInfo(String url) throws Exception{
        /*
         * workbook:工作簿,就是整个Excel文档
         * sheet:工作表
         * row:行
         * cell:单元格
         */

//        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(url)));
//        支持excel2003、2007
        File excelFile = new File(url);//创建excel文件对象
        InputStream is = new FileInputStream(excelFile);//创建输入流对象
        checkExcelVaild(excelFile);
        Workbook workbook = getWorkBook(is, excelFile);
//        Workbook workbook = WorkbookFactory.create(is);//同时支持2003、2007、2010
//        获取Sheet数量
        int sheetNum = workbook.getNumberOfSheets();
//      创建二维数组保存所有读取到的行列数据，外层存行数据，内层存单元格数据
        List<List<String>> dataList = new ArrayList<List<String>>();
//        FormulaEvaluator formulaEvaluator = null;
//        遍历工作簿中的sheet,第一层循环所有sheet表
        for(int index = 0;index<sheetNum;index++){
            Sheet sheet = workbook.getSheetAt(index);
            if(sheet==null){
                continue;
            }
            System.out.println("表单行数："+sheet.getLastRowNum());

//            如果当前行没有数据跳出循环，第二层循环单sheet表中所有行
            for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){
                Row row = sheet.getRow(rowIndex);
//            根据文件头可以控制从哪一行读取，在下面if中进行控制
                if(row==null){
                    continue;
                }
                Row rowFirst=sheet.getRow(0);
                List<String> cellList = new ArrayList<String>();
                if (rowIndex == 0){
                    for(int cellIndex=0;cellIndex<rowFirst.getLastCellNum();cellIndex++){
                        Cell cell = row.getCell(cellIndex);
                        cellList.add(cell.toString().trim());

                    }
                }else {
                       //遍历每一行的每一列，第三层循环行中所有单元格
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
     *获取单元格的数据
     *
     *
     */
    public static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell==null || cell.toString().trim().equals("")){
            return null;
        }
        CellType cellType = cell.getCellTypeEnum();

        System.out.println("类型："+cellType);

        if(cellType==CellType.STRING){
            cellValue = cell.getStringCellValue().trim();
            String str = "\'" + cellValue + "\'" ;
            return str;
            //return cellValue = StringUtil.isEmpty(cellValue)?"":cellValue;
        }
        if(cellType==CellType.NUMERIC){
            if (HSSFDateUtil.isCellDateFormatted(cell)) {  //判断日期类型
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
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
    public static Workbook getWorkBook(InputStream is,File file) throws Exception{

        Workbook workbook = null;
        if(file.getName().endsWith(EXCEL_XLS)){
            workbook = new HSSFWorkbook(is);
        }else if(file.getName().endsWith(EXCEL_XLSX)){
            workbook = new HSSFWorkbook(is);
        }

        return workbook;
    }
    /**
     *校验文件是否为excel
     * @throws Exception
     *
     *
     */
    public static void checkExcelVaild(File file) throws Exception {
        if(!file.exists()){
            throw new Exception("文件不存在！");
        }
        if(!file.isFile()||((!file.getName().endsWith(EXCEL_XLS)&&!file.getName().endsWith(EXCEL_XLSX)))){
            System.out.println(file.isFile()+"==="+file.getName().endsWith(EXCEL_XLS)+"==="+file.getName().endsWith(EXCEL_XLSX));
            System.out.println(file.getName());
            throw new Exception("文件不是Excel");
        }
    }


    public static void main(String[] args) throws Exception {
        List<List<String>> lists= readExcelInfo("accountList6.xlsx");
        System.out.println(lists);
            Map param = new HashMap();
            param.put("table_name", "dim_gift_info_map");
            String join = StringUtils.join(lists.get(0));
            System.out.println(join);
            if (!join.isEmpty()){
                param.put("list", join);
            }
            System.out.println(param);
            SqlSession session = MyBatisUtil.getSqlSession();
            String statement = "executor.com.tt.mapper.UserMapper.insertTable";
            for (int i = 1; i < lists.size(); i++) {
                String data = StringUtils.join(lists.get(i));
                System.out.println(data);
                if (!data.isEmpty()) {
                    param.put("data", data);
                    int insert = session.insert(statement, param);
                    session.commit();
                }
            }
            session.close();
        
    }


}