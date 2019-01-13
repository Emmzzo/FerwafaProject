package ferwafa.dao.interfc;
import java.util.List;

import ferwafa.domain.UserCategory;



public interface IUserCategory {
	   public UserCategory saveUsercategory(UserCategory usercategory);
       public List<UserCategory> getListUsercategory();
       public UserCategory gettUserCatById(int userCatId,String primaryKeyclomunName);
        public UserCategory UpdateUsercategory(UserCategory usercategory);
        public UserCategory getUsersCatWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}

