package executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@JobHandler(value = "pushSql")
public class PushSqlHandler extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) throws Exception {

        try {
            File file =new File("d://push.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter=new FileWriter(file,true);
            fileWriter.write(s);
            fileWriter.close();

            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));

        }catch (IOException e){
            e.printStackTrace();
        }

        return SUCCESS;
    }
}
