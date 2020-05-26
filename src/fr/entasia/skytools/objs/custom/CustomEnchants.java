package fr.entasia.skytools.objs.custom;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public enum CustomEnchants {

	// full armure
	LAVA_EATER("Mangeur de Lave", 2),

	// casque
	VISION("Vision", 1), // basic

	// bottes
	SLOW_FALLING("Chute Lente", 1), // a voir si j'y arrive
	JUMP("Saut", 3), // basic
	SPEED("Vitesse", 3), // basic

	// épée
	VAMPIRE("Vampire", 5),

	// canne à pêche
	SKY_FISHER("Pécheur du ciel", 3),

	// houe
	SEEDS_CANOON("Canon", 1);

	// REGARDE LES NEWS SUR DISCORD

	;

	public String name;
	public int maxlvl;

	CustomEnchants(String name, int maxlvl){
		this.name = name;
		this.maxlvl = maxlvl;
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
			if(n.equals("§7"+name)){
				lvl = roman(split[split.length-1]);
				if(lvl!=0){
					return Math.min(maxlvl, lvl);
				}
			}
		}
		return 0;
	}


	public static int roman(String s){
		switch (s){
			case "I":
				return 1;
			case "II":
				return 2;
			case "III":
				return 3;
			case "IV":
				return 4;
			case "V":
				return 5;
		}
		return 0;
	}
}
