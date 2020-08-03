package executor.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {

    private Object obj;

    public MyInvocationHandler() {
        // TODO Auto-generated constructor stub
    }

    //构造函数，给我们的真实对象赋值
    public MyInvocationHandler(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object invoke = method.invoke(obj, args);
        //在真实的对象执行之后我们可以添加自己的操作
        System.out.println("after invoke。。。");
        System.out.println("method:"+method.getName());
        System.out.println("after invoke。。。");
        return invoke;
    }


    public void countIndex(){

    }
}
