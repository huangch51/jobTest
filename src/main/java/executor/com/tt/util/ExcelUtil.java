package executor.com.tt.util;

import executor.com.tt.base.MyBatisUtil;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

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
public class ExcelUtil {

    public static boolean createModel(List<String> list, String modelName) {
        boolean newFile = false;
        //创建excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建工作表sheet
        HSSFSheet sheet = workbook.createSheet();
        //创建第一行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell;
        //设置样式
        CellStyle style = workbook.createCellStyle();
        //style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        style.setLocked(true);//设置锁定



        //插入第一行数据的表头
        for (int i = 0; i < list.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(list.get(i));
        }
        //创建excel文件
        File file = new File(modelName);
        try {
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


    public static void main(String[] args) {
        String modelFileName = "accountList7.xlsx";
        //下载展示的文件名
        ResponseEntity<InputStreamResource> response = null;
        try {
            Map param =new HashMap();
            param.put("table_name","dim_gift_info_map");
            SqlSession session = MyBatisUtil.getSqlSession();
            String statement = "executor.com.tt.mapper.UserMapper.queryColumn";//映射sql的标识字符串

            List<String> head= session.selectList(statement,param);

//            传人数据库的字端，创建资料的模版
            boolean model = createModel(head, modelFileName);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
