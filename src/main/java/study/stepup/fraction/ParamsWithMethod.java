package study.stepup.fraction;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class ParamsWithMethod {
    private Method method;
    private Object[] params;


    public ParamsWithMethod( Method method, Object[] params) {
        this.method = method;
        this.params = params;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ParamsWithMethod that)) return false;

        if (!Objects.equals(method, that.method)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        int result = method != null ? method.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }
}
