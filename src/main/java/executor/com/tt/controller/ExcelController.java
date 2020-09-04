package executor.com.tt.controller;

import executor.com.tt.util.ExcelUtilTwo;
import executor.com.tt.util.ImportExcelUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangcaihuan
 * @Since 2020/9/3
 */
@RestController
@RequestMapping("/import")
@CrossOrigin
public class ExcelController {


    @RequestMapping(value = "/importExcel",method = RequestMethod.POST)
    public String  importExcel(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        if(file==null){

        }

        Map param=new HashMap();
        String tableName= "dim_gift_info_map";
        String fileName="new1.xls";
        param.put("file_name",fileName);
        param.put("table_name",tableName);
       new ImportExcelUtil().importData(param);
        return "success";
    }

    @RequestMapping(value = "/importE")
    public String  importE() throws Exception {
        Map param=new HashMap();
        String tableName= "dim_gift_info_map";
        String fileName="accountList6.xlsx";
        param.put("file_name",fileName);
        param.put("table_name",tableName);
        new ImportExcelUtil().importData(param);
        return "success";
    }

    @RequestMapping(value = "/upload")
    public String upload() throws Exception{
        String table_name= "dim_gift_info_map";
        String fileName="new1";
       new ExcelUtilTwo().uploadFile(table_name,fileName);
        return "success";
    }
}
