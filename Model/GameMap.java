package Model;

import Utils.DB.SingleConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class GameMap {
	Map<Integer, Node> map;
	public GameMap(Map<Integer, Node> map) {
		// place holder
		this.map = map;
	}

	/**
	 * currently used in test
	 * @return a map with key of node index, value of node instance
	 */
	public Map<Integer, Node> getMap() {
		return map;
	}

	/**
	 * return the gamemap from Database
	 * current all use singleton method
	 * @return a GameMap instance
	 */
	public static GameMap getGameMapFromDB() {
		// get all node id
		try {
			Statement stmt = SingleConnection.getInstance().getConnection().createStatement();
			Map<Integer, Node> res = new HashMap<>();
			ResultSet rs = stmt.executeQuery("select * from map_node");
			while(rs.next()) {
				int curId = rs.getInt("id");
				String name = rs.getString("name");
				res.put(curId, Node.getNode(curId, name));
			}
			return new GameMap(res);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
