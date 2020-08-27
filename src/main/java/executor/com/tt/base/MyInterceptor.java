package executor.com.tt.base;


import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Properties;
@Component
@Intercepts( {
       @Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class,Integer.class}),
        //@Signature(type = Executor.class, method = "update",
         //       args = {MappedStatement.class, Object.class }),
})
public class MyInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        //通过invocation获取代理的目标对象
        Object target=invocation.getTarget();
        //通过java反射获得mappedStatement属性值
        //可以获得mybatis里的resultype
        StatementHandler statementHandler= (StatementHandler) target;
        BoundSql boundSql=statementHandler.getBoundSql();
        String sql=boundSql.getSql();
        System.out.println("interceptor:"+sql);

         Method method = invocation.getMethod();
         Object[] args = invocation.getArgs();
//        MappedStatement mappedStatement= (MappedStatement) target;
//        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
//        List<ResultMap> users= mappedStatement.getResultMaps();
//        System.out.println("List"+users);


//        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
//        Object parameter = invocation.getArgs()[1];
//        BoundSql boundSql1 = mappedStatement.getBoundSql(parameter);
//        String oldsql = boundSql1.getSql();
//        System.out.println("old:"+oldsql);


        System.out.println(target+" "+method+" "+args);
        Object result = invocation.proceed();
        System.out.println("Invocation.proceed()");
        return result;
    }

    @Override
    public Object plugin(Object target) {
        // TODO Auto-generated method stub
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的
        // 次数
        return Plugin.wrap(target,this);
//        if (target instanceof StatementHandler) {
//            System.out.println("plugin...");
//            return Plugin.wrap(target,this);
//        } else {
//            return target;
//        }
    }

    @Override
    public void setProperties(Properties properties) {
        String prop1 = properties.getProperty("prop1");
        String prop2 = properties.getProperty("prop2");
        System.out.println(prop1 + "------" + prop2);
    }

}
