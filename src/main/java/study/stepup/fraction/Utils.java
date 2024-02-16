package study.stepup.fraction;

import java.lang.reflect.Proxy;

public class Utils {
    public static <T> T cache(T tClassElement){
        ClassLoader tClassLoader = tClassElement.getClass().getClassLoader();
        Class[] interfaces = tClassElement.getClass().getInterfaces();
        return (T) Proxy.newProxyInstance(tClassLoader, interfaces, new CacheInvocationHandler(tClassElement));
    }
}
