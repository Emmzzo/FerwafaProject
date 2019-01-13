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
import ferwafa.dao.impl.TeamImpl;
import ferwafa.domain.Champion;
import ferwafa.domain.Player;
import ferwafa.domain.Referees;
import ferwafa.domain.Stadium;
import ferwafa.domain.Team;
import ferwafa.domain.Users;
import ferwafa.rsi.dto.StadiumDto;
import ferwafa.rsi.dto.TeamDto;

@SuppressWarnings("unused")
@ManagedBean
@ViewScoped
public class TeamController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "UserAccountController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;
	private Users usersSession;
	private Team team;
	private List<Team> teamDetails = new ArrayList<Team>();
	private TeamImpl teamImpl = new TeamImpl();
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	private String email;
	private String phone;
	private String teamName;
	private int teamSize;
	private List<TeamDto> teamDtDetail = new ArrayList<TeamDto>();
	private List<Champion> champDetails = new ArrayList<Champion>();
	private List<Player> playerDetails = new ArrayList<Player>();
	private PlayerImpl playerImpl = new PlayerImpl();
	private ChampionImpl champImpl = new ChampionImpl();
	private List<Stadium> stadeDetail = new ArrayList<Stadium>();
	private List<Team> teamstanding = new ArrayList<Team>();
	private TeamDto teamDto;
	StadiumImpl stadeImpl = new StadiumImpl();
	private boolean renderHeadForm;
	private boolean renderHeadList;
	private boolean renderForm;
	private boolean renderProfile;
	private boolean renderLogo;
	private boolean renderPanel;
	private boolean renderTeamPlayerProfile;
	private String range;
	private boolean renderStade = true;
	Stadium stadium = new Stadium();
	private String ground;
	JSFBoundleProvider provider = new JSFBoundleProvider();

	@SuppressWarnings({ "unchecked" })
	@PostConstruct
	public void init() {
		HttpSession session = SessionUtils.getSession();
		usersSession = (Users) session.getAttribute("userSession");
		if (team == null) {
			team = new Team();
		}
		
		if (teamDto == null) {
			teamDto = new TeamDto();
		}
		if(stadium==null) {
			stadium= new Stadium();
		}
		try {
			teamDetails = teamImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "Team", "  teamId desc");
			teamSize = teamSize(teamDetails);
			teamDetails=teamImpl.getListWithHQL("from Team", 0,endrecord);
			teamDtDetail=teamDetail(teamDetails);
			this.renderHeadList = true;
			stadeDetail = stadeImpl.getGenericListWithHQLParameter(new String[] { "stadeStatus" },
					new Object[] { ACTIVE }, "Stadium", "  stadeId desc");
			
				teamstanding=teamImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
						new Object[] { ACTIVE }, "Team", "points desc");
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

	}
	
	public int goalsDifference(Team info) {
		if(null!=info) {
		return (info.getGoalScored()-(info.getGoalAgainst()));
	}
		return (0);
	}
	@SuppressWarnings("unchecked")
	public void showTeam() throws Exception {

		if (range.equals("5") || (range.equals("10")) || (range.equals("15"))) {
			int endRecords = Integer.parseInt(range);
			teamDetails = teamImpl.getListWithHQL("from Team", 0, endRecords);
			teamDtDetail = teamDetail(teamDetails);
		} else {
			teamDetails = teamImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "Team", "teamId desc");
			teamDtDetail = teamDetail(teamDetails);
			LOGGER.info(" SHOW ALL RECORDS:::::::::::::");
		}
	}

	public int teamSize(List<Team> teamList) {
		List<Team> addTeam = new ArrayList<Team>();
		for (Team team : teamList) {
			addTeam.add(team);
		}
		return (addTeam.size());
	}

	public void newTeam() {
		this.renderHeadForm = true;
		this.renderHeadList = false;
		this.renderForm = true;
		this.renderPanel = true;
	}

	@SuppressWarnings("rawtypes")
	public List teamDetail(List<Team> teamDtoDetails) {
		List<TeamDto> teamDtodetails = new ArrayList<TeamDto>();
		for (Team team : teamDtoDetails) {
			TeamDto teamDto = new TeamDto();
			teamDto.setEditable(false);
			teamDto.setTeamId(team.getTeamId());
			teamDto.setAddress(team.getAddress());
			teamDto.setCreatedBy(team.getCreatedBy());
			teamDto.setEmail(team.getEmail());
			teamDto.setPhone(team.getPhone());
			teamDto.setPobox(team.getPobox());
			teamDto.setTeamName(team.getTeamName());
			teamDto.setLogo(team.getLogo());
			teamDto.setGenericStatus(team.getGenericStatus());
			if (team.getLogo() != null) {
				teamDto.setAction(true);
			} else {
				teamDto.setAction(false);
			}
			teamDtodetails.add(teamDto);
		}
		return (teamDtodetails);
	}

	public String editAction(TeamDto team) {
		team.setEditable(true);
		return null;
	}

	public String cancel(TeamDto team) {
		team.setEditable(false);
		return null;

	}

	public String saveTeamAction(TeamDto team) {
		LOGGER.info("update  saveAction method");
		// get all existing value but set "editable" to false
		if (null != team) {
			Team tm = new Team();
			tm = new Team();
			team.setEditable(false);
			tm.setCreatedBy(team.getCreatedBy());
			tm.setTeamId(team.getTeamId());
			tm.setAddress(team.getAddress());
			tm.setCreatedBy(team.getCreatedBy());
			tm.setEmail(team.getEmail());
			tm.setGenericStatus(team.getGenericStatus());
			tm.setTeamName(team.getTeamName());
			tm.setUpdatedBy(usersSession.getUsername());
			tm.setPobox(team.getPobox());
			tm.setPhone(team.getPhone());
			tm.setLogo(team.getLogo());
			teamImpl.UpdateTeam(tm);
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.details.team"));
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.champerror"));
		}
		return null;

	}

	public String viewTeamProfile(TeamDto cont) {
		HttpSession sessionuser = SessionUtils.getSession();

		if (null != cont) {
			// Session creation to get user info from dataTable row
			sessionuser.setAttribute("teaminfo", cont);
			LOGGER.info("Info Founded are teamId:>>>>>>>>>>>>>>>>>>>>>>>:" + cont.getTeamId() + "tname:"
					+ cont.getTeamName());
			TeamDto team = new TeamDto();
			team = viewLogo();
			LOGGER.info("TEAM LOGO NAME" + team.getLogo());
			this.renderProfile = true;
			this.renderHeadForm = false;
			this.renderHeadList = false;
			this.renderForm = false;
			this.renderPanel = false;
		}
		return null;
	}

	public void newPlayer(TeamDto tDto) {
		HttpSession sessionuser = SessionUtils.getSession();
		if (null != tDto) {
			this.renderHeadForm = true;
			this.renderHeadList = false;
			this.renderPanel = true;
			this.renderForm = true;
			sessionuser.setAttribute("teamId", tDto);
		}
	}

	@SuppressWarnings("rawtypes")
	public List viewTeamPlayers() {
		this.renderTeamPlayerProfile = true;
		this.renderProfile = false;
		teamDto = viewLogo();
		playerDetails = new ArrayList<Player>();
		for (Object[] data : playerImpl.reportList(
				"select p.playerId,p.fname,p.lname,p.nationality,p.phone,p.email,p.contractYear,p.image,p.team,p.DateOfBirth from Player p,Team t  where p.team=t.teamId and p.team="
						+ teamDto.getTeamId() + "")) {
			Player p = new Player();
			p.setPlayerId(Integer.parseInt(data[0] + ""));
			LOGGER.info("id:" + data[0] + "names::" + data[1] + " " + data[2]);
			p.setFname(data[1] + "");
			p.setLname(data[2] + "");
			p.setNationality(data[3] + "");
			p.setPhone(data[4] + "");
			p.setEmail(data[5] + "");
			p.setContractYear(Integer.parseInt(data[6] + ""));
			p.setImage(data[7] + "");
			p.setTeam((Team) data[8]);
			p.setDateOfBirth((Date) data[9]);
			playerDetails.add(p);
		}
		return (playerDetails);

	}

	public String addTeamLogo(TeamDto cont) {
		HttpSession sessionuser = SessionUtils.getSession();

		if (null != cont) {
			// Session creation to get user info from dataTable row
			sessionuser.setAttribute("teamLogo", cont);
			LOGGER.info("Info Founded are teamId:>>>>>>>>>>>>>>>>>>>>>>>:" + cont.getTeamId() + "tname:"
					+ cont.getTeamName());
			addLogo();
		}
		return "/menu/EditClubLogo.xhtml?faces-redirect=true";
	}

	public void addLogo() {
		HttpSession session = SessionUtils.getSession();
		// Get the values from the session
		teamDto = (TeamDto) session.getAttribute("teamLogo");
	}

	public TeamDto saveLogo() {
		HttpSession session = SessionUtils.getSession();
		// Get the values from the session
		teamDto = (TeamDto) session.getAttribute("teamLogo");
		return (teamDto);
	}

	public TeamDto viewLogo() {
		HttpSession session = SessionUtils.getSession();
		// Get the values from the session
		teamDto = (TeamDto) session.getAttribute("teaminfo");
		return teamDto;
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public String saveTeam() {
		try {
			Stadium stad = new Stadium();
			try {
				champDetails = champImpl.getGenericListWithHQLParameter(new String[] { "championStatus" },
						new Object[] { ACTIVE }, "Champion", "champId desc");
				LOGGER.info("ChampSize::::" + champDetails.size());

				stad = stadeImpl.getModelWithMyHQL(new String[] { "stadeName" }, new Object[] { ground },
						"from Stadium");
				Team team = new Team();
				team = teamImpl.getModelWithMyHQL(new String[] { "email" }, new Object[] { email }, "from Team");
				if (null != team) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.email"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: email already  recorded in the system! ");
					return null;
				}
				team = teamImpl.getModelWithMyHQL(new String[] { "phone" }, new Object[] { phone }, "from Team");
				if (null != team) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.phone.number"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: phone already  recorded in the system! ");
					return null;
				}
				team = teamImpl.getModelWithMyHQL(new String[] { "teamName" }, new Object[] { teamName }, "from Team");
				if (null != team) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.teamname"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: team already  recorded in the system! ");
					return null;
				}
				if (champDetails.size() == 0) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.champstate"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: Create Champion or make one Active");
				}
				if (null == stad) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.stadeavail"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: Stade not found");
				}
			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
				return null;
			}
			Champion champ = champDetails.get(0);
			int result = 0;
			team.setCreatedBy(usersSession.getUsername());
			team.setCrtdDtTime(timestamp);
			team.setGenericStatus(ACTIVE);
			team.setEmail(email);
			team.setPhone(phone);
			team.setTeamName(teamName);
			team.setStade(ground);
			team.setMathPlayed(0);
			team.setWin(0);
			team.setLose(0);
			team.setDraw(0);
			team.setGoalAgainst(0);
			team.setGoalScored(0);
			team.setPoints(0);
			teamImpl.saveTeam(team);
			stadium.setStadeId(stad.getStadeId());
			stadium.setAddress(stad.getAddress());
			stadium.setCreatedBy(stad.getCreatedBy());
			stadium.setGenericStatus(stad.getGenericStatus());
			stadium.setStadeName(stad.getStadeName());
			stadium.setStadeStatus(DESACTIVE);
			stadium.setUpdatedBy(usersSession.getUsername());
			stadeImpl.UpdateStadium(stadium);
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.team"));
			LOGGER.info(CLASSNAME + ":::team Details is saved");
			clearUserFuileds();
		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::team Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	public void showTeamForm() {
		if (null != ground) {
			this.renderForm = true;
			this.renderStade = false;
		}
	}

	public void clearUserFuileds() {

		team = new Team();
		teamDetails = null;
		this.email = null;
		this.phone = null;
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

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public List<Team> getTeamDetails() {
		return teamDetails;
	}

	public void setTeamDetails(List<Team> teamDetails) {
		this.teamDetails = teamDetails;
	}

	public TeamImpl getTeamImpl() {
		return teamImpl;
	}

	public void setTeamImpl(TeamImpl teamImpl) {
		this.teamImpl = teamImpl;
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

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(int teamSize) {
		this.teamSize = teamSize;
	}

	public List<TeamDto> getTeamDtDetail() {
		return teamDtDetail;
	}

	public void setTeamDtDetail(List<TeamDto> teamDtDetail) {
		this.teamDtDetail = teamDtDetail;
	}

	public TeamDto getTeamDto() {
		return teamDto;
	}

	public void setTeamDto(TeamDto teamDto) {
		this.teamDto = teamDto;
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

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
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

	public boolean isRenderTeamPlayerProfile() {
		return renderTeamPlayerProfile;
	}

	public void setRenderTeamPlayerProfile(boolean renderTeamPlayerProfile) {
		this.renderTeamPlayerProfile = renderTeamPlayerProfile;
	}

	public List<Champion> getChampDetails() {
		return champDetails;
	}

	public void setChampDetails(List<Champion> champDetails) {
		this.champDetails = champDetails;
	}

	public ChampionImpl getChampImpl() {
		return champImpl;
	}

	public void setChampImpl(ChampionImpl champImpl) {
		this.champImpl = champImpl;
	}

	public StadiumImpl getStadeImpl() {
		return stadeImpl;
	}

	public void setStadeImpl(StadiumImpl stadeImpl) {
		this.stadeImpl = stadeImpl;
	}

	public boolean isRenderStade() {
		return renderStade;
	}

	public void setRenderStade(boolean renderStade) {
		this.renderStade = renderStade;
	}

	public Stadium getStadium() {
		return stadium;
	}

	public void setStadium(Stadium stadium) {
		this.stadium = stadium;
	}

	public String getGround() {
		return ground;
	}

	public void setGround(String ground) {
		this.ground = ground;
	}

	public List<Stadium> getStadeDetail() {
		return stadeDetail;
	}

	public void setStadeDetail(List<Stadium> stadeDetail) {
		this.stadeDetail = stadeDetail;
	}

	public List<Team> getTeamstanding() {
		return teamstanding;
	}

	public void setTeamstanding(List<Team> teamstanding) {
		this.teamstanding = teamstanding;
	}

}
