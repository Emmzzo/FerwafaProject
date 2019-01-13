package ferwafa.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import ferwafa.dao.generic.AbstractDao;
import ferwafa.dao.interfc.ITeam;
import ferwafa.domain.Champion;
import ferwafa.domain.Team;


public class TeamImpl extends AbstractDao<Long, Team> implements ITeam{
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	@Override
	public Team saveTeam(Team team) {
		return saveIntable(team);
	}

	@Override
	public List<Team> getListTeam() {
		return (List<Team>) (Object) getModelList();
	}

	@Override
	public Team getTeamById(int teamId, String primaryKeyclomunName) {
		return (Team) getModelById(teamId, primaryKeyclomunName);
	}

	@Override
	public Team UpdateTeam(Team team) {
		return updateIntable(team);
	}

	@Override
	public String myNane() {
		return"Emma";
	}

	@Override
	public Team getTeamWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (Team) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}

	
}