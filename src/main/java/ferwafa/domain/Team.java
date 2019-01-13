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
@Table(name = "Team")
@NamedQuery(name = "Team.findAll",
        query = "SELECT r FROM Team r order by teamId desc")
public class Team  extends CommonDomain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name = "teamId")
    private int teamId;

    @Column(name = "teamName")
    private String teamName;
	@Column(name = "address")
	private String address;
	@Column(name = "logo")
	private String logo;
	@Column(name = "email", unique = true)
	private String email;
	@Column(name = "phone", unique = true)
	private String phone;
	@Column(name = "pobox", unique = true)
	private String pobox;
	@Column(name = "stade", unique = true)
	private String stade;
	@JoinColumn(name = "matchPlayed")
	private int mathPlayed;
	@JoinColumn(name = "win")
	private int win;
	@JoinColumn(name = "draw")
	private int draw;
	@JoinColumn(name = "lose")
	private int lose;
	@JoinColumn(name = "goalScored")
	private int goalScored;
	@JoinColumn(name = "goalAgainst")
	private int goalAgainst;
	@JoinColumn(name = "points")
	private int points;
	public String getStade() {
		return stade;
	}
	public void setStade(String stade) {
		this.stade = stade;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
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
	public String getPobox() {
		return pobox;
	}
	public void setPobox(String pobox) {
		this.pobox = pobox;
	}
	public int getMathPlayed() {
		return mathPlayed;
	}
	public void setMathPlayed(int mathPlayed) {
		this.mathPlayed = mathPlayed;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getLose() {
		return lose;
	}
	public void setLose(int lose) {
		this.lose = lose;
	}
	public int getGoalScored() {
		return goalScored;
	}
	public void setGoalScored(int goalScored) {
		this.goalScored = goalScored;
	}
	public int getGoalAgainst() {
		return goalAgainst;
	}
	public void setGoalAgainst(int goalAgainst) {
		this.goalAgainst = goalAgainst;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
}
