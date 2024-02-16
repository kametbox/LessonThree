package study.stepup.fraction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class CacheInvocationHandler implements InvocationHandler, Runnable {

    private Object object;
    private ConcurrentHashMap<Integer,ObjectWithMethodResult> cacheTableObject;
    private long cacheTimeOut = 0;
    private boolean needClearCache = false;

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

                    if (objFromCache.getObject().equals(object) && paramsWithMethod.equals(objFromCache.getParamsWithMethod())) {
                        cacheTimeOut = method.getAnnotation(Cache.class).timeout();

                        if (System.currentTimeMillis() - objFromCache.getLastUse() < cacheTimeOut) {
                            objFromCache.setLastUse(System.currentTimeMillis());
                            return objFromCache.getMethodResult();

                        } else {
                            needClearCache = true;
                            //cacheTableObject.remove(objAndMethodHashCode);
                        }
                    }
                }
            }
            var methodResult = method.invoke(object, args);
            cacheTableObject.put(objAndMethodHashCode, new ObjectWithMethodResult(object, methodResult, System.currentTimeMillis(), paramsWithMethod));

            return methodResult;
        }
        if (method.isAnnotationPresent(Mutator.class)) {
            cacheTableObject.clear();
        }
        return method.invoke(object, args);
    }

    @Override
    public void run() {
        long lastStart = System.currentTimeMillis();

        do {
            if(!Thread.interrupted()){
                if (needClearCache || System.currentTimeMillis() - lastStart > cacheTimeOut) {
                    clearCache();
                    lastStart = System.currentTimeMillis();
                }
            } else {
                return;
            }

            try{
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
        while (true);
    }
    public void clearCache() {
        cacheTableObject.forEach((k, v) -> {
                    if (System.currentTimeMillis() - v.getLastUse() < cacheTimeOut) {
                        cacheTableObject.remove(k);
                    }
                }
        );
        needClearCache = false;
    }
}