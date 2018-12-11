package Model;

import Utils.DB.SingleConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.io.DataOutputStream;



public class MapNode {
	private int id;
	private Map<String, Integer> neighbors; // neighbor node id --> direction
	private String name;
	private String description;
	private Set<Character> characters;

	public MapNode(int id, Map<String, Integer> neighbors, String name, String des) {
		this.id = id;
		this.neighbors = neighbors;
		this.name = name;
		this.description = des;
		this.characters = new HashSet<>();
	}
	
	public Map<String, Integer> getNeighbors() {
		return neighbors;
	}
	
	public int getId () {
		return id;
	}
	public String getName() {
		return name;
	}
	
	public synchronized void register(Character user, String lastPositionName) {
		//TODO: announce new user to other users
		String info = " suddenly occurs";
		if (lastPositionName != null) {
			info = " comes from " + lastPositionName;
		}
		for(Character c : characters) {
			c.print("\n" + user.getName() + info + ".\n");
		}
		characters.add(user);
		user.setPosition(this);
	}
	public synchronized void release(Character user, String direction) {
		//TODO: announce new user to other users
		characters.remove(user);
		for(Character c : characters) {
			c.print("\n" + user.getName() + " leaves " + name + " and goes to " + direction + ".\n");
		}
	}

	public synchronized void release(Character user) {
		characters.remove(user);
		for(Character c : characters) {
			c.print("\n" + user.getName() + " leaves game.\n");
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("**").append(name).append("**\n").append(description).append("\n");
		sb.append("Directions: ");
		sb.append(String.join(", ", neighbors.keySet()));
		sb.append("\nPeople:\n");
		for(Character c : characters) {
			sb.append(c.getName()).append("\n");
		}
		return sb.toString();
	}
	
//
//	public static Node getNode(int id, String name) {
//		try {
//			PreparedStatement stmt = SingleConnection.getInstance().getConnection().prepareStatement("select * from map_connection where fromId = ? ");
//			stmt.setInt(1, id);
//			ResultSet rs = stmt.executeQuery();
//			List<Integer> neighborId = new ArrayList<>();
//
//			while(rs.next()) {
//				int toId = rs.getInt("toId");
//				neighborId.add(toId);
//			}
//			return new Node(id, neighborId, name);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
}
