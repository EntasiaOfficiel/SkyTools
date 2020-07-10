package fr.entasia.skytools.objs.villagers;

import fr.entasia.apis.other.ItemBuilder;
import fr.entasia.skytools.Main;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;

public class Trade {

	public TradeItem result;
	public TradeItem[] needs = new TradeItem[2];
	public int maxUses = 10;

	public Trade(TradeItem result){
		this.result = result;
	}

	public Trade maxUses(int maxUses){
		this.maxUses = maxUses;
		return this;
	}

	public Trade need(TradeItem item){
		if(needs[0]==null)needs[0] = item;
		else needs[1] = item;
		return this;
	}

	public MerchantRecipe buildOne(){
		MerchantRecipe a = new MerchantRecipe(result.build(), 0, applyRandom(maxUses), true);
		for(TradeItem ti : needs) a.addIngredient(ti.build());
		return a;
	}

//	public Trade need(ItemStack item){
//		addIngredient(item);
//		return this;
//	}



	protected static int applyRandom(int a){
		return (int) (a*(Main.r.nextInt(10)/10f+0.95));
	}
}
