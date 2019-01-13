package ferwafa.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import ferwafa.dao.generic.AbstractDao;
import ferwafa.dao.interfc.IReferees;
import ferwafa.domain.Champion;
import ferwafa.domain.Referees;

public class RefereesImpl extends AbstractDao<Long, Referees> implements IReferees{
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	@Override
	public Referees saveReferees(Referees referee) {
		return saveIntable(referee);
	}

	@Override
	public List<Referees> getListReferees() {
		return (List<Referees>) (Object) getModelList();
	}

	@Override
	public Referees getRefereesById(int refereId, String primaryKeyclomunName) {
		return (Referees) getModelById(refereId, primaryKeyclomunName);
	}

	@Override
	public Referees UpdateReferees(Referees referee) {
		return updateIntable(referee);
	}

	@Override
	public String myNane() {
		return"Emma";
	}

	@Override
	public Referees getRefereesWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (Referees) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}

	
}