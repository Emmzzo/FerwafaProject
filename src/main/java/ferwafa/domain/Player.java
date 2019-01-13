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
import javax.persistence.Transient;

/**
 *
 * @author Emmanuel
 */
@Entity
@Table(name = "Player")
@NamedQuery(name = "Player.findAll", query = "SELECT r FROM Player r order by playerId desc")
public class Player extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "playerId")
	private int playerId;
	@Column(name = "fname")
	private String fname;
	@Column(name = "lname")
	private String lname;
	@Column(name = "DateOfBirth", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date DateOfBirth;

	@Column(name = "image")
	private String image;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "phone", unique = true)
	private String phone;

	@Column(name = "itc")
	private String itc;
	@Column(name = "nationality")
	private String nationality;
	@Column(name = "contractYear")
	private int contractYear;
	@ManyToOne
	@JoinColumn(name = "team")
	private Team team;
	@JoinColumn(name = "goals")
	private int goals;
	@JoinColumn(name = "goalstatus")
	private String goalstatus;
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public Date getDateOfBirth() {
		return DateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getItc() {
		return itc;
	}
	public void setItc(String itc) {
		this.itc = itc;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public int getContractYear() {
		return contractYear;
	}
	public void setContractYear(int contractYear) {
		this.contractYear = contractYear;
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
	public int getGoals() {
		return goals;
	}
	public void setGoals(int goals) {
		this.goals = goals;
	}
	public String getGoalstatus() {
		return goalstatus;
	}
	public void setGoalstatus(String goalstatus) {
		this.goalstatus = goalstatus;
	}	
}
