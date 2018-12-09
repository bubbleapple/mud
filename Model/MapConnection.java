package Model;

import java.util.Map;

public class MapConnection {
	private int start;
	private int end;
	String s2e;
	String e2s;
	public MapConnection(int start, int end, String s2e, String e2s) {
		this.start = start;
		this.end = end;
		this.s2e = s2e;
		this.e2s = e2s;
	}
	public void connect(GameMap m) {
		Map<Integer, MapNode> nodes = m.getMap();
		MapNode s = nodes.get(start);
		MapNode e = nodes.get(end);
		s.getNeighbors().put(s2e, end);
		e.getNeighbors().put(e2s, start);
	}
}