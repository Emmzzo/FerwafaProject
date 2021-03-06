package ferwafa.domain;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
@Table(name = "Users")
@NamedQuery(name = "Users.findAll", query = "SELECT r FROM Users r order by userId desc")
public class Users extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "userId")
	private int userId;

	/* this is for userName */
	@Column(name = "username", unique = true)
	private String username;

	/* this is for pasword */
	@Column(name = "password")
	private String password;

	@Column(name = "fname")
	private String fname;

	@Column(name = "lname")
	private String lname;

	@Column(name = "address")
	private String address;

	@Column(name = "gender")
	private String gender;

	@Column(name = "DateOfBirth", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date DateOfBirth;

	@Column(name = "image")
	private String image;

	@Column(name = "loginStatus")
	private String loginStatus;

	@Column(name = "status")
	private String status;

	@Column(name = "createdDate", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "phone", unique = true)
	private String phone;

	@ManyToOne
	@JoinColumn(name = "userCategory")
	private UserCategory userCategory;

	@ManyToOne
	@JoinColumn(name = "registrationRequest")
	private RegistrationRequest registrationRequest;
	@Transient
	private String action;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public UserCategory getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(UserCategory userCategory) {
		this.userCategory = userCategory;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return DateOfBirth;
	}

	public void setDateOfBirth(Date DateOfBirth) {
		this.DateOfBirth = DateOfBirth;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return fname + " " + lname;
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

	public RegistrationRequest getRegistrationRequest() {
		return registrationRequest;
	}

	public void setRegistrationRequest(RegistrationRequest registrationRequest) {
		this.registrationRequest = registrationRequest;
	}
}
