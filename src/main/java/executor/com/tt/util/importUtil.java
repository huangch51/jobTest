package executor.com.tt.util;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangcaihuan
 * @Since 2020/9/1
 */
public class importUtil {




    public static List dealsheet(Workbook workbook, int sheetnum, String tablename){
        //用于存放sql集合
        List sqlList = new ArrayList();
        for(int s=0;s<sheetnum;s++){
            //用于判断是否有id列
            String idSign = "false";
            //获取要解析的表格（第一个sheet）
            HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(s);
            //获取总行数(包括列头)
            int arrays = sheet.getPhysicalNumberOfRows();
            //行数为0，说明此sheet没有数据，直接忽略掉。
            if(arrays == 0){
                continue;
            }
            //用于存储insertsql的前半部分（取sheet的第一行）
            String sql = "";
            //用于存储insertsql的后半部分
            String sqltemp = "";
            //获取总列数
            int rows = sheet.getRow(0).getPhysicalNumberOfCells();
            if(arrays>0){//拼接insert sql的前半部分(列头)
                HSSFRow row = sheet.getRow(0);//获得要解析的行(第一行列头部分)
                for(int i=0;i<rows;i++){
                    Cell cell = row.getCell(i);
                    cell.setCellType(CellType.STRING);//读取数据前设置单元格类型 如果不设置有可能会报错
                    sql += cell.getStringCellValue()+",";
                    if("ID".equals(cell.getStringCellValue().toUpperCase())){
                        idSign = "true";//sheet中有id列
                    }
                }
                //sheet中有id列
                if("true".equals(idSign)){
                    sql = "insert into "+tablename+" ("+sql.substring(0,sql.length()-1)+") values ";
                }else{
                    sql = "insert into "+tablename+" (id,"+sql.substring(0,sql.length()-1)+") values ";
                }
            }
            //拼接insert sql的后半部分(值)
            for (int k = 1; k<arrays; k++) {
                //获得要解析的行
                HSSFRow row = sheet.getRow(k);
                for(int f=0;f<rows;f++){
                    Cell cell = row.getCell(f);
                    if(cell != null){
                        if(cell.getCellType() == 0 && HSSFDateUtil.isCellDateFormatted(cell)){//判断是否是时间类型的
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                            String datevalue=df.format(cell.getDateCellValue());
                            //String datevalue = Date.fo(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
                            sqltemp += datevalue+",";
                        }else{
                            cell.setCellType(CellType.STRING);//读取数据前设置单元格类型 如果不设置有可能会报错
                            sqltemp += cell.getStringCellValue()+",";
                        }
                    }else{
                        sqltemp += ",";
                    }
                }
                //sheet中有id列
                if("true".equals(idSign)){
                    sqlList.add(sql+"('"+sqltemp.substring(0,sqltemp.length()-1).replaceAll(",","','")+"')");
                }else{
                    sqlList.add(sql+"(UUID(),'"+sqltemp.substring(0,sqltemp.length()-1).replaceAll(",","','")+"')");
                }
                sqltemp = "";
            }
        }
        return sqlList;
    }
}
