package executor.com.tt.one;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * @author huangcaihuan
 * @Since 2020/8/28
 */
@Intercepts(    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class}))
public class ClickhouseInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args=invocation.getArgs();
        MappedStatement mappedStatement= (MappedStatement) invocation.getArgs()[0];
        Object parameterObject = args[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        String sql=boundSql.getSql();
        System.out.println("截取sql："+sql);
//        SqlCommandType sqlCommandType=mappedStatement.getSqlCommandType();
//        System.out.println(sqlCommandType);
//
//        List<ResultMap> users= mappedStatement.getResultMaps();
//        System.out.println(users);


        BoundBerry boundBerry= (BoundBerry) invocation.getArgs()[1];
        boundBerry.setLimit(100);
        boundBerry.setOffset(0);


        System.out.println(boundBerry.getLimit());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}