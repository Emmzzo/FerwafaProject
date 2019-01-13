package com.rsi.ferwafa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;

@ManagedBean
@ViewScoped
public class TestController implements Serializable {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "UserCategoryController :: ";
	private static final long serialVersionUID = 1L;
	/*
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
	
		if (userCategory == null) {
			userCategory = new UserCategory();
		}

		try {
			categoryDetails = userCatImpl.getListWithHQL(SELECT_USERCATEGORY);
			for (UserCategory userCat : categoryDetails) {
				UserCategoryDto catDto = new UserCategoryDto();
				catDto.setEditable(false);
				catDto.setUserCatid(userCat.getUserCatid());
				catDto.setUsercategoryName(userCat.getUsercategoryName());
				categoryDtoDetails.add(catDto);
			}
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

	}*/

	public String anotherPage() {
		try {
			LOGGER.info("HERE WE ARE::::::::::::::::::::::::::::::::::BOSSS");
			return "contact.xhtml?faces-redirect=true";
		} catch (Exception e) {
		LOGGER.info("Founded exception::::::::::::::"+e.getMessage());
		e.printStackTrace();
		}
		return null;
		
	}

}
