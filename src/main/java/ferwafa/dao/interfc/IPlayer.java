package ferwafa.dao.interfc;

import java.util.List;
import ferwafa.domain.Player;
public interface IPlayer {
	public Player savePlayer(Player player);
	public List<Player> getListPlayer();
	public Player getPlayerById(int playerId, String primaryKeyclomunName);
	public Player UpdatePlayer(Player player);
	public String myNane();
	public Player getPlayerWithQuery(final String[] propertyName, final Object[] value, final String hqlStatement);
}
