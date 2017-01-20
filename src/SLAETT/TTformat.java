package SLAETT;

public class TTformat<T> {
	public TTformat() {}
	public TTformat(Tenzor A, INumber<T> epsilon)
	{
		int 	d = A.dim(),
				N = A.size();
		int []r = new int[d];
		r[0] = 1;
		INumber<T> delta = epsilon.div(epsilon.toNumber(d-1).sqrt());
		Tenzor C = A.clone();
		
		for (int k = 1; k < d; k++)
		{
			C = C.unfolding(k);
		}
		
	}
	
	private Tenzor []_cores;
}
