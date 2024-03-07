package study.stepup.fraction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class CacheInvocationHandler implements InvocationHandler, Runnable {

    private Object object;
    private ConcurrentHashMap<Integer,ObjectWithMethodResult> cacheTableObject;
    private long cacheTimeOut = 0;
    private boolean needClearCache = false;
    private TimeChecker timeChecker;

    public CacheInvocationHandler(Object object, TimeChecker timeChecker) {
        this.object = object;
        this.cacheTableObject = new ConcurrentHashMap<>();
        this.timeChecker = timeChecker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.isAnnotationPresent(Cache.class)) {
            var paramsWithMethod = new ParamsWithMethod(method, args);
            var objAndMethodHashCode = object.hashCode() + paramsWithMethod.hashCode();

            if (!cacheTableObject.isEmpty()) {
                if (cacheTableObject.containsKey(objAndMethodHashCode)) {
                    var objFromCache = cacheTableObject.get(objAndMethodHashCode);

                    if (paramsWithMethod.equals(objFromCache.getParamsWithMethod()) && objFromCache.getObject().equals(object)) {
                        cacheTimeOut = method.getAnnotation(Cache.class).timeout();

                        if (timeChecker.curTimeMill() - objFromCache.getLastUse() < cacheTimeOut) {
                            objFromCache.setLastUse(timeChecker.curTimeMill());
                            System.out.println("вывод из перехваченного рефлексией метода. расчета не было, значение взяли из кэша");
                            return objFromCache.getMethodResult();

                        } else {
                            needClearCache = true;
                        }
                    }
                }
            }
            var methodResult = method.invoke(object, args);
            cacheTableObject.put(objAndMethodHashCode, new ObjectWithMethodResult(object, methodResult, paramsWithMethod, timeChecker.curTimeMill()));

            return methodResult;
        }
        if (method.isAnnotationPresent(Mutator.class)) {
            cacheTableObject.clear();
        }
        return method.invoke(object, args);
    }

    @Override
    public void run() {
        long lastStart = timeChecker.curTimeMill();

        do {
            if(!Thread.interrupted()){
                if (needClearCache || timeChecker.curTimeMill() - lastStart > cacheTimeOut) {
                    clearCache();
                    lastStart = timeChecker.curTimeMill();
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
                    if (timeChecker.curTimeMill() - v.getLastUse() < cacheTimeOut) {
                        cacheTableObject.remove(k);
                    }
                }
        );
        needClearCache = false;
    }
}