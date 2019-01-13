package ferwafa.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import ferwafa.dao.generic.AbstractDao;
import ferwafa.dao.interfc.IRegistrationRequest;
import ferwafa.domain.RegistrationRequest;

public class RegistrationRequestImpl  extends AbstractDao<Long, RegistrationRequest> implements IRegistrationRequest{
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	@Override
	public RegistrationRequest saveRequest(RegistrationRequest request) {
		return saveIntable(request);
	}

	@Override
	public List<RegistrationRequest> getListRequest() {
		return (List<RegistrationRequest>) (Object) getModelList();
	}

	@Override
	public RegistrationRequest getRequestById(int requestId, String primaryKeyclomunName) {
		return (RegistrationRequest) getModelById(requestId, primaryKeyclomunName);
	}

	@Override
	public RegistrationRequest UpdateRequest(RegistrationRequest request) {
		return updateIntable(request);
	}
	@Override
	public RegistrationRequest DeleteRequest(RegistrationRequest request) {
		return deleteIntable(request);
	}
	@Override
	public String myNane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegistrationRequest getRequestWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (RegistrationRequest) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}

}
