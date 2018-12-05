package Model;

import Utils.DB.SingleConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Node {
	private int id;
	private List<Integer> neightbors;
	private String name;

	public Node(int id, List<Integer> neighbors, String name) {
		this.id = id;
		this.neightbors = neighbors;
		this.name = name;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id).append(',').append(name);

		sb.append('[');
		for(Integer nei : neightbors) {
			sb.append(nei).append(',');
		}
		sb.append(']');
		return sb.toString();
	}

	public static Node getNode(int id, String name) {
		try {
			PreparedStatement stmt = SingleConnection.getInstance().getConnection().prepareStatement("select * from map_connection where fromId = ? ");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			List<Integer> neighborId = new ArrayList<>();

			while(rs.next()) {
				int toId = rs.getInt("toId");
				neighborId.add(toId);
			}
			return new Node(id, neighborId, name);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
