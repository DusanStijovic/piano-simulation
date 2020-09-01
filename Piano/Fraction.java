package piano;

public class Fraction {
    private int num, den;
    public  static Fraction QUARTER = new Fraction(1,4);
    public static Fraction EIGHT = new Fraction(1,8);
    public Fraction(int num, int den) {
        this.num = num;
        this.den = den;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return num == fraction.num &&
                den == fraction.den;
    }
	public int den() {
		return den;
	}

}
