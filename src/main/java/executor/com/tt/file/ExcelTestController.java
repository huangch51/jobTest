package executor.com.tt.file;

import executor.com.tt.base.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangcaihuan
 * @Since 2020/9/7
 */
@RestController
@RequestMapping("/excel")
@CrossOrigin
public class ExcelTestController {


    /**
     * 下载
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload")
    public String upload(@RequestBody Map param) throws Exception{
        String tableName= (String) param.get("tableName");
        String title= (String) param.get("title");
        param.put("table_name",tableName);
        SqlSession sqlSession= MyBatisUtil.getSqlSession();
        String statement = "executor.com.tt.mapper.UserMapper.queryColumnList";
        List<Map> list=sqlSession.selectList(statement,param);
        try {
            new ExcelUtil(title,tableName,list).uploadExcel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }


    /**
     * 导入
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importExcel",method = RequestMethod.POST)
    public String  importExcel(@RequestParam(value = "file", required = false) MultipartFile file, String tableName) throws Exception {
        Map param=new HashMap();
        String fileName= file.getOriginalFilename();
        param.put("file_name",fileName);
        param.put("table_name",tableName);
        SqlSession sqlSession= MyBatisUtil.getSqlSession();
        String statement = "executor.com.tt.mapper.UserMapper.insertByMap";
        List<Map<String, Object>> list=new ExcelUtil().importExcel(fileName);
        System.out.println(list);
        for (Map map:list){
            param.put("params",map);
            sqlSession.insert(statement,param);
            sqlSession.commit();
        }
        return "success";
    }

}
