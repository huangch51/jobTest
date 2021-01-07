//package executor.com.tt.fianl;
//
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFFont;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.HorizontalAlignment;
//import org.apache.poi.ss.usermodel.VerticalAlignment;
//
//import java.io.File;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.URLEncoder;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author huangcaihuan
// * @Since 2020/12/2
// */
//public class fianl {
//}
//
//
//
//
//
//
//
//
//package com.quwan.modules.userprofile.util;
//
//        import org.apache.commons.io.FileUtils;
//        import org.apache.poi.hssf.usermodel.*;
//        import org.apache.poi.ss.usermodel.*;
//        import org.springframework.web.context.request.RequestContextHolder;
//        import org.springframework.web.context.request.ServletRequestAttributes;
//
//        import javax.servlet.http.HttpServletResponse;
//        import java.io.*;
//        import java.lang.reflect.InvocationTargetException;
//        import java.lang.reflect.Method;
//        import java.math.BigDecimal;
//        import java.math.BigInteger;
//        import java.net.URLEncoder;
//        import java.util.*;
//
//
//public class ExcelUtil {
//
//    //标题
//    private String title;
//
//    //下载数据
//    private List dataList;
//
//    // 字段列表
//    private List<String> infoList;
//
//
//    HttpServletResponse response;
//
//
//    public ExcelUtil(){
//
//    }
//
//    public ExcelUtil(String title, List dataList,List<String> infoList) {
//        this.title=title;
//        this.dataList=dataList;
//        this.infoList=infoList;
//    }
//
//    /**
//     * 下载excel
//     * @throws Exception
//     */
//    public void uploadExcel() throws Exception{
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet();
//
//
//        // 产生表格标题行
//        HSSFCellStyle columnTopStyle = (HSSFCellStyle) this.getColumnTopStyle(workbook);
//        HSSFCellStyle columnDataStyle = (HSSFCellStyle)this.getColumnDataStyle(workbook);
//
//        HSSFRow rowRowName = sheet.createRow(0);
//
//        // 将列头设置到sheet的单元格中
//        int cellNum = 0;
//        for (String rowName : infoList) {
//            HSSFCell cellRowName = rowRowName.createCell(cellNum++);
//            HSSFRichTextString text = new HSSFRichTextString(rowName);
//            cellRowName.setCellValue(text);
//            cellRowName.setCellStyle(columnTopStyle);                        //设置列头单元格样式
//        }
//
//        int index=0;
//        HSSFCell cell= null;
//        HSSFRow row=null;
//
//        //将查询出的数据设置到sheet对应的单元格中
//        for (int i = 0; i < dataList.size(); i++) {
//            if ((i+ 1) % 65535 == 0) {
//                sheet=workbook.createSheet();
//                index++;
//            }
//            row = sheet.createRow((i + 1) - (index * 65535));
//            Object obj = dataList.get(i);
//            int j = 0;
//            sheet.setColumnWidth(i, (dataList.get(i).toString()).getBytes().length * 2 * 256);
//            for (String column : infoList) {
//                Object value = obj instanceof Map ? ((Map) obj).get(column) : getMethodValue(obj, column);
//                cell = row.createCell(j);
//                cell.setCellStyle(columnDataStyle);
//                putCell(cell,value);
////                sheet.setColumnWidth(i, (dataList.get(i).toString()).getBytes().length * 2 * 256);
//                j++;
//            }
//        }
//
//        try {
//            String fileName = URLEncoder.encode( title+ "-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls", "UTF-8");
//            File file = new File(fileName);
//            if(!file.exists()){
//                file.createNewFile();
//            }
//            //将excel写入
//            FileOutputStream stream = FileUtils.openOutputStream(file);
//            workbook.write(stream);
//            stream.flush();
//            stream.close();
////            if (workbook != null) {
////                try {
////                    String fileName = URLEncoder.encode(title + "-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls", "UTF-8");
////                    String headStr = "attachment; filename=\"" + fileName + "\"";
////                    response = getResponse();
////                    response.setContentType("APPLICATION/OCTET-STREAM");
////                    response.setHeader("Content-Disposition", headStr);
////                    OutputStream out = response.getOutputStream();
////                    workbook.write(out);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    protected void putCell(HSSFCell cell, Object value) {
//        try {
//            if (value == null) {
//                cell.setCellValue("");
//            } else if (value instanceof Integer) {
//                cell.setCellValue((Integer) value);
//            } else if (value instanceof Double) {
//                cell.setCellValue((Double) value);
//            } else if (value instanceof Float) {
//                cell.setCellValue((Float) value);
//            } else if (value instanceof Long) {
//                cell.setCellValue((Long) value);
//            } else if (value instanceof Boolean) {
//                cell.setCellValue((Boolean) value);
//            } else if (value instanceof BigInteger) {
//                cell.setCellValue(((BigInteger) value).longValue());
//            } else if (value instanceof BigDecimal) {
//                cell.setCellValue(Double.parseDouble(value.toString()));
//            }else {
//                cell.setCellValue(value.toString());
//            }
//        } catch (Exception e) {
//            cell.setCellValue("");
//            System.err.println("putCell.valueException value = " + value);
//        }
//    }
//
//
//    /**
//     * 获取response
//     **/
//    private  HttpServletResponse getResponse() {
//        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//    }
//
//
//    /**
//     * 设置标题格式
//     * @param workbook
//     * @return
//     */
//    public CellStyle getColumnTopStyle(HSSFWorkbook workbook) {
//        HSSFFont font = workbook.createFont();
//        font.setFontHeightInPoints((short) 12);
//        font.setFontName("宋体");
//        HSSFCellStyle style = workbook.createCellStyle();
//        style.setFont(font);
//        //自动换行
//        style.setWrapText(false);
//        //设置水平居中
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        return style;
//    }
//
//
//
//    /**
//     * 设置单元格格式
//     * @param workbook
//     * @return
//     */
//    public CellStyle getColumnDataStyle(HSSFWorkbook workbook) {
//        HSSFFont font = workbook.createFont();
//        font.setFontHeightInPoints((short) 12);
//        font.setFontName("宋体");
//        HSSFCellStyle style = workbook.createCellStyle();
//        style.setFont(font);
//        //自动换行
//        style.setWrapText(false);
//        //设置水平居中
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//
//        return style;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static Method getGetMethod(Class objectClass, String fieldName) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("get");
//        sb.append(fieldName.substring(0, 1).toUpperCase());
//        sb.append(fieldName.substring(1));
//        try {
//            return objectClass.getMethod(sb.toString());
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//    private static Object getMethodValue(Object obj,String fieldName){
//        try {
//            Method method = getGetMethod(obj.getClass(),fieldName);
//            if (method != null){
//                return method.invoke(obj);
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//}
















//private List<String> getExcelColumns(Class clazz) {
//        Field[] declaredFields = clazz.getDeclaredFields();
//        List<String> column = new ArrayList<>();
//        for (Field field : declaredFields) {
//        String name = field.getName();
//        column.add(name);
//        }
//        return column;
//        }