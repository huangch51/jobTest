package executor.com.tt.execl;

import executor.com.tt.base.MyBatisUtil;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangcaihuan
 * @Since 2020/9/1
 */
public class ExcelUtilTwo {


    /**
     *
     * @param list   下载的数据
     * @param modelName  文件名称
     * @return
     */
    public static boolean createModel(List<Map> list, String modelName) {
        boolean newFile = false;
        //创建excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建工作表sheet
        HSSFSheet sheet = workbook.createSheet();
        //创建第一行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell;
        //设置样式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        style.setLocked(true);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        //2.得到一个POI的工具类
        CreationHelper factory = workbook.getCreationHelper();

        //3.得到一个换图的对象
        Drawing drawing = sheet.createDrawingPatriarch();
        //4. ClientAnchor是附属在WorkSheet上的一个对象，  其固定在一个单元格的左上角和右下角.
        ClientAnchor anchor = factory.createClientAnchor();


        //插入第一行数据的表头
        for (int i = 0; i < list.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            //设置单元格为字段名
            cell.setCellValue((String) list.get(i).get("column_name"));
            //设置单元格为字段名的注释
            String column_comment= (String) list.get(i).get("column_comment");
            Comment comment0 = drawing.createCellComment(anchor);
            RichTextString str0 = factory.createRichTextString(column_comment);
            comment0.setString(str0);
            cell.setCellComment(comment0);
            sheet.setColumnWidth(i,((String) list.get(i).get("column_name")).getBytes().length*2*256);
        }
        //创建excel文件

        try {
            File file = new File(modelName);
            if(!file.exists()){
                file.createNewFile();
            }
            newFile = file.createNewFile();
            //将excel写入
            FileOutputStream stream = FileUtils.openOutputStream(file);
            workbook.write(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }

    /**
     *
     * @param tableName 表名
     * @param modelFileName  文件名
     */
    private static void uploadFile(String tableName,String modelFileName){
        //TODO 查询数据库表结构
        Map param=new HashMap();
        param.put("table_name",tableName);
        SqlSession sqlSession=MyBatisUtil.getSqlSession();
        String statement = "executor.com.tt.mapper.UserMapper.queryColumnList";//映射sql的标识字符串
        List<Map> list=sqlSession.selectList(statement,param);
        //TODO 创建文件并且下载
        createModel(list,modelFileName);


    }


    public static void main(String[] args) {
        String modelFileName = "accountNew1.xlsx";
        String tableName="dim_room_type_map";
        try {
          uploadFile(tableName,modelFileName);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
