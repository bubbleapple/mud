package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import IO.IO;

public class Character {
	protected MapNode currentPosition;
	protected GameMap currentMap;
	protected String name;
	protected int mapId;
	protected IO output;
	protected int id;
	
	public Character(GameMap map, MapNode node, String n, int id) {
		currentPosition = node;
		currentMap = map;
		name = n;
		node.register(this, null);
		output = null;
		this.id = id;
	}

	public synchronized MapNode getPosition() {
		return currentPosition;
	}
	
	public synchronized void setPosition(MapNode p) {
		currentPosition = p;
	}

	public int getId() {
		return this.id;
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

	public int getCurMapId() {
		if(currentMap == null) throw new NullPointerException("User don't have map instance");
		return currentMap.getMapId();
	}

	public int getCurNodeId() {
		if(currentPosition == null) throw new NullPointerException("User don't have node instance");
		return currentPosition.getId();
	}

	public synchronized void quit() {
		// release in curNode
		currentPosition.release(this);
	}
}
