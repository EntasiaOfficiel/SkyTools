package fr.entasia.skytools.objs.custom;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum CustomItems {
	PADIDE("Flèche d'Artifice"),


	;

	public String name;

	CustomItems(String name){
		this.name = name;
	}

	public boolean is(ItemStack item){
		if(item==null)return false;
		ItemMeta meta = item.getItemMeta();
		if(meta==null)return false;
		return ("§f"+name).equals(meta.getDisplayName());
	}
}
