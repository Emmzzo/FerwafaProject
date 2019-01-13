package com.rsi.ferwafa;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.primefaces.model.UploadedFile;

import ferwafa.common.DbConstant;
import ferwafa.common.Formating;
import ferwafa.common.JSFBoundleProvider;
import ferwafa.common.JSFMessagers;
import ferwafa.common.SessionUtils;
import ferwafa.dao.impl.ChampionImpl;
import ferwafa.dao.impl.PlayerImpl;
import ferwafa.dao.impl.RefereesImpl;
import ferwafa.dao.impl.StadiumImpl;
import ferwafa.domain.Champion;
import ferwafa.domain.Player;
import ferwafa.domain.Referees;
import ferwafa.domain.Stadium;
import ferwafa.domain.Users;

@SuppressWarnings("unused")
@ManagedBean
@ViewScoped
public class RefereesController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "UserAccountController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;
	private Users usersSession;
	private Referees referees;
	private List<Referees> refereDetails = new ArrayList<Referees>();
	private RefereesImpl referImpl = new RefereesImpl();
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	private String email;
	private String phone;
	JSFBoundleProvider provider = new JSFBoundleProvider();
	private Date dateofBirth;

	@SuppressWarnings({ "unchecked" })
	@PostConstruct
	public void init() {
		HttpSession session = SessionUtils.getSession();
		usersSession = (Users) session.getAttribute("userSession");

		if (referees == null) {
			referees = new Referees();
		}
		
		try {

			refereDetails = referImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "Referees", " referId desc");

		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

	}
	@SuppressWarnings("static-access")
	public String saveReferees() {
		try {
			try {
				Referees refer = new Referees();
				refer = referImpl.getModelWithMyHQL(new String[] { "email"}, new Object[] { email },
						"from Referees");
				if (null != refer) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email already  recorded in the system! ");
					return null;
				}
				refer = referImpl.getModelWithMyHQL(new String[] { "phone"}, new Object[] { phone },
						"from Referees");
				if (null != refer) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.phone.number"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: phone already  recorded in the system! ");
					return null;
				}
			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
				return null;
			}
		
					referees.setCreatedBy(usersSession.getUsername());
					referees.setCrtdDtTime(timestamp);
					referees.setGenericStatus(ACTIVE);
					referees.setEmail(email);
					referees.setPhone(phone);
					referImpl.saveReferees(referees);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.referees"));
					LOGGER.info(CLASSNAME + ":::referee Details is saved");
					clearUserFuileds();
			
		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::referee Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	public void clearUserFuileds() {

		referees = new Referees();
		refereDetails = null;
		this.email=null;
		this.phone=null;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public Users getUsersSession() {
		return usersSession;
	}
	public void setUsersSession(Users usersSession) {
		this.usersSession = usersSession;
	}
	public Referees getReferees() {
		return referees;
	}
	public void setReferees(Referees referees) {
		this.referees = referees;
	}
	public List<Referees> getRefereDetails() {
		return refereDetails;
	}
	public void setRefereDetails(List<Referees> refereDetails) {
		this.refereDetails = refereDetails;
	}
	public RefereesImpl getReferImpl() {
		return referImpl;
	}
	public void setReferImpl(RefereesImpl referImpl) {
		this.referImpl = referImpl;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public JSFBoundleProvider getProvider() {
		return provider;
	}
	public void setProvider(JSFBoundleProvider provider) {
		this.provider = provider;
	}
	public Date getDateofBirth() {
		return dateofBirth;
	}
	public void setDateofBirth(Date dateofBirth) {
		this.dateofBirth = dateofBirth;
	}


}
