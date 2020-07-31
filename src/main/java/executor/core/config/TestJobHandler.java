package executor.core.config;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@JobHandler(value = "testJobHandler")
public class TestJobHandler extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) {

        Map hashMap = JSON.parseObject(s, HashMap.class);

        System.out.println("testHandler："+hashMap);

        //String str1=JSON.toJSONString(s);
        //System.out.println(str1);

        System.out.println(hashMap.get("name").toString());
        System.out.println(hashMap.get("date").toString());
        hashMap.put("k","r");
        System.out.println("twoHandler："+hashMap);
        return SUCCESS;
    }
}
