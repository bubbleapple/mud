package Model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import IO.CommandParser;
import IO.IO;

class NPCThread extends Thread {
    protected Socket socket;
    private NPC npc;
    private GameMap map;

    public NPCThread(Socket npcSocket, GameMap map, NPC npc) {
        this.socket = npcSocket;
        this.map = map;
        this.npc = npc;
    }

    public void run() {
        DataOutputStream output = null;
        try {
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
        	System.err.println("Unable to create output pipe for npc.");
            return;
        }   
        npc.updateOutputStream(new IO(output));
        while (true) {
            // take action
        	try {
        		// sleep
        		System.out.println(npc.getName() + " sleeps off.");
				Thread.sleep(5000);
				System.out.println(npc.getName() + " wakes up.");
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
    }
}



public class NPC extends Figure {
	public NPC(GameMap map, MapNode node, String n, int id) {
		super(map, node, n, id);
		node.register(this, null);
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
	
	public void activate (Socket socket) {
		// create and launch npcThread
		new NPCThread(socket, currentMap, this).start();   
	}
	public static Set<NPC> createSampleNPCs(GameMap map) {
		Set<NPC> npcs = new HashSet<> ();
		npcs.add(new NPC(map, map.getNodeById(1), "SuLaoShi", 1));
		npcs.add(new NPC(map, map.getNodeById(2), "JiQiMao", 2));
		return npcs;
	}
}
