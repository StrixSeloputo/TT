package SLAETT;

public class SVD {
	public SVD() {}
	// T is some unfolding of some Tenzor
	public SVD(Tenzor T, INumber delta, int p)
	{
		if (T.dim() !=2)
			throw new IllegalArgumentException(
					"Error in SVD::SVD(Tenzor, Number, int): you can't get svd decmposition of any Tenzor - just its unfolding"
				);
		
		MultiIndex m_by_n = T.mBYn();
		initializeUSV(m_by_n.component(0), m_by_n.component(1));
		
		svdOLS();
	}
	
//	protected SVD clone()
//	{
//		
//		return null;
//	}
	
	
	private void svdOLS()
	{
		
	}
	private void initializeUSV(int m, int n)
	{
		int []tmpIndices = { m, m };
		_U = new Tenzor(new MultiIndex(tmpIndices));
		tmpIndices[1] = n;
		_S = new Tenzor(new MultiIndex(tmpIndices));
		tmpIndices[0] = n;
		_V = new Tenzor(new MultiIndex(tmpIndices));
	}
	
	private Tenzor _U, _S, _V;
}
