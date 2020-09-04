package executor.com.tt.util;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import executor.com.tt.base.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyExcelUtils {



    /**
     *
     * @param response
     * @param fileName  文件名称
     * @param tableName 表名
     *
     */
    public static void download(HttpServletResponse response, String fileName, String tableName) throws Exception {
        try {

            EasyExcel.write(EasyExcelUtils.outputStream(response, fileName)).head(EasyExcelUtils.head(tableName))
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("下载失败");
        }
    }


    public static List<List<String>> head(String tableName) {
        List<List<String>> list = new ArrayList<>();
        Map param =new HashMap();
        param.put("table_name",tableName);
        SqlSession session = MyBatisUtil.getSqlSession();
        String statement = "executor.com.tt.mapper.UserMapper.queryColumn";//映射sql的标识字符串

        List<String> head= session.selectList(statement,param);
        System.out.println(head.get(1));
        list.add(head);
        return list;
    }



    public static ServletOutputStream outputStream(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //防止中文乱码
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        return response.getOutputStream();
    }

    public static void main(String[] args) {
        List<List<String>> one= head("dim_gift_info_map");
        System.out.println(one);
    }



    @Test
    public void variableTitleWrite() {

        //路径
        String fileName = "1.xlsx";

        List<List<String>> one= head("dim_gift_info_map");
        System.out.println(one);

        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName).head(one).sheet();

    }



}