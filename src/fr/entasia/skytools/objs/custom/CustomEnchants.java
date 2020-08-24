package fr.entasia.skytools.objs.custom;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CustomEnchants {

	// full armure
	LAVA_EATER("Mangeur de Lave", 2),

	// casque
	VISION("Vision", 1), // basic

	// bottes
	JUMP("Saut", 3), // basic
	SPEED("Vitesse", 3), // basic
	GREENER("Bottes vertes", 1),

	// épée
	VAMPIRE("Vampire", 4),
	WITHER("Wither", 2),

	// pickaxe
	SPAWNER_BREAKER("Casseur de spawners", 1),

	// houe
	TONDEUSE("Tondeuse", 3),

	// canne à pêche
	SKY_FISHER("Pécheur du ciel", 3),

	// REGARDE LES NEWS SUR DISCORD

	;

	public String name;
	public int maxlvl;

	CustomEnchants(String name, int maxlvl){
		this.name = name;
		this.maxlvl = maxlvl;
	}

	public static CustomEnchants get(String s){
		for(CustomEnchants ce : values()){
			if(("§6§r§7"+ce.name).equals(s))return ce;
		}
		return null;
	}

	public boolean hasEnchant(ItemStack item){
		return getLevel(item)!=0;
	}

	public int getLevel(ItemStack item){
		if(item==null)return 0;
		List<String> list = item.getLore();
		if(list==null)return 0;
		String[] split;
		String n;
		int lvl;
		for(String s : list){
			split = s.split(" ");
			n = String.join(" ", Arrays.copyOfRange(split, 0, split.length-1));
			if(("§6§r§7"+name).equals(n)){
				lvl = RomanUtils.toInt(split[split.length-1]);
				if(lvl!=0)return Math.min(maxlvl, lvl);
			}
		}
		return 0;
	}

	public boolean enchant(ItemStack item, int lvl){
		if(maxlvl<lvl)return false;
		List<String> lore = item.getLore();
		if(lore==null)lore = new ArrayList<>();
		lore.add(str(lvl));
		item.setLore(lore);
		return true;
	}

	public String str(int lvl){
		return "§6§r§7"+name+" "+RomanUtils.toRoman(Math.min(maxlvl, lvl));
	}
}
