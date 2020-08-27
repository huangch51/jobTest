package executor.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserHandler implements InvocationHandler {

    private Object obj;

    public UserHandler(){

    }

    public UserHandler(Object obj){
        this.obj=obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("invoke before");
        Object invoke=method.invoke(obj,args);
        System.out.println("invoke after");
        return invoke;
    }
}
