package ferwafa.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import ferwafa.dao.generic.AbstractDao;
import ferwafa.dao.interfc.IUsers;
import ferwafa.domain.Users;

public class UserImpl extends AbstractDao<Long, Users> implements IUsers{
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	
	public Users saveUsers(Users users) {
		return saveIntable(users);
	}

	public List<Users> getListUsers() {
		return (List<Users>) (Object) getModelList();
	}

	public Users gettUserById(int userId, String primaryKeyclomunName) {
		return (Users) getModelById(userId, primaryKeyclomunName);
	}

	public Users UpdateUsers(Users users) {
		return updateIntable(users);
	}

	public Users getUsersWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (Users) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}

	public String myNane() {
		return"nBAGO ERIC";
	}
	
	public UserImpl() {
		
	}
}
