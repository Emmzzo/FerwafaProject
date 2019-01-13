package ferwafa.dao.interfc;

import java.util.List;

import ferwafa.domain.Champion;

public interface IChampion {
	public Champion saveChampion(Champion champion);

	public List<Champion> getListChampion();

	public Champion gettChampById(int champId, String primaryKeyclomunName);

	public Champion UpdateChampion(Champion champion);

	public String myNane();

	public Champion getChampionWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}
