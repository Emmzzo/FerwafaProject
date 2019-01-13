package ferwafa.rsi.dto;

import java.io.Serializable;
import java.util.Date;
import ferwafa.domain.Users;

public class ChampionDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private int champId;
    private String championYear;
    private String championStatus;
	private Date startDate;
	private Date endDate;
	private Date recordedDate;
	private Users users;
	private boolean editable;
	private String genericStatus;
	public int getChampId() {
		return champId;
	}
	public void setChampId(int champId) {
		this.champId = champId;
	}
	public String getChampionYear() {
		return championYear;
	}
	public void setChampionYear(String championYear) {
		this.championYear = championYear;
	}
	public String getChampionStatus() {
		return championStatus;
	}
	public void setChampionStatus(String championStatus) {
		this.championStatus = championStatus;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getRecordedDate() {
		return recordedDate;
	}
	public void setRecordedDate(Date recordedDate) {
		this.recordedDate = recordedDate;
	}
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
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
	
}
