package ferwafa.rsi.dto;

import java.io.Serializable;
import java.util.Date;
import ferwafa.domain.Team;

public class PlayerDto implements Serializable  {

	private static final long serialVersionUID = 1L;
	private int playerId;
	private String fname;
	private String lname;
	private Date DateOfBirth;
	private String image;
	private String email;
	private String phone;
	private String itc;
	private String nationality;
	private int contractYear;
	private Team team;
	private boolean editable;
	private String genericStatus;
	private String updatedBy;
	private String action;
	private String createdBy;
	private int goals;
	public int getGoals() {
		return goals;
	}
	public void setGoals(int goals) {
		this.goals = goals;
	}
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
	public int getContractYear() {
		return contractYear;
	}
	public void setContractYear(int contractYear) {
		this.contractYear = contractYear;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public String getGenericStatus() {
		return genericStatus;
	}
	public void setGenericStatus(String genericStatus) {
		this.genericStatus = genericStatus;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	

}
