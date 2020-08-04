package fr.entasia.skytools.objs.villagers;

import fr.entasia.skytools.Main;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;

public class Trade {

	public TradeItem result;
	public ArrayList<TradeItem> needs = new ArrayList<>();
	public int maxUses = 10;

	public Trade(TradeItem result){
		this.result = result;
	}

	public Trade need(TradeItem item){
		needs.add(item);
		return this;
	}

	public MerchantRecipe buildOne(){
		MerchantRecipe a = new MerchantRecipe(result.build(), 0, 10+Main.r.nextInt(20), true);
		for(TradeItem ti : needs){
			a.addIngredient(ti.build());
		}
		return a;
	}

}
