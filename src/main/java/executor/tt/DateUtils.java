package executor.tt;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_DATET = "yyyy-MM-dd";
    public static String FORMAT_MONTH = "yyyy-MM";

    public static String FORMAT_DATE_DT = "yyyy-MM-dd 00:00:00";

    public static String DB_FORMAT_DATE = "%Y-%m-%d";
    public static String DB_FORMAT_WEEK = "%V";
    public static String DB_FORMAT_MONTH = "%Y-%m-01";

    public static final String DATE_TYPE_DAY = "d";
    public static final String DATE_TYPE_WEEK = "w";
    public static final String DATE_TYPE_MONTH = "m";

    public static String today() {
        return new LocalDateTime().toString(FORMAT_DATET);
    }

    public static boolean isDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATET);
        try {
            sdf.setLenient(false);
            sdf.parse(str);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String addDay(String date, int n) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATET);
        try {
            sdf.setLenient(false);
            Date d = sdf.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DAY_OF_MONTH, n);
            return sdf.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String getYearOfWeek(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setMinimalDaysInFirstWeek(4);
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.setTime(dateFormat.parse(date));
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            String monday = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DATE, 6);
            String sunday = dateFormat.format(calendar.getTime());
            return monday + ":" + sunday;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String getDayOfMonth(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(date));
            int ld = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int sd = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
            DateFormat df = new SimpleDateFormat("yyyy-MM");
            String month = df.format(calendar.getTime());
            return month + "-" + sd + ":" + month + "-" + ld;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String getMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_MONTH);
        try {
            return sdf.format(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String addMonth(String date, int n) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_MONTH);
        try {
            sdf.setLenient(false);
            Date d = sdf.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.MONTH, n);
            return sdf.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static int currentHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取今年一周中的周一日期
     *
     * @param year 年份
     * @param weekOfYear 一年中的周
     * @return
     */
    public static String getMonday(int year, int weekOfYear) {
        return new LocalDate().withYear(year).withWeekOfWeekyear(weekOfYear).withDayOfWeek(1).toString();
    }


    public static void main(String[] args) {
        System.out.println(new LocalDate().plusDays(-1));
    }

}
