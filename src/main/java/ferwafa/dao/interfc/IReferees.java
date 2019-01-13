package ferwafa.dao.interfc;

import java.util.List;

import ferwafa.domain.Referees;

public interface IReferees {
	public Referees saveReferees(Referees referee);

	public List<Referees> getListReferees();

	public Referees getRefereesById(int refereId, String primaryKeyclomunName);

	public Referees UpdateReferees(Referees referee);

	public String myNane();

	public Referees getRefereesWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}
