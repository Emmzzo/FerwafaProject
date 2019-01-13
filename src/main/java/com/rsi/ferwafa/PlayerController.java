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
import ferwafa.dao.impl.LoginImpl;
import ferwafa.dao.impl.PlayerImpl;
import ferwafa.dao.impl.StadiumImpl;
import ferwafa.dao.impl.TeamImpl;
import ferwafa.dao.impl.UserImpl;
import ferwafa.domain.Champion;
import ferwafa.domain.Player;
import ferwafa.domain.Stadium;
import ferwafa.domain.Team;
import ferwafa.domain.Users;
import ferwafa.rsi.dto.PlayerDto;
import ferwafa.rsi.dto.TeamDto;

@SuppressWarnings("unused")
@ManagedBean
@ViewScoped
public class PlayerController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "UserAccountController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;
	private Users usersSession;
	private Player player = new Player();
	private PlayerDto playerDto = new PlayerDto();
	TeamDto teamDto = new TeamDto();
	TeamImpl teamImpl = new TeamImpl();
	Team team = new Team();
	Users users= new Users();
	UserImpl userImpl= new UserImpl();
	LoginImpl loginImpl= new LoginImpl();
	private List<PlayerDto> playerDtoDetails = new ArrayList<PlayerDto>();
	private List<Player> playerDetails = new ArrayList<Player>();
	private List<Player> playerstatistics = new ArrayList<Player>();
	private PlayerImpl playerImpl = new PlayerImpl();
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	private String email;
	private String phone;
	JSFBoundleProvider provider = new JSFBoundleProvider();
	private boolean renderItc;
	private String option;
	private Date dateofBirth;
	private int days, age;
	private int contractYear;
	private Date startDate;
	private Date endDate;
	private boolean renderHeadForm;
	private boolean renderHeadList;
	private boolean renderForm;
	private boolean renderProfile;
	private boolean renderLogo;
	private boolean renderPanel;
	private boolean renderpassword;
	private String range;
	private String password;
	private String confirmPswd;

	@SuppressWarnings({ "unchecked" })
	@PostConstruct
	public void init() {
		HttpSession session = SessionUtils.getSession();
		usersSession = (Users) session.getAttribute("userSession");

		if (player == null) {
			player = new Player();
		}
		if (teamDto == null) {
			teamDto = new TeamDto();
		}
		if (team == null) {
			team = new Team();
		}
		if (playerDto == null) {
			playerDto = new PlayerDto();
		}
		if(users==null) {
			users= new Users();
		}
		try {

			playerDetails = playerImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "Player", "playerId desc");
			this.renderHeadList = true;
			playerDetails = playerImpl.getListWithHQL("from Player", 0, endrecord);
			playerDtoDetails = playerList(playerDetails);
			playerstatistics = playerImpl.getGenericListWithHQLParameter(new String[] { "genericStatus", "goalstatus" },
					new Object[] { ACTIVE, ACTIVE }, "Player", "goals desc");
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public String savePlayer() {
		HttpSession session = SessionUtils.getSession();
		try {
			try {
				TeamDto teamDto = new TeamDto();
				teamDto = (TeamDto) session.getAttribute("teamId");
				if (null != teamDto) {
					team = teamImpl.getTeamById(teamDto.getTeamId(), "teamId");
				}

				Player player = new Player();
				player = playerImpl.getModelWithMyHQL(new String[] { "email" }, new Object[] { email }, "from Player");
				if (null != player) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email already  recorded in the system! ");
					return null;
				}
				player = playerImpl.getModelWithMyHQL(new String[] { "phone" }, new Object[] { phone }, "from Player");
				if (null != player) {
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
			if (endDate.after(startDate)) {
				Formating fmt = new Formating();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date today = fmt.getCurrentDateFormtNOTime();
				days = fmt.daysBetween(dateofBirth, today);
				age = days / 365;
				contractYear = fmt.daysBetween(startDate, endDate) / 365;
				LOGGER.info("CONTRACT YEAR:::::::::" + contractYear);
				if (age > 16) {
					player.setCreatedBy(usersSession.getUsername());
					player.setCrtdDtTime(timestamp);
					player.setGenericStatus(ACTIVE);
					player.setDateOfBirth(dateofBirth);
					player.setContractYear(contractYear);
					player.setEmail(email);
					player.setPhone(phone);
					player.setGoals(0);
					player.setTeam(team);
					playerImpl.savePlayer(player);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.player"));
					LOGGER.info(CLASSNAME + ":::player Details is saved");
					clearUserFuileds();
				} else {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("help.userdob.message"));
				}
			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("help.playercontract.message"));
			}

		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::player Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	public List playerList(List<Player> playerdetails) {
		List<PlayerDto> playerDtoList = new ArrayList<PlayerDto>();
		for (Player player : playerdetails) {

			PlayerDto playerDto = new PlayerDto();
			playerDto.setEditable(false);
			playerDto.setPlayerId(player.getPlayerId());
			playerDto.setFname(player.getFname());
			playerDto.setLname(player.getLname());
			playerDto.setContractYear(player.getContractYear());
			playerDto.setDateOfBirth(player.getDateOfBirth());
			playerDto.setNationality(player.getNationality());
			playerDto.setImage(player.getImage());
			playerDto.setEmail(player.getEmail());
			playerDto.setItc(player.getItc());
			playerDto.setCreatedBy(player.getCreatedBy());
			playerDto.setGenericStatus(player.getGenericStatus());
			playerDto.setPhone(player.getPhone());
			playerDto.setTeam(player.getTeam());
			playerDtoList.add(playerDto);
		}
		return (playerDtoList);
	}

	public void renderItcPlayer() {
		if (option.equals(Yes_Option)) {
			renderItc = true;
			LOGGER.info("OPTION VALUES:::::::::::::" + option);
		} else {
			renderItc = false;
		}
	}

	public String editAction(PlayerDto player) {
		player.setEditable(true);
		return null;
	}

	public String viewPlayerProfile(PlayerDto cont) {
		HttpSession sessionuser = SessionUtils.getSession();

		if (null != cont) {
			// Session creation to get user info from dataTable row
			sessionuser.setAttribute("playerinfo", cont);
			LOGGER.info("Info Founded are playerId:>>>>>>>>>>>>>>>>>>>>>>>:" + cont.getPlayerId() + "tname:"
					+ cont.getFname());
			playerDto = viewProfile();
			this.renderProfile = true;
			this.renderHeadForm = false;
			this.renderHeadList = false;
			this.renderForm = false;
			this.renderPanel = false;
		}
		return null;
	}

	public String cancel(PlayerDto player) {
		player.setEditable(false);
		return null;

	}

	public String addplayerProfile(PlayerDto cont) {
		HttpSession sessionuser = SessionUtils.getSession();

		if (null != cont) {
			// Session creation to get user info from dataTable row
			sessionuser.setAttribute("playerImage", cont);
			LOGGER.info("Info Founded are playerId:>>>>>>>>>>>>>>>>>>>>>>>:" + cont.getPlayerId() + "tname:"
					+ cont.getFname());
			addImage();
		}
		return "/menu/EditPlayerProfile.xhtml?faces-redirect=true";
	}

	public String savePlayerAction(PlayerDto player) {
		LOGGER.info("update  saveAction method");
		// get all existing value but set "editable" to false
		if (null != player) {
			Player tm = new Player();
			tm = new Player();
			player.setEditable(false);
			tm.setPlayerId(player.getPlayerId());
			tm.setFname(player.getFname());
			tm.setLname(player.getLname());
			tm.setContractYear(player.getContractYear());
			tm.setDateOfBirth(player.getDateOfBirth());
			tm.setNationality(player.getNationality());
			tm.setImage(player.getImage());
			tm.setEmail(player.getEmail());
			tm.setItc(player.getItc());
			tm.setCreatedBy(player.getCreatedBy());
			tm.setGenericStatus(player.getGenericStatus());
			tm.setPhone(player.getPhone());
			tm.setGoals(player.getGoals());
			tm.setTeam(player.getTeam());
			playerImpl.UpdatePlayer(tm);
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.details.player"));
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.playererror"));
		}
		return null;

	}

	public PlayerDto addImage() {
		HttpSession session = SessionUtils.getSession();
		// Get the values from the session
		playerDto = (PlayerDto) session.getAttribute("playerImage");
		return (playerDto);
	}

	public PlayerDto viewProfile() {
		HttpSession session = SessionUtils.getSession();
		// Get the values from the session
		playerDto = (PlayerDto) session.getAttribute("playerinfo");
		return playerDto;
	}

	@SuppressWarnings("unchecked")
	public void showPlayer() throws Exception {

		if (range.equals("5") || (range.equals("10")) || (range.equals("15"))) {
			int endRecords = Integer.parseInt(range);
			playerDetails = playerImpl.getListWithHQL("from Player", 0, endRecords);
			playerDtoDetails = playerList(playerDetails);
		} else {
			playerDetails = playerImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "Player", "playerId desc");
			playerDtoDetails = playerList(playerDetails);
			LOGGER.info(" SHOW ALL RECORDS:::::::::::::");
		}
	}
	public void saveChanges() {
		try {
			HttpSession session = SessionUtils.getSession();
			Users user= new Users();
			users = (Users) session.getAttribute("userSession");
			LOGGER.info("USER LOGGED IN:::::::::::"+user.getEmail()+"NAMES"+users.getFname());
			if(password.equalsIgnoreCase(confirmPswd)&&null!=users) {
			user.setUserId(users.getUserId());
			user.setAddress(users.getAddress());
			user.setCreatedBy(users.getCreatedBy());
			user.setDateOfBirth(users.getDateOfBirth());
			user.setFname(users.getFname());
			user.setLname(users.getLname());
			user.setGender(users.getGender());
			user.setGenericStatus(users.getGenericStatus());
			user.setImage(users.getImage());
			user.setLoginStatus(users.getLoginStatus());
			user.setUserCategory(users.getUserCategory());
			user.setUsername(users.getUsername());
			user.setPassword(loginImpl.criptPassword(confirmPswd));
			user.setEmail(users.getEmail());
			user.setPhone(users.getPhone());
			user.setCreatedDate(users.getCreatedDate());
			user.setStatus(users.getStatus());
			user.setRegistrationRequest(users.getRegistrationRequest());
			userImpl.UpdateUsers(user);
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.users"));
			LOGGER.info(CLASSNAME + ":::users Details is changed");
			}else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.password.mismatch"));
			}
		} catch (Exception e) {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
		}
		
		
	}
	public void clearUserFuileds() {

		player = new Player();
		playerDetails = null;
		this.email = null;
		this.phone = null;
	}

	public void changePassword() {

		this.renderpassword = true;
	}
	public void cancelChanges(){

		this.renderpassword = false;
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

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public PlayerDto getPlayerDto() {
		return playerDto;
	}

	public void setPlayerDto(PlayerDto playerDto) {
		this.playerDto = playerDto;
	}

	public TeamDto getTeamDto() {
		return teamDto;
	}

	public void setTeamDto(TeamDto teamDto) {
		this.teamDto = teamDto;
	}

	public TeamImpl getTeamImpl() {
		return teamImpl;
	}

	public void setTeamImpl(TeamImpl teamImpl) {
		this.teamImpl = teamImpl;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public List<PlayerDto> getPlayerDtoDetails() {
		return playerDtoDetails;
	}

	public void setPlayerDtoDetails(List<PlayerDto> playerDtoDetails) {
		this.playerDtoDetails = playerDtoDetails;
	}

	public List<Player> getPlayerDetails() {
		return playerDetails;
	}

	public void setPlayerDetails(List<Player> playerDetails) {
		this.playerDetails = playerDetails;
	}

	public PlayerImpl getPlayerImpl() {
		return playerImpl;
	}

	public void setPlayerImpl(PlayerImpl playerImpl) {
		this.playerImpl = playerImpl;
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

	public boolean isRenderItc() {
		return renderItc;
	}

	public void setRenderItc(boolean renderItc) {
		this.renderItc = renderItc;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public Date getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth(Date dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getContractYear() {
		return contractYear;
	}

	public void setContractYear(int contractYear) {
		this.contractYear = contractYear;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isRenderHeadForm() {
		return renderHeadForm;
	}

	public void setRenderHeadForm(boolean renderHeadForm) {
		this.renderHeadForm = renderHeadForm;
	}

	public boolean isRenderHeadList() {
		return renderHeadList;
	}

	public void setRenderHeadList(boolean renderHeadList) {
		this.renderHeadList = renderHeadList;
	}

	public boolean isRenderForm() {
		return renderForm;
	}

	public void setRenderForm(boolean renderForm) {
		this.renderForm = renderForm;
	}

	public boolean isRenderProfile() {
		return renderProfile;
	}

	public void setRenderProfile(boolean renderProfile) {
		this.renderProfile = renderProfile;
	}

	public boolean isRenderLogo() {
		return renderLogo;
	}

	public void setRenderLogo(boolean renderLogo) {
		this.renderLogo = renderLogo;
	}

	public boolean isRenderPanel() {
		return renderPanel;
	}

	public void setRenderPanel(boolean renderPanel) {
		this.renderPanel = renderPanel;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public List<Player> getPlayerstatistics() {
		return playerstatistics;
	}

	public void setPlayerstatistics(List<Player> playerstatistics) {
		this.playerstatistics = playerstatistics;
	}

	public boolean isRenderpassword() {
		return renderpassword;
	}

	public void setRenderpassword(boolean renderpassword) {
		this.renderpassword = renderpassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPswd() {
		return confirmPswd;
	}

	public void setConfirmPswd(String confirmPswd) {
		this.confirmPswd = confirmPswd;
	}

}
