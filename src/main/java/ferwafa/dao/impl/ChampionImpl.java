package ferwafa.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import ferwafa.dao.generic.AbstractDao;
import ferwafa.dao.interfc.IChampion;
import ferwafa.domain.Champion;

public class ChampionImpl extends AbstractDao<Long, Champion> implements IChampion{
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	@Override
	public Champion saveChampion(Champion champion) {
		return saveIntable(champion);
	}

	@Override
	public List<Champion> getListChampion() {
		return (List<Champion>) (Object) getModelList();
	}

	@Override
	public Champion gettChampById(int champId, String primaryKeyclomunName) {
		return (Champion) getModelById(champId, primaryKeyclomunName);
	}

	@Override
	public Champion UpdateChampion(Champion champion) {
		return updateIntable(champion);
	}

	@Override
	public String myNane() {
		return"Emma";
	}

	@Override
	public Champion getChampionWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (Champion) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}
	
}