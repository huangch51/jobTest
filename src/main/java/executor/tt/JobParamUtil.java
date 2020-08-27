package executor.tt;


import org.joda.time.LocalDate;

import java.util.*;

public class JobParamUtil {

    public static Map<String, Object> parseParam(String s) {
        Map<String, Object> param = new HashMap<>();
        String today = DateUtils.today();
        String date = DateUtils.addDay(today, -1);
        if (s.equals("") || s == null) {
            param.put("start", date);
            param.put("end", date);
            param.put("types",Arrays.asList("d","w","m"));
        } else if (s.length() > 0) {
            String[] strs = s.split(" ");
            String start = strs.length > 0 ? strs[0] : date;
            String end = strs.length >= 2 ? strs[1] : date;
            if (DateUtils.isDate(start) && DateUtils.isDate(end)) {
                param.put("start", start);
                param.put("end", end);
            } else {
                param.put("start", date);
                param.put("end", date);
            }
            if(strs.length>2){
                List list = new ArrayList();
                for (int i = 2; i < strs.length ; i++) {
                    list.add(strs[i]);
                }
                param.put("types",list);
            } else {
                param.put("types",Arrays.asList("d","w","m"));
            }
        }
        return param;
    }
    public static Map structParam(Map param,String date,String type){
        param.put("type",type);
        switch (type){
            case "d":
                param.put("date",date);
                param.put("start",date);
                param.put("end",date);
                break;
            case "w":
                String[] weeks = DateUtils.getYearOfWeek(date).split(":");
                param.put("date",weeks[0]);
                param.put("start",weeks[0]);
                param.put("end",endDate(weeks[1]));
                break;
            case "m":
                String month = DateUtils.getMonth(date);
                String lastDayOfMonth = DateUtils.addDay(DateUtils.addMonth(month,1)+"-01",-1);
                param.put("date",month);
                param.put("start",month+"-01");
                param.put("end",endDate(lastDayOfMonth));
                break;
            default:
                break;
        }
        return param;
    }
    public static Map structRetainParam(Map param,String date,String type){
        param.put("type",type);
        switch (type){
            case "d":
                param.put("date",date);
                param.put("retainStart",date);
                param.put("retainEnd",date);
                break;
            case "w":
                String[] weeks = DateUtils.getYearOfWeek(date).split(":");
                param.put("date",weeks[0]);
                param.put("retainStart",weeks[0]);
                param.put("retainEnd",endDate(weeks[1]));
                break;
            case "m":
                String month = DateUtils.getMonth(date);
                String lastDayOfMonth = DateUtils.addDay(DateUtils.addMonth(month,1)+"-01",-1);
                param.put("date",month);
                param.put("retainStart",month+"-01");
                param.put("retainEnd",endDate(lastDayOfMonth));
                break;
            default:
                break;
        }
        return param;
    }
    public static Map structRetainParam(Map param,String date,String type,int re){
        param.put("type",type);
        switch (type){
            case "d":
                date = new LocalDate(date).plusDays(re).toString();
                param.put("date",date);
                param.put("retainStart",date);
                param.put("retainEnd",date);
                break;
            case "w":
                date = new LocalDate(date).plusWeeks(re).toString();
                String[] weeks = DateUtils.getYearOfWeek(date).split(":");
                param.put("date",weeks[0]);
                param.put("retainStart",weeks[0]);
                param.put("retainEnd",endDate(weeks[1]));
                break;
            case "m":
                date = new LocalDate(date).plusMonths(re).toString();
                String month = DateUtils.getMonth(date);
                String lastDayOfMonth = DateUtils.addDay(DateUtils.addMonth(month,1)+"-01",-1);
                param.put("date",month);
                param.put("retainStart",month+"-01");
                param.put("retainEnd",endDate(lastDayOfMonth));
                break;
            default:
                break;
        }
        return param;
    }
    public static String endDate(String date){
        String yesterday = new LocalDate().plusDays(-1).toString();
        if (date.compareTo(yesterday)<=0){
            return date;
        }else {
            return yesterday;
        }
    }

    public static Map structBaseParam(Map param,String s){
        Map<String, Object> dateRange = JobParamUtil.parseParam(s);
        String start = dateRange.get("start").toString();
        String end = dateRange.get("end").toString();
        param.put("excStr",s);
        param.put("start",start);
        param.put("end",end);
        param.put("types",dateRange.get("types"));
        param.put("ods",JobConstantConf.ODS_DATA_BASE);
        param.put("dw",JobConstantConf.DW_DATA_BASE);
        param.put("dwd",JobConstantConf.DWD_DATA_BASE);
        param.put("ads",JobConstantConf.ADS_DATA_BASE);
        param.put("backup",JobConstantConf.BACK_DATA_BASE);

        param.put("device_base_table",JobConstantConf.BASE_DEVICE_TABLE);
        param.put("uid_base_table",JobConstantConf.BASE_UID_TABLE);

        param.put("RETAIN_DECIMAL_P",JobConstantConf.RETAIN_DECIMAL_P);
        param.put("LTV_DECIMAL_P",JobConstantConf.LTV_DECIMAL_P);
        param.put("CON_DECIMAL_P",JobConstantConf.CON_DECIMAL_P);
        param.put("GLOBAL_DECIMAL_P",JobConstantConf.GLOBAL_DECIMAL_P);

        param.put("APPID_ONLINE_DAY",JobConstantConf.APPID_ONLINE_DATE);
        param.put("APPID_ONLINE_WEEK",JobConstantConf.APPID_ONLINE_WEEK);
        param.put("APPID_ONLINE_MONTH",JobConstantConf.APPID_ONLINE_MONTH);

        param.put("STANDARD_ROOM_PREFIX",JobConstantConf.STANDARD_ROOM_PREFIX);

        param.put("LIMIT",JobConstantConf.LIMIT);
        return param;
    }

    public static Map structRetainParam(Map param){
        param.put("DAY_RETAIN",JobConstantConf.DAY_RETAIN);
        param.put("WEEK_RETAIN",JobConstantConf.WEEK_RETAIN);
        param.put("MONTH_RETAIN",JobConstantConf.MONTH_RETAIN);
        return param;
    }

}
