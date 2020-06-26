package fr.entasia.skytools.objs.villagers;

import fr.entasia.apis.other.Randomiser;
import fr.entasia.apis.utils.ItemBuilder;
import net.minecraft.server.v1_12_R1.Village;
import org.bukkit.Material;
import static org.bukkit.entity.Villager.Profession;

import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;

public enum Villagers {


	A(Profession.BLACKSMITH, 15,
		new ComplexTrade(
			new Trade(15, new ItemBuilder(Material.STONE)).need(new ItemBuilder(Material.STONE_HOE).name("Bjr"))
		)
	),
	B(Profession.FARMER, 0),
	C(Profession.FARMER, 0),
	D(Profession.FARMER, 0),
	E(Profession.FARMER, 0),
	F(Profession.FARMER, 0),
	DEFAULT(Profession.LIBRARIAN, 0),
	;

	public static Randomiser r = new Randomiser(0, false);

	static{
		for(Villagers v : values())r.max+=v.chance;
	}

	public Profession p;
	public int chance;
	public ComplexTrade[] cTrades;

	Villagers(Profession p, int chance, ComplexTrade... cTrades){
		this.p = p;
		this.chance = chance;
		this.cTrades = cTrades;
	}

	public static Villagers getOne(){
		r.regen();
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
		v.setProfession(p);
		v.setRecipes(list);
	}
}
