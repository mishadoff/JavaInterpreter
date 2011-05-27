class Factorial {
	public static void main(String[] a) {
			System.out.println(new Fac().ComputeFac(10));
	}
}

class Fac {
	public int ComputeFac(int num) {
		int numAux;
		if (num < 1)
			numAux = 1;
		else
			numAux = num * (this.ComputeFac(num-1));
		return numAux;
	}
}
