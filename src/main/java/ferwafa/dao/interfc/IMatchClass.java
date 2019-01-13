package ferwafa.dao.interfc;

import java.util.List;

import ferwafa.domain.MatchClass;

public interface IMatchClass {
	public MatchClass saveMatchClass(MatchClass matchclass);

	public List<MatchClass> getListMatchClass();

	public MatchClass gettMatchClassById(int classId, String primaryKeyclomunName);

	public MatchClass UpdateMatchClass(MatchClass matchClass);

	public String myNane();

	public MatchClass getMatchClassWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);

}
