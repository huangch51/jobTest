package executor.com.tt.util;

import com.alibaba.excel.EasyExcel;
import executor.com.tt.base.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.*;

/**
 * @author huangcaihuan
 * @Since 2020/9/1
 */
public class ThreeUtil {

    /**
     * 不创建对象的写
     */
    @Test
    public void noModelWrite() {
        // 写法1
        String fileName = "noModelWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName).head(head()).sheet("模板").doWrite(dataList());
    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        Map param =new HashMap();
        param.put("table_name","dim_gift_info_map");
        SqlSession session = MyBatisUtil.getSqlSession();
        String statement = "executor.com.tt.mapper.UserMapper.queryColumnList";//映射sql的标识字符串

        List<Map> head= session.selectList(statement,param);

        for(int i=0;i<head.size();i++){
            if(!head.get(i).get("column_name").equals("id")){
                System.out.println(head.get(i).get("column_comment"));
                list.add(Collections.singletonList(head.get(i).get("column_comment").toString()));
            }

        }
        System.out.println(list);

        return list;
    }

    private List<List<Object>> dataList() {
        List<List<Object>> list = new ArrayList<List<Object>>();
        return list;
    }
}
