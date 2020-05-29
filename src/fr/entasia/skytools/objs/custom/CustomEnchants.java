package fr.entasia.skytools.objs.custom;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

	private static final String[] roman = new String[]{"I", "II", "III", "IV", "V"};

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
				lvl = toInt(split[split.length-1]);
				if(lvl!=0)return Math.min(maxlvl, lvl);
			}
		}
		return 0;
	}

	public String toRoman(int level){
		return roman[level-1];
	}


	public static int toInt(String s){
		for(int i=0;i<roman.length;i++){
			if(roman[i].equals(s))return i+1;
		}
		return 0;
	}

	public void enchant(ItemMeta meta, int level){
		List<String> lore = meta.getLore();
		if(lore==null)lore = new ArrayList<>();
		lore.add("§7"+name+" "+toRoman(level));
		meta.setLore(lore);
	}
}
