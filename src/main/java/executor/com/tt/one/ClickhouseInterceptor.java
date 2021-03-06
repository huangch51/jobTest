package executor.com.tt.one;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author huangcaihuan
 * @Since 2020/8/28
 */
@Intercepts(    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class}))
public class ClickhouseInterceptor implements Interceptor {

    @Value("${spring.kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args=invocation.getArgs();
        MappedStatement mappedStatement= (MappedStatement) invocation.getArgs()[0];
        Object parameterObject = args[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        String sql=boundSql.getSql();
        System.out.println("截取sql："+sql);
        pushSqlToBloodLineSystem(sql,"mysql" ,"select");
//        SqlCommandType sqlCommandType=mappedStatement.getSqlCommandType();
//        System.out.println(sqlCommandType);
//
//        List<ResultMap> users= mappedStatement.getResultMaps();
//        System.out.println(users);


//        BoundBerry boundBerry= (BoundBerry) invocation.getArgs()[1];
//        boundBerry.setLimit(100);
//        boundBerry.setOffset(0);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private void pushSqlToBloodLineSystem(String sql, String dialectType,String queryType){
        // TODO: 2020-9-16 写到Kafka
        JSONObject msg = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataTime=df.format(new Date());
        msg.put("sql", sql);
        msg.put("eventTime",dataTime);
        msg.put("dialectType",dialectType);
        msg.put("queryType",queryType);
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, msg.toJSONString());
        kafkaTemplate.send(record);
        System.out.println("msg : "+msg);
    }

}