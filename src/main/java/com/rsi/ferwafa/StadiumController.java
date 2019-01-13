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
import ferwafa.dao.impl.StadiumImpl;
import ferwafa.domain.Champion;
import ferwafa.domain.Stadium;
import ferwafa.domain.Users;
import ferwafa.rsi.dto.ChampionDto;
import ferwafa.rsi.dto.StadiumDto;

@ManagedBean
@ViewScoped
public class StadiumController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "UserAccountController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;
	private Users usersSession;
	private Stadium stade;
	private StadiumDto stadeDto;
	private List<StadiumDto> stadeDtoDetail = new ArrayList<StadiumDto>();
	private List<Stadium> stadeDetails = new ArrayList<Stadium>();
	private List<Stadium> stadeDetail = new ArrayList<Stadium>();
	private StadiumImpl stadeImpl = new StadiumImpl();
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	private String stadeName;
	JSFBoundleProvider provider = new JSFBoundleProvider();
	private boolean renderItc;
	private String option;
	private String range;
	private boolean renderHeadForm;
	private boolean renderHeadList;
	private boolean renderForm;
	private boolean renderStade = true;
	private String stadium;

	@SuppressWarnings({ "unchecked" })
	@PostConstruct
	public void init() {
		HttpSession session = SessionUtils.getSession();
		usersSession = (Users) session.getAttribute("userSession");

		if (stade == null) {
			stade = new Stadium();
		}
		if (stadeDto == null) {
			stadeDto = new StadiumDto();
		}
		try {

			/*
			 * stadeDetails = stadeImpl.getGenericListWithHQLParameter(new String[] {
			 * "genericStatus" }, new Object[] { ACTIVE }, "Stadium", "stadeId desc");
			 */
			stadeDetails = stadeImpl.getListWithHQL("from Stadium", 0, endrecord);
			stadeDtoDetail = stadeDetail(stadeDetails);
			stadeDetail = stadeImpl.getGenericListWithHQLParameter(new String[] { "stadeStatus" },
					new Object[] { ACTIVE }, "Stadium", "  stadeId desc");
			this.renderHeadList = true;
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
	}
	

	@SuppressWarnings("unchecked")
	public void showStadium() throws Exception {

		if (range.equals("5") || (range.equals("10")) || (range.equals("15"))) {
			int endRecords = Integer.parseInt(range);
			stadeDetails = stadeImpl.getListWithHQL("from Stadium", 0, endRecords);
			stadeDtoDetail = stadeDetail(stadeDetails);
		} else {
			stadeDetails = stadeImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "Stadium", "stadeId desc");
			stadeDtoDetail = stadeDetail(stadeDetails);
			LOGGER.info(" SHOW ALL RECORDS:::::::::::::");
		}
	}

	@SuppressWarnings("rawtypes")
	public List stadeDetail(List<Stadium> stadeDtoDetails) {
		List<StadiumDto> stadeDtodetails = new ArrayList<StadiumDto>();
		for (Stadium stade : stadeDtoDetails) {
			StadiumDto stadeDto = new StadiumDto();
			stadeDto.setEditable(false);
			stadeDto.setAddress(stade.getAddress());
			stadeDto.setStadeId(stade.getStadeId());
			stadeDto.setGenericStatus(stade.getGenericStatus());
			stadeDto.setStadeName(stade.getStadeName());
			stadeDtodetails.add(stadeDto);
		}
		return (stadeDtodetails);
	}

	public void newStadium() {
		this.renderHeadForm = true;
		this.renderHeadList = false;
		this.renderForm = true;
	}

	public String saveStadium() {
		try {
			try {
				Stadium stade = new Stadium();
				stade = stadeImpl.getModelWithMyHQL(new String[] { "stadeName" }, new Object[] { stadeName },
						"from Stadium");
				if (null != stade) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.dupicate.stadium"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: stade already  recorded in the system! ");
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
			stade.setCreatedBy(usersSession.getUsername());
			stade.setCrtdDtTime(timestamp);
			stade.setGenericStatus(ACTIVE);
			stade.setStadeStatus(ACTIVE);
			stade.setStadeName(stadeName);
			stadeImpl.saveStadium(stade);
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.stade"));
			LOGGER.info(CLASSNAME + ":::stadium Details is saved");
			clearUserFuileds();
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

	public void renderItcPlayer() {
		if (option.equals(Yes_Option)) {
			renderItc = true;
			LOGGER.info("OPTION VALUES:::::::::::::" + option);
		} else {
			renderItc = false;
		}
	}

	public void clearUserFuileds() {

		stade = new Stadium();
		stadeDetails = null;
	}

	public String editAction(StadiumDto stad) {
		stad.setEditable(true);
		return null;
	}

	public String cancel(StadiumDto stad) {
		stad.setEditable(false);
		return null;

	}

	public String saveStadiumAction(StadiumDto stad) {
		LOGGER.info("update  saveAction method");
		// get all existing value but set "editable" to false
		if (null != stad) {
			Stadium stade = new Stadium();
			stade = new Stadium();
			stad.setEditable(false);
			stade.setCreatedBy(stad.getCreatedBy());
			stade.setStadeId(stad.getStadeId());
			stade.setAddress(stad.getAddress());
			stade.setGenericStatus(stad.getGenericStatus());
			stade.setStadeName(stad.getStadeName());
			stade.setUpdatedBy(usersSession.getUsername());
			stadeImpl.UpdateStadium(stade);
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.details.stade"));
		} else {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.champerror"));
		}
		return null;

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

	public JSFBoundleProvider getProvider() {
		return provider;
	}

	public void setProvider(JSFBoundleProvider provider) {
		this.provider = provider;
	}

	public Stadium getStade() {
		return stade;
	}

	public void setStade(Stadium stade) {
		this.stade = stade;
	}

	public List<Stadium> getStadeDetails() {
		return stadeDetails;
	}

	public void setStadeDetails(List<Stadium> stadeDetails) {
		this.stadeDetails = stadeDetails;
	}

	public StadiumImpl getStadeImpl() {
		return stadeImpl;
	}

	public void setStadeImpl(StadiumImpl stadeImpl) {
		this.stadeImpl = stadeImpl;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getStadeName() {
		return stadeName;
	}

	public void setStadeName(String stadeName) {
		this.stadeName = stadeName;
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

	public StadiumDto getStadeDto() {
		return stadeDto;
	}

	public void setStadeDto(StadiumDto stadeDto) {
		this.stadeDto = stadeDto;
	}

	public List<StadiumDto> getStadeDtoDetail() {
		return stadeDtoDetail;
	}

	public void setStadeDtoDetail(List<StadiumDto> stadeDtoDetail) {
		this.stadeDtoDetail = stadeDtoDetail;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
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

	public List<Stadium> getStadeDetail() {
		return stadeDetail;
	}

	public void setStadeDetail(List<Stadium> stadeDetail) {
		this.stadeDetail = stadeDetail;
	}

	public boolean isRenderStade() {
		return renderStade;
	}

	public void setRenderStade(boolean renderStade) {
		this.renderStade = renderStade;
	}

	public String getStadium() {
		return stadium;
	}

	public void setStadium(String stadium) {
		this.stadium = stadium;
	}

	

}
