package com.rsi.ferwafa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.primefaces.model.UploadedFile;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import ferwafa.common.DbConstant;
import ferwafa.common.Formating;
import ferwafa.common.JSFBoundleProvider;
import ferwafa.common.JSFMessagers;
import ferwafa.common.SessionUtils;
import ferwafa.dao.impl.ChampionImpl;
import ferwafa.dao.impl.MatchClassImpl;
import ferwafa.dao.impl.MatchRefereesImpl;
import ferwafa.dao.impl.PlayerImpl;
import ferwafa.dao.impl.RefereesImpl;
import ferwafa.dao.impl.StadiumImpl;
import ferwafa.dao.impl.TeamImpl;
import ferwafa.domain.Champion;
import ferwafa.domain.MatchClass;
import ferwafa.domain.MatchReferees;
import ferwafa.domain.Player;
import ferwafa.domain.Referees;
import ferwafa.domain.Stadium;
import ferwafa.domain.Team;
import ferwafa.domain.Users;
import ferwafa.rsi.dto.StadiumDto;
import ferwafa.rsi.dto.TeamDto;

@ManagedBean
@ViewScoped
public class TimeTableController implements Serializable, DbConstant {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private String CLASSNAME = "UserAccountController :: ";
	private static final long serialVersionUID = 1L;
	/* to manage validation messages */
	private boolean isValid;
	private Users usersSession;
	Timestamp timestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
	private String email;
	private int teamSize;
	private TeamDto teamDto;
	private Champion champ;
	private Stadium stade;
	private Team team;
	private MatchClass match;
	private List<Team> teamDetails = new ArrayList<Team>();
	private List<Stadium> stadeDetails = new ArrayList<Stadium>();
	private List<Champion> champDetails = new ArrayList<Champion>();
	private List<TeamDto> teamDtDetail = new ArrayList<TeamDto>();
	private List<Player> playerDetails = new ArrayList<Player>();
	private List<MatchClass> matchDetails = new ArrayList<MatchClass>();
	private List<MatchClass> viewFixtures = new ArrayList<MatchClass>();
	private List<MatchReferees> viewMatchReferees = new ArrayList<MatchReferees>();
	private TeamImpl teamImpl = new TeamImpl();
	private PlayerImpl playerImpl = new PlayerImpl();
	private StadiumImpl stadeImpl = new StadiumImpl();
	private ChampionImpl champImpl = new ChampionImpl();
	private MatchClassImpl classImpl = new MatchClassImpl();
	private List<Team> teamBDetails = new ArrayList<Team>();
	private List<Referees> refereDetails = new ArrayList<Referees>();
	private RefereesImpl referImpl = new RefereesImpl();
	private MatchReferees matchRefer;
	private MatchRefereesImpl matchreferImpl = new MatchRefereesImpl();
	private List<Champion> chapDetails = new ArrayList<Champion>();
	private List<Team>teamsRanking= new ArrayList<Team>();
	private List<Player>recTeamPlayers= new ArrayList<Player>();
	private List<Player>vsTeamPlayers= new ArrayList<Player>();
	private List<Team> teamstanding = new ArrayList<Team>();
	private List<Player> playerstatistics = new ArrayList<Player>();
	private Player player= new Player();
	private String range;
	private boolean renderFooter, rendercombo;
	private boolean renderRefer;
	private boolean renderCal;
	private int referId;
	private boolean renderTable;
	private boolean renderDataTable,renderScore;
	private boolean renderPost = true;
	private Date from, to;
	private int days;
	private String receiveTeamgoals;
	private String visitTeamgoals;
	private boolean renderDatePanel,renderGoal;
	private int visTeamPlayer;
	private int recTeamPlayer;
	private String vsPlGoal;
	private String recPlGoal;
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
		if (champ == null) {
			champ = new Champion();
		}
		if (stade == null) {
			stade = new Stadium();
		}
		if (match == null) {
			match = new MatchClass();
		}
		if (matchRefer == null) {
			matchRefer = new MatchReferees();
		}
		
