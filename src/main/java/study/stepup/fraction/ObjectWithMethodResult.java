package study.stepup.fraction;

public class ObjectWithMethodResult {
    private Object object;
    private Object methodResult;
    private long lastUse;

    public ObjectWithMethodResult(Object object, Object methodResult, long lastUse) {
        this.object = object;
        this.methodResult = methodResult;
        this.lastUse = lastUse;
    }

    public Object getObject() {
        return object;
    }

    public Object getMethodResult() {
        return methodResult;
    }

    public long getLastUse() {
        return lastUse;
    }

    public void setLastUse(long lastUse) {
        this.lastUse = lastUse;
    }
}
