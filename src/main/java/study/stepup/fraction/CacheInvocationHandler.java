package study.stepup.fraction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class CacheInvocationHandler implements InvocationHandler {

    private Object object;
    private ConcurrentHashMap<Integer,ObjectWithMethodResult> cacheTableObject;

    public CacheInvocationHandler(Object object) {
        this.object = object;
        this.cacheTableObject = new ConcurrentHashMap<>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.isAnnotationPresent(Cache.class)) {
            var paramsWithMethod = new ParamsWithMethod(method, args);
            var objAndMethodHashCode = object.hashCode() + paramsWithMethod.hashCode();

            if (!cacheTableObject.isEmpty()) {
                if (cacheTableObject.containsKey(objAndMethodHashCode)) {
                    var objFromCache = cacheTableObject.get(objAndMethodHashCode);

                    if (objFromCache.getObject().equals(object)) {
                        var cache = method.getAnnotation(Cache.class);

                        if (System.currentTimeMillis() - objFromCache.getLastUse() < cache.timeout()) {
                            objFromCache.setLastUse(System.currentTimeMillis());
                            return objFromCache.getMethodResult();

                        } else {
                            cacheTableObject.remove(objAndMethodHashCode);

                            var thread = new Thread(){
                                public void run(){
                                    cacheTableObject.forEach((k, v) -> {
                                                if (System.currentTimeMillis() - v.getLastUse() < cache.timeout()) {
                                                    cacheTableObject.remove(k);
                                                }
                                            }
                                    );
                                }
                            };
                            thread.setPriority(Thread.MIN_PRIORITY);
                            thread.start();
                        }
                    }
                }
            }
            var methodResult = method.invoke(object, args);
            cacheTableObject.put(objAndMethodHashCode, new ObjectWithMethodResult(object, methodResult, System.currentTimeMillis()));

            return methodResult;
        }
        if (method.isAnnotationPresent(Mutator.class)) {
            cacheTableObject.clear();
        }
        return method.invoke(object, args);
    }
}