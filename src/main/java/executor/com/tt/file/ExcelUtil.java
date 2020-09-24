package executor.com.tt.file;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangcaihuan
 * @Since 2020/9/4
 */
public class ExcelUtil {

    //标题
    private String title;

    //表名
    private String tableName;

    //标题数据
    private List<Map> dataList;

    private static final String EXCEL_XLS=".xls";

    private static final String EXCEL_XLSX=".xlsx";

    private static final String TITLE_EXPAND="配置模板";


    HttpServletResponse response;


    public ExcelUtil(){

    }

    public ExcelUtil(String title,String tableName, List<Map> dataList) {
        this.title=title;
        this.tableName=tableName;
        this.dataList=dataList;
    }


    /**
     * 下载excel
     * @throws Exception
     */
    public void uploadExcel() throws Exception{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        //设置标题
        int columnNum = dataList.size();
        HSSFRow rowm = sheet.createRow(0);
        HSSFCell cellTiltle = rowm.createCell(0);
        HSSFCellStyle columnTitleStyle = (HSSFCellStyle) this.getColumnTitleStyle(workbook);
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (columnNum - 1)));
        cellTiltle.setCellStyle(columnTitleStyle);
        cellTiltle.setCellValue(title+TITLE_EXPAND);


        //1.创建数据行
        HSSFRow row = sheet.createRow(2);
        CellStyle columnDataStyle = this.getColumnDataStyle(workbook);//获取列头样式对象
        HSSFCell cell;

        //2.得到一个POI的工具类
        CreationHelper factory = workbook.getCreationHelper();
        //3.得到一个换图的对象
        Drawing drawing = sheet.createDrawingPatriarch();
        //4. ClientAnchor是附属在WorkSheet上的一个对象, 其固定在一个单元格的左上角和右下角.
        ClientAnchor anchor = factory.createClientAnchor();

        //插入第一行数据的表头
        for (int i = 0; i < dataList.size(); i++) {
            cell = row.createCell(i);
            //设置单元格为字段名
            cell.setCellValue((String) dataList.get(i).get("column_name"));
            //设置单元格为字段名的注释
            String column_comment= (String) dataList.get(i).get("column_comment");
            Comment comment0 = drawing.createCellComment(anchor);
            RichTextString str0 = factory.createRichTextString(column_comment);
            comment0.setString(str0);
            cell.setCellComment(comment0);
            cell.setCellStyle(columnDataStyle);
            sheet.setColumnWidth(i,((String) dataList.get(i).get("column_name")).getBytes().length*2*256);
        }
        try {
            //reponse
//            String modelName = URLEncoder.encode( tableName+"-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls", "UTF-8");
//            String headStr = "attachment; filename=\"" + modelName + "\"";
//            response = getResponse();
//            response.setContentType("APPLICATION/OCTET-STREAM");
//            response.setHeader("Content-Disposition", headStr);
//            OutputStream out = response.getOutputStream();
//            workbook.write(out);
//            out.close();

            //local
            String fileName = URLEncoder.encode( tableName+ "-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls", "UTF-8");
            File file = new File(fileName);
            if(!file.exists()){
                file.createNewFile();
            }
            //将excel写入
            FileOutputStream stream = FileUtils.openOutputStream(file);
            workbook.write(stream);
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取response
     **/
    private  HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }


    /**
     * 设置表头标题格式
     * @param workbook
     * @return
     */
    public CellStyle getColumnTitleStyle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setFontName("Consolas");
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        //自动换行
        style.setWrapText(false);
        //设置水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }



    /**
     * 设置单元格格式
     * @param workbook
     * @return
     */
    public CellStyle getColumnDataStyle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Consolas");
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        //自动换行
        style.setWrapText(false);
        //设置水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //solid 填充  foreground  前景色
        HSSFPalette palette = workbook.getCustomPalette();
        palette.setColorAtIndex(HSSFColor.LIME.index, (byte) 226, (byte) 239, (byte) 218);
        style.setFillForegroundColor(HSSFColor.LIME.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }


    /**
     * 解析Excel
     * @throws Exception
     */
    public List<Map<String, Object>> importExcel(String fileName){
        File excelFile = new File(fileName);
        InputStream is = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            is = new FileInputStream(excelFile);
            //校验文件是否是Excel
            checkExcelVaild(excelFile);
            //获取Workbook
            Workbook workbook=getWorkBook(is,excelFile);

            if(workbook!=null){
                Sheet sheet = workbook.getSheetAt(0);
                // 得到标题行
                Row titleRow = sheet.getRow(2);
                int lastRowNum = sheet.getLastRowNum();
                int lastCellNum = titleRow.getLastCellNum();

                //获取数据行行号
                int dataRowNum=titleRow.getRowNum()+1;
                //循环
                for(int i = dataRowNum; i <= lastRowNum; i++ ){
                    Map<String, Object> map = new HashMap<>();
                    Row row = sheet.getRow(i);
                    //没有数据的行默认是null　
                    if(row==null) {
                        continue;
                    }
                    for(int j = 0; j < lastCellNum; j++){
                        //得到列名
                        String key = titleRow.getCell(j).getStringCellValue();
                        //得到值
                        Cell cell=row.getCell(j);
                        String value= getCellValue(cell);
                        //放进map
                        map.put(key, value);
                    }
                    list.add(map);
                }
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 校验文件是否为Excel
     * @param file
     */
    public void checkExcelVaild(File file) {
        try {
            if(!file.exists()){
                System.out.println("文件不存在");
            }
            if(!file.isFile()||((!file.getName().endsWith(EXCEL_XLS)&&!file.getName().endsWith(EXCEL_XLSX)))){
                System.out.println(file.isFile()+"==="+file.getName().endsWith(EXCEL_XLS)+"==="+file.getName().endsWith(EXCEL_XLSX));
                System.out.println(file.getName());
                System.out.println("文件不是Excel");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 判断excel的版本，并根据文件流数据获取workbook
     * @param is
     * @param file
     * @return
     * @throws Exception
     */
    public Workbook getWorkBook(InputStream is,File file) throws Exception{
        Workbook workbook=null;
        if(file.getName().endsWith(EXCEL_XLS)){
            workbook = new HSSFWorkbook(is);
        }else if (file.getName().endsWith(EXCEL_XLSX)){
            workbook=new XSSFWorkbook(is);
        }
        return workbook;
    }


    /**
     * 获取单元格的数据，校验数据格式并返回
     * @param cell
     * @return
     */
    public String getCellValue(Cell cell){
        String cellValue="";
        if(cell==null || cell.toString().trim().equals("")){
            return null;
        }
        CellType cellType = cell.getCellTypeEnum();
        if(cellType==CellType.STRING){
            cellValue = cell.getStringCellValue().trim();
            return cellValue;
        }
        if(cellType==CellType.NUMERIC){
            if (HSSFDateUtil.isCellDateFormatted(cell)) {  //判断日期类型
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cellValue=df.format(cell.getDateCellValue().getTime());
            } else {  //否
                cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
            }
            return cellValue;
        }if(cellType==CellType.BOOLEAN){
            cellValue=String.valueOf(cell.getBooleanCellValue());
            return cellValue;
        }if(cellType==CellType.ERROR){
            cellValue="";
            return cellValue;
        }
        return null;
    }

}
