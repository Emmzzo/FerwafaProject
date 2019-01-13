package ferwafa.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import ferwafa.dao.generic.AbstractDao;
import ferwafa.dao.interfc.IChampion;
import ferwafa.dao.interfc.IMatchReferees;
import ferwafa.domain.Champion;
import ferwafa.domain.MatchReferees;

public class MatchRefereesImpl extends AbstractDao<Long, MatchReferees> implements IMatchReferees{
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	@Override
	public MatchReferees saveMatchRefer(MatchReferees matchRefer) {
		return saveIntable(matchRefer);
	}

	@Override
	public List<MatchReferees> getListMatchReferees() {
		return (List<MatchReferees>) (Object) getModelList();
	}

	@Override
	public MatchReferees getMatchRefereesById(int matchReferId, String primaryKeyclomunName) {
		return (MatchReferees) getModelById(matchReferId, primaryKeyclomunName);
	}

	@Override
	public MatchReferees UpdateMatchReferees(MatchReferees matchRefer) {
		return updateIntable(matchRefer);
	}

	@Override
	public String myNane() {
		return"Emma";
	}

	@Override
	public MatchReferees getMatchRefereesWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (MatchReferees) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}

}