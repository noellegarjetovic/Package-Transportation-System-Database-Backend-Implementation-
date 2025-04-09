package student;

import rs.etf.sab.operations.PackageOperations;

public class gn210186_Pair<Integer,BigDecimal> implements PackageOperations.Pair<Integer,BigDecimal>{
	private final Integer first;
	private final BigDecimal second;
	
	public gn210186_Pair(Integer first, BigDecimal second) {
		this.first=first;
		this.second=second;
	}

	@Override
	public Integer getFirstParam() {
		return first;
	}

	@Override
	public BigDecimal getSecondParam() {
		return second;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this==o) return true;
		if(o==null || getClass()!=o.getClass()) return false;
		
		gn210186_Pair<?,?> pair=(gn210186_Pair<?,?>) o;
		if(!first.equals(pair.first)) return false;
		return second.equals(pair.second);
	}

}
