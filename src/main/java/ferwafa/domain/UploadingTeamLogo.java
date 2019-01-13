package ferwafa.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "UploadingTeamLogo")
@NamedQuery(name = "UploadingTeamLogo.findAll", query = "SELECT r FROM UploadingTeamLogo r order by v desc")
public class UploadingTeamLogo extends CommonDomain implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "upLoadId")
	private long upLoadId;
	@OneToOne
	@JoinColumn(name = "documents")
	private Documents documents;
	@ManyToOne
	@JoinColumn(name = "user")
	private Users user;
	@ManyToOne
	@JoinColumn(name = "team")
	private Team team;
	@ManyToOne
	@JoinColumn(name = "player")
	private Player player;
	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public long getUpLoadId() {
		return upLoadId;
	}

	public void setUpLoadId(long upLoadId) {
		this.upLoadId = upLoadId;
	}

	public Documents getDocuments() {
		return documents;
	}

	public void setDocuments(Documents documents) {
		this.documents = documents;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
