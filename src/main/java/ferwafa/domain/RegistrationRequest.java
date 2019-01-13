package ferwafa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
/**
*
* @author Emmanuel
*/
@Entity
@Table(name = "RegistrationRequest")
@NamedQuery(name = "RegistrationRequest.findAll", query = "SELECT r FROM RegistrationRequest r order by requestId desc")
public class RegistrationRequest extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "requestId")
	private int requestId;
	@Column(name = "fname")
	private String fname;

	@Column(name = "lname")
	private String lname;
	
	@Column(name = "address")
	private String address;

	@Column(name = "gender")
	private String gender;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "phone", unique = true)
	private String phone;
	@Column(name = "subject")
	private String subject;
	@Column(name = "message")
	private String message;
	@Column(name = "userRole")
	private String userRole;
	@Column(name = "status")
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}

