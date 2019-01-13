package com.rsi.ferwafa;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import ferwafa.common.DbConstant;
import ferwafa.common.GenerateNotificationTemplete;
import ferwafa.common.JSFBoundleProvider;
import ferwafa.common.JSFMessagers;
import ferwafa.common.SessionUtils;
import ferwafa.common.UploadUtility;
import ferwafa.dao.impl.DocumentsImpl;
import ferwafa.dao.impl.PlayerImpl;
import ferwafa.dao.impl.TeamImpl;
import ferwafa.dao.impl.UploadingFilesImpl;
import ferwafa.dao.impl.UserImpl;
import ferwafa.domain.Documents;
import ferwafa.domain.Player;
import ferwafa.domain.Team;
import ferwafa.domain.UploadingTeamLogo;
import ferwafa.domain.Users;
import ferwafa.rsi.dto.PlayerDto;
import ferwafa.rsi.dto.TeamDto;
@ManagedBean
@RequestScoped

public class FileUploadController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "FormSampleController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;
	/* end manage validation messages */
	private Users users;
	TeamDto teamDto= new TeamDto();
	PlayerDto playerDto= new PlayerDto();
	Team team= new Team();
	Player player= new Player();
	/* class injection */
	GenerateNotificationTemplete gen = new GenerateNotificationTemplete();
	JSFBoundleProvider provider = new JSFBoundleProvider();
	UserImpl usersImpl = new UserImpl();
	DocumentsImpl documentsImpl = new DocumentsImpl();
	UploadingFilesImpl uploadingFilesImpl = new UploadingFilesImpl();
	/* end class injection */
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	private Documents documents;
	private UploadingTeamLogo uploadingFiles;
	private Users usersSession;
	TeamImpl teamImpl= new TeamImpl();
	PlayerImpl playerImpl= new PlayerImpl();
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		HttpSession session = SessionUtils.getSession();
		usersSession = (Users) session.getAttribute("userSession");
		if (users == null) {
			users = new Users();
		}

		if (documents == null) {
			documents = new Documents();
		}
		if (uploadingFiles == null) {
			uploadingFiles = new UploadingTeamLogo();
		}
		if(team==null) {
			team= new Team();
		}
		if(teamDto==null) {
			teamDto= new TeamDto();
		}
		if(player==null) {
			player= new Player();
		}
		if(playerDto==null) {
			playerDto= new PlayerDto();
		}
		try {
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

	}

	public void fileUpload(FileUploadEvent event) {

		UploadUtility ut = new UploadUtility();
		String validationCode = "PROFILEIMAGE";
		try {
			documents = ut.fileUploadUtil(event, validationCode);

			// need to put exact users
			Users u = new Users();
			u.setUserId(1);
			uploadingFiles.setUser(u);
			uploadingFiles.setCrtdDtTime(timestamp);
			uploadingFiles.setGenericStatus(ACTIVE);
			uploadingFiles.setDocuments(documents);
			uploadingFiles.setCreatedBy(usersSession.getUsername());
			uploadingFilesImpl.saveIntable(uploadingFiles);

			LOGGER.info(CLASSNAME + event.getFile().getFileName() + "uploaded successfully ... ");
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("upload.message.success"));
		} catch (Exception e) {
			LOGGER.info(CLASSNAME + "testing save methode ");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			e.printStackTrace();
		}

	}
	public String uploadTeamLogo(FileUploadEvent event) {

		try {
			if (null != usersSession) {
				TeamController teamCont= new TeamController();
				
				teamDto=teamCont.saveLogo();
				int teamId=teamDto.getTeamId();
				team=teamImpl.getTeamById(teamId, "teamId");
				if(null!=team) {
					UploadUtility ut = new UploadUtility();
					String validationCode = "PROFILEIMAGE";
					documents = ut.fileUploadUtil(event, validationCode);
					
						uploadingFiles.setUser(usersSession);
						uploadingFiles.setTeam(team);
						uploadingFiles.setCrtdDtTime(timestamp);
						uploadingFiles.setGenericStatus(ACTIVE);
						uploadingFiles.setDocuments(documents);
						uploadingFilesImpl.saveIntable(uploadingFiles);
						team.setTeamId(team.getTeamId());
						team.setAddress(team.getAddress());
						team.setCreatedBy(team.getCreatedBy());
						team.setEmail(team.getEmail());
						team.setGenericStatus(team.getGenericStatus());
						team.setLogo(documents.getSysFilename());
						team.setTeamName(team.getTeamName());
						team.setUpdatedBy(team.getUpdatedBy());
						team.setPhone(team.getPhone());
						team.setPobox(team.getPobox());
						teamImpl.UpdateTeam(team);
						LOGGER.info(CLASSNAME + event.getFile().getFileName() + "uploaded successfully ... ");
						JSFMessagers.resetMessages();
						setValid(true);
						JSFMessagers.addInfoMessage(getProvider().getValue("upload.message.success"));
						/*addErrorMessage(getProvider().getValue("upload.message.success"));*/
				}
					return null;
				}else {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.errorsession"));
				}		
		} catch (Exception e) {
			LOGGER.info(CLASSNAME + "testing profile upload methode ");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.userprofile.error"));
			e.printStackTrace();
		}
		return "";
	}
	public String uploadPlayerProfile(FileUploadEvent event) {

		try {
			if (null != usersSession) {
				PlayerController playercont= new  PlayerController();
				playerDto=playercont.addImage();
				int playerId=playerDto.getPlayerId();
				player=playerImpl.getPlayerById(playerId, "playerId");
				if(null!=player) {
					UploadUtility ut = new UploadUtility();
					String validationCode = "PROFILEIMAGE";
					documents = ut.fileUploadUtil(event, validationCode);
						uploadingFiles.setUser(usersSession);
						uploadingFiles.setPlayer(player);
						uploadingFiles.setCrtdDtTime(timestamp);
						uploadingFiles.setGenericStatus(ACTIVE);
						uploadingFiles.setDocuments(documents);
						uploadingFilesImpl.saveIntable(uploadingFiles);
						player.setPlayerId(player.getPlayerId());
						player.setContractYear(player.getContractYear());
						player.setCreatedBy(player.getCreatedBy());
						player.setCrtdDtTime(player.getCrtdDtTime());
						player.setDateOfBirth(player.getDateOfBirth());
						player.setEmail(player.getEmail());
						player.setFname(player.getFname());
						player.setLname(player.getLname());
						player.setGenericStatus(player.getGenericStatus());
						player.setItc(player.getItc());
						player.setNationality(player.getNationality());
						player.setPhone(player.getPhone());
						player.setTeam(player.getTeam());
						player.setUpdatedBy(usersSession.getUsername());
						player.setUpDtTime(timestamp);
						player.setImage(documents.getSysFilename());
						playerImpl.UpdatePlayer(player);
						LOGGER.info(CLASSNAME + event.getFile().getFileName() + "uploaded successfully ... ");
						JSFMessagers.resetMessages();
						setValid(true);
						JSFMessagers.addInfoMessage(getProvider().getValue("upload.message.success"));
						/*addErrorMessage(getProvider().getValue("upload.message.success"));*/
				}
					return null;
				}else {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.errorsession"));
				}		
		} catch (Exception e) {
			LOGGER.info(CLASSNAME + "testing profile upload methode ");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.userprofile.error"));
			e.printStackTrace();
		}
		return "";
	}
	public String uploadPlayerItc(FileUploadEvent event) {

		try {
			if (null != usersSession) {
				PlayerController playercont= new  PlayerController();
				playerDto=playercont.addImage();
				int playerId=playerDto.getPlayerId();
				player=playerImpl.getPlayerById(playerId, "playerId");
				if(null!=player) {
					UploadUtility ut = new UploadUtility();
					String validationCode = "PROFILEIMAGE";
					documents = ut.fileUploadUtil(event, validationCode);
						uploadingFiles.setUser(usersSession);
						uploadingFiles.setPlayer(player);
						uploadingFiles.setCrtdDtTime(timestamp);
						uploadingFiles.setGenericStatus(ACTIVE);
						uploadingFiles.setDocuments(documents);
						uploadingFilesImpl.saveIntable(uploadingFiles);
						player.setPlayerId(player.getPlayerId());
						player.setContractYear(player.getContractYear());
						player.setCreatedBy(player.getCreatedBy());
						player.setCrtdDtTime(player.getCrtdDtTime());
						player.setDateOfBirth(player.getDateOfBirth());
						player.setEmail(player.getEmail());
						player.setFname(player.getFname());
						player.setLname(player.getLname());
						player.setGenericStatus(player.getGenericStatus());
						player.setItc(documents.getSysFilename());
						player.setNationality(player.getNationality());
						player.setPhone(player.getPhone());
						player.setTeam(player.getTeam());
						player.setUpdatedBy(usersSession.getUsername());
						player.setUpDtTime(timestamp);
						player.setImage(player.getImage());
						playerImpl.UpdatePlayer(player);
						LOGGER.info(CLASSNAME + event.getFile().getFileName() + "uploaded successfully ... ");
						JSFMessagers.resetMessages();
						setValid(true);
						JSFMessagers.addInfoMessage(getProvider().getValue("upload.message.success"));
						/*addErrorMessage(getProvider().getValue("upload.message.success"));*/
				}
					return null;
				}else {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.errorsession"));
				}		
		} catch (Exception e) {
			LOGGER.info(CLASSNAME + "testing profile upload methode ");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.userprofile.error"));
			e.printStackTrace();
		}
		return "";
	}
