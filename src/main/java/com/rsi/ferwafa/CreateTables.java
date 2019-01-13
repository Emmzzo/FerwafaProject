package com.rsi.ferwafa;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import ferwafa.common.JSFBoundleProvider;
import ferwafa.common.JSFMessagers;
import ferwafa.dao.impl.UserImpl;

@ManagedBean
@SessionScoped
public class CreateTables {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	UserImpl userImpl = new UserImpl();
	private boolean valid;
	JSFBoundleProvider provider= new JSFBoundleProvider();
	@PostConstruct
	public void init() {
		if(userImpl==null) {
			userImpl= new UserImpl();
		}
		
	}
	
	public void cretaAllTable() {

		try {
			
			userImpl.creatingNewTable();
			setValid(true);
			JSFMessagers.addInfoMessage(getProvider().getValue("com.create.success"));
			LOGGER.info("table created");
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addInfoMessage(getProvider().getValue("com.create.error"));
		}

	}
	public String contactRedirect() {
		try {
			LOGGER.info("HERE WE ARE::::::::::::::::::::::::::::::::::BOSSS");
			return"index.xhtml?faces-redirect=true";
		} catch (Exception e) {
		LOGGER.info("Founded exception::::::::::::::"+e.getMessage());
		e.printStackTrace();
		}
		return null;
		
	}

	public UserImpl getUserImpl() {
		return userImpl;
	}

	public void setUserImpl(UserImpl userImpl) {
		this.userImpl = userImpl;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public JSFBoundleProvider getProvider() {
		return provider;
	}

	public void setProvider(JSFBoundleProvider provider) {
		this.provider = provider;
	}

}
