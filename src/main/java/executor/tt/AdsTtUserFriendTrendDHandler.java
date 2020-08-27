package executor.tt;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import executor.com.tt.mapper.UserMapper;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@JobHandler(value = "adsTtUserFriendTrendDHandler")
public class AdsTtUserFriendTrendDHandler extends IJobHandler {


    //private int[] days = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 29};

    private List<Integer> days = Arrays.asList(1, 2, 3);
    //2020-08-05   2020-08-07

    @Autowired
    private UserMapper userMapper;

    @Override
    public ReturnT<String> execute(String s) throws Exception {

        Map<String, Object> dateRange = JobParamUtil.parseParam(s);
        String start = dateRange.get("start").toString();
       String day1= new LocalDate(dateRange.get("start").toString()).plusWeeks(-2).withDayOfWeek(1).toString();
       String day2=new LocalDate(dateRange.get("start").toString()).plusWeeks(-2).withDayOfWeek(6).toString();
        System.out.println(start+"day1:"+day1+"day2:"+day2);
        return ReturnT.SUCCESS;
//        String end = dateRange.get("end").toString();
//
//        //System.out.println("日期：：："+start+end);
//
//        Map params = new HashMap();
//        params.put("end", end);
//        params.put("start", start);
//
//        while (start.compareTo(end) <= 0) {
//            for (int d : days) {
//                String r_start = new LocalDate(start).plusDays(-d + 1).toString();
//                params.put("r_start", r_start);
//                params.put("r_end", r_start);
//                params.put("column", "day" + d);
//                this.runJob(params);
//            }
//            start = new LocalDate(start).plusDays(1).toString();
//            params.put("start", start);
////          System.out.println("start Date：：" + start);
//        }
//        return ReturnT.SUCCESS;
    }


     private void runJob(Map params) throws InterruptedException {
         System.out.println(params.toString());
         userMapper.selectList();
         System.out.println("ssssssss");
     }

}