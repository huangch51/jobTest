package executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * XxlJob开发示例（Bean模式）
 * <p>
 * 开发步骤：
 * 1、在Spring Bean实例中，开发Job方法，方式格式要求为 "public ReturnT<String> execute(String param)"
 * 2、为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 3、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 */
@Component
public class SampleXxlJob {

    /**
     * 1、简单任务示例（Bean模式）
     */
    @JobHandler(value = "demoJobHandler")
    @Component
    public class DemoJobHandler extends IJobHandler {
        @Override
        public ReturnT<String> execute(String s) throws Exception {
            XxlJobLogger.log("XXL-JOB, Hello World.");

            for (int i = 0; i < 5; i++) {
                XxlJobLogger.log("beat at:" + i);
                TimeUnit.SECONDS.sleep(2);
            }
            return SUCCESS;
        }
    }


    @Service
    @JobHandler(value = "two")
    public class twoJobHandler extends IJobHandler{

        @Override
        public ReturnT<String> execute(String s) throws Exception {
            XxlJobLogger.log("xxx-two");
            System.out.println("XXL-JOB-TWO");
            return SUCCESS;
        }
    }
}
