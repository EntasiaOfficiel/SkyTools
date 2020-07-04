package fr.entasia.skytools.objs.villagers;

import fr.entasia.apis.other.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class Trade extends MerchantRecipe {

	public Trade(ItemBuilder item){
		this(item.build());
	}

	public Trade(ItemStack item) {
		super(item, 99999);
	}

	public Trade maxUses(int maxUses){
		setMaxUses(maxUses);
		return this;
	}

	public Trade need(ItemBuilder item){
		addIngredient(item.build());
		return this;
	}

	public Trade need(ItemStack item){
		addIngredient(item);
		return this;
	}
}
