package SLAETT;

import java.util.ArrayList;
import java.util.List;

public class SVD {
	public SVD() {}
	// T is some unfolding of some Tenzor
	public SVD(Tenzor T, INumber delta, int p)
	{
		if (T.dim() !=2)
			throw new IllegalArgumentException(
					"Error in SVD::SVD(Tenzor, Number, int): you can't get svd decmposition of any Tenzor - just its unfolding"
				);

		_T = T; _p = p; _delta = delta;
		MultiIndex m_by_n = _T.mBYn();
		_m = m_by_n.component(0); 
		_n = m_by_n.component(1);
		initializeUSV(_m, _n);
		svdOLS_my();
		svdOLS_ml();
	}
	
	private void svdOLS_ml()
	{
//		List<Tenzor> lU = new ArrayList<Tenzor>();
//		List<Tenzor> lV = new ArrayList<Tenzor>();
//		List<Tenzor> lP = new ArrayList<Tenzor>();
////		List<Tenzor> S = new ArrayList<Tenzor>();
//		Tenzor	U = Tenzor.getMatrixI(_m),	// U1 = Imm
//				V = getV(U),				// V1 = V(U1)
//				P = U.mul(V);				// P1 = U*V
//		lU.add(U); lV.add(V); // lP.add(P);
//		for (;_T.sub(P).FrobeniusNorm().compareTo(_delta) > 0;)
//		{
////			_T = _T.sub(P);
//			U = getU(V); V = getV(U); P = U.mul(V);
//			lU.add(U); lV.add(V); // lP.add(P);
//		}
//		_U_my=U.normalize(true);
//		_V_my=V.normalize(false);
	}
	private void svdOLS_my()
	{
		List<Tenzor> lU = new ArrayList<Tenzor>();
		List<Tenzor> lV = new ArrayList<Tenzor>();
		Tenzor	U = Tenzor.getMatrixI(_m),	// U1 = Imm
				V = getV(U),				// V1 = V(U1)
				P = U.mul(V);				// P1 = U*V
		lU.add(U); lV.add(V); // lP.add(P);
		for (;_T.sub(P).FrobeniusNorm().compareTo(_delta) > 0;)
		{
			U = getU(V); V = getV(U); P = U.mul(V);
			lU.add(U); lV.add(V); // lP.add(P);
		}
		Pair<Tenzor, Vector> TN = U.normalizeRows(0);
		_U_my = TN._1(); Vector UmyNorm = TN._2();
		TN =  V.normalizeRows(1);
		_V_my = TN._1(); Vector VmyNorm = TN._2();
		for (int i = 0; i < Math.min(_m, _n); i++)
		{
			int []ii = { i, i }; MultiIndex Iii = new MultiIndex(ii);
			_S_my = _S_my.setComponent(Iii, UmyNorm.component(i).mul(VmyNorm.component(i)));
		}
	}
	private Tenzor getU(Tenzor V)
	{
		MultiIndex	mm = _U_my.mBYn(),
					I = new MultiIndex(_n);
		Tenzor U = new Tenzor(mm);
		INumber zero = U.component(I).zero();
		for (; I.compareTo(mm) < 0; I.inc(mm))
		{
			INumber u_ik = zero,
					v2_kj = zero;
			int i = I.component(0),
				k = I.component(1);
			for (int j = 0; i < _n; j++)
			{
				int []ij = { i, j }; MultiIndex Iij = new MultiIndex(ij);
				int []kj = { k, j }; MultiIndex Ikj = new MultiIndex(kj);
				INumber	v_lj = zero,
						v_kj = U.component(Ikj);
				for (int l = 0; l < _p; l++)
				{
					if (l == k)
						continue;
					int []lj = { l, j }; MultiIndex Ilj = new MultiIndex(lj);
					v_lj = v_lj.add(U.component(Ilj));
				}
				u_ik = u_ik.add((_T.component(Iij).sub(v_lj)).mul(v_kj));	// u_ik += Sum_i(t_ij - Sum_l(v_lj))*v_kj)
				v2_kj = v2_kj.add(v_kj.add(v_kj));
			}
			U.setComponent(I, u_ik.div(v2_kj));
		}
		return U;
	}
	private Tenzor getV(Tenzor U)
	{
		MultiIndex nn = _V_my.mBYn();
		MultiIndex I = new MultiIndex(_n);
		Tenzor V = new Tenzor(nn);
		INumber zero = V.component(I).zero();
		for (; I.compareTo(nn) < 0; I.inc(nn))
		{
			INumber v_kj = zero,
					u2_ik = zero;
			int k = I.component(0),
				j = I.component(1);
			for (int i = 0; i < _m; i++)
			{
				int []ij = { i, j }; MultiIndex Iij = new MultiIndex(ij);
				int []ik = { i, k }; MultiIndex Iik = new MultiIndex(ik);
				INumber	u_il = zero,
						u_ik = U.component(Iik);
				for (int l = 0; l < _p; l++)
				{
					if (l == k)
						continue;
					int []il = { i, l }; MultiIndex Iil = new MultiIndex(il);
					u_il = u_il.add(U.component(Iil));
				}
				v_kj = v_kj.add((_T.component(Iij).sub(u_il)).mul(u_ik));	// v_kj += Sum_i(t_ij - Sum_l(u_il))*u_ik)
				u2_ik = u2_ik.add(u_ik.add(u_ik));
			}
			V.setComponent(I, v_kj.div(u2_ik));
		}
		return V;
	}
	private void initializeUSV(int m, int n)
	{
		int []tmpIndices = { m, m };
		_U_my = new Tenzor(new MultiIndex(tmpIndices));
		_U_ml = new Tenzor(new MultiIndex(tmpIndices));
		tmpIndices[1] = n;
		_S_my = new Tenzor(new MultiIndex(tmpIndices));
		_S_ml = new Tenzor(new MultiIndex(tmpIndices));
		tmpIndices[0] = n;
		_V_my = new Tenzor(new MultiIndex(tmpIndices));
		_V_ml = new Tenzor(new MultiIndex(tmpIndices));
	}
	
	private Tenzor	_U_my, _S_my, _V_my,
					_T,
					_U_ml, _S_ml, _V_ml;
	private int _m, _n, _p;
	private INumber _delta;
}
