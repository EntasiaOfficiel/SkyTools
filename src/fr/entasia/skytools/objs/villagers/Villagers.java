package fr.entasia.skytools.objs.villagers;

import fr.entasia.apis.other.Randomiser;
import fr.entasia.apis.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;

public enum Villagers {
	A(15,
		new ComplexTrade(
			new Trade(15, new ItemBuilder(Material.STONE)).need(new ItemBuilder(Material.STONE_HOE).name("Bjr"))
		)
	),
	B(0),
	C(0),
	D(0),
	E(0),
	F(0),
	DEFAULT(0),
	;

	public int chance;
	public ComplexTrade[] cTrades;

	Villagers(int chance, ComplexTrade... cTrades){
		this.chance = chance;
		this.cTrades = cTrades;
	}

	public static Villagers getOne(){
		Randomiser r = new Randomiser();
		for(Villagers v : values()){
			if(r.isInNext(v.chance))return v;
		}
		return A;
	}

	public void apply(Villager v){
		ArrayList<MerchantRecipe> list = new ArrayList<>();
		for(ComplexTrade ct : cTrades){
			list.add(ct.getTrade());
		}
		System.out.println(list.size());
		v.setRecipes(list);
	}
}
