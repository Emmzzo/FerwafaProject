package ferwafa.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import ferwafa.dao.generic.AbstractDao;
import ferwafa.dao.interfc.IMatchClass;
import ferwafa.domain.MatchClass;

public class MatchClassImpl extends AbstractDao<Long, MatchClass> implements IMatchClass{
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	@Override
	public MatchClass saveMatchClass(MatchClass matchclass) {
		return saveIntable(matchclass);
	}

	@Override
	public List<MatchClass> getListMatchClass() {
		return (List<MatchClass>) (Object) getModelList();
	}

	@Override
	public MatchClass gettMatchClassById(int classId, String primaryKeyclomunName) {
		return (MatchClass) getModelById(classId, primaryKeyclomunName);
	}

	@Override
	public MatchClass UpdateMatchClass(MatchClass matchClass) {
		return updateIntable(matchClass);
	}

	@Override
	public String myNane() {
		return"Emma";
	}

	@Override
	public MatchClass getMatchClassWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (MatchClass) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}

}