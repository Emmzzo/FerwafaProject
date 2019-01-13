package ferwafa.dao.interfc;

import java.util.List;

import ferwafa.domain.Stadium;

public interface IStadium {
	public Stadium saveStadium(Stadium stadium);

	public List<Stadium> getListStadium();

	public Stadium gettStadiumById(int stadId, String primaryKeyclomunName);

	public Stadium UpdateStadium(Stadium stadium);

	public String myNane();

	public Stadium getStadiumWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}
