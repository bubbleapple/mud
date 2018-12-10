package Model;

import Utils.DB.SingleConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {
	private Map<Integer, MapNode> map;
	private int initial_position;
	private String welcomeMessage;
	private int id;
	
	public GameMap() {
		map = null;
		initial_position = -1;
	}

	public GameMap(int[] nodesIds, String[] names, String[] descriptions, 
				   MapConnection[] cons, int init, String welcomeMessage,
				   int mapId) {
		int N = nodesIds.length;
		map = new HashMap<>();
		MapNode[] nodes = new MapNode[N];
		for (int i = 0; i < N; i++) {
			nodes[i] = new MapNode(nodesIds[i], new HashMap<String, Integer>(), names[i], descriptions[i]);
			map.put(nodesIds[i], nodes[i]);
		}
		for (MapConnection con : cons) {
				con.connect(this);
		}
		initial_position = init;
		this.welcomeMessage = welcomeMessage;
		this.id = mapId;
	}

	/**
	 * currently used in test
	 * @return a map with key of node index, value of node instance
	 */
	public Map<Integer, MapNode> getMap() {
		return map;
	}
		
	public static GameMap getSampleMap(int mapId) {
		int[] ids = {0, 1, 2, 3};
		String[] names = {"living room", "kitchen", "roomA", "roomB"};
		String[] descriptions = {"This is a cozy place! All the four guys used to hang out here.",
								 "Nice kitchen, though it is a little dirty. Do you want to dump the trash for them?",
								 "Wow, the largest room of this apartment! It seems the host is not here.",
								 "Another nice room. Wait, what's this smell?"};
		MapConnection[] cons = {
				new MapConnection(0, 1, "kitchen", "living_room"),
				new MapConnection(1, 3, "roomB", "out"),
				new MapConnection(0, 2, "roomA", "out"),
			};
		return new GameMap(ids, names, descriptions, cons, 0, "Welcome to the apartment 933!!", mapId);
	}
	
	public String welcome() {
		return "\n" + welcomeMessage + "\n";
	}
	
	public MapNode getInitPosition() {
		return map.get(initial_position);
	}
		


	/**
	 * return the gamemap from Database
	 * current all use singleton method
	 * @return a GameMap instance
	 */
	public static Map<Integer, GameMap> getGameMapFromDB() {
		// get all node id
		Map<Integer, GameMap> res = new HashMap<>();
		try {
			Statement stmt = SingleConnection.getInstance().getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from map");
			while(rs.next()) {
				res.put(rs.getInt("id"), getGameMapByResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static GameMap getGameMapByResultSet(ResultSet rs) {
		try {
			int mapId = rs.getInt("id");
			String mapName = rs.getString("name");
			String mapDescription = rs.getString("description");
			int initId = rs.getInt("enterId");

			int count = getNodeLengthByMapId(mapId);

			// get the node data
			int[] nodesIds = new int[count];
			String[] names = new String[count];
			String[] descriptions = new String[count];

			PreparedStatement stmt = SingleConnection.getInstance().getConnection().prepareStatement("select * from map_node where mapId = ?");
			stmt.setInt(1, mapId);
			ResultSet nodeRs = stmt.executeQuery();
			int index = 0;
			while(nodeRs.next()) {
				nodesIds[index] = nodeRs.getInt("id");
				names[index] = nodeRs.getString("name");
				descriptions[index] = nodeRs.getString("description");
				index++;
			}

			// getMap connections
			int conLength = getConLengthByMapId(mapId);
			MapConnection [] cons = new MapConnection[conLength];

			stmt = SingleConnection.getInstance().getConnection().prepareStatement("select * from map_connection where mapId = ?");
			stmt.setInt(1, mapId);
			ResultSet conRs = stmt.executeQuery();
			index = 0;
			while(conRs.next()) {
				cons[index++] = new MapConnection(
						conRs.getInt("fromId"),
						conRs.getInt("toId"),
						conRs.getString("s2e"),
						conRs.getString("e2s")
					);
			}

			return new GameMap(nodesIds, names, descriptions, cons, initId, mapDescription, mapId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int getNodeLengthByMapId(int mapId) throws SQLException {
		PreparedStatement stmt = SingleConnection.getInstance().getConnection().prepareStatement("select count(*) from map_node where mapId = ?");
		stmt.setInt(1, mapId);
		ResultSet countRs = stmt.executeQuery();
		countRs.next();
		return countRs.getInt(1);
	}

	private static int getConLengthByMapId(int mapId) throws SQLException {
		PreparedStatement stmt = SingleConnection.getInstance().getConnection().prepareStatement("select count(*) from map_connection where mapId = ?");
		stmt.setInt(1, mapId);
		ResultSet countRs = stmt.executeQuery();
		countRs.next();
		return countRs.getInt(1);
	}
}
