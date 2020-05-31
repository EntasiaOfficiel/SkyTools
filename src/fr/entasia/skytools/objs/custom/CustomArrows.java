package fr.entasia.skytools.objs.custom;

import fr.entasia.apis.nbt.NBTComponent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum CustomArrows {
	FIREWORKS("Flèche d'Artifice"),
	EXPLOSION("Flèche à Explosion"),

	FIRE("Flèche incendiaire"),
	DRAGON("Flèche du dragon"),

	;

	public String name;

	CustomArrows(String name){
		this.name = name;
	}

	public boolean hasEnchant(ItemStack item){
		if(item==null)return false;
		ItemMeta meta = item.getItemMeta();
		if(meta==null)return false;
		return ("§f"+name).equals(meta.getDisplayName());
	}

	public void enchant(ItemStack item){
		if(item==null)return;
		List<String> lore = item.getLore();
		if(lore==null)lore = new ArrayList<>();
		lore.add("§7"+name);
		item.setLore(lore);
	}

}
