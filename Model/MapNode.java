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
	private Set<User> users;

	public MapNode(int id, Map<String, Integer> neighbors, String name, String des) {
		this.id = id;
		this.neighbors = neighbors;
		this.name = name;
		this.description = des;
		this.users = new HashSet<>();
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
	
	public synchronized void register(User user, String lastPositionName) {
		//TODO: announce new user to other users
		String info = " suddenly occurs";
		if (lastPositionName != null) {
			info = " comes from " + lastPositionName;
		}
		for(User u : users) {
			u.print("\n" + user.getName() + info + ".\n");
		}
		users.add(user);
		user.setPosition(this);
	}
	public synchronized void release(User user, String direction) {
		//TODO: announce new user to other users
		users.remove(user);
		for(User u : users) {
			u.print("\n" + user.getName() + " leaves " + name + " and goes to " + direction + ".\n");
		}
	}

	public synchronized void releaseWithUserQuit(User user) {
		users.remove(user);
		for(User u : users) {
			u.print("\n" + user.getName() + " leaves game.\n");
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("**").append(name).append("**\n").append(description).append("\n");
		sb.append("Directions: ");
		sb.append(String.join(", ", neighbors.keySet()));
		sb.append("\nPeople:\n");
		for(User u : users) {
			sb.append(u.getName()).append("\n");
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
