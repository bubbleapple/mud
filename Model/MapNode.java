package Model;

import Utils.DB.SingleConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;



public class MapNode {
	private int id;
	private Map<String, Integer> neighbors; // neighbor node id --> direction
	private String name;
	private String description;

	public MapNode(int id, Map<String, Integer> neighbors, String name, String des) {
		this.id = id;
		this.neighbors = neighbors;
		this.name = name;
		this.description = des;
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("\n\n").append(description).append("\n\n");
		sb.append("Directions: ");
		sb.append(String.join(", ", neighbors.keySet()));
		sb.append("\n\n");
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
