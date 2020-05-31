package fr.entasia.skytools.objs.custom;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

	// épée
	VAMPIRE("Vampire", 4),
	WITHER("Wither", 2),

	// canne à pêche
	SKY_FISHER("Pécheur du ciel", 3),

	// houe
	AREA("Aire", 2),
	SEEDS_CANOON("Canon", 1),

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
				lvl = RomanUtils.toInt(split[split.length-1]);
				if(lvl!=0)return Math.min(maxlvl, lvl);
			}
		}
		return 0;
	}

	public void enchant(ItemStack item, int level){
		List<String> lore = item.getLore();
		if(lore==null)lore = new ArrayList<>();
		lore.add("§7"+name+" "+ RomanUtils.toRoman(level));
		item.setLore(lore);
	}
}
