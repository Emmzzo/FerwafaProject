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
@Table(name = "Champion")
@NamedQuery(name = "Champion.findAll",
        query = "SELECT r FROM Champion r order by champId desc")
public class Champion  extends CommonDomain implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name = "champId")
    private int champId;

    @Column(name = "championYear")
    private String championYear;
    @Column(name = "championStatus")
    private String championStatus;
    @Column(name = "startDate", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	@Column(name = "endDate", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	@Column(name = "recordedDate", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordedDate;
	@ManyToOne
	@JoinColumn(name = "Users")
	private Users users;
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
	
}
