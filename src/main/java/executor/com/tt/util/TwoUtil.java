package executor.com.tt.util;

import com.alibaba.excel.EasyExcel;
import executor.com.tt.base.MyBatisUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangcaihuan
 * @Since 2020/9/1
 */
public class TwoUtil {

    /**
     * 导出 Excel ：一个 sheet，带表头
     * 自定义WriterHandler 可以定制行列数据进行灵活化操作
     *
     * @param response HttpServletResponse
     * @param list     数据 list
     * @param fileName 导出的文件名
     */
    public static <T> void writeExcel( List<T> list,
                                      String fileName) throws  Exception {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        EasyExcel.write(fileName).head((List<List<String>>) list).sheet();
    }



    public static void main(String[] args) throws Exception {

        String modelFileName = "accountList.xlsx";
        Map param =new HashMap();
        param.put("table_name","dim_gift_info_map");
        SqlSession session = MyBatisUtil.getSqlSession();
        String statement = "executor.com.tt.mapper.UserMapper.queryColumn";//映射sql的标识字符串

        List<String> head= session.selectList(statement,param);
        head.remove(0);
        List<List<String>> list= Collections.singletonList(head);
        HttpServletResponse response =null;
        writeExcel(list,modelFileName);
    }

}
