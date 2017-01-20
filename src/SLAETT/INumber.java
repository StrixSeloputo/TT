package SLAETT;

public interface INumber<T> extends Comparable<INumber<T>> {
	public T value();
	public INumber<T> zero();			// 0
	public INumber<T> identity();		// 1
	public INumber<T> toNumber(int k);	// (Number)k
	
	public INumber<T> neg();			// -this
	public INumber<T> rev();			// 1/this
	public INumber<T> sqrt();			// sqrt(this)
	public INumber<T> add(T n);	// this + n
	public INumber<T> sub(T n);	// this - n
	public INumber<T> mul(T n);	// this * n
	public INumber<T> div(T n);	// this / n
	
}
