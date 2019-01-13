package ferwafa.dao.interfc;

import java.util.List;

import ferwafa.domain.Team;


public interface ITeam {
	public Team saveTeam(Team team);

	public List<Team> getListTeam();

	public Team getTeamById(int teamId, String primaryKeyclomunName);

	public Team UpdateTeam(Team team);

	public String myNane();

	public Team getTeamWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}
