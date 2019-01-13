package ferwafa.rsi.dto;

import java.io.Serializable;

public class StadiumDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private int stadeId;
	private String stadeName;
	private String address;
	private boolean editable;
	private String genericStatus;
	private String createdBy;
	public String getGenericStatus() {
		return genericStatus;
	}
	public void setGenericStatus(String genericStatus) {
		this.genericStatus = genericStatus;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public int getStadeId() {
		return stadeId;
	}
	public void setStadeId(int stadeId) {
		this.stadeId = stadeId;
	}
	public String getStadeName() {
		return stadeName;
	}
	public void setStadeName(String stadeName) {
		this.stadeName = stadeName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
}
