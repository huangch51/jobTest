//package executor.com.tt.util;
//
//import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author huangcaihuan
// * @Since 2020/9/1
// */
//public class test {
//
//    /**
//     *
//     * @param response
//     * @param fileName  文件名称
//     * @param headArray 文件头
//     * @param dataList  数据list
//     */
//    public static void download(HttpServletResponse response, String fileName, String[] headArray, List<Map<String, Object>> dataList) throws Exception {
//        try {
//            EasyExcel.write(EasyExcelUtils.outputStream(response, fileName)).head(EasyExcelUtils.head(headArray))
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet()
//                    .doWrite(dataList);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new Exception("导出失败");
//        }
//    }
//
//
//    public static List<List<String>> head(String[] array) {
//        List<List<String>> list = new ArrayList<>();
//        for (String s : array) {
//            List<String> head = new ArrayList<>();
//            head.add(s);
//            list.add(head);
//        }
//        return list;
//    }
//
//    public static List<List<Object>> dataList(List<Map<String, Object>> list, String[] listKey) throws MalformedURLException {
//        List<List<Object>> dataList = new ArrayList<List<Object>>();
//        for (Map<String, Object> map : list) {
//            List<Object> data = new ArrayList<Object>();
//            for (String s : listKey) {
//                if (map.get(s) == null) {
//                    data.add("");
//                } else {
//                    //数据格式处理 发现包含showImg字段就展示网络图片（简单的判断）
//                    //也可以根据自己的需求进行格式化操作都放在这里
//                    Object obj = map.get(s);
//                    if(s.contains("showImg")  && obj.toString().contains("http")){
//                        data.add(new URL(obj.toString()));
//                    }else {
//                        data.add(obj.toString());
//                    }
//                }
//            }
//            dataList.add(data);
//        }
//        return dataList;
//    }
//
//    public static ServletOutputStream outputStream(HttpServletResponse response, String fileName) throws IOException {
//        response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
//        //防止中文乱码
//        fileName = URLEncoder.encode(fileName, "UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//        return response.getOutputStream();
//    }
//}
