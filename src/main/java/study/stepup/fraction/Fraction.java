package study.stepup.fraction;

public class Fraction implements Fractionable{
    private int num;
    private int denum;
    public  Fraction(int num, int denum){
        if (denum == 0) throw new IllegalArgumentException("Знаменатель не может быть равен нулю");
        this.num = num;
        this.denum = denum;
    }
    @Override
    public void setNum(int num) {
        this.num = num;
    }
    @Override
    public void setDenum(int denum) {
        this.denum = denum;
    }
    @Override
    public double doubleValue(){
        //System.out.println("invoke double value");
        return (double) num/denum;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Fraction fraction)) return false;

        if (num != fraction.num) return false;
        return denum == fraction.denum;
    }

    @Override
    public int hashCode() {
        int result = num;
        result = 31 * result + denum;
        return result;
    }
}
