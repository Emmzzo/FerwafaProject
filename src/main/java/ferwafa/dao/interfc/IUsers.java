package ferwafa.dao.interfc;

import java.util.List;

import ferwafa.domain.Users;

/**
 *
 * @author Emmanuel
 */
public interface IUsers {
	public Users saveUsers(Users users);

	public List<Users> getListUsers();

	public Users gettUserById(int userId, String primaryKeyclomunName);

	public Users UpdateUsers(Users users);

	public String myNane();

	public Users getUsersWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}
