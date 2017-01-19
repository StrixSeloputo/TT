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
	
	public Vector neg() 
	{
		Vector v = clone();
		for (int i = 0; i < dim(); i++)
			v._values[i] = v._values[i].neg();
		return v;
	}
	public Vector add(Vector n) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	public Vector sub(Vector n) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	public Vector mul(Vector n) 
	{
		// TODO Auto-generated method stub
		return null;
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
