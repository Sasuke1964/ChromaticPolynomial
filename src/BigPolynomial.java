import java.math.BigInteger;

public class BigPolynomial {
    private BigInteger[] coef;  // coefficients
    private int deg;     // degree of polynomial (0 for the zero polynomial)



    /** Creates the constant polynomial P(x) = 1.
      */
    public BigPolynomial(){
        coef = new BigInteger[1];
        coef[0] = BigInteger.valueOf(1);
        deg = 0;
    }



    /** Creates the linear polynomial of the form P(x) =  x + a.
      */
    public BigPolynomial(BigInteger a){
        coef = new BigInteger[2];
        coef[1] = BigInteger.valueOf(1);
        coef[0] = a;
        deg = 1;
    }




    /** Creates the polynomial P(x) = a * x^b.
      */
    public BigPolynomial(BigInteger a, int b) {
        coef = new BigInteger[b+1];
        for (int c = 0; c < coef.length; c++) {
        	coef[c] = BigInteger.valueOf(0);
        }
        coef[b] = a;
        deg = degree();
    }





    /** Return the degree of this polynomial (0 for the constant polynomial).
      */
    public int degree() {
        int d = 0;
        for (int i = 0; i < coef.length; i++)
            if (coef[i] != BigInteger.valueOf(0)) d = i;
        return d;
    }





    /** Return the sum of this polynomial and b, i.e., return c = this + b.
      */
    public BigPolynomial plus(BigPolynomial b) {
        BigPolynomial a = this;
        BigPolynomial c = new BigPolynomial(BigInteger.valueOf(0), Math.max(a.deg, b.deg));
        
        for (int i = 0; i <= a.deg; i++) {
        	if (a.coef[i] != null) {
        		c.coef[i] = c.coef[i].add(a.coef[i]);
        	}
        	//System.out.println(c.coef[i]);
        }
        for (int i = 0; i <= b.deg; i++) {
        	if (b.coef[i] != null) {
        		c.coef[i] = c.coef[i].add(b.coef[i]);
        	}
        }
        
        c.deg = c.degree();
        return c;
    }






    /** Return the difference of this polynomial and b, i.e., return (this - b).
      */
    public BigPolynomial minus(BigPolynomial b) {
        BigPolynomial a = this;
        BigPolynomial c = new BigPolynomial(BigInteger.valueOf(1), Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) {
        	if (c.coef[i] != null) {
        		c.coef[i] = c.coef[i].add(a.coef[i]);
        	}
        }
        for (int i = 0; i <= b.deg; i++) {
        	if (c.coef[i] != null) {
        		c.coef[i] = c.coef[i].subtract(b.coef[i]);
        	}
        }
        
        c.deg = c.degree();
        return c;
    }






    /** Return the product of this polynomial and b, i.e., return (this * b).
      */
    public BigPolynomial times(BigPolynomial b) {
        BigPolynomial a = this;
        BigPolynomial c = new BigPolynomial(BigInteger.valueOf(0), a.deg + b.deg);
        for (int i = 0; i <= a.deg; i++)
            for (int j = 0; j <= b.deg; j++)
            	if (c.coef[i+j] != null && a.coef[i] != null && b.coef[j] != null) {
            		c.coef[i+j] = c.coef[i+j].add(a.coef[i].multiply(b.coef[j]));
            	}
        c.deg = c.degree();
        return c;
    }






    /** Return the composite of this polynomial and b, i.e., return this(b(x))  - compute using Horner's method.
      */
    public BigPolynomial compose(BigPolynomial b) {
        BigPolynomial a = this;
        BigPolynomial c = new BigPolynomial(BigInteger.valueOf(0), 0);
        for (int i = a.deg; i >= 0; i--) {
            BigPolynomial term = new BigPolynomial(a.coef[i], 0);
            c = term.plus(b.times(c));
        }
        return c;
    }




