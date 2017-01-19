package SLAETT;

public interface INumber {
	public INumber zero();			// 0
	public INumber toNumber(int k);	// (Number)k
	
	public INumber neg();			// -this
	public INumber rev();			// 1/this
	public INumber sqrt();			// sqrt(this)
	public INumber add(INumber n);	// this + n
	public INumber sub(INumber n);	// this - n
	public INumber mul(INumber n);	// this * n
	public INumber div(INumber n);	// this / n
	
}