/*
	public void downloadFile() {
		UploadUtility ut = new UploadUtility();
		try {
			ut.downloadFileUtil();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	public List<UploadingTeamLogo> fileList() {

		try {
			return uploadingFilesImpl.getListWithHQL("from UploadingFiles");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void sendMailTest() {
		/* sending content in a table example */
		String name = "Mukamana";
		String fname = "Eric";

		String msg = "<p>Kindly refer to the  below status.</p>" + "<table width=\"50%\" border=\"5px\">\n"
				+ "  <tbody>\n" + "	<tr>\n" + "      <td class=\"labelbold\">Fname</td>\n" + "      <td>\n" + "		  "
				+ name + "\n" + "	  </td>\n" + "    </tr>\n" + "	<tr>\n"
				+ "      <td class=\"labelbold\">Lname</td>\n" + "      <td>\n" + "		  " + fname + "\n"
				+ "	  </td>\n"

				+ "  </tbody>\n" + "</table>\n";
		/* End send content in table sample */
		gen.sendEmailNotification("sibo2540@gmail.com", "Sibo Emma", "Test Email", msg);
		LOGGER.info("::: notidficatio sent   ");
	}

	public void sendUserMailTest(String useremail, String userfname, String userlname) {
		/* sending content in a table example */
		String name = "Mukamana";
		String fname = "Eric";

		String msg = "<p>Kindly refer to the  below status.</p>" + "<table width=\"50%\" border=\"5px\">\n"
				+ "  <tbody>\n" + "	<tr>\n" + "      <td class=\"labelbold\">Fname</td>\n" + "      <td>\n" + "		  "
				+ name + "\n" + "	  </td>\n" + "    </tr>\n" + "	<tr>\n"
				+ "      <td class=\"labelbold\">Lname</td>\n" + "      <td>\n" + "		  " + fname + "\n"
				+ "	  </td>\n"

				+ "  </tbody>\n" + "</table>\n";
		/* End send content in table sample */
		gen.sendEmailNotification(useremail, userfname + " " + userlname, "Test Email", msg);
		LOGGER.info("::: notidficatio sent   ");
	}

	public void saveData() {
		LOGGER.info(CLASSNAME + "testing save methode ");
		JSFMessagers.resetMessages();
		setValid(false);
		JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));

	}

	public void changeSelectBox(String name) {

		LOGGER.info("Ajax is working with listener::::::" + name);
	}

	public String getCLASSNAME() {
		return CLASSNAME;
	}

	public void setCLASSNAME(String cLASSNAME) {
		CLASSNAME = cLASSNAME;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
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

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public DocumentsImpl getDocumentsImpl() {
		return documentsImpl;
	}

	public void setDocumentsImpl(DocumentsImpl documentsImpl) {
		this.documentsImpl = documentsImpl;
	}

	public UploadingFilesImpl getUploadingFilesImpl() {
		return uploadingFilesImpl;
	}

	public void setUploadingFilesImpl(UploadingFilesImpl uploadingFilesImpl) {
		this.uploadingFilesImpl = uploadingFilesImpl;
	}

	public Documents getDocuments() {
		return documents;
	}

	public void setDocuments(Documents documents) {
		this.documents = documents;
	}
	public UploadingTeamLogo getUploadingFiles() {
		return uploadingFiles;
	}

	public void setUploadingFiles(UploadingTeamLogo uploadingFiles) {
		this.uploadingFiles = uploadingFiles;
	}

	public Users getUsersSession() {
		return usersSession;
	}

	public void setUsersSession(Users usersSession) {
		this.usersSession = usersSession;
	}


}