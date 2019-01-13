package ferwafa.dao.interfc;

import java.util.List;

import ferwafa.domain.MatchReferees;

public interface IMatchReferees {
	public MatchReferees saveMatchRefer(MatchReferees matchRefer);

	public List<MatchReferees> getListMatchReferees();

	public MatchReferees getMatchRefereesById(int matchReferId, String primaryKeyclomunName);

	public MatchReferees UpdateMatchReferees(MatchReferees matchRefer);

	public String myNane();

	public MatchReferees getMatchRefereesWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}
