package com.rsi.ferwafa;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
import ferwafa.dao.impl.LoginImpl;
import ferwafa.dao.impl.PlayerImpl;
import ferwafa.dao.impl.RefereesImpl;
import ferwafa.dao.impl.RegistrationRequestImpl;
import ferwafa.dao.impl.StadiumImpl;
import ferwafa.dao.impl.TeamImpl;
import ferwafa.dao.impl.UserCategoryImpl;
import ferwafa.dao.impl.UserImpl;
import ferwafa.domain.Champion;
import ferwafa.domain.Player;
import ferwafa.domain.Referees;
import ferwafa.domain.RegistrationRequest;
import ferwafa.domain.Stadium;
import ferwafa.domain.Team;
import ferwafa.domain.UserCategory;
import ferwafa.domain.Users;
import ferwafa.rsi.dto.StadiumDto;
import ferwafa.rsi.dto.TeamDto;

@ManagedBean
@ViewScoped
public class RegistrationRequestController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "RegistrationRequestController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;
	private Users usersSession;
	LoginImpl loginImpl = new LoginImpl();
	private Users user;
	private UserImpl userImpl= new UserImpl();
	private RegistrationRequest request;
	private List<RegistrationRequest> requestDetails = new ArrayList<RegistrationRequest>();
	private RegistrationRequestImpl requestImpl = new RegistrationRequestImpl();
	private List<RegistrationRequest> requestList = new ArrayList<RegistrationRequest>();
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	private String email;
	private String phone;
	private String teamName;
	private String userRole;
	private int guestSize;
	private int repSize;
	private int totReq;
	private String range;
	private UserCategory userCat;
	private boolean showConfirmBtn=true;
	private UserCategoryImpl catImpl= new UserCategoryImpl();
	JSFBoundleProvider provider = new JSFBoundleProvider();

	@SuppressWarnings({ "unchecked" })
	@PostConstruct
	public void init() {
		HttpSession session = SessionUtils.getSession();
		usersSession = (Users) session.getAttribute("userSession");

		if (request == null) {
			request = new RegistrationRequest();
		}
		if(user==null) {
			user= new Users();
		}
		if(userCat==null) {
			userCat=new UserCategory();
		}
		try {
			requestList=requestImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "RegistrationRequest", "requestId desc");
			
			requestDetails = requestImpl.getGenericListWithHQLParameter(new String[] { "genericStatus","userRole" },
					new Object[] { ACTIVE,guest}, "RegistrationRequest", "requestId desc");
			guestSize=guestSize(requestDetails);
			requestDetails = requestImpl.getGenericListWithHQLParameter(new String[] { "genericStatus","userRole" },
					new Object[] { ACTIVE,service}, "RegistrationRequest", "requestId desc");
			repSize=guestSize(requestDetails);
			totReq=repSize+guestSize;
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

	}
	@SuppressWarnings("unchecked")
	public void showRequest()throws Exception{

		/*if (range.equals("5") || (range.equals("10")) || (range.equals("15"))) {
			int endRecords = Integer.parseInt(range);
			requestList=requestImpl.getListWithHQL("from RegistrationRequest", 0,endRecords);
		} else */
			if(range.equals(CONFIRM_REQ)){	
			requestList=requestImpl.getGenericListWithHQLParameter(new String[] { "status" },
					new Object[] { CONFIRM_REQ }, "RegistrationRequest", "requestId desc");
			this.showConfirmBtn=false;
			LOGGER.info(" SHOW ALL RECORDS:::::::::::::");
		}else {
			requestList=requestImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "RegistrationRequest", "requestId desc");
			this.showConfirmBtn=true;
			LOGGER.info(" SHOW ALL RECORDS:::::::::::::");
		}
	}
	public String saveRegistration() {
		try {
			try {
				RegistrationRequest regReq = new RegistrationRequest();
				regReq =requestImpl.getModelWithMyHQL(new String[] { "email"}, new Object[] { email },
						"from RegistrationRequest");
				if (null != regReq) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email already  recorded in the system! ");
					return null;
				}
				regReq = requestImpl.getModelWithMyHQL(new String[] { "phone"}, new Object[] { phone },
						"from RegistrationRequest");
				if (null != regReq) {
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
					if(userRole.equals(service)) {
					request.setCrtdDtTime(timestamp);
					request.setGenericStatus(ACTIVE);
					request.setEmail(email);
					request.setPhone(phone);
					request.setStatus(ACTIVE);
					request.setUserRole(userRole);
					requestImpl.saveRequest(request);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addInfoMessage(getProvider().getValue("com.save.form.request"));
					LOGGER.info(CLASSNAME + ":::user Details is saved");
					clearUserFuileds();	
					}else {
						request.setStatus(ACTIVE);
						request.setCrtdDtTime(timestamp);
						request.setGenericStatus(ACTIVE);
						request.setEmail(email);
						request.setPhone(phone);
						request.setUserRole(userRole);
						requestImpl.saveRequest(request);
						JSFMessagers.resetMessages();
						setValid(true);
						JSFMessagers.addInfoMessage(getProvider().getValue("com.save.form.request"));
						LOGGER.info(CLASSNAME + ":::user Details is saved");
						clearUserFuileds();	
					}
		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::user Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}
	
	public int guestSize(List<RegistrationRequest> requestList) {
		List<RegistrationRequest> addrequest= new ArrayList<RegistrationRequest>();
		for (RegistrationRequest request : requestList) {
			addrequest.add(request);
		}
		return (addrequest.size());
	}
	@SuppressWarnings("unchecked")
	public void confirmAction(RegistrationRequest info) throws Exception {
		boolean valid=false;
		 Random rand = new Random(); 
		 int rand_int1 = rand.nextInt(1000); 
		 SendSupportEmail  suport= new SendSupportEmail();
		String msgcontent="Hello"+info.getLname()+"!\n we would like to inform that your request has been accepted\n "
				+ "now you can access our system with the given credentials\n "
				+ "which you can change at any time\n"
				+ " Your username:"+info.getEmail()+".\nPassword:"+Distributed_Pswd+""+rand_int1 +""
						+ "\nRegards,\n"
						+ "\nSupport Team";
		if(info.getUserRole().equals(service)) {
			userCat=catImpl.getModelWithMyHQL(new String[] {"usercategoryName"},
					new Object[] {CLUB_REP}, "from UserCategory");			
			LOGGER.info("INFO FOUND:::::::::::::::::"+info.getEmail()+"::::::::"+info.getFname()+"::::"+info.getLname());
			LOGGER.info("CATEGORY FOUNDED::::::::"+userCat);
			valid=suport.sendMailTestVersion(info.getFname(), info.getLname(),info.getEmail(), msgcontent);	
			if(valid) {
				String crpted_pswd=Distributed_Pswd+""+rand_int1;
				
				LOGGER.info("PASSWORD TOBE INCRYPTED::"+crpted_pswd);
				user.setCreatedBy(usersSession.getUsername());
				user.setUsername(info.getEmail());
				user.setPassword(loginImpl.criptPassword(crpted_pswd));
				user.setFname(info.getFname());
				user.setLname(info.getLname());
				user.setEmail(info.getEmail());
				user.setPhone(info.getPhone());
				user.setAddress(info.getAddress());
				user.setGenericStatus(ACTIVE);
				user.setStatus(ACTIVE);
				user.setGender(info.getGender());
				user.setLoginStatus(OFFLINE);
				user.setUserCategory(userCat);
				userImpl.saveUsers(user);
				updateRequest(info);
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addInfoMessage(getProvider().getValue("com.server.side.internal.request"));
				requestList=requestImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
						new Object[] { ACTIVE }, "RegistrationRequest", "requestId desc");
			}
		}else {
				userCat=catImpl.getModelWithMyHQL(new String[] {"usercategoryName"}, new Object[] {Guest_REQ },
						"from UserCategory");
				LOGGER.info("CATEGORY FOUNDED::::::::"+userCat);
				valid=suport.sendMailTestVersion(info.getFname(), info.getLname(),info.getEmail(), msgcontent);	
				if(valid) {
					String crpted_pswd=Distributed_Pswd+""+rand_int1;
					user.setCreatedBy(usersSession.getUsername());
					user.setUsername(info.getEmail());
					user.setPassword(loginImpl.criptPassword(crpted_pswd));
					user.setFname(info.getFname());
					user.setLname(info.getLname());
					user.setEmail(info.getEmail());
					user.setPhone(info.getPhone());
					user.setAddress(info.getAddress());
					user.setGenericStatus(ACTIVE);
					user.setGender(info.getGender());
					user.setLoginStatus(OFFLINE);
					user.setUserCategory(userCat);
					userImpl.saveUsers(user);
					updateRequest(info);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addInfoMessage(getProvider().getValue("com.server.side.internal.request"));
					requestList=requestImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
							new Object[] { ACTIVE }, "RegistrationRequest", "requestId desc");
				}
		}
		}
	@SuppressWarnings("unchecked")
	public void RejectRequest(RegistrationRequest info) throws Exception {
		if(null!=info) {
			requestImpl.DeleteRequest(info);
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addWarningMessage(getProvider().getValue("com.server.side.internal.request.reject"));
			requestList=requestImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "RegistrationRequest", "requestId desc");
		}
	}
	public void updateRequest(RegistrationRequest info) {
		RegistrationRequest req= new RegistrationRequest();

		req.setRequestId(info.getRequestId());
		req.setAddress(info.getAddress());
		req.setCreatedBy(info.getCreatedBy());
		req.setEmail(info.getEmail());
		req.setFname(info.getFname());
		req.setLname(info.getLname());
		req.setGender(info.getGender());
		req.setGenericStatus(DESACTIVE);
		req.setPhone(info.getPhone());
		req.setMessage(info.getMessage());
		req.setStatus(CONFIRM_REQ);
		req.setSubject(info.getSubject());
		req.setUpdatedBy(usersSession.getUsername());
		req.setUpDtTime(timestamp);
		req.setUserRole(info.getUserRole());
		requestImpl.UpdateRequest(req);
	}
	public void clearUserFuileds() {

		request = new RegistrationRequest();
		requestDetails = null;
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

	public RegistrationRequest getRequest() {
		return request;
	}

	public void setRequest(RegistrationRequest request) {
		this.request = request;
	}

	public List<RegistrationRequest> getRequestDetails() {
		return requestDetails;
	}

	public void setRequestDetails(List<RegistrationRequest> requestDetails) {
		this.requestDetails = requestDetails;
	}

	public RegistrationRequestImpl getRequestImpl() {
		return requestImpl;
	}

	public void setRequestImpl(RegistrationRequestImpl requestImpl) {
		this.requestImpl = requestImpl;
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

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public JSFBoundleProvider getProvider() {
		return provider;
	}

	public void setProvider(JSFBoundleProvider provider) {
		this.provider = provider;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public int getGuestSize() {
		return guestSize;
	}
	public void setGuestSize(int guestSize) {
		this.guestSize = guestSize;
	}
	public int getRepSize() {
		return repSize;
	}
	public void setRepSize(int repSize) {
		this.repSize = repSize;
	}
	public int getTotReq() {
		return totReq;
	}
	public void setTotReq(int totReq) {
		this.totReq = totReq;
	}
	public List<RegistrationRequest> getRequestList() {
		return requestList;
	}
	public void setRequestList(List<RegistrationRequest> requestList) {
		this.requestList = requestList;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public UserImpl getUserImpl() {
		return userImpl;
	}
	public void setUserImpl(UserImpl userImpl) {
		this.userImpl = userImpl;
	}
	public UserCategory getUserCat() {
		return userCat;
	}
	public void setUserCat(UserCategory userCat) {
		this.userCat = userCat;
	}
	public UserCategoryImpl getCatImpl() {
		return catImpl;
	}
	public void setCatImpl(UserCategoryImpl catImpl) {
		this.catImpl = catImpl;
	}
	public boolean isShowConfirmBtn() {
		return showConfirmBtn;
	}
	public void setShowConfirmBtn(boolean showConfirmBtn) {
		this.showConfirmBtn = showConfirmBtn;
	}
	
}
