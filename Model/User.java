package Model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import IO.IO;

public class User {
	private MapNode currentPosition;
	private GameMap currentMap;
	private String name;
	private String password;
	private int mapId;
	private IO output;
	
	public User(GameMap map, MapNode node, String n, String p) {
		currentPosition = node;
		currentMap = map;
		name = n;
		password = p;
		node.register(this, null);
		output = null;
	}
	public synchronized MapNode getPosition() {
		return currentPosition;
	}
	
	public synchronized void setPosition(MapNode p) {
		currentPosition = p;
	}

	public static User getSampleUser(GameMap map, int id) {
		if (id == 0)
			return new User(map, map.getInitPosition(), "xiaoming", "abc");
		else
			return new User(map, map.getInitPosition(), "panghu", "abc");
	}
	public String getName() {
		return name;
	}
	public GameMap getGameMap() {
		return currentMap;
	}
	public synchronized void updateOutputStream(IO output) {
		this.output = output;
	}
	public synchronized boolean move(String direction) {
		Map<String, Integer> nbrs = currentPosition.getNeighbors();
		if (nbrs.containsKey(direction)) {
			MapNode nextPosition = currentMap.getMap().get(nbrs.get(direction));
			currentPosition.release(this, direction);
			nextPosition.register(this, currentPosition.getName());
			return true;
		}
		return false;
	}
	public void print(String s) {
		output.print(s);
	}
}
