package SLAETT;

public class Tenzor {
	public Tenzor() {}
	public Tenzor(MultiIndex n)
	{
		_n = n;
	}
	
	public int dim()
	{
		return _n.dim();
	}
	public int size()
	{
		return _array.length;
	}
	public MultiIndex mBYn()
	{
		return _n;
	}
//	public Number []row(int i)
//	public Number []column(int j)
	public Tenzor reshape(MultiIndex I)
	{
		if (_n.absoluteValue() != I.absoluteValue())
			throw new IllegalArgumentException(
					"Error in Tenzor::reshape(): current tezor has another component number than tenzor that it must become"
				);
		Tenzor T = new Tenzor(I);
		// may be there must be smthg more complex
		T._array = _array.clone();
		return T;
	}
	public Tenzor unfolding(int k)
	{
		if (k >= _n.dim()-1)
			throw new IllegalArgumentException(
					"Error in Tenzor::unfolding(int): wrong unfolding index - it must be less on 1+ than tenzor dimention"
				);
		
		int []rowsNcols = { 1, 0} ;
		for (int i = 0; i < k; i++)
			rowsNcols[0] *= _n.component(i);
		rowsNcols[1] = _n.absoluteValue()/rowsNcols[1];
		return reshape(new MultiIndex(rowsNcols));
	}
	public INumber FrobeniusNorm()
	{
		INumber norm = _array[0].zero();
		for (INumber comp : _array)
			norm.add(comp.mul(comp));
		return norm.sqrt();
	}
//	public 
	
	
	protected Tenzor clone()
	{
		Tenzor T = new Tenzor(_n.clone());
		T._array = _array.clone();
		return T;
	}

	private INumber []_array;
	private MultiIndex _n;
}
