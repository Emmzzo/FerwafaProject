package ferwafa.services.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import ferwafa.dao.impl.UserImpl;
import ferwafa.services.interfaces.ILoginControllerService;
@Stateless
public class LoginControllerServiceImpl  implements ILoginControllerService{

	@Inject
	public  transient UserImpl usersImpl;

	@Override
	public String getMyNgaboName() {
		
		return usersImpl.myNane();
	}
		}
	

