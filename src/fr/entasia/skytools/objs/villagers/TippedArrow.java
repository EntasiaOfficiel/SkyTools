package fr.entasia.skytools.objs.villagers;

import fr.entasia.apis.other.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class TippedArrow extends ItemBuilder {

	public TippedArrow() {
		super(Material.TIPPED_ARROW);
	}

	public TippedArrow effect(PotionType type){
		PotionMeta pmeta = (PotionMeta) meta;
		pmeta.setBasePotionData(new PotionData(type));
		return this;
	}
}
