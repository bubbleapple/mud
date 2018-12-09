package Model;

import Utils.DB.SingleConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class GameMap {
	private Map<Integer, MapNode> map;
	private int initial_position;
	private String welcomeMessage;
	public GameMap() {
		map = null;
		initial_position = -1;
	}
	public GameMap(int[] ids, String[] names, String[] descriptions, MapConnection[] cons, int init, String welcomeMessage) {
		int N = ids.length;
		map = new HashMap<>();
		MapNode[] nodes = new MapNode[N];
		for (int i = 0; i < N; i++) {
			nodes[i] = new MapNode(ids[i], new HashMap<String, Integer>(), names[i], descriptions[i]);
			map.put(ids[i], nodes[i]);
		}
		for (MapConnection con : cons) {
				con.connect(this);
		}
		initial_position = init;
		this.welcomeMessage = welcomeMessage;
	}
	/**
	 * currently used in test
	 * @return a map with key of node index, value of node instance
	 */
	public Map<Integer, MapNode> getMap() {
		return map;
	}
		
	public static GameMap getSampleMap() {
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
		return new GameMap(ids, names, descriptions, cons, 0, "Welcome to the apartment 933!!");
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
//	public static GameMap getGameMapFromDB() {
//		// get all node id
//		try {
//			Statement stmt = SingleConnection.getInstance().getConnection().createStatement();
//			Map<Integer, Node> res = new HashMap<>();
//			ResultSet rs = stmt.executeQuery("select * from map_node");
//			while(rs.next()) {
//				int curId = rs.getInt("id");
//				String name = rs.getString("name");
//				res.put(curId, Node.getNode(curId, name));
//			}
//			return new GameMap(res);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
}
