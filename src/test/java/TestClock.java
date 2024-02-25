import study.stepup.fraction.TimeChecker;

public class TestClock implements TimeChecker {
    private long time;
    @Override
    public long curTimeMill() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public TestClock(long time) {
        this.time = time;
    }
}
