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
@Table(name = "Stadium")
@NamedQuery(name = "Stadium.findAll", query = "SELECT r FROM Stadium r order by stadeId desc")
public class Stadium extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "stadeId")
	private int stadeId;
	@Column(name = "stadeName")
	private String stadeName;
	@Column(name = "address")
	private String address;
	@Column(name = "stadeStatus")
	private String stadeStatus;
	
	public String getStadeStatus() {
		return stadeStatus;
	}
	public void setStadeStatus(String stadeStatus) {
		this.stadeStatus = stadeStatus;
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
	
}