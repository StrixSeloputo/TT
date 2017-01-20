package SLAETT;

public class Vector {
	public Vector() {}
	public Vector(int dim)
	{
		_values = new INumber[dim];
	}
	public Vector(INumber []vals)
	{
		_values = vals.clone();
	}
	
	public int dim()
	{
		return _values.length;
	}	
	public INumber norm() {
		INumber norm = _values[0].zero();
		for (INumber val : _values)
		{
			norm = norm.add(val.mul(val));
		}
		return norm.sqrt();
	}
	public Vector neg() 
	{
		Vector v = clone();
		for (int i = 0; i < dim(); i++)
			v._values[i] = v._values[i].neg();
		return v;
	}
	public INumber component(int i)
	{
		if (i >= dim())
			throw new IllegalArgumentException(
					"Error in Vector::component(int): illegal index - dimention must be less than it"
				);
		return _values[i];
	}
	public Vector setComponent(int i, INumber v_i)
	{
		Vector V = clone();
		V._values[i] = v_i;
		return V;
	}
	public Vector add(Vector v) 
	{
		int d = Math.max(dim(), v.dim());
		int p = Math.min(dim(), v.dim());
		Vector u = new Vector (d);
		INumber izero = _values[0].zero();
		for (int i = 0; i < p; i++)
			u._values[i] = _values[i].add(v._values[i]);
		if (dim() < d)
			for (int i = p; i < d; i++)
				u._values[i] = v._values[i].add(izero);
		else
			for (int i = p; i < d; i++)
				u._values[i] = _values[i].add(izero);
		return u;
	}
	public Vector sub(Vector v) 
	{
		int d = Math.max(dim(), v.dim());
		int p = Math.min(dim(), v.dim());
		Vector u = new Vector (d);
		INumber izero = _values[0].zero();
		for (int i = 0; i < p; i++)
			u._values[i] = _values[i].sub(v._values[i]);
		if (dim() < d)
			for (int i = p; i < d; i++)
				u._values[i] = v._values[i].sub(izero);
		else
			for (int i = p; i < d; i++)
				u._values[i] = _values[i].sub(izero);
		return u;
	}
	public INumber scalarMul(Vector v) 
	{
		int d = Math.min(dim(), v.dim());
		INumber scalar = _values[0].zero();
		for (int i = 0; i < d; i++)
			scalar = scalar.add(_values[i].mul(v.component(i)));
		return scalar;
	}
	// it decides that 'this' is the column-vector & 'v' is row-vector
	// so result of this*v is 2d-tenzor (or just matrix)
	public Tenzor mulT(Vector v) 
	{
		return new Tenzor(this, v);
	}
	public Vector kmul(INumber k)
	{
		Vector v = clone();
		for (int i = 0; i < dim(); i++)
			v._values[i] = v._values[i].mul(k);
		return v;
	}
	
	@Override
	protected Vector clone()
	{
		return new Vector(_values);
	}

	private INumber []_values;

}
