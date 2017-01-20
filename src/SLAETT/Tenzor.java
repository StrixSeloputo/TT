package SLAETT;

public class Tenzor {
	public Tenzor() {}
	public Tenzor(MultiIndex n)
	{
		_n = n;
	}
	Tenzor(Vector u, Vector v) // like the product of column-vector u & row-vector v
	{
		int 	m = u.dim(),
				n = v.dim();
		int []m_by_n = { m, n };
		_n = new MultiIndex(m_by_n);
		_array = new INumber[m*n];
		for (MultiIndex i = new MultiIndex(2); i.compareTo(_n) < 0; i.inc(_n))
		{
			_array[i.value(_n, true)] = u.component(i.component(0))	// u_i
											.mul(					// *
										v.component(i.component(1))	// v_i
												);
		}
	}
	
	public static Tenzor getMatrixI(int n)
	{
		int []QMMIa = { n, n };
		MultiIndex QMMI = new MultiIndex(QMMIa);
		Tenzor In = new Tenzor(QMMI);
		INumber I = In._array[0].identity();
		for (int i = 0; i < n; i++)
		{
			QMMIa[0] = QMMIa[1] = i;
			QMMI = new MultiIndex(QMMIa);
			In._array[QMMI.value(In._n, true)] = I;
		}
		return In;
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
	public INumber component(MultiIndex i)
	{
		if (i.compareTo(_n)>=0 || !i.isValidIndex())
//			throw new IllegalArgumentException(
//					"Error in Vector::component(int): illegal multi-index"
//				);
			return _array[0].zero();
		
		return _array[i.value(_n, true)];
	}
	public Tenzor setComponent(MultiIndex i, INumber el) {
		if (i.compareTo(_n)>=0 || !i.isValidIndex())
			throw new IllegalArgumentException(
					"Error in Vector::component(int): illegal multi-index"
				);
		Tenzor T = clone();
		T._array[i.value(_n, true)] = el;
		return T;
	}
	public Vector row(MultiIndex mi, int i)
	{
		if (i >= dim())
			throw new IllegalArgumentException(
					"Error in Vector::row(MultiIndex, int): illegal index - dimention must be less than it"
				);
		if (mi.dim() != dim())
			throw new IllegalArgumentException(
					"Error in Vector::row(MultiIndex, int): dimentions are different"
				);
		int n_i = _n.component(i);
		INumber []row = new INumber[n_i];
		for (int l = 0; l < n_i; l++)
		{
			mi.setComponent(i, l);
			row[l] = component(mi);
		}
		return new Vector(row);
	}
//	public INumber []column(int j)
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
	public Tenzor mul(Tenzor v) {
		if (dim()!=2 || v.dim()!=2)
			throw new UnsupportedOperationException("Not now, maybe in future there will Tenzor multiply-operation");
		int m = _n.component(0),
			k = Math.min(_n.component(1), v._n.component(0)),
			n = _n.component(1);
		int[]mn = { m, n }; MultiIndex Imn = new MultiIndex(mn);
		Tenzor T = new Tenzor(Imn);
		for (MultiIndex I = new MultiIndex(2); I.compareTo(Imn) < 0; I.inc(Imn))
		{
			for (int l = 0; l < k; l++)
				T._array[I.value(_n, true)] = 
					T._array[I.value(_n, true)].add(
											component(I.setComponent(1, l)).mul(
													v.component(I.setComponent(0, l))
												)
							);
		}
		return T;
	}
	public Tenzor sub(Tenzor v) {
		if (dim()!=2 || v.dim()!=2)
			throw new UnsupportedOperationException("Not now, maybe in future there will Tenzor multiply-operation");
		int m = Math.max(_n.component(1), v._n.component(1)),
			n = Math.max(_n.component(1), v._n.component(1));
		int[]mn = { m, n }; MultiIndex Imn = new MultiIndex(mn);
		Tenzor T = new Tenzor(Imn);
		for (MultiIndex I = new MultiIndex(2); I.compareTo(Imn) < 0; I.inc(Imn))
				T._array[I.value(_n, true)] = component(I).sub(v.component(I));
		return T;
	}
	public Pair<Tenzor, Vector> normalizeRows(int i) {
		Tenzor T = clone();
		Vector V = new Vector(_n.component(i));
		
		boolean []wasNormalized = new boolean[size()/_n.component(i)];
		for (MultiIndex I = new MultiIndex(dim()); I.compareTo(_n) < 0; I.inc(_n))
		{
			int index = I.setComponent(i, 0).value(_n, true);
			if (!wasNormalized[index])
			{
				V = V.setComponent(i, T.normalizeRow(I, I.component(i)));
				wasNormalized[index] = true;
			}
		}
		return new Pair<Tenzor, Vector>(T, V);
	}
	private INumber normalizeRow(MultiIndex I, int i)
	{
		Vector row = row(I, i);
		INumber rowNorm = row.norm();
		for (int j = 0; j < _n.component(j); j++)
		{
			I = I.setComponent(i, j);
			_array[I.value(_n, true)] = row.component(i).div(rowNorm);
		}
		return rowNorm;
	}
	
	protected Tenzor clone()
	{
		Tenzor T = new Tenzor(_n.clone());
		T._array = _array.clone();
		return T;
	}

	private INumber []_array;
	private MultiIndex _n;
}
