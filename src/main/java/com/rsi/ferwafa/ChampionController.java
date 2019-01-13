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
import ferwafa.common.JSFBoundleProvider;
import ferwafa.common.JSFMessagers;
import ferwafa.common.SessionUtils;
import ferwafa.dao.impl.ChampionImpl;
import ferwafa.domain.Champion;
import ferwafa.domain.Users;
import ferwafa.rsi.dto.ChampionDto;

@SuppressWarnings("unused")
@ManagedBean
@ViewScoped
public class ChampionController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "UserAccountController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;
	private Users usersSession;
	private Champion champion;
	private List<Champion> chapDetails = new ArrayList<Champion>();
	private ChampionImpl champImpl = new ChampionImpl();
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	private String championYear;
	JSFBoundleProvider provider = new JSFBoundleProvider();
	private Date to;
	private Date from;
	private String range;
	private boolean renderForm;
	private boolean renderHeadList;
	private boolean renderHeadForm;
	private boolean renderDashBoard;
	private ChampionDto champDto;
	private List<ChampionDto> chapDtoDetails = new ArrayList<ChampionDto>();

	@SuppressWarnings({ "unchecked" })
	@PostConstruct
	public void init() {
		HttpSession session = SessionUtils.getSession();
		usersSession = (Users) session.getAttribute("userSession");

		if (champion == null) {
			champion = new Champion();
		}
		if (champDto == null) {
			champDto = new ChampionDto();
		}
		try {	 			
			chapDetails = champImpl.getListWithHQL("from Champion", 0,endrecord);
			chapDtoDetails = viewChampion(chapDetails);	
			this.renderHeadList=true;
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

	}

	public String saveChampion() throws Exception {
		try {
			Champion chap= new Champion();
			try {
				Champion champ = new Champion();
				champ = champImpl.getModelWithMyHQL(new String[] { "championYear", "championStatus" },
						new Object[] { championYear, ACTIVE }, "from Champion");
				if (null != champ) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.champion"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: Champion already  recorded in the system! ");
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
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			fmt.format(to);
			fmt.format(from);
			int lastRecordId = champImpl.selectByCountDays("SELECT max(i.champId) FROM Champion i");
			LOGGER.info("LAST RECORD FOUNDED::::::::::::::" + lastRecordId);
				if(lastRecordId!=0) {
			chap = champImpl.getModelWithMyHQL(new String[] { "champId" },
					new Object[] { lastRecordId}, "from Champion");
			
			if (to.after(from)) {

				champion.setCreatedBy(usersSession.getUsername());
				champion.setRecordedDate(timestamp);
				champion.setGenericStatus(ACTIVE);
				champion.setCrtdDtTime(timestamp);
				champion.setChampionStatus(ACTIVE);
				champion.setStartDate(new java.sql.Date(from.getTime()));
				champion.setEndDate(new java.sql.Date(to.getTime()));
				champion.setUsers(usersSession);
				champion.setChampionYear(championYear);
				if(chap.getChampionStatus().equals(ACTIVE)&&chap.getGenericStatus().equals(ACTIVE)) {
					chap.setChampId(chap.getChampId());
					chap.setChampionStatus(DESACTIVE);
					chap.setChampionYear(chap.getChampionYear());
					chap.setGenericStatus(ACTIVE);
					chap.setStartDate(chap.getStartDate());
					chap.setEndDate(chap.getEndDate());
					chap.setCreatedBy(chap.getCreatedBy());
					chap.setUpdatedBy(chap.getUpdatedBy());
					chap.setCrtdDtTime(chap.getCrtdDtTime());
					chap.setUpDtTime(chap.getUpDtTime());
					chap.setUsers(chap.getUsers());
					champImpl.UpdateChampion(chap);
					champImpl.saveChampion(champion);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.champ"));
					LOGGER.info(CLASSNAME + ":::champion Details is saved");
					clearUserFuileds();	
				}
				}else {
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.invalidDate"));		
				}
			} else {
				if (to.after(from)) {
					champion.setCreatedBy(usersSession.getUsername());
					champion.setRecordedDate(timestamp);
					champion.setGenericStatus(ACTIVE);
					champion.setCrtdDtTime(timestamp);
					champion.setChampionStatus(ACTIVE);
					champion.setStartDate(new java.sql.Date(to.getTime()));
					champion.setEndDate(new java.sql.Date(from.getTime()));
					champion.setUsers(usersSession);
					champion.setChampionYear(championYear);
					champImpl.saveChampion(champion);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.champ"));
					LOGGER.info(CLASSNAME + ":::champion Details is saved");
					clearUserFuileds();
					
				}else {
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.invalidDate"));
				}
				
			}
		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::champion Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}
	@SuppressWarnings("rawtypes")
	public List viewChampion(List<Champion> chapList) {
		List<ChampionDto> chapDtoList = new ArrayList<ChampionDto>();
		for (Champion champ : chapList) {
			ChampionDto chapDto = new ChampionDto();
			chapDto.setEditable(false);
			chapDto.setChampId(champ.getChampId());
			chapDto.setChampionStatus(champ.getChampionStatus());
			chapDto.setChampionYear(champ.getChampionYear());
			chapDto.setStartDate(champ.getStartDate());
			chapDto.setEndDate(champ.getEndDate());
			chapDto.setUsers(champ.getUsers());
			chapDto.setGenericStatus(champ.getGenericStatus());
			chapDtoList.add(chapDto);
		}
		return (chapDtoList);
	}

	public void newChampion() {
		this.renderHeadForm = true;
		this.renderHeadList = false;
		this.renderForm = true;

	}

	@SuppressWarnings("unchecked")
	public void showChampion() throws Exception {

		if (range.equals("5") || (range.equals("10")) || (range.equals("15"))) {
			int endRecords = Integer.parseInt(range);
			chapDetails = champImpl.getListWithHQL("from Champion", 0, endRecords);
			chapDtoDetails = viewChampion(chapDetails);
		} else {
			chapDetails = champImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "Champion", "champId desc");
			chapDtoDetails = viewChampion(chapDetails);
			LOGGER.info(" SHOW ALL RECORDS:::::::::::::");
		}
	}

	public String editAction(ChampionDto chap) {
		chap.setEditable(true);
		return null;
	}

	public String cancel(ChampionDto chap) {
		chap.setEditable(false);
		return null;

	}

	public String saveChampionAction(ChampionDto chap) {
		LOGGER.info("update  saveAction method");
		// get all existing value but set "editable" to false
		if (null != chap && chap.getEndDate().after(chap.getStartDate())) {
			Champion champ = new Champion();
			champ = new Champion();
			chap.setEditable(false);
			champ.setChampId(chap.getChampId());
			champ.setStartDate(chap.getStartDate());
			champ.setEndDate(chap.getEndDate());
			champ.setChampionStatus(chap.getChampionStatus());
			champ.setChampionYear(chap.getChampionYear());
			champ.setUpDtTime(timestamp);
			champ.setUpdatedBy(usersSession.getUsername());
			champ.setGenericStatus(chap.getGenericStatus());
			champImpl.UpdateChampion(champ);
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.details.champ"));
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.dateerror"));
		}
		return null;

	}

	public void clearUserFuileds() {

		champion = new Champion();
		chapDetails = null;
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

	public Champion getChampion() {
		return champion;
	}

	public void setChampion(Champion champion) {
		this.champion = champion;
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

	public String getChampionYear() {
		return championYear;
	}

	public void setChampionYear(String championYear) {
		this.championYear = championYear;
	}

	public JSFBoundleProvider getProvider() {
		return provider;
	}

	public void setProvider(JSFBoundleProvider provider) {
		this.provider = provider;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public boolean isRenderForm() {
		return renderForm;
	}

	public void setRenderForm(boolean renderForm) {
		this.renderForm = renderForm;
	}

	public boolean isRenderHeadList() {
		return renderHeadList;
	}

	public void setRenderHeadList(boolean renderHeadList) {
		this.renderHeadList = renderHeadList;
	}

	public boolean isRenderHeadForm() {
		return renderHeadForm;
	}

	public void setRenderHeadForm(boolean renderHeadForm) {
		this.renderHeadForm = renderHeadForm;
	}

	public ChampionDto getChampDto() {
		return champDto;
	}

	public void setChampDto(ChampionDto champDto) {
		this.champDto = champDto;
	}

	public List<ChampionDto> getChapDtoDetails() {
		return chapDtoDetails;
	}

	public void setChapDtoDetails(List<ChampionDto> chapDtoDetails) {
		this.chapDtoDetails = chapDtoDetails;
	}

	public boolean isRenderDashBoard() {
		return renderDashBoard;
	}

	public void setRenderDashBoard(boolean renderDashBoard) {
		this.renderDashBoard = renderDashBoard;
	}
	
}