    /** Return true whenever this polynomial and b are identical to one another.
      */
    public boolean equals(BigPolynomial b) {
        BigPolynomial a = this;
        if (a.deg != b.deg) return false;
        for (int i = a.deg; i >= 0; i--)
            if (a.coef[i] != b.coef[i]) return false;
        return true;
    }





    /** Evaluate this polynomial at x, i.e., return this(x).
      */
    public BigInteger evaluate(BigInteger x) {
        BigInteger p = BigInteger.valueOf(0);
        for (int i = deg; i >= 0; i--)
        	if (coef[i] != null) {
        		p = coef[i].add(x.multiply(p));
        	}
        return p;
    }






    /** Return the derivative of this polynomial.
      */
    public BigPolynomial differentiate() {
        if (deg == 0) return new BigPolynomial(BigInteger.valueOf(0), 0);
        BigPolynomial deriv = new BigPolynomial(BigInteger.valueOf(0), deg - 1);
        deriv.deg = deg - 1;
        for (int i = 0; i < deg; i++)
        	if (deriv.coef[i] != null) {
        		deriv.coef[i] = (BigInteger.valueOf(i + 1)).multiply(coef[i + 1]);
        	}
        return deriv;
    }





    /** Return a textual representationof this polynomial.
      */
    public String toString() {
        if (deg ==  0) return "" + coef[0];
        if (deg ==  1) return coef[1] + "x + " + coef[0];
        String s = coef[deg] + "x^" + deg;
        for (int i = deg-1; i >= 0; i--) {
        	if (coef[i] != null) {
            if      (coef[i].compareTo(BigInteger.valueOf(0)) == 0) continue;
            else if (coef[i].compareTo(BigInteger.valueOf(0)) > 0) s = s + " + " + ( coef[i]);
            else if (coef[i].compareTo(BigInteger.valueOf(0)) < 0) s = s + " - " + (coef[i].negate());
            if      (i == 1) s = s + "x";
            else if (i >  1) s = s + "x^" + i;
        	}
        }
        return s;
    }






    public static void main(String[] args) {
        BigPolynomial zero = new BigPolynomial(BigInteger.valueOf(0), 0);

        BigPolynomial p1   = new BigPolynomial(BigInteger.valueOf(4), 3);
        BigPolynomial p2   = new BigPolynomial(BigInteger.valueOf(3), 2);
        BigPolynomial p3   = new BigPolynomial(BigInteger.valueOf(-1), 0);
        BigPolynomial p4   = new BigPolynomial(BigInteger.valueOf(-2), 1);
        BigPolynomial p    = p1.plus(p2).plus(p3).plus(p4);   // 4x^3 + 3x^2  - 2x - 1

        BigPolynomial q1   = new BigPolynomial(BigInteger.valueOf(3), 2);
        BigPolynomial q2   = new BigPolynomial(BigInteger.valueOf(5), 0);
        BigPolynomial q    = q1.minus(q2);                     // 3x^2 - 5


        BigPolynomial r    = p.plus(q);
        BigPolynomial s    = p.times(q);
        BigPolynomial t    = p.compose(q);

        System.out.println("zero(x) =     " + zero);
        System.out.println("p(x) =        " + p);
        System.out.println("q(x) =        " + q);
        System.out.println("p(x) + q(x) = " + r);
        System.out.println("p(x) * q(x) = " + s);
        System.out.println("p(q(x))     = " + t);
        System.out.println("0 - p(x)    = " + zero.minus(p));
        System.out.println("p(3)        = " + p.evaluate(BigInteger.valueOf(3)));
        System.out.println("p'(x)       = " + p.differentiate());
        System.out.println("p''(x)      = " + p.differentiate().differentiate());


        BigPolynomial poly = new BigPolynomial();

        for(int k=0; k<=3; k++){
            poly = poly.times(new BigPolynomial(BigInteger.valueOf(-k)));
        }

        System.out.println(poly);
   }

}