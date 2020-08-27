package executor.tt;

import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.List;

/**
 * @author wb
 */
public class JobConstantConf {
    public static final String ODS_DATA_BASE = "default";
    public static final String DWD_DATA_BASE = "default";
    public static final String DW_DATA_BASE = "dw";
    public static final String ADS_DATA_BASE = "ads";
    public static final String BACK_DATA_BASE = "backup";

    public static final String STANDARD_ROOM_PREFIX = "";

    public static final String APPID_ONLINE_DATE="2019-11-14";
    public static final String APPID_ONLINE_WEEK=new LocalDate("2019-11-01").withDayOfWeek(1).toString();
    public static final String APPID_ONLINE_MONTH=new LocalDate(APPID_ONLINE_DATE).withDayOfMonth(1).toString();

    public static final Integer SPLIT_DATA_LENGTH=10000;
    /**
     * 留存百分比小数点
     */
    public static final Integer RETAIN_DECIMAL_P = 1;
    /**
     *LTV百分比小数点
     */
    public static final Integer LTV_DECIMAL_P = 2;
    /**
     * 消费百分比小数点
     */
    public static final Integer CON_DECIMAL_P = 2;

    /**全局小数点位数*/
    public static final Integer GLOBAL_DECIMAL_P = 2;

    public static final List<Integer> DAY_RETAIN = Arrays.asList(1,2,3,4,5,6,7,8,9,10,15,30);

    public static final List<Integer> WEEK_RETAIN = Arrays.asList(1,2,3,4,5,6,7,8,9);

    public static final List<Integer> MONTH_RETAIN = Arrays.asList(1,2,3,4,5,6,7,8);

    public static final String BASE_DEVICE_TABLE = "dwd_app_device_final_all";

    public static final String BASE_UID_TABLE = "dwd_tt_uid_day_all";

    public static final String TYPE_D = "d";
    public static final String TYPE_W = "w";
    public static final String TYPE_M = "m";

    public static final int LIMIT=1000000000;

}

