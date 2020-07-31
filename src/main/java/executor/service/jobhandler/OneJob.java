package executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;


@Service
@JobHandler(value = "oneJobHandler")
public class OneJob extends IJobHandler {

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("XXL-JOB, run oneJOB."+s);
        System.out.println("run oneJOB"+s);
        BufferedWriter out = null;
        try {
            //FileOutputStream构造函数中的第二个参数true表示以追加形式写文件

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }
}
