package executor.com.tt.base;


import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Properties;
@Component
@Intercepts( {
        @Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class,Integer.class})
//        @Signature(method = "query", type = Executor.class,  args = {MappedStatement.class, Object.class,
//                RowBounds.class, ResultHandler.class})
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

//        MappedStatement mappedStatement= (MappedStatement) target;
//        List<ResultMap> users= mappedStatement.getResultMaps();
//        System.out.println(users);

        Object result = invocation.proceed();
        System.out.println("Invocation.proceed()");
        System.out.println("Invocation.proceed()");
        System.out.println("Invocation.proceed()");
        return result;
    }

    @Override
    public Object plugin(Object target) {
        // TODO Auto-generated method stub
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的
        // 次数
        if (target instanceof StatementHandler) {
            System.out.println("plugin...");
            return Plugin.wrap(target,this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        String prop1 = properties.getProperty("prop1");
        String prop2 = properties.getProperty("prop2");
        System.out.println(prop1 + "------" + prop2);
    }

}
