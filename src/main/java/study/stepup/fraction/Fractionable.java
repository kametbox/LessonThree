package study.stepup.fraction;

public interface Fractionable {
    @Cache(timeout = 1000)
    double doubleValue();
    //@Mutator
    void setNum(int num);
    //@Mutator
    void setDenum(int denum);
}