		if(player==null) {
			player= new Player();
		}
		try {
			teamDetails = teamImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "Team", "  teamId desc");
			teamSize = teamSize(teamDetails);
			teamDetails = teamImpl.getListWithHQL("from Team", 0, endrecord);
			teamDtDetail = teamDetail(teamDetails);
			viewFixtures = classImpl.getListWithHQL("from MatchClass", 0, endrecord);
			refereDetails = referImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "Referees", " referId desc");
			this.renderTable = true;
			this.rendercombo = true;
			viewMatchReferees = matchreferImpl.getListWithHQL("from MatchReferees", 0, endrecord);
			chapDetails = champImpl.getGenericListWithHQLParameter(new String[] { "championStatus" },
					new Object[] { ACTIVE }, "Champion", "champId desc");
		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}

	}

	public int teamSize(List<Team> teamList) {
		List<Team> addTeam = new ArrayList<Team>();
		for (Team team : teamList) {
			addTeam.add(team);
		}
		return (addTeam.size());
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

	@SuppressWarnings({ "unchecked", "unused" })
	public String createTimetable() throws Exception {
		try {
			try {
				teamDetails = teamImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
						new Object[] { ACTIVE }, "Team", "  teamId desc");
				stadeDetails = stadeImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
						new Object[] { ACTIVE }, "Stadium", "stadeId desc");
				champDetails = champImpl.getGenericListWithHQLParameter(new String[] { "championStatus" },
						new Object[] { ACTIVE }, "Champion", "champId desc");

				LOGGER.info("StadeSize::::" + stadeDetails.size() + "Team Size:::::" + teamDetails.size());
				if (teamDetails.size() < 3) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.teamavail"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: You should have at least 4 Teams ");
				}
				if (stadeDetails.size() == 0) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.stadeavail"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: You should have to create stadium ");
				}
				if (champDetails.size() == 0) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.champstate"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: Create Champion or make one Active");
				}
			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date matchDate = champDetails.get(0).getStartDate();
			int day = 1;
			int num1 = 0, num2 = 0;
			for (int i = 0; i < teamDetails.size() - 1; i++) {
				num2 = num2 + i;
			}
			java.util.Date mdate = null;
			for (int counter = 0; counter < teamDetails.size() - 1; counter++) {
				mdate = matchDate;
				for (int i = counter + 1; i < teamDetails.size(); i++) {
					match.setMatchDay("Day" + " " + i);
					match.setMatchPhase("Home");
					match.setMatchStatus(ACTIVE);
					match.setMatchDate(new java.sql.Date(mdate.getTime()));
					match.setTeam(teamDetails.get(counter));
					match.setVsTeam(teamDetails.get(i).getTeamName());
					match.setChampion(champDetails.get(0));
					Stadium st = new Stadium();
					st = stadeImpl.getModelWithMyHQL(new String[] { "stadeName" },
							new Object[] { teamDetails.get(counter).getStade() }, "from Stadium");
					if (null != st) {
						match.setStade(st);
						classImpl.saveMatchClass(match);
						Calendar cal1 = new GregorianCalendar();
						cal1.setTime(mdate);
						cal1.add(Calendar.DATE, 9);

						String newDate = sdf.format(cal1.getTime());
						mdate = sdf.parse(newDate);
						day++;
						num1++;
					} else {
						JSFMessagers.resetMessages();
						setValid(false);
						JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.stadeErro"));
					}

				}
				Calendar cal2 = new GregorianCalendar();
				cal2.setTime(matchDate);
				cal2.add(Calendar.DATE, 3);
				String newDate = sdf.format(cal2.getTime());
				matchDate = sdf.parse(newDate);
			}
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.fixtures"));
			LOGGER.info(CLASSNAME + ":::champion Details is saved");
			matchRetour();
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

	@SuppressWarnings("unchecked")
	public String matchRetour() throws Exception {
		try {
			try {
				teamBDetails = teamImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
						new Object[] { ACTIVE }, "Team", "  teamId desc");
				/*
				 * stadeDetails = stadeImpl.getGenericListWithHQLParameter(new String[] {
				 * "genericStatus" }, new Object[] { ACTIVE }, "Stadium", "stadeId desc");
				 */

				matchDetails = classImpl.getListWithHQL("select m from MatchClass m order by m.matchDate");
				champDetails = champImpl.getGenericListWithHQLParameter(new String[] { "championStatus" },
						new Object[] { ACTIVE }, "Champion", "champId desc");

				LOGGER.info("Team Size:::::" + teamDetails.size());
				if (champDetails.size() == 0) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.champstate"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: Create Champion or make one Active");
				}
			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			MatchClass match = new MatchClass();
			java.util.Date matchDate = matchDetails.get(matchDetails.size() - 1).getMatchDate();
			Calendar cal1 = new GregorianCalendar();
			cal1.setTime(matchDate);
			cal1.add(Calendar.DATE, 3);
			String newDate = sdf.format(cal1.getTime());
			matchDate = sdf.parse(newDate);
			for (int counter = 0; counter < teamBDetails.size() - 1; counter++) {
				java.util.Date mdat = matchDate;
				for (int i = counter + 1; i < teamBDetails.size(); i++) {
					match.setMatchDay("Day" + " " + i);
					match.setMatchPhase("Away");
					match.setMatchStatus(ACTIVE);
					match.setMatchDate(new java.sql.Date(mdat.getTime()));
					match.setTeam(teamBDetails.get(i));
					match.setVsTeam(teamBDetails.get(counter).getTeamName());
					match.setChampion(champDetails.get(0));
					Stadium std1 = new Stadium();
					/*
					 * std1.setStadeName(teamBDetails.get(i).getStade());
					 * stadeImpl.UpdateStadium(std1);
					 */
					std1 = stadeImpl.getModelWithMyHQL(new String[] { "stadeName" },
							new Object[] { teamBDetails.get(i).getStade() }, "from Stadium");
					if (null != std1) {
						match.setStade(std1);
						classImpl.saveMatchClass(match);
						Calendar cal = new GregorianCalendar();
						cal.setTime(mdat);
						cal.add(Calendar.DATE, 9);
						SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
						String newD = sd.format(cal.getTime());
						mdat = sdf.parse(newD);
					}
				}
				Calendar cal2 = new GregorianCalendar();
				cal2.setTime(matchDate);
				cal2.add(Calendar.DATE, 3);
				SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
				String newDat2 = sdt.format(cal2.getTime());
				matchDate = sdt.parse(newDat2);
			}

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

	@SuppressWarnings("unchecked")
	public void showFixtures() throws Exception {
		if (range.equals("Home") || (range.equals("Away"))) {
			viewFixtures = classImpl.getGenericListWithHQLParameter(new String[] { "matchPhase" },
					new Object[] { range }, "MatchClass", "matchId desc");
			this.renderFooter = true;
		} else if (range.equals("5") || (range.equals("10")) || (range.equals("15"))) {
			int endRecords = Integer.parseInt(range);
			viewFixtures = classImpl.getListWithHQL("from MatchClass", 0, endRecords);
			this.renderFooter = true;
		} else if (range.equals("date")) {
			this.renderDatePanel = true;
			this.renderTable = false;
			this.renderDataTable = true;
		} else {
			viewFixtures = classImpl.getGenericListWithHQLParameter(new String[] { "matchStatus" },
					new Object[] { ACTIVE }, "MatchClass", "matchDate asc");
			this.renderFooter = true;
		}
	}

	@SuppressWarnings("unchecked")
	public void showMatchReferees() throws Exception {
		if (range.equals("5") || (range.equals("10")) || (range.equals("15"))) {
			int endRecords = Integer.parseInt(range);
			viewMatchReferees = matchreferImpl.getListWithHQL("from MatchReferees", 0, endRecords);
		} else {
			viewMatchReferees = matchreferImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "MatchReferees", "matchReferId desc");
		}

	}

	@SuppressWarnings("static-access")
	public void showTimeTableByDateBetween() {
		try {
			if (to.after(from)) {
				Formating fmt = new Formating();
				LOGGER.info("Here We are :--------------->>" + "Start Date:" + fmt.getMysqlFormatV2(from)
						+ "End Date:-------->>>" + fmt.getMysqlFormatV2(to));
				days = fmt.daysBetween(from, to);

				LOGGER.info("Days founded:......................" + days);
				if (days <= 30) {

					viewFixtures = new ArrayList<MatchClass>();
					for (Object[] data : classImpl.reportList(
							" select c.matchId,c.genericStatus,c.matchDate,c.matchDay,c.matchPhase,c.matchStatus,c.champion,c.stade,c.vsTeam,c.team from MatchClass c,Team t,Stadium s,Champion ch where t.teamId=c.team and s.stadeId=c.stade and ch.champId=c.champion and c.matchDate between '"
									+ fmt.getMysqlFormatV2(from) + "' and  '" + fmt.getMysqlFormatV2(to)
									+ "' order by c.matchDate asc")) {
						LOGGER.info("users::::::::::::::::::::::::::::::::::::::::::::::::>>" + data[0] + ":: "
								+ data[1] + "");
						MatchClass clas = new MatchClass();
						clas.setMatchId(Integer.parseInt(data[0] + ""));
						clas.setGenericStatus(data[1] + "");
						clas.setMatchDate((Date) data[2]);
						clas.setMatchDay(data[3] + "");
						clas.setMatchPhase(data[4] + "");
						clas.setMatchStatus(data[5] + "");
						clas.setChampion((Champion) data[6]);
						clas.setStade((Stadium) data[7]);
						clas.setVsTeam(data[8] + "");
						clas.setTeam((Team) data[9]);
						viewFixtures.add(clas);
					}
					renderTable = true;
					this.rendercombo = false;
					this.renderFooter = false;

				} else {
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.invalidDaysRange"));
				}
			} else {
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.invalidRange"));
			}

		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public void printTimeTableByDateBetween() {
		try {
			if (to.after(from)) {
				Formating fmt = new Formating();
				LOGGER.info("Here We are :--------------->>" + "Start Date:" + fmt.getMysqlFormatV2(from)
						+ "End Date:-------->>>" + fmt.getMysqlFormatV2(to));
				days = fmt.daysBetween(from, to);

				LOGGER.info("Days founded:......................" + days);
				if (days <= 30) {

					viewFixtures = new ArrayList<MatchClass>();
					for (Object[] data : classImpl.reportList(
							" select c.matchId,c.genericStatus,c.matchDate,c.matchDay,c.matchPhase,c.matchStatus,c.champion,c.stade,c.vsTeam,c.team from MatchClass c,Team t,Stadium s,Champion ch where t.teamId=c.team and s.stadeId=c.stade and ch.champId=c.champion and c.matchDate between '"
									+ fmt.getMysqlFormatV2(from) + "' and  '" + fmt.getMysqlFormatV2(to)
									+ "' order by c.matchDate asc")) {
						LOGGER.info("users::::::::::::::::::::::::::::::::::::::::::::::::>>" + data[0] + ":: "
								+ data[1] + "");
						MatchClass clas = new MatchClass();
						clas.setMatchId(Integer.parseInt(data[0] + ""));
						clas.setGenericStatus(data[1] + "");
						clas.setMatchDate((Date) data[2]);
						clas.setMatchDay(data[3] + "");
						clas.setMatchPhase(data[4] + "");
						clas.setMatchStatus(data[5] + "");
						clas.setChampion((Champion) data[6]);
						clas.setStade((Stadium) data[7]);
						clas.setVsTeam(data[8] + "");
						clas.setTeam((Team) data[9]);
						viewFixtures.add(clas);
					}

					/// TIMETABLE PRINTING PROCESSS START
					Date date = new Date();
					SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
					String xdate = dt.format(date);
					FacesContext context = FacesContext.getCurrentInstance();
					Document document = new Document();
					Rectangle rect = new Rectangle(20, 20, 800, 600);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					PdfWriter writer = PdfWriter.getInstance(document, baos);
					MyFooter event = new MyFooter();
					writer.setPageEvent(event);
					writer.setBoxSize("art", rect);
					document.setPageSize(rect);
					if (!document.isOpen()) {
						document.open();
					}
					Image img1 = Image.getInstance(
							"C:\\Users\\TRES-SDA\\Documents\\EclipseProject\\Ferwafa\\RSIProject_Dev\\src\\main\\webapp\\resources\\image\\header.png");
					document.add(img1);
					document.add(new Paragraph("\n"));

					Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
					PdfPTable table = new PdfPTable(6);

					table.setWidthPercentage(105);
					Paragraph header1 = new Paragraph("RWANDA SOCCER TIMETABLE REPORT MADE ON  " + xdate + "");
					// header.setAlignment(Element.ALIGN_RIGHT);
					header1.setAlignment(Element.ALIGN_CENTER);
					// header.add(header1);
					document.add(header1);
					document.add(new Paragraph("                                        "));
					// String myBoardName=t.getBoardName();

					PdfPCell pc = new PdfPCell(new Paragraph("FERWAFA ADMIN REPORT FOR " + "TIMETABLE FIXTURES "));
					pc.setColspan(6);
					pc.setBackgroundColor(BaseColor.CYAN);
					pc.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(pc);

					PdfPCell pc1 = new PdfPCell(new Paragraph("No", font0));
					pc1.setBackgroundColor(BaseColor.ORANGE);
					table.addCell(pc1);

					PdfPCell pc2 = new PdfPCell(new Paragraph("Match Date", font0));
					pc2.setBackgroundColor(BaseColor.ORANGE);
					table.addCell(pc2);

					PdfPCell pc3 = new PdfPCell(new Paragraph("Match Status", font0));
					pc3.setBackgroundColor(BaseColor.ORANGE);
					table.addCell(pc3);
					PdfPCell pc4 = new PdfPCell(new Paragraph("Receive Team", font0));
					pc4.setBackgroundColor(BaseColor.ORANGE);
					table.addCell(pc4);
					PdfPCell pc5 = new PdfPCell(new Paragraph(" Visit Team", font0));
					pc5.setBackgroundColor(BaseColor.ORANGE);
					table.addCell(pc5);
					PdfPCell pc6 = new PdfPCell(new Paragraph("Stadium", font0));
					pc6.setBackgroundColor(BaseColor.ORANGE);
					table.addCell(pc6);
					table.setHeaderRows(1);
					int index = 1;

					if (range.equals("date")) {
						document.add(new Paragraph(
								new Chunk("Match From The Date:  " + dt.format(from) + " To: " + dt.format(to) + "",
										FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, BaseColor.ORANGE))));
						document.add(new Paragraph("\n"));
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						for (MatchClass clas : viewFixtures) {
							LOGGER.info("tes1 1::" + clas.getChampion().getChampionYear());
							table.addCell(index + "");
							table.addCell(sdf.format(clas.getMatchDate()));
							table.addCell(clas.getMatchStatus() + "");
							table.addCell(clas.getTeam().getTeamName() + "");
							table.addCell(clas.getVsTeam() + "");
							table.addCell(clas.getStade().getStadeName() + "");
							index++;
						}
						document.add(table);
						document.add(new Paragraph("\n"));
						document.add(new Paragraph("\n"));
						document.add(new Paragraph(new Chunk("Printed By:  " + usersSession.getUsername() + "",
								FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, BaseColor.ORANGE))));
						document.close();

						writePDFToResponse(context.getExternalContext(), baos, "ADMIN_report");

						context.responseComplete();
					}
				} else {
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.invalidDaysRange"));
				}
			} else {
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.invalidRange"));
			}

		} catch (Exception e) {
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(e.getMessage());
			e.printStackTrace();
		}
	}

	public void renderReferees() {
		this.renderRefer = true;
		this.renderTable = false;
	}

	public void clearUserFuileds() {

		match = new MatchClass();
		matchDetails = null;

	}
	// CREATING FOOTER AND HEADER FOR PAGES

	class MyFooter extends PdfPageEventHelper {

		Font ffont1 = new Font(Font.FontFamily.UNDEFINED, 12, Font.ITALIC);

		Font ffont2 = new Font(Font.FontFamily.UNDEFINED, 16, Font.ITALIC);

		public void onEndPage(PdfWriter writer, Document document) {

			try {
				PdfContentByte cb = writer.getDirectContent();
				Phrase header = new Phrase("");
				document.add(new Paragraph("\n"));
				Phrase footer = new Phrase("@Copyright FERWAFA...!\n", ffont2);
				ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header,
						(document.right() - document.left()) / 2 + document.leftMargin(), document.top() + 10, 0);
				ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
						(document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);

			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	// Codes for creating the table and its contents

	@SuppressWarnings("unchecked")
	public void createTimeTablePdf() throws IOException, DocumentException {
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		String xdate = dt.format(date);
		FacesContext context = FacesContext.getCurrentInstance();
		Document document = new Document();
		Rectangle rect = new Rectangle(20, 20, 800, 600);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		MyFooter event = new MyFooter();
		writer.setPageEvent(event);
		writer.setBoxSize("art", rect);
		document.setPageSize(rect);
		if (!document.isOpen()) {
			document.open();
		}
		Image img1 = Image.getInstance(
				"C:\\Users\\TRES-SDA\\Documents\\EclipseProject\\Ferwafa\\RSIProject_Dev\\src\\main\\webapp\\resources\\image\\header.png");
		document.add(img1);
		document.add(new Paragraph("\n"));

		Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
		PdfPTable table = new PdfPTable(6);

		table.setWidthPercentage(105);
		Paragraph header1 = new Paragraph("RWANDA SOCCER TIMETABLE REPORT MADE ON  " + xdate + "");
		// header.setAlignment(Element.ALIGN_RIGHT);
		header1.setAlignment(Element.ALIGN_CENTER);
		// header.add(header1);
		document.add(header1);
		document.add(new Paragraph("                                        "));
		// String myBoardName=t.getBoardName();

		PdfPCell pc = new PdfPCell(new Paragraph("FERWAFA ADMIN REPORT FOR " + "TIMETABLE FIXTURES "));
		pc.setColspan(6);
		pc.setBackgroundColor(BaseColor.CYAN);
		pc.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(pc);

		PdfPCell pc1 = new PdfPCell(new Paragraph("No", font0));
		pc1.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc1);

		PdfPCell pc2 = new PdfPCell(new Paragraph("Match Date", font0));
		pc2.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc2);

		PdfPCell pc3 = new PdfPCell(new Paragraph("Match Status", font0));
		pc3.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc3);
		PdfPCell pc4 = new PdfPCell(new Paragraph("Receive Team", font0));
		pc4.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc4);
		PdfPCell pc5 = new PdfPCell(new Paragraph(" Visit Team", font0));
		pc5.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc5);
		PdfPCell pc6 = new PdfPCell(new Paragraph("Stadium", font0));
		pc6.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc6);
		table.setHeaderRows(1);
		int index = 1;
		try {
			if (range.equals("Home") || (range.equals("Away"))) {
				viewFixtures = classImpl.getGenericListWithHQLParameter(new String[] { "matchPhase" },
						new Object[] { range }, "MatchClass", "matchId desc");
				document.add(new Paragraph(new Chunk("Match:  " + range + " ",
						FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, BaseColor.ORANGE))));
				document.add(new Paragraph("\n"));
			} else if (range.equals("5") || (range.equals("10")) || (range.equals("15"))) {
				int endRecords = Integer.parseInt(range);
				viewFixtures = classImpl.getListWithHQL("from MatchClass", 0, endRecords);
				document.add(new Paragraph(new Chunk("Match:  " + range + " Only",
						FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, BaseColor.ORANGE))));
				document.add(new Paragraph("\n"));
			} else {
				viewFixtures = classImpl.getGenericListWithHQLParameter(new String[] { "matchStatus" },
						new Object[] { ACTIVE }, "MatchClass", "matchId desc");
				document.add(new Paragraph(new Chunk("Match:  " + range + "",
						FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, BaseColor.ORANGE))));
				document.add(new Paragraph("\n"));
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (MatchClass clas : viewFixtures) {
				LOGGER.info("tes1 1::" + clas.getChampion().getChampionYear());
				table.addCell(index + "");
				table.addCell(sdf.format(clas.getMatchDate()));
				table.addCell(clas.getMatchStatus() + "");
				table.addCell(clas.getTeam().getTeamName() + "");
				table.addCell(clas.getVsTeam() + "");
				table.addCell(clas.getStade().getStadeName() + "");
				index++;
			}
			document.add(table);
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph(new Chunk("Printed By:  " + usersSession.getUsername() + "",
					FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, BaseColor.ORANGE))));
			document.close();

			writePDFToResponse(context.getExternalContext(), baos, "ADMIN_report");

			context.responseComplete();

		} catch (Exception e) {

			LOGGER.info(e.getMessage() + "kamana arahari");
			e.printStackTrace();
		}
	}

	

	@SuppressWarnings("unchecked")
	public void printTeamStandingsPdf() throws IOException, DocumentException {
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		String xdate = dt.format(date);
		FacesContext context = FacesContext.getCurrentInstance();
		Document document = new Document();
		Rectangle rect = new Rectangle(20, 20, 800, 600);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		MyFooter event = new MyFooter();
		writer.setPageEvent(event);
		writer.setBoxSize("art", rect);
		document.setPageSize(rect);
		if (!document.isOpen()) {
			document.open();
		}
		Image img1 = Image.getInstance(
				"C:\\Users\\TRES-SDA\\Documents\\EclipseProject\\Ferwafa\\RSIProject_Dev\\src\\main\\webapp\\resources\\image\\header.png");
		document.add(img1);
		document.add(new Paragraph("\n"));

		Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
		PdfPTable table = new PdfPTable(10);

		table.setWidthPercentage(105);
		Paragraph header1 = new Paragraph("RWANDA SOCCER TEAM STANDINGS REPORT MADE ON  " + xdate + "");
		// header.setAlignment(Element.ALIGN_RIGHT);
		header1.setAlignment(Element.ALIGN_CENTER);
		// header.add(header1);
		document.add(header1);
		document.add(new Paragraph("                                        "));
		// String myBoardName=t.getBoardName();

		PdfPCell pc = new PdfPCell(new Paragraph("FERWAFA ADMIN REPORT FOR " + "TEAM STANDINGS"));
		pc.setColspan(10);
		pc.setBackgroundColor(BaseColor.CYAN);
		pc.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(pc);

		PdfPCell pc1 = new PdfPCell(new Paragraph("No", font0));
		pc1.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc1);

		PdfPCell pc2 = new PdfPCell(new Paragraph("Club", font0));
		pc2.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc2);

		PdfPCell pc3 = new PdfPCell(new Paragraph("MP", font0));
		pc3.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc3);
		
		PdfPCell pc4 = new PdfPCell(new Paragraph("W", font0));
		pc4.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc4);
		
		PdfPCell pc5 = new PdfPCell(new Paragraph("D", font0));
		pc5.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc5);
		
		PdfPCell pc6 = new PdfPCell(new Paragraph("L", font0));
		pc6.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc6);
		
		PdfPCell pc7 = new PdfPCell(new Paragraph("GF", font0));
		pc7.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc7);
		
		PdfPCell pc8 = new PdfPCell(new Paragraph("GA", font0));
		pc8.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc8);
		
		PdfPCell pc9 = new PdfPCell(new Paragraph("GD", font0));
		pc9.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc9);
		
		PdfPCell pc10 = new PdfPCell(new Paragraph("Pts", font0));
		pc10.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc10);
		
		table.setHeaderRows(1);
		int index = 1;
		try {
			teamstanding=teamImpl.getGenericListWithHQLParameter(new String[] { "genericStatus" },
					new Object[] { ACTIVE }, "Team", "points desc");
			for (Team team : teamstanding) {
				LOGGER.info("tes1 1::" + team.getTeamId());
				table.addCell(index + "");
				table.addCell(team.getTeamName()+"");
				table.addCell(team.getMathPlayed()+ "");
				table.addCell(team.getWin()+ "");
				table.addCell(team.getDraw() + "");
				table.addCell(team.getLose() + "");
				table.addCell(team.getGoalScored()+ "");
				table.addCell(team.getGoalAgainst()+ "");
				table.addCell((team.getGoalScored())-(team.getGoalAgainst())+"");
				table.addCell(team.getPoints()+ "");
				index++;
			}
			document.add(table);
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph(new Chunk("Printed By:  " + usersSession.getUsername() + "",
					FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, BaseColor.ORANGE))));
			document.close();

			writePDFToResponse(context.getExternalContext(), baos, "ADMIN_report");

			context.responseComplete();

		} catch (Exception e) {

			LOGGER.info(e.getMessage() + "kamana arahari");
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void printPlayerStatisticsPdf() throws IOException, DocumentException {
		Date date = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		String xdate = dt.format(date);
		FacesContext context = FacesContext.getCurrentInstance();
		Document document = new Document();
		Rectangle rect = new Rectangle(20, 20, 800, 600);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		MyFooter event = new MyFooter();
		writer.setPageEvent(event);
		writer.setBoxSize("art", rect);
		document.setPageSize(rect);
		if (!document.isOpen()) {
			document.open();
		}
		Image img1 = Image.getInstance(
				"C:\\Users\\TRES-SDA\\Documents\\EclipseProject\\Ferwafa\\RSIProject_Dev\\src\\main\\webapp\\resources\\image\\header.png");
		document.add(img1);
		document.add(new Paragraph("\n"));

		Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
		PdfPTable table = new PdfPTable(4);

		table.setWidthPercentage(105);
		Paragraph header1 = new Paragraph("RWANDA SOCCER TOP SCORER REPORT MADE ON  " + xdate + "");
		// header.setAlignment(Element.ALIGN_RIGHT);
		header1.setAlignment(Element.ALIGN_CENTER);
		// header.add(header1);
		document.add(header1);
		document.add(new Paragraph("                                        "));
		// String myBoardName=t.getBoardName();

		PdfPCell pc = new PdfPCell(new Paragraph("FERWAFA ADMIN REPORT FOR " + "TOP SCORER"));
		pc.setColspan(4);
		pc.setBackgroundColor(BaseColor.CYAN);
		pc.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(pc);

		PdfPCell pc1 = new PdfPCell(new Paragraph("No", font0));
		pc1.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc1);

		PdfPCell pc2 = new PdfPCell(new Paragraph("Player", font0));
		pc2.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc2);

		PdfPCell pc3 = new PdfPCell(new Paragraph("Club", font0));
		pc3.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc3);
		
		PdfPCell pc4 = new PdfPCell(new Paragraph("Goals", font0));
		pc4.setBackgroundColor(BaseColor.ORANGE);
		table.addCell(pc4);
		table.setHeaderRows(1);
		int index = 1;
		try {
			playerstatistics=playerImpl.getGenericListWithHQLParameter(new String[] { "genericStatus","goalstatus" },
					new Object[] { ACTIVE,ACTIVE }, "Player", "goals desc");
			for (Player player : playerstatistics) {
				LOGGER.info("tes1 1::" + player.getPlayerId());
				table.addCell(index + "");
				table.addCell(player.getFname()+"  "+player.getLname()+"");
				table.addCell(player.getTeam().getTeamName()+ "");
				table.addCell(player.getGoals()+"");
				index++;
			}
			document.add(table);
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph(new Chunk("Printed By:  " + usersSession.getUsername() + "",
					FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, BaseColor.ORANGE))));
			document.close();

			writePDFToResponse(context.getExternalContext(), baos, "ADMIN_report");

			context.responseComplete();

		} catch (Exception e) {

			LOGGER.info(e.getMessage() + "kamana arahari");
			e.printStackTrace();
		}
	}
	public void saveReferees(MatchClass info) {

		HttpSession sessionuser = SessionUtils.getSession();
		if (null != info) {
			// Session creation to get user info from dataTable row
			sessionuser.setAttribute("matchReferees", info);
			LOGGER.info("Info Founded are Visit team:>>>>>>>>>>>>>>>>>>>>>>>:" + info.getVsTeam() + "Receive Teamname:"
					+ info.getTeam().getTeamName());
			match = addMatchReferees();
			this.renderRefer = true;
			this.renderTable = false;
		}
	}

	@SuppressWarnings("unchecked")
	public void addPlayerGoals(MatchClass info) {
		try {
			if (null != info) {
				this.renderCal = true;
				this.renderTable = false;
				HttpSession sessionuser = SessionUtils.getSession();
				sessionuser.setAttribute("playerGoals", info);
				LOGGER.info("TEAM INFO:::::::::DATE:" + info.getMatchDate() + "ID:::::" + info.getMatchId());
				match = addMatchGoals();
				recTeamPlayers = playerImpl.getGenericListWithHQLParameter(new String[] {"team"},
						new Object[] { match.getTeam() }, "Player", "playerId desc");
				Team team2= new Team();
				team2=teamImpl.getModelWithMyHQL(new String[] { "teamName" }, new Object[] { match.getVsTeam()},
						"from Team");
				vsTeamPlayers = playerImpl.getGenericListWithHQLParameter(new String[] { "team" },
						new Object[] {team2 }, "Player", "playerId desc");
			}
		} catch (Exception e) {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	public MatchClass addMatchGoals() {
		HttpSession session = SessionUtils.getSession();
		// Get the values from the session
		match = (MatchClass) session.getAttribute("playerGoals");
		return (match);
	}
	public void showRecTeamField() {

			this.renderGoal=true;
	}
	
	public void showVisTeamField() {
			this.renderScore=true;
	}
	public void saveGoalScored() {
		try {
			Player p1= new Player();
			Player p2= new Player();
			int recG=0;
			int vsG=0;
			if(vsPlGoal==null) {
				vsPlGoal="0";
				recG=Integer.parseInt(vsPlGoal);
			}
			if(recPlGoal==null) {
				recPlGoal="0";
				recG=Integer.parseInt(recPlGoal);
			}
			if(recPlGoal!=null) {
				recG=Integer.parseInt(recPlGoal);	
			}
			
			if(vsPlGoal!=null) {
			vsG=Integer.parseInt(vsPlGoal);
			}
			p1=playerImpl.getModelWithMyHQL(new String[] { "playerId" }, new Object[] {recTeamPlayer},
					"from Player");
			p2=playerImpl.getModelWithMyHQL(new String[] { "playerId" }, new Object[] {visTeamPlayer},
					"from Player");
			
			if(null!=p1) {
			if(recG!=0) {
				player.setPlayerId(p1.getPlayerId());
				player.setPhone(p1.getPhone());
				player.setContractYear(p1.getContractYear());
				player.setCreatedBy(p1.getCreatedBy());
				player.setDateOfBirth(p1.getDateOfBirth());
				player.setEmail(p1.getEmail());
				player.setFname(p1.getFname());
				player.setLname(p1.getLname());
				player.setGenericStatus(p1.getGenericStatus());
				player.setGoalstatus(ACTIVE);
				player.setImage(p1.getImage());
				player.setItc(p1.getItc());
				player.setGoals(p1.getGoals()+recG);	
				player.setNationality(p1.getNationality());
				player.setTeam(p1.getTeam());
				playerImpl.UpdatePlayer(player);
			}else {
				recG=0;
				player.setPlayerId(p1.getPlayerId());
				player.setPhone(p1.getPhone());
				player.setContractYear(p1.getContractYear());
				player.setCreatedBy(p1.getCreatedBy());
				player.setDateOfBirth(p1.getDateOfBirth());
				player.setEmail(p1.getEmail());
				player.setFname(p1.getFname());
				player.setLname(p1.getLname());
				player.setGenericStatus(p1.getGenericStatus());
				player.setGoalstatus(ACTIVE);
				player.setImage(p1.getImage());
				player.setItc(p1.getItc());
				player.setGoals(p1.getGoals()+recG);
				player.setNationality(p1.getNationality());
				player.setTeam(p1.getTeam());
				playerImpl.UpdatePlayer(player);
			}		
			}
			
			if(null!=p2) {
				if(vsG!=0) {
					player.setPlayerId(p2.getPlayerId());
					player.setPhone(p2.getPhone());
					player.setContractYear(p2.getContractYear());
					player.setCreatedBy(p2.getCreatedBy());
					player.setDateOfBirth(p2.getDateOfBirth());
					player.setEmail(p2.getEmail());
					player.setFname(p2.getFname());
					player.setLname(p2.getLname());
					player.setGenericStatus(p2.getGenericStatus());
					player.setGoalstatus(ACTIVE);
					player.setImage(p2.getImage());
					player.setItc(p2.getItc());
					player.setGoals(p2.getGoals()+vsG);	
					player.setNationality(p2.getNationality());
					player.setTeam(p2.getTeam());
					playerImpl.UpdatePlayer(player);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.player.goal.scored"));
				}else {
					vsG=0;
					player.setPlayerId(p2.getPlayerId());
					player.setPhone(p2.getPhone());
					player.setContractYear(p2.getContractYear());
					player.setCreatedBy(p2.getCreatedBy());
					player.setDateOfBirth(p2.getDateOfBirth());
					player.setEmail(p2.getEmail());
					player.setFname(p2.getFname());
					player.setLname(p2.getLname());
					player.setGenericStatus(p2.getGenericStatus());
					player.setGoalstatus(ACTIVE);
					player.setImage(p2.getImage());
					player.setItc(p2.getItc());
					player.setGoals(p2.getGoals()+vsG);
					player.setNationality(p2.getNationality());
					player.setTeam(p2.getTeam());
					playerImpl.UpdatePlayer(player);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.player.goal.scored"));
					
				}	
					
				}			
			
		} catch (Exception e) {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	public void saveMatchResult() {
		try {
			match = addMatchReferees();
			Team team1 = new Team();
			Team team2 = new Team();
			int recTeamgoals = Integer.parseInt(receiveTeamgoals);
			int visTeamgoals = Integer.parseInt(visitTeamgoals);
			team1 = teamImpl.getModelWithMyHQL(new String[] { "teamId" }, new Object[] { match.getTeam().getTeamId() },
					"from Team");
			team2=teamImpl.getModelWithMyHQL(new String[] { "teamName" }, new Object[] { match.getVsTeam()},
					"from Team");
			
			///RECEIVE TEAM RESULT REGISTRATION START HERE
			if (null != team1) {

				if (recTeamgoals > visTeamgoals) {
					team.setTeamId(team1.getTeamId());
					team.setAddress(team1.getAddress());
					team.setCreatedBy(team1.getCreatedBy());
					team.setTeamName(team1.getTeamName());
					team.setPhone(team1.getPhone());
					team.setPobox(team1.getPobox());
					team.setEmail(team1.getEmail());
					team.setStade(team1.getStade());
					team.setLogo(team1.getLogo());
					team.setMathPlayed(team1.getMathPlayed()+1);
					team.setDraw(team1.getDraw());
					team.setWin(team1.getWin()+1);
					team.setLose(team1.getLose());
					team.setPoints(team1.getPoints()+3);
					team.setGoalAgainst(team1.getGoalAgainst()+visTeamgoals);
					team.setGoalScored(team1.getGoalScored()+recTeamgoals);
					team.setGenericStatus(ACTIVE);
					team.setUpdatedBy(usersSession.getUsername());
					teamImpl.UpdateTeam(team);
					
				} else if(recTeamgoals==visTeamgoals) {
					team.setTeamId(team1.getTeamId());
					team.setAddress(team1.getAddress());
					team.setCreatedBy(team1.getCreatedBy());
					team.setTeamName(team1.getTeamName());
					team.setPhone(team1.getPhone());
					team.setPobox(team1.getPobox());
					team.setEmail(team1.getEmail());
					team.setStade(team1.getStade());
					team.setLogo(team1.getLogo());
					team.setMathPlayed(team1.getMathPlayed()+1);
					team.setDraw(team1.getDraw()+1);
					team.setWin(team1.getWin());
					team.setLose(team1.getLose());
					team.setPoints(team1.getPoints()+1);
					team.setGoalAgainst(team1.getGoalAgainst()+visTeamgoals);
					team.setGoalScored(team1.getGoalScored()+recTeamgoals);
					team.setGenericStatus(ACTIVE);
					team.setUpdatedBy(usersSession.getUsername());
					teamImpl.UpdateTeam(team);
				}else {
					team.setTeamId(team1.getTeamId());
					team.setAddress(team1.getAddress());
					team.setCreatedBy(team1.getCreatedBy());
					team.setTeamName(team1.getTeamName());
					team.setPhone(team1.getPhone());
					team.setPobox(team1.getPobox());
					team.setEmail(team1.getEmail());
					team.setStade(team1.getStade());
					team.setLogo(team1.getLogo());
					team.setMathPlayed(team1.getMathPlayed()+1);
					team.setDraw(team1.getDraw());
					team.setWin(team1.getWin());
					team.setLose(team1.getLose()+1);
					team.setPoints(team1.getPoints());
					team.setGoalAgainst(team1.getGoalAgainst()+visTeamgoals);
					team.setGoalScored(team1.getGoalScored()+recTeamgoals);
					team.setGenericStatus(ACTIVE);
					team.setUpdatedBy(usersSession.getUsername());
					teamImpl.UpdateTeam(team);;
				}
			}
				
			///VISIT TEAM RESULT REGISTRATION START HERE
			if (null != team2) {

				if (visTeamgoals > recTeamgoals) {
					team.setTeamId(team2.getTeamId());
					team.setAddress(team2.getAddress());
					team.setCreatedBy(team2.getCreatedBy());
					team.setTeamName(team2.getTeamName());
					team.setPhone(team2.getPhone());
					team.setPobox(team2.getPobox());
					team.setEmail(team2.getEmail());
					team.setStade(team2.getStade());
					team.setLogo(team2.getLogo());
					team.setMathPlayed(team2.getMathPlayed()+1);
					team.setDraw(team2.getDraw());
					team.setWin(team2.getWin()+1);
					team.setLose(team2.getLose());
					team.setPoints(team2.getPoints()+3);
					team.setGoalAgainst(team2.getGoalAgainst()+recTeamgoals);
					team.setGoalScored(team2.getGoalScored()+visTeamgoals);
					team.setGenericStatus(ACTIVE);
					team.setUpdatedBy(usersSession.getUsername());
					teamImpl.UpdateTeam(team);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.teamResult"));
					LOGGER.info(CLASSNAME + ":::team Details is saved");
					clearTeamFuileds();
					
				} else if(recTeamgoals==visTeamgoals) {
					team.setTeamId(team2.getTeamId());
					team.setAddress(team2.getAddress());
					team.setCreatedBy(team2.getCreatedBy());
					team.setTeamName(team2.getTeamName());
					team.setPhone(team2.getPhone());
					team.setPobox(team2.getPobox());
					team.setEmail(team2.getEmail());
					team.setStade(team2.getStade());
					team.setLogo(team2.getLogo());
					team.setMathPlayed(team2.getMathPlayed()+1);
					team.setDraw(team2.getDraw()+1);
					team.setWin(team2.getWin());
					team.setLose(team2.getLose());
					team.setPoints(team2.getPoints()+1);
					team.setGoalAgainst(team2.getGoalAgainst()+recTeamgoals);
					team.setGoalScored(team2.getGoalScored()+visTeamgoals);
					team.setGenericStatus(ACTIVE);
					team.setUpdatedBy(usersSession.getUsername());
					teamImpl.UpdateTeam(team);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.teamResult"));
					LOGGER.info(CLASSNAME + ":::team Details is saved");
					clearTeamFuileds();
				}else {
					team.setTeamId(team2.getTeamId());
					team.setAddress(team2.getAddress());
					team.setCreatedBy(team2.getCreatedBy());
					team.setTeamName(team2.getTeamName());
					team.setPhone(team2.getPhone());
					team.setPobox(team2.getPobox());
					team.setEmail(team2.getEmail());
					team.setStade(team2.getStade());
					team.setLogo(team2.getLogo());
					team.setMathPlayed(team2.getMathPlayed()+1);
					team.setDraw(team2.getDraw());
					team.setWin(team2.getWin());
					team.setLose(team2.getLose()+1);
					team.setPoints(team2.getPoints());
					team.setGoalAgainst(team2.getGoalAgainst()+recTeamgoals);
					team.setGoalScored(team2.getGoalScored()+visTeamgoals);
					team.setGenericStatus(ACTIVE);
					team.setUpdatedBy(usersSession.getUsername());
					teamImpl.UpdateTeam(team);
					JSFMessagers.resetMessages();
					setValid(true);
					JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.teamResult"));
					LOGGER.info(CLASSNAME + ":::team Details is saved");
					clearTeamFuileds();
				}
			}		
		} catch (Exception e) {
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + e.getMessage());
			e.printStackTrace();
		}

	}
	
	public void clearTeamFuileds() {
		team= new Team();
		this.receiveTeamgoals= new String();
		this.visitTeamgoals= new String();
	}
	public void postponeMatch(MatchClass info) {
		if (null != info) {
			this.renderCal = true;
			this.renderTable = false;
			HttpSession sessionuser = SessionUtils.getSession();
			sessionuser.setAttribute("postpone", info);
			LOGGER.info("TEAM INFO:::::::::DATE:" + info.getMatchDate() + "ID:::::" + info.getMatchId());

		}
	}

	@SuppressWarnings("static-access")
	public void saveMatchPostPoned() {
		try {
			try {

				Formating fmt = new Formating();
				List<MatchClass> clasView = new ArrayList<MatchClass>();

				for (Object[] data : classImpl.reportList(
						" select c.matchId,c.genericStatus,c.matchDate,c.matchDay,c.matchPhase,c.matchStatus,c.champion,c.stade,c.vsTeam,c.team from MatchClass c,Team t,Stadium s,Champion ch where t.teamId=c.team and s.stadeId=c.stade and ch.champId=c.champion and c.matchDate='"
								+ fmt.getMysqlFormatV2(to) + "'")) {
					LOGGER.info("RELATED INFO::::::::::::::::::::::::::::::::::::::::::::::::>>" + data[0] + ":: "
							+ data[2] + "");
					MatchClass clas = new MatchClass();
					clas.setMatchId(Integer.parseInt(data[0] + ""));
					clas.setGenericStatus(data[1] + "");
					clas.setMatchDate((Date) data[2]);
					clas.setMatchDay(data[3] + "");
					clas.setMatchPhase(data[4] + "");
					clas.setMatchStatus(data[5] + "");
					clas.setChampion((Champion) data[6]);
					clas.setStade((Stadium) data[7]);
					clas.setVsTeam(data[8] + "");
					clas.setTeam((Team) data[9]);
					clasView.add(clas);
				}

				LOGGER.info("LIST SIZE:::" + clasView.size());
				if (clasView.size() > 0) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.matchdate"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: MatchDate is already scheduled in the system! ");
				}
			} catch (Exception e) {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
				LOGGER.info(CLASSNAME + "" + e.getMessage());
				e.printStackTrace();

			}
			MatchClass matches = new MatchClass();
			HttpSession session = SessionUtils.getSession();
			matches = (MatchClass) session.getAttribute("postpone");
			LOGGER.info("MATCH FOUNDED" + matches.getVsTeam() + " -----" + matches.getTeam().getTeamName());

			if (null != matches && null != to) {
				match.setMatchId(matches.getMatchId());
				match.setChampion(matches.getChampion());
				match.setMatchDate(new java.sql.Date(to.getTime()));
				match.setCreatedBy(matches.getCreatedBy());
				match.setMatchDay(matches.getMatchDay());
				match.setMatchPhase(matches.getMatchPhase());
				match.setStade(matches.getStade());
				match.setTeam(matches.getTeam());
				match.setUpdatedBy(usersSession.getUsername());
				match.setVsTeam(matches.getVsTeam());
				match.setGenericStatus(matches.getGenericStatus());
				match.setMatchStatus(matches.getMatchStatus());
				classImpl.UpdateMatchClass(match);
				JSFMessagers.resetMessages();
				setValid(true);
				JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.postponed"));
				LOGGER.info(CLASSNAME + ":::Match postponed");
				clearUserFuileds();
			} else {
				JSFMessagers.resetMessages();
				setValid(false);
				JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.match"));
				LOGGER.info(CLASSNAME + "sivaserside validation :: match not  recorded in the system! ");
			}
		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::matchreferee Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}

	}

	public void cancel() {
		this.renderCal = false;
		this.renderPost = true;
	}

	public String getMyFormattedDate(Date myDate) {
		return new SimpleDateFormat("dd-MM-yyyy").format(myDate);
	}

	public MatchClass addMatchReferees() {
		HttpSession session = SessionUtils.getSession();
		// Get the values from the session
		match = (MatchClass) session.getAttribute("matchReferees");
		return (match);
	}

	public String saveMatchReferees() {

		try {
			match = addMatchReferees();
			LOGGER.info("MATCH FOUNDED" + match.getVsTeam() + " -----" + match.getTeam().getTeamName());
			Referees refer = new Referees();
			try {

				refer = referImpl.getModelWithMyHQL(new String[] { "referId" }, new Object[] { referId },
						"from Referees");
				if (null == match) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.match"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: match not  recorded in the system! ");
					return null;
				}
				if (null == refer) {
					JSFMessagers.resetMessages();
					setValid(false);
					JSFMessagers.addErrorMessage(getProvider().getValue("error.server.side.refer"));
					LOGGER.info(CLASSNAME + "sivaserside validation :: Referee not  recorded in the system! ");
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
			matchRefer.setCreatedBy(usersSession.getUsername());
			matchRefer.setGenericStatus(ACTIVE);
			matchRefer.setReferee(refer);
			matchRefer.setMatchclass(match);
			matchreferImpl.saveMatchRefer(matchRefer);
			JSFMessagers.resetMessages();
			setValid(true);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.save.form.matchreferees"));
			LOGGER.info(CLASSNAME + ":::matchreferee Details is saved");
			clearUserFuileds();

		} catch (HibernateException ex) {
			LOGGER.info(CLASSNAME + ":::matchreferee Details is fail with HibernateException  error");
			JSFMessagers.resetMessages();
			setValid(false);
			JSFMessagers.addErrorMessage(getProvider().getValue("com.server.side.internal.error"));
			LOGGER.info(CLASSNAME + "" + ex.getMessage());
			ex.printStackTrace();
		}
		return "";
	}

	public void writePDFToResponse(ExternalContext externalContext, ByteArrayOutputStream baos, String fileName) {
		try {
			externalContext.responseReset();
			externalContext.setResponseContentType("application/pdf");
			externalContext.setResponseHeader("Expires", "0");
			externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			externalContext.setResponseHeader("Pragma", "public");
			externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
			externalContext.setResponseContentLength(baos.size());
			OutputStream out = externalContext.getResponseOutputStream();
			baos.writeTo(out);
			externalContext.responseFlushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public JSFBoundleProvider getProvider() {
		return provider;
	}

	public void setProvider(JSFBoundleProvider provider) {
		this.provider = provider;
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

	public Champion getChamp() {
		return champ;
	}

	public void setChamp(Champion champ) {
		this.champ = champ;
	}

	public Stadium getStade() {
		return stade;
	}

	public void setStade(Stadium stade) {
		this.stade = stade;
	}

	public MatchClass getMatch() {
		return match;
	}

	public void setMatch(MatchClass match) {
		this.match = match;
	}

	public List<Stadium> getStadeDetails() {
		return stadeDetails;
	}

	public void setStadeDetails(List<Stadium> stadeDetails) {
		this.stadeDetails = stadeDetails;
	}

	public List<Champion> getChampDetails() {
		return champDetails;
	}

	public void setChampDetails(List<Champion> champDetails) {
		this.champDetails = champDetails;
	}

	public List<Player> getPlayerDetails() {
		return playerDetails;
	}

	public void setPlayerDetails(List<Player> playerDetails) {
		this.playerDetails = playerDetails;
	}

	public List<MatchClass> getMatchDetails() {
		return matchDetails;
	}

	public void setMatchDetails(List<MatchClass> matchDetails) {
		this.matchDetails = matchDetails;
	}

	public PlayerImpl getPlayerImpl() {
		return playerImpl;
	}

	public void setPlayerImpl(PlayerImpl playerImpl) {
		this.playerImpl = playerImpl;
	}

	public StadiumImpl getStadeImpl() {
		return stadeImpl;
	}

	public void setStadeImpl(StadiumImpl stadeImpl) {
		this.stadeImpl = stadeImpl;
	}

	public ChampionImpl getChampImpl() {
		return champImpl;
	}

	public void setChampImpl(ChampionImpl champImpl) {
		this.champImpl = champImpl;
	}

	public MatchClassImpl getClassImpl() {
		return classImpl;
	}

	public void setClassImpl(MatchClassImpl classImpl) {
		this.classImpl = classImpl;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public List<Team> getTeamBDetails() {
		return teamBDetails;
	}

	public void setTeamBDetails(List<Team> teamBDetails) {
		this.teamBDetails = teamBDetails;
	}

	public List<MatchClass> getViewFixtures() {
		return viewFixtures;
	}

	public void setViewFixtures(List<MatchClass> viewFixtures) {
		this.viewFixtures = viewFixtures;
	}

	public boolean isRenderFooter() {
		return renderFooter;
	}

	public void setRenderFooter(boolean renderFooter) {
		this.renderFooter = renderFooter;
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

	public int getReferId() {
		return referId;
	}

	public void setReferId(int referId) {
		this.referId = referId;
	}

	public boolean isRenderRefer() {
		return renderRefer;
	}

	public void setRenderRefer(boolean renderRefer) {
		this.renderRefer = renderRefer;
	}

	public boolean isRenderTable() {
		return renderTable;
	}

	public void setRenderTable(boolean renderTable) {
		this.renderTable = renderTable;
	}

	public MatchReferees getMatchRefer() {
		return matchRefer;
	}

	public void setMatchRefer(MatchReferees matchRefer) {
		this.matchRefer = matchRefer;
	}

	public MatchRefereesImpl getMatchreferImpl() {
		return matchreferImpl;
	}

	public void setMatchreferImpl(MatchRefereesImpl matchreferImpl) {
		this.matchreferImpl = matchreferImpl;
	}

	public List<MatchReferees> getViewMatchReferees() {
		return viewMatchReferees;
	}

	public void setViewMatchReferees(List<MatchReferees> viewMatchReferees) {
		this.viewMatchReferees = viewMatchReferees;
	}

	public boolean isRenderCal() {
		return renderCal;
	}

	public void setRenderCal(boolean renderCal) {
		this.renderCal = renderCal;
	}

	public boolean isRenderPost() {
		return renderPost;
	}

	public void setRenderPost(boolean renderPost) {
		this.renderPost = renderPost;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public boolean isRenderDataTable() {
		return renderDataTable;
	}

	public void setRenderDataTable(boolean renderDataTable) {
		this.renderDataTable = renderDataTable;
	}

	public boolean isRenderDatePanel() {
		return renderDatePanel;
	}

	public void setRenderDatePanel(boolean renderDatePanel) {
		this.renderDatePanel = renderDatePanel;
	}

	public boolean isRendercombo() {
		return rendercombo;
	}

	public void setRendercombo(boolean rendercombo) {
		this.rendercombo = rendercombo;
	}

	public List<Champion> getChapDetails() {
		return chapDetails;
	}

	public void setChapDetails(List<Champion> chapDetails) {
		this.chapDetails = chapDetails;
	}

	public String getReceiveTeamgoals() {
		return receiveTeamgoals;
	}

	public void setReceiveTeamgoals(String receiveTeamgoals) {
		this.receiveTeamgoals = receiveTeamgoals;
	}

	public String getVisitTeamgoals() {
		return visitTeamgoals;
	}

	public void setVisitTeamgoals(String visitTeamgoals) {
		this.visitTeamgoals = visitTeamgoals;
	}

	public List<Team> getTeamsRanking() {
		return teamsRanking;
	}

	public void setTeamsRanking(List<Team> teamsRanking) {
		this.teamsRanking = teamsRanking;
	}

	public List<Player> getRecTeamPlayers() {
		return recTeamPlayers;
	}

	public void setRecTeamPlayers(List<Player> recTeamPlayers) {
		this.recTeamPlayers = recTeamPlayers;
	}

	public List<Player> getVsTeamPlayers() {
		return vsTeamPlayers;
	}

	public void setVsTeamPlayers(List<Player> vsTeamPlayers) {
		this.vsTeamPlayers = vsTeamPlayers;
	}

	public int getVisTeamPlayer() {
		return visTeamPlayer;
	}

	public void setVisTeamPlayer(int visTeamPlayer) {
		this.visTeamPlayer = visTeamPlayer;
	}

	public int getRecTeamPlayer() {
		return recTeamPlayer;
	}

	public void setRecTeamPlayer(int recTeamPlayer) {
		this.recTeamPlayer = recTeamPlayer;
	}
	public String getVsPlGoal() {
		return vsPlGoal;
	}

	public void setVsPlGoal(String vsPlGoal) {
		this.vsPlGoal = vsPlGoal;
	}

	public String getRecPlGoal() {
		return recPlGoal;
	}

	public void setRecPlGoal(String recPlGoal) {
		this.recPlGoal = recPlGoal;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean isRenderGoal() {
		return renderGoal;
	}

	public void setRenderGoal(boolean renderGoal) {
		this.renderGoal = renderGoal;
	}

	public boolean isRenderScore() {
		return renderScore;
	}

	public void setRenderScore(boolean renderScore) {
		this.renderScore = renderScore;
	}

	public List<Team> getTeamstanding() {
		return teamstanding;
	}

	public void setTeamstanding(List<Team> teamstanding) {
		this.teamstanding = teamstanding;
	}

	public List<Player> getPlayerstatistics() {
		return playerstatistics;
	}

	public void setPlayerstatistics(List<Player> playerstatistics) {
		this.playerstatistics = playerstatistics;
	}

}
