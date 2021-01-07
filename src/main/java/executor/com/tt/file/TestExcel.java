package executor.com.tt.file;

import executor.com.tt.base.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangcaihuan
 * @Since 2020/9/4
 */
public class TestExcel {

    public static void main(String[] args) throws Exception{

        Map param=new HashMap();
        param.put("table_name","dim_room_type_map");
        SqlSession sqlSession= MyBatisUtil.getSqlSession();
        String statement = "executor.com.tt.mapper.UserMapper.queryColumnList";
        List<Map> list=sqlSession.selectList(statement,param);
        try {
            new ExcelUtil("房间表","dim_gift_info_map",list).uploadExcel();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testImport() throws Exception{
        Map param=new HashMap();
        String fileName="dim_gift_info_map-442498111.xls";
        String tableName="dim_room_type_map";
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
    }


}
