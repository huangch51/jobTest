package executor.com.tt.power;


import org.apache.ibatis.mapping.BoundSql;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 拦截sqlsource 修改返回的boundsql
 * @author peter
 *
 */
public class SqlSourceInterceptor implements InvocationHandler{

    private Object target;

    public SqlSourceInterceptor(Object target) {
        this.target = target;
    }

    //拦截sqlSource的getBoundSql方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        BoundSql boundSql = (BoundSql)method.invoke(target, args);
        return DataAuthorInterceptor.modifyBoundSql(boundSql);
    }

    public static <T> T wrap(Object target, Class<T> clazz) {
        if(!Proxy.isProxyClass(target.getClass())) {
            Class<?> type = target.getClass();
            return (T)Proxy.newProxyInstance(type.getClassLoader(), type.getInterfaces(), new SqlSourceInterceptor(target));
        }
        return (T)target;
    }

}

