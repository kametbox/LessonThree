import org.junit.jupiter.api.Test;
import study.stepup.fraction.Fraction;
import study.stepup.fraction.Fractionable;
import study.stepup.fraction.Utils;


import static org.junit.jupiter.api.Assertions.*;

public class Lesson3Test {
    @Test
    public void createFractionWithDenumZero() {
        assertThrows(IllegalArgumentException.class, () -> new Fraction(1,0));
    }
    @Test
    public void createFractionNormaly() {
        var x = new Fraction(1,2);
        assertEquals((double) 1/2,x.doubleValue());
    }

    @Test
    public void runProxyFractWithSetNum() {
        Fraction fr = new Fraction(4,2);
        Fractionable proxyNum = Utils.cache(fr);

        proxyNum.doubleValue(); //sout сработал

        proxyNum.setNum(5);
        proxyNum.doubleValue(); //sout сработал
        Double d = proxyNum.doubleValue(); //sout молчит

        assertEquals((double) 5/2, d);
    }
    @Test
    public void runProxyFractWithSetDenum() {
        Fraction fr = new Fraction(6,2);
        Fractionable proxyNum = Utils.cache(fr);

        proxyNum.doubleValue(); //sout сработал
        proxyNum.doubleValue(); //sout сработал
        proxyNum.doubleValue(); //sout молчит

        proxyNum.setDenum(5);
        Double d = proxyNum.doubleValue(); //sout сработал

        assertEquals((double) 6/5, d);
    }
    @Test
    public void runProxyTestFractWithoutMutator() {
        TestFraction tFr = new TestFraction(2,3);
        Fractionable proxyNum = Utils.cache(tFr);

        proxyNum.doubleValue(); //sout сработал
        proxyNum.doubleValue(); //sout молчит
        Double d = proxyNum.doubleValue(); //sout молчит

        assertEquals((double) 2/3, d);
        assertEquals(1, tFr.getCnt());
    }
    @Test
    public void runProxyTestFractWithSetNum() {
        TestFraction tFr = new TestFraction(4,2);
        Fractionable proxyNum = Utils.cache(tFr);

        proxyNum.doubleValue(); //sout сработал

        proxyNum.setNum(5);
        proxyNum.doubleValue(); //sout сработал
        Double d = proxyNum.doubleValue(); //sout молчит

        assertEquals((double) 5/2, d);
        assertEquals(2, tFr.getCnt());
    }
    @Test
    public void runProxyTestFractWithSetDenum() {
        TestFraction tFr = new TestFraction(6,2);
        Fractionable proxyNum = Utils.cache(tFr);

        proxyNum.doubleValue(); //sout сработал
        proxyNum.doubleValue(); //sout сработал
        proxyNum.doubleValue(); //sout молчит

        proxyNum.setDenum(5);
        Double d = proxyNum.doubleValue(); //sout сработал

        assertEquals((double) 6/5, d);
        assertEquals(2, tFr.getCnt());
    }

    @Test
    public void runProxyTestFractWithDelay() {
        TestFraction tFr = new TestFraction(2,3);
        TestClock testClock = new TestClock(1L);
        Fractionable proxyNum = Utils.cache(tFr,testClock);

        System.out.print("1"); proxyNum.doubleValue();// sout сработал
        System.out.print("2"); proxyNum.doubleValue();// sout молчит

        proxyNum.setNum(5);
        System.out.print("3"); proxyNum.doubleValue();// sout сработал
        System.out.print("4"); proxyNum.doubleValue();// sout молчит

        proxyNum.setNum(2);
        System.out.print("5"); proxyNum.doubleValue();// sout молчит
        System.out.print("6"); proxyNum.doubleValue();// sout молчит

        //Thread.sleep(1500);
        testClock.setTime(1500L);
        System.out.print("7"); proxyNum.doubleValue();// sout сработал
        System.out.print("8"); var d = proxyNum.doubleValue();// sout молчит

        assertEquals((double) 2/3, d);
        assertEquals(3, tFr.getCnt());
    }
}


