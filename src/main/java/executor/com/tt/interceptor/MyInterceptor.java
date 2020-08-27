//package executor.com.tt.interceptor;
//
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.plugin.*;
//import org.springframework.stereotype.Component;
//
//import java.sql.Connection;
//import java.util.Properties;
//
//@Intercepts({
//        @Signature(type = StatementHandler.class ,method = "prepare",args = {Connection.class,Integer.class}),
//        //@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
//})
//@Component
//public class MyInterceptor implements Interceptor {
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        Object target=invocation.getTarget();
//        StatementHandler statementHandler= (StatementHandler) target;
//        BoundSql boundSql=statementHandler.getBoundSql();
//        String sql=boundSql.getSql();
//        System.out.println("two---"+sql);
//
//
//        //判断sql是否有where条件。改变sql。
//        boolean status=sql.toLowerCase().contains("where");
//        Object parameterObject = boundSql.getParameterMappings();
//        System.out.println(status);
//        return invocation.proceed();
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        if(target instanceof StatementHandler){
//            return Plugin.wrap(target,this);
//        }else {
//            return target;
//        }
//
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//        String po1= properties.getProperty("prop1");
//        System.out.println("two-prop1::"+po1);
//        System.out.println("add");
//
//    }
//}
