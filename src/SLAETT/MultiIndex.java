package SLAETT;

public class MultiIndex {
	public MultiIndex() {}
	public MultiIndex(int dim)
	{
		_indices = new int[dim];
	}
	public MultiIndex(int[] indices)
	{
		_indices = indices.clone();
	}
	
	public int dim()
	{
		return _indices.length;
	}
	public int absoluteValue()
	{
		int d = 0;
		for (int component : _indices)
			d += component;
		return d;
	}
	public int component(int i)
	{
		if (0 >= i|| i >= _indices.length)
			throw new IllegalArgumentException(
					"Error in MultiIndex::getComponent(): you can't get component - dimention of multi-index don't allow"
				);
		
		return _indices[i];
	}
	public int value(MultiIndex tenzorDim, boolean fromLeftToRight)
	{
		if (dim() != tenzorDim.dim())
			throw new IllegalArgumentException(
					"Error in MultiIndex::value(): different dimentions of two multi-index are"
				);
		if (fromLeftToRight) return valueFromLeftToRight(tenzorDim);
		else return valueFromRightToLeft(tenzorDim);
	}
	
	
	protected MultiIndex clone()
	{
		MultiIndex MI = new MultiIndex(_indices);
		MI._indices = _indices.clone();
		return MI;
	}
	
	private int valueFromLeftToRight(MultiIndex tenzorDim)
	{
		int val = _indices[0];
		for (int i = 1; i < dim(); i++)
			val = val*tenzorDim.component(i)+_indices[i];
		return val;
	}
	private int valueFromRightToLeft(MultiIndex tenzorDim)
	{	
		int val = _indices[dim()-1];
		for (int i = dim()-2; i >= 0; i--)
			val = val*tenzorDim.component(i)+_indices[i];
		return val;
	}

	private int []_indices;
}
