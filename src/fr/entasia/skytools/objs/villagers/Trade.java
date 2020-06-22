package fr.entasia.skytools.objs.villagers;

import fr.entasia.apis.utils.ItemBuilder;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import javax.annotation.Nullable;

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
