package cn.nineton.onetake.media;

public class Ratio {
    int den;
    int num;

    public Ratio(int num, int den) {
        this.num = num;
        this.den = den;
    }

    public int getDen() {
        return den;
    }

    public void setDen(int den) {
        this.den = den;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean equals(Ratio r) {
        return r.num == this.num && r.den == this.den;
    }

    public Ratio clone() {
        return new Ratio(this.num, this.den);
    }

    public Ratio inverse() {
        return new Ratio(this.den, this.num);
    }

    public boolean isOne() {
        return this.num == this.den;
    }

    public long multiply(long value) {
        return (((long) this.num) * value) / ((long) this.den);
    }

    public long multiplyInverse(long value) {
        return (((long) this.den) * value) / ((long) this.num);
    }

    double asDouble() {
        return ((double) this.num) / ((double) this.den);
    }

    public String toString() {
        return String.format("%d/%d", new Object[]{Integer.valueOf(this.num), Integer.valueOf(this.den)});
    }
}