//package com.tt.datacenter.comm.bloodline;
//
//
//import com.alibaba.fastjson.JSONObject;
//import com.tt.datacenter.configuration.ClickHouseDevEnvConfig;
//import com.tt.datacenter.configuration.KafkaSenderConfig;
//import com.tt.datacenter.dao.mysql.metadata.MetadataTableBloodMapper;
//import com.tt.datacenter.utils.SpringContextUtil;
//import com.xxl.job.core.log.XxlJobLogger;
//import lombok.extern.java.Log;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlCommandType;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.apache.kafka.clients.admin.AdminClient;
//import org.apache.kafka.clients.admin.ListTopicsResult;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.nutz.dao.util.cri.Static;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.core.KafkaAdmin;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;
//
//import javax.validation.constraints.NotNull;
//import java.io.*;
//import java.lang.reflect.Method;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//import java.util.stream.StreamSupport;
//
//@Intercepts({
//        @Signature(type = Executor.class, method = "update",
//                args = {MappedStatement.class, Object.class }),
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
//                RowBounds.class, ResultHandler.class})})
//@Log
//public class ClickHouseBloodLineInterceptor implements Interceptor {
//
//    private static final int MAPPED_STATEMENT_INDEX = 0;
//    private static final int PARAM_OBJ_INDEX = 1;
//
//    private static final String PUSH_SQL_FILE_NAME= "/data/applogs/xxl-job/datacenter-job/push-sql.txt";
//
//    private static final String  QUERY_TYPE = "insert_select";
//
//    private static final String  MYSQL_DIALECT_TYPE = "mysql";
//
//    private static final String  CLICKHOUSE_DIALECT_TYPE = "clickhouse";
//
//    private static Logger logger = LoggerFactory.getLogger(ClickHouseBloodLineInterceptor.class);
//
//    private static MetadataTableBloodMapper metadataTableBloodMapper;
//
//    private static ClickHouseDevEnvConfig clickHouseDevEnvConfig;
//
//    private Map<String, String> querySqlMap = new ConcurrentHashMap<>();
//
//    private static String TOPIC="daqi_lineage_sql";
//
//    private static KafkaSenderConfig kafkaSenderConfig;
//
//    public static KafkaSenderConfig getKafkaSenderConfig(){
//        if(kafkaSenderConfig==null){
//            kafkaSenderConfig = (KafkaSenderConfig) SpringContextUtil.getApplicationContext().getBean("kafkaSenderConfig");
//            return kafkaSenderConfig;
//        }else{
//            return kafkaSenderConfig;
//        }
//    }
//
//
//    public static ClickHouseDevEnvConfig getClickHouseDevEnvConfig(){
//        if(clickHouseDevEnvConfig==null){
//            clickHouseDevEnvConfig = (ClickHouseDevEnvConfig) SpringContextUtil.getApplicationContext().getBean("clickHouseDevEnvConfig");
//            return clickHouseDevEnvConfig;
//        }else{
//            return clickHouseDevEnvConfig;
//        }
//    }
//
//
//    public static MetadataTableBloodMapper getMetadataTableBloodMapper(){
//        if(metadataTableBloodMapper==null){
//            metadataTableBloodMapper = (MetadataTableBloodMapper) SpringContextUtil.getApplicationContext().getBean("metadataTableBloodMapper");
//            return metadataTableBloodMapper;
//        }else{
//            return metadataTableBloodMapper;
//        }
//    }
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[MAPPED_STATEMENT_INDEX];
//        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
////        String sql = invocationSql(invocation);
//        // 如果是ck查询SQL，并且返回值是void,那就直接入库
//        if(SqlCommandType.INSERT.equals(sqlCommandType)
//                ||SqlCommandType.SELECT.equals(sqlCommandType)
//                ||SqlCommandType.UPDATE.equals(sqlCommandType)
//        ){
//            if(mappedStatement.getId().contains("com.tt.datacenter.dao.clickhouse.mapper")
//                    ||mappedStatement.getId().contains("com.tt.datacenter.dao.clickhouse.base")) {
//                return handleClickHouseQuerySql(invocation);
//            }
//        }
//
//        return invocation.proceed();
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target,this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {}
//
//    private Object handleMysqlInsertSql(Invocation invocation) throws Exception{
//        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[MAPPED_STATEMENT_INDEX];
//        String namespace = mappedStatement.getId();
//        String className = namespace.substring(0,namespace.lastIndexOf("."));
//        String methedName= namespace.substring(namespace.lastIndexOf(".") + 1,namespace.length());
//        Method[] ms = Class.forName(className).getMethods();
//        String key = className+":"+methedName;
//        for(Method m : ms) {
//            if (m.getName().equals(methedName)) {
//                Map params = (Map) invocation.getArgs()[PARAM_OBJ_INDEX];
//                List list = (List) params.get("list");
//                if(null !=getQuerySql(key)) {
//                    pushSqlToBloodLineSystem(getQuerySql(key),MYSQL_DIALECT_TYPE);
//                } else if(list instanceof RecordDataSourceList){
//                    String queryKey = ((RecordDataSourceList) list).getQueryFromKey();
//                    String mysqlSql = invocationSql(invocation);
//                    if(!mysqlSql.startsWith("update")){
//                        mysqlSql = mysqlSql.split("values")[0];
//                        mysqlSql = mysqlSql.replaceAll("totalDate","");
//                        mysqlSql = mysqlSql.replaceAll("eventDate","");
//                        mysqlSql = mysqlSql.replaceAll("event_date","");
//                        mysqlSql = mysqlSql+getQuerySql(queryKey);
//                        putQuerySql(key,mysqlSql);
//                        pushSqlToBloodLineSystem(mysqlSql,MYSQL_DIALECT_TYPE);
//                    }
//                }
//            }
//        }
//        return invocation.proceed();
//
//    }
//    private Object handleClickHouseQuerySql(Invocation invocation) throws Exception{
//        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[MAPPED_STATEMENT_INDEX];
//        String namespace = mappedStatement.getId();
//        String className = namespace.substring(0,namespace.lastIndexOf("."));
//        String methedName= namespace.substring(namespace.lastIndexOf(".") + 1,namespace.length());
//        String key = className+":"+methedName;
//        Method[] ms = Class.forName(className).getMethods();
//        for(Method m : ms) {
//            if (m.getName().equals(methedName)) {
//                Type type = m.getGenericReturnType();
//                // 返回类型是List类型
//                String querySql = invocationSql(invocation);
//                if(type instanceof ParameterizedType){
//                    putQuerySql(key,querySql);
//                    List list = (List) invocation.proceed();
//                    RecordDataSourceList recordDataSourceList = new RecordDataSourceList();
//                    recordDataSourceList.addAll(list);
//                    recordDataSourceList.setQueryFromKey(key);
//                    return recordDataSourceList;
//                }else{
//                    //判断是否正式环境
//                    if(getClickHouseDevEnvConfig().isPushSql()) {
//                        pushSqlToBloodLineSystem(querySql,CLICKHOUSE_DIALECT_TYPE);
//                    }
//                    return invocation.proceed();
//                }
//            }
//        }
//        return invocation.proceed();
//
//    }
//
//    private void putQuerySql(String key,String sql){
//        querySqlMap.put(key,sql);
//    }
//
//    private String getQuerySql(String key){
//        return querySqlMap.get(key);
//    }
//
//    /**
//     * 推送SQL到血缘分析系统
//     * @param sql
//     * @param dialectType
//     */
//    private void pushSqlToBloodLineSystem(String sql, String dialectType) {
//        // TODO: 2020-9-16 写到Kafka
//        JSONObject msg = new JSONObject();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dataTime = df.format(new Date());
//        msg.put("sql", sql);
//        msg.put("eventTime", dataTime);
//        msg.put("dialectType", dialectType);
//        msg.put("queryType", QUERY_TYPE);
//        final ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC, msg.toJSONString());
//        KafkaTemplate<String, String> kafkaTemplate = getKafkaSenderConfig().kafkaTemplate();
//        //异步发送消息。异常时打印异常信息或发送结果
//        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
//        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
//            public void onFailure(@NotNull Throwable throwable) {
//                logger.error("sent sql-message to kafka=[{}] failed!", msg.toJSONString(), throwable);
//            }
//
//            public void onSuccess(SendResult<String, String> result) {
//                logger.info("sent sql-message to kafka=[{}] with offset=[{}] success!", msg.toJSONString(), result.getRecordMetadata());
//            }
//        });
//    }
//
//
//    private void bloodLineInfo(Invocation invo) throws Exception{
//        String sql = invocationSql(invo);
//        String targetTable = InsertSqlParser.targetTable(sql);
//        List<String> fromTableList = InsertSqlParser.fromTable(sql);
//        List<Map> list = new ArrayList<>();
//        for (String table: fromTableList) {
//            if(!targetTable.equals(table)) {
//                Map map = new HashMap();
//                map.put("database", "dw");
//                map.put("tableName", targetTable);
//                map.put("parentTable", table);
//                list.add(map);
//            }
//        }
//        if(list.size()>0) {
//            Map map = new HashMap();
//            map.put("list", list);
//            getMetadataTableBloodMapper().insertTableBlood(map);
//        }
//    }
//
//
//
//    private MappedStatement getMappedStatement(Invocation invocation) {
//        Object[] args = invocation.getArgs();
//        Object mappedStatement = args[MAPPED_STATEMENT_INDEX];
//        return (MappedStatement) mappedStatement;
//    }
//
//
//    private String invocationSql(Invocation invocation){
//        final Object[] args = invocation.getArgs();
//        MappedStatement ms = getMappedStatement(invocation);
//        Object parameterObject = args[1];
//        BoundSql boundSql = ms.getBoundSql(parameterObject);
//        return boundSql.getSql();
//    }
//
//}
