package Model;

import java.util.Map;

public class User {
	private MapNode currentPosition;
	private GameMap currentMap;
	private String name;
	private String password;
	
	public User(GameMap map, MapNode node, String n, String p) {
		currentPosition = node;
		currentMap = map;
		name = n;
		password = p;
	}
	public MapNode getPosition() {
		return currentPosition;
	}

	public static User getSampleUser() {
		GameMap map = GameMap.getSampleMap();
		return new User(map, map.getInitPosition(), "xiaoming", "abc");
	}
	public String getName() {
		return name;
	}
	public GameMap getMap() {
		return currentMap;
	}
	public boolean go(String direction) {
		Map<String, Integer> nbrs = currentPosition.getNeighbors();
		if (nbrs.containsKey(direction)) {
			currentPosition = currentMap.getMap().get(nbrs.get(direction));
			return true;
		}
		return false;
	}
	
	

}
