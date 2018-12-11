package Model;


import java.util.HashMap;
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

	public int getMapId() {
		return this.id;
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

	public MapNode getNodeById(int id) {
		if(map.containsKey(id)) return map.get(id);
		throw new NullPointerException("Game map not contains that node id");
	}
}
