package SLAETT;

public class RealNumber implements INumber<RealNumber>
{
	public RealNumber() {}
	public RealNumber(double r)
	{
		_x = r;
	}

	@Override
	public RealNumber value()
	{
		return this;
	}
	@Override
	public INumber<RealNumber> zero() {
		return new RealNumber();
	}
	@Override
	public INumber<RealNumber> identity() {
		return new RealNumber(1d);
	}
	@Override
	public INumber<RealNumber> toNumber(int k) {
		return new RealNumber(k*1d);
	}
	@Override
	public INumber<RealNumber> neg() {
		return new RealNumber(-_x);
	}
	@Override
	public INumber<RealNumber> rev() {
		return new RealNumber(1/_x);
	}
	@Override
	public INumber<RealNumber> sqrt() {
		return new RealNumber(Math.sqrt(_x));
	}
	@Override
	public INumber<RealNumber> add(RealNumber n) {
		return new RealNumber(_x+n._x);
	}
	@Override
	public INumber<RealNumber> sub(RealNumber n) {
		return new RealNumber(_x-n._x);
	}
	@Override
	public INumber<RealNumber> mul(RealNumber n) {
		return new RealNumber(_x*n._x);
	}
	@Override
	public INumber<RealNumber> div(RealNumber n) {
		return new RealNumber(_x/n._x);
	} 
	@Override
	public int compareTo(INumber<RealNumber> o) {
		return (int)Math.signum(_x-o._x);
	}
	
	private double _x;


}
