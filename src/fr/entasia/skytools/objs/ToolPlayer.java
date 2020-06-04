package fr.entasia.skytools.objs;

import org.apache.commons.lang.mutable.MutableBoolean;
import org.bukkit.entity.Player;

public class ToolPlayer {

	public Player p;

	public MutableBoolean vision = new MutableBoolean(false);
	public MutableBoolean speed = new MutableBoolean(false);
	public MutableBoolean jump = new MutableBoolean(false);

	public long cdCanoon, cdFirework;

	public ToolPlayer(Player p){
		this.p = p;
	}

}
