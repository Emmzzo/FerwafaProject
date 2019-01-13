package com.rsi.ferwafa;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ferwafa.common.DbConstant;
import ferwafa.common.JSFBoundleProvider;
import ferwafa.common.JSFMessagers;
import ferwafa.common.SessionUtils;
import ferwafa.dao.impl.ChampionImpl;
import ferwafa.dao.impl.UserImpl;
import ferwafa.domain.Champion;
import ferwafa.domain.UserCategory;
import ferwafa.domain.Users;

@ManagedBean
@SessionScoped
public class LoadUserInformationsController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "LoadUserInformationsController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;
	JSFBoundleProvider provider = new JSFBoundleProvider();
	UserImpl usersImpl = new UserImpl();
	private String userCatName;
	private Users users;
	private ChampionImpl champImpl = new ChampionImpl();
	/* end class injection */
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	private UserCategory userCategory;
	private List<Champion> chapDetails = new ArrayList<Champion>();
	private boolean renderPswd=true;
	private boolean renderNoPswd=false;
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		HttpSession session = SessionUtils.getSession();
		if (users == null) {
			users = new Users();
		}

		if (userCategory == null) {
			userCategory = new UserCategory();
		}
		
		users = (Users) session.getAttribute("userSession");
		if (null != users) {
			LOGGER.info("HHH>>" + users.getUserCategory().getUsercategoryName());
			userCategory = users.getUserCategory();
			userCatName = users.getUserCategory().getUsercategoryName();
		}
		try {
			chapDetails = champImpl.getGenericListWithHQLParameter(new String[] { "championStatus" },
					new Object[] { ACTIVE }, "Champion", "champId desc");
		} catch (Exception e) {
			LOGGER.info("error loading generic menu:::");
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
	}

	public String indexPage() {

		return "/index.xhtml?faces-redirect=true";
	}
	public String getContextPath() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return request.getContextPath();
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public JSFBoundleProvider getProvider() {
		return provider;
	}

	public void setProvider(JSFBoundleProvider provider) {
		this.provider = provider;
	}

	public UserImpl getUsersImpl() {
		return usersImpl;
	}

	public void setUsersImpl(UserImpl usersImpl) {
		this.usersImpl = usersImpl;
	}

	public String getUserCatName() {
		return userCatName;
	}

	public void setUserCatName(String userCatName) {
		this.userCatName = userCatName;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public UserCategory getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(UserCategory userCategory) {
		this.userCategory = userCategory;
	}

	public List<Champion> getChapDetails() {
		return chapDetails;
	}

	public void setChapDetails(List<Champion> chapDetails) {
		this.chapDetails = chapDetails;
	}

	public ChampionImpl getChampImpl() {
		return champImpl;
	}

	public void setChampImpl(ChampionImpl champImpl) {
		this.champImpl = champImpl;
	}

	public boolean isRenderPswd() {
		return renderPswd;
	}

	public void setRenderPswd(boolean renderPswd) {
		this.renderPswd = renderPswd;
	}

	public boolean isRenderNoPswd() {
		return renderNoPswd;
	}

	public void setRenderNoPswd(boolean renderNoPswd) {
		this.renderNoPswd = renderNoPswd;
	}

}
