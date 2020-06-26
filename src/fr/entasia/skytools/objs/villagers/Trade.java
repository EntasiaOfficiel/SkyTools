package fr.entasia.skytools.objs.villagers;

import fr.entasia.apis.other.ItemBuilder;
import org.bukkit.inventory.MerchantRecipe;

public class Trade extends MerchantRecipe {

	public int percent;

	public Trade(int percent, ItemBuilder item){
		super(item.build(), 99999);
		this.percent = percent;
	}

	public Trade maxUses(int maxUses){
		setMaxUses(maxUses);
		return this;
	}

	public Trade need(ItemBuilder item){
		addIngredient(item.build());
		return this;
	}
}
