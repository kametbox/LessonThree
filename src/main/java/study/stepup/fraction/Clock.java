package study.stepup.fraction;

public class Clock implements TimeChecker{
    @Override
    public long curTimeMill() {
        return System.currentTimeMillis();
    }
}
