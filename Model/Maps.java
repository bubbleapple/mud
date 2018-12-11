package Model;

import Utils.DB.SingleConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * singleton class Maps.
 * usage: Maps.getInstance().getGameMapById(id);
 *        Maps.getInstance().getMaps();
 *
 */
public class Maps {
    private static Maps instance = new Maps();
    private Map<Integer, GameMap> maps;

    private Maps() {
        maps = getGameMapFromDB();
    }

    public Map<Integer, GameMap> getMaps() {
        return maps;
    }

    public GameMap getGameMapById(int id) {
        if(maps.containsKey(id)) return maps.get(id);
        throw new NullPointerException("Can't find GameMap by Id: " + id);
    }

    public static Maps getInstance() {
        return instance;
    }

    /**
     * return the gamemap from Database
     * current all use singleton method
     * @return a GameMap instance
     */
    public static Map<Integer, GameMap> getGameMapFromDB() {
        // get all node id
        Map<Integer, GameMap> res = new HashMap<>();
        try {
            Statement stmt = SingleConnection.getInstance().getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from map");
            while(rs.next()) {
                res.put(rs.getInt("id"), getGameMapByResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static GameMap getGameMapByResultSet(ResultSet rs) {
        try {
            int mapId = rs.getInt("id");
            String mapName = rs.getString("name");
            String mapDescription = rs.getString("description");
            int initId = rs.getInt("enterId");

            int count = getNodeLengthByMapId(mapId);

            // get the node data
            int[] nodesIds = new int[count];
            String[] names = new String[count];
            String[] descriptions = new String[count];

            PreparedStatement stmt = SingleConnection.getInstance().getConnection().prepareStatement("select * from map_node where mapId = ?");
            stmt.setInt(1, mapId);
            ResultSet nodeRs = stmt.executeQuery();
            int index = 0;
            while(nodeRs.next()) {
                nodesIds[index] = nodeRs.getInt("id");
                names[index] = nodeRs.getString("name");
                descriptions[index] = nodeRs.getString("description");
                index++;
            }

            // getMap connections
            int conLength = getConLengthByMapId(mapId);
            MapConnection [] cons = new MapConnection[conLength];

            stmt = SingleConnection.getInstance().getConnection().prepareStatement("select * from map_connection where mapId = ?");
            stmt.setInt(1, mapId);
            ResultSet conRs = stmt.executeQuery();
            index = 0;
            while(conRs.next()) {
                cons[index++] = new MapConnection(
                        conRs.getInt("fromId"),
                        conRs.getInt("toId"),
                        conRs.getString("s2e"),
                        conRs.getString("e2s")
                );
            }

            return new GameMap(nodesIds, names, descriptions, cons, initId, mapDescription, mapId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getNodeLengthByMapId(int mapId) throws SQLException {
        PreparedStatement stmt = SingleConnection.getInstance().getConnection().prepareStatement("select count(*) from map_node where mapId = ?");
        stmt.setInt(1, mapId);
        ResultSet countRs = stmt.executeQuery();
        countRs.next();
        return countRs.getInt(1);
    }

    private static int getConLengthByMapId(int mapId) throws SQLException {
        PreparedStatement stmt = SingleConnection.getInstance().getConnection().prepareStatement("select count(*) from map_connection where mapId = ?");
        stmt.setInt(1, mapId);
        ResultSet countRs = stmt.executeQuery();
        countRs.next();
        return countRs.getInt(1);
    }
}
