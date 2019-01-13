package ferwafa.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import ferwafa.dao.generic.AbstractDao;
import ferwafa.dao.interfc.IStadium;
import ferwafa.domain.Champion;
import ferwafa.domain.Stadium;

public class StadiumImpl extends AbstractDao<Long, Stadium> implements IStadium{
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	@Override
	public Stadium saveStadium(Stadium stadium) {
		return saveIntable(stadium);
	}

	@Override
	public List<Stadium> getListStadium() {
		return (List<Stadium>) (Object) getModelList();
	}

	@Override
	public Stadium gettStadiumById(int stadId, String primaryKeyclomunName) {
		return (Stadium) getModelById(stadId, primaryKeyclomunName);
	}

	@Override
	public Stadium UpdateStadium(Stadium stadium) {
		return updateIntable(stadium);
	}

	@Override
	public String myNane() {
		return"Emma";
	}

	@Override
	public Stadium getStadiumWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (Stadium) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}
}