package fr.entasia.skytools.objs;

import fr.entasia.apis.utils.TextUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Warp {

	public static Warp spawnWarp;
	public static Map<String, Warp> warps = new HashMap<>();

	public String name = "Erreur";
	public List<String> desc = new ArrayList<>();
	public Location loc;
	public String[] aliases = new String[0];

	public Warp(){
	}

	public static Warp getWarp(String warp) {
		Warp a = warps.get(warp);
		if (a == null) {
			for (Warp i : warps.values()) {
				for (String j : i.aliases) {
					if (j.equals(warp)) {
						return i;
					}
				}
			}
			return null;
		} else return a;
	}


	public void teleport(Player p, boolean msg) {
		p.teleport(loc);
		p.setFallDistance(0);
		if(msg){
			if(name.equals("spawn")) p.sendMessage("§6Tu as été téléporté au §eSpawn§6 !");
			else p.sendMessage("§6Tu as été téléporté au warp §e"+ TextUtils.firstLetterUpper(name)+"§6 !");
		}
	}
}
