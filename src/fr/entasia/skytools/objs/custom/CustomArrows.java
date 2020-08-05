package fr.entasia.skytools.objs.custom;

import fr.entasia.skytools.events.cenchants.EnchantEvents;
import net.minecraft.server.v1_12_R1.ItemFireworks;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

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
		List<String> list = item.getLore();
		if(list==null)return false;
		for(String s : list){
			if(("§6§r§7"+name).equals(s))return true;
		}
		return false;
	}

	public void enchant(ItemStack item){
		if(item==null)return;
		List<String> lore = item.getLore();
		if(lore==null)lore = new ArrayList<>();
		lore.add(str());
		item.setLore(lore);
		item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.addUnsafeEnchantment(Enchantment.LUCK, 1);
	}

	public String str(){
		return "§6§r§7"+name;
	}

}
