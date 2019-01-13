package ferwafa.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import ferwafa.dao.generic.AbstractDao;
import ferwafa.dao.interfc.IPlayer;
import ferwafa.domain.Player;


public class PlayerImpl extends AbstractDao<Long, Player> implements IPlayer{
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	@Override
	public Player savePlayer(Player player) {
		return saveIntable(player);
	}

	@Override
	public List<Player> getListPlayer() {
		return (List<Player>) (Object) getModelList();
	}

	@Override
	public Player getPlayerById(int playerId, String primaryKeyclomunName) {
		return (Player) getModelById(playerId, primaryKeyclomunName);
	}

	@Override
	public Player UpdatePlayer(Player player) {
		return updateIntable(player);
	}

	@Override
	public String myNane() {
		return"Emma";
	}

	@Override
	public Player getPlayerWithQuery(String[] propertyName, Object[] value, String hqlStatement) {
		try {
			return (Player) getModelWithMyHQL(propertyName, value, hqlStatement);
		} catch (Exception ex) {
			LOGGER.info("getUsersWithQuery  Query error ::::" + ex.getMessage());
		}
		return null;
	}
	}