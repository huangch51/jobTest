package executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@JobHandler(value = "twoJobHandler")
public class TwoJob extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("twoHandler");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        String dataTime=df.format(new Date());
        try {
            File file = new File("d://pushSql1.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
//            FileWriter fileWritter = new FileWriter(file,true);
//            fileWritter.write(dataTime+"delete"+"\r\n");
//            fileWritter.flush();
//            fileWritter.close();

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));
            out.write("BufferedWriterBufferedWriter");
            out.flush();
            out.close();
        }catch(IOException e){
            System.out.println("HELLO,TWOHANDLER"+s+dataTime+"sel");
            e.printStackTrace();
        }
        //System.out.println("HELLO,TWOHANDLER"+s+dataTime+"sel");
        return SUCCESS;
    }

    public void two(){
        System.out.println("could action?");
    }
}


//spring.kafka.listener.concurrency=1
//        spring.kafka.listener.ack-mode= manual_immediate
//
//        spring.kafka.consumer.bootstrap-servers=192.168.9.225:9092
//        spring.kafka.consumer.group-id=daqi-lineage-group
//        spring.kafka.consumer.auto-offset-reset=earliest
//        spring.kafka.consumer.enable-auto-commit=false
//        spring.kafka.consumer.auto-commit-interval=100
//        spring.kafka.topic=daqi_lineage_sql