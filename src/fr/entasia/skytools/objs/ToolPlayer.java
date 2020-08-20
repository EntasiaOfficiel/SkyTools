package fr.entasia.skytools.objs;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ToolPlayer {

	public Player p;

	public ArrayList<TPRequest> tpRequests = new ArrayList<>();
	public MutableBoolean vision = new MutableBoolean(false);
	public MutableBoolean speed = new MutableBoolean(false);
	public MutableBoolean jump = new MutableBoolean(false);

	public long cdFirework;

	public ToolPlayer(Player p){
		this.p = p;
	}

}
