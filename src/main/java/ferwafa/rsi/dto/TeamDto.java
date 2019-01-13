package ferwafa.rsi.dto;

import java.io.Serializable;
public class TeamDto  implements Serializable{

    private static final long serialVersionUID = 1L;
    private int teamId;
    private String teamName;
	private String address;
	private String logo;
	private String email;
	private String phone;
	private String pobox;
	private boolean editable;
	private String genericStatus;
	private String updtadedBy;
	private String createdBy;
	private boolean action;
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
	public String getUpdtadedBy() {
		return updtadedBy;
	}
	public void setUpdtadedBy(String updtadedBy) {
		this.updtadedBy = updtadedBy;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
	public boolean isAction() {
		return action;
	}
	public void setAction(boolean action) {
		this.action = action;
	}

}
