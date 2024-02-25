package study.stepup.fraction;

import java.lang.reflect.Proxy;

public class Utils {
    public static <T> T cache(T tClassElement, TimeChecker timeChecker){
        ClassLoader tClassLoader = tClassElement.getClass().getClassLoader();
        Class[] interfaces = tClassElement.getClass().getInterfaces();

        var CacheInvocationHandler = new CacheInvocationHandler(tClassElement, timeChecker);

        Thread thread = new Thread(CacheInvocationHandler, "Thread of CacheInvocationHandler");
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

        return (T) Proxy.newProxyInstance(tClassLoader, interfaces, CacheInvocationHandler);
    }
    public static <T> T cache(T tClassElement){
        return cache(tClassElement, new Clock());
    }
}
