package ferwafa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Emmanuel
 */
@Entity
@Table(name = "MatchClass")
@NamedQuery(name = "MatchClass.findAll", query = "SELECT r FROM MatchClass r order by matchId desc")
public class MatchClass extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "matchId")
	private int matchId;

	@Column(name = "matchDay")
	private String matchDay;
	@Column(name = "matchStatus")
	private String matchStatus;
	@Column(name = "matchDate", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date matchDate;
	@Column(name = "matchPhase")
	private String matchPhase;
	@Column(name = "vsTeam")
	private String vsTeam;
	@ManyToOne
	@JoinColumn(name = "champion")
	private Champion champion;
	@ManyToOne
	@JoinColumn(name = "team")
	private Team team;
	@ManyToOne
	@JoinColumn(name = "stade")
	private Stadium stade;

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public String getMatchDay() {
		return matchDay;
	}

	public void setMatchDay(String matchDay) {
		this.matchDay = matchDay;
	}

	public String getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(String matchStatus) {
		this.matchStatus = matchStatus;
	}

	public Date getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	public String getMatchPhase() {
		return matchPhase;
	}

	public void setMatchPhase(String matchPhase) {
		this.matchPhase = matchPhase;
	}

	public String getVsTeam() {
		return vsTeam;
	}

	public void setVsTeam(String vsTeam) {
		this.vsTeam = vsTeam;
	}

	public Champion getChampion() {
		return champion;
	}

	public void setChampion(Champion champion) {
		this.champion = champion;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Stadium getStade() {
		return stade;
	}

	public void setStade(Stadium stade) {
		this.stade = stade;
	}

}
