package ferwafa.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "MatchReferees")
@NamedQuery(name = "MatchReferees.findAll", query = "select r from MatchReferees r order by matchReferId desc")
public class MatchReferees extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "matchReferId")
	private int matchReferId;

	@ManyToOne
	@JoinColumn(name = "matchclass")
	private MatchClass matchclass;
	
	@ManyToOne
	@JoinColumn(name = "referee")
	private Referees referee;

	public int getMatchReferId() {
		return matchReferId;
	}

	public void setMatchReferId(int matchReferId) {
		this.matchReferId = matchReferId;
	}

	public MatchClass getMatchclass() {
		return matchclass;
	}

	public void setMatchclass(MatchClass matchclass) {
		this.matchclass = matchclass;
	}

	public Referees getReferee() {
		return referee;
	}

	public void setReferee(Referees referee) {
		this.referee = referee;
	}

	
}
