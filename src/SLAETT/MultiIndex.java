package SLAETT;

public class MultiIndex implements Comparable<MultiIndex>{
	public MultiIndex() {}
	public MultiIndex(int dim)
	{
		_indices = new int[dim];
	}
	public MultiIndex(int[] indices)
	{
		_indices = indices.clone();
	}

	@Override
	public int compareTo(MultiIndex other) {
		if (dim() != other.dim())
			throw new IllegalArgumentException(
					"Error in MultiIndex::compareTo(MultiIndex): dimentions of multi-index are different"
				);
		for (int i = 0; i < dim(); i++) 
		{
			int sgn = (_indices[i]-other._indices[i])/Math.abs(_indices[i]-other._indices[i]);
			if (sgn != 0)
				return sgn;
		}
		return 0;
	}

	public boolean isValidIndex() {
		for (int i = 0; i < dim(); i++)
			if (_indices[i] < 0)
				return false;
		return true;
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
	public MultiIndex inc(MultiIndex sup)
	{
		if (this.compareTo(sup) >= 0)
			throw new IllegalArgumentException(
					"Error in MultiIndex::inc(MultiIndex): supremum is reached"
				);
		MultiIndex next = clone();
		for (int d = dim()-1; d >= 0; d--)
		{
			next._indices[d]++;
			if (next._indices[d] < sup._indices[d])
				return next;
		}
		return next;
	}
	public int component(int i)
	{
		if (0 >= i|| i >= _indices.length)
			throw new IllegalArgumentException(
					"Error in MultiIndex::getComponent(): you can't get component - dimention of multi-index don't allow"
				);
		
		return _indices[i];
	}
	public MultiIndex setComponent(int i, int n_i)
	{
		if (0 >= i|| i >= _indices.length)
			throw new IllegalArgumentException(
					"Error in MultiIndex::getComponent(): you can't get component - dimention of multi-index don't allow"
				);
		MultiIndex changed = clone();
		changed._indices[i] = n_i;
		return changed;
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
