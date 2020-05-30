package fr.entasia.skytools.objs.custom;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum CustomArrows {
	FIREWORKS("Flèche d'Artifice"),
	EXPLOSION("Flèche à Explosion"),

	FIRE("Flèche de feu"),
	DRAGON("Flèche du dragon"),


	;

	public String name;

	CustomArrows(String name){
		this.name = name;
	}

	public boolean is(ItemStack item){
		if(item==null)return false;
		ItemMeta meta = item.getItemMeta();
		if(meta==null)return false;
		return ("§f"+name).equals(meta.getDisplayName());
	}

	public void enchant(ItemMeta meta){
		meta.setDisplayName("§f"+name);
		meta.addEnchant(Enchantment.LURE, 1, false);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	}
}
