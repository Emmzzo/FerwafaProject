package ferwafa.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import ferwafa.dao.generic.AbstractDao;
import ferwafa.dao.interfc.IUserCategory;
import ferwafa.domain.UserCategory;

/**
 *
 * @author Emmanuel
 */
public class UserCategoryImpl extends AbstractDao<Long, UserCategory> implements IUserCategory {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	public UserCategory saveUsercategory(UserCategory usercategory) {

		return saveIntable(usercategory);
	}

	@SuppressWarnings("unchecked")
	public List<UserCategory> getListUsercategory() {

		return (List<UserCategory>) (Object) getModelList();
	}

	public UserCategory UpdateUsercategory(UserCategory usercategory) {

		return updateIntable(usercategory);
	}

	@Override
	public UserCategory gettUserCatById(int userCatId, String primaryKeyclomunName) {
		return (UserCategory) getModelById(userCatId, primaryKeyclomunName);
	}

	@Override
	public UserCategory getUsersCatWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (UserCategory) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}

}
