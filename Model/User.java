package Model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Map;

import IO.IO;

public class User extends Figure {
	private String password;

	
	public User(GameMap map, MapNode node, String n, String p, int id) {
		super(map, node, n, id);
		node.register(this, null);
		password = p;
	}
	public static User getSampleUser(GameMap map, int id) {
		if (id == 0)
			return new User(map, map.getInitPosition(), "xiaoming", "abc", 1);
		else
			return new User(map, map.getInitPosition(), "panghu", "abc", 2);
	}

	public synchronized void quit(Connection con) throws SQLException {
		// release in curNode
		currentPosition.release(this);
		// store information update in DB
		updateUser(con, this);
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
	
	public synchronized void quit() {
		// release in curNode
		currentPosition.release(this);
	}

	public static int createUser(Connection con, String un, String pw) throws SQLException {
        String query = " insert into user (username, password, mapId, nodeId) values (?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, un);
        stmt.setString(2, pw);
        stmt.setInt(3, 1);
        stmt.setInt(4, 1);

        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
    }

    private static User getUserByResultSet(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");

        int mapId = rs.getInt("mapId");
        GameMap curMap = Maps.getInstance().getGameMapById(mapId);
        int nodeId = rs.getInt("nodeId");
        MapNode node = curMap.getNodeById(nodeId);
        int id = rs.getInt("id");

        return new User(curMap, node, username, password, id);
    }

    public static User getUserById(Connection con, int id) throws SQLException {
        String query = " select * from user where id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            return getUserByResultSet(rs);
        } else {
            throw new SQLException("Can not find user with id:" + id);
        }
    }

    public static User getUserByAuth(Connection con, String un, String pw) throws SQLException {
	    String query = "select * from user where username = ? and password = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, un);
        stmt.setString(2, pw);

        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            return getUserByResultSet(rs);
        } throw new SQLDataException("Invalid username or password");
    }

    private static void updateUser(Connection con, User u) throws SQLException {
		String query = "update user set mapId = ?, nodeId = ? where id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, u.getCurMapId());
		stmt.setInt(2, u.getCurNodeId());
		stmt.setInt(3, u.getId());
		stmt.executeUpdate();
	}
}
