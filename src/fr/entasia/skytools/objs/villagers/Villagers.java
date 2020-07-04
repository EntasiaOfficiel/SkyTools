package fr.entasia.skytools.objs.villagers;

import com.sun.deploy.util.SyncFileAccess;
import fr.entasia.apis.nbt.EntityNBT;
import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.apis.nbt.NBTTypes;
import fr.entasia.apis.other.ItemBuilder;
import fr.entasia.apis.other.Randomiser;
import fr.entasia.apis.utils.ServerUtils;
import fr.entasia.skytools.Main;
import net.minecraft.server.v1_12_R1.IMerchant;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMerchant;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.entity.Villager.Profession;

public enum Villagers {

	A(0, Profession.BLACKSMITH, 15, 1, 2,
		new TradeLevel(
			new Trade(new ItemBuilder(Material.BREAD).name("§7Pain divin")).need(new ItemStack(Material.EMERALD)),
			new Trade(new ItemStack(Material.EXP_BOTTLE, 5)).need(new ItemStack(Material.GOLD_INGOT, 2))
		),
		new TradeLevel(
			new Trade(new ItemStack(Material.BREWING_STAND_ITEM)).need(new ItemStack(Material.GLASS_BOTTLE, 64)).need(new ItemStack(Material.GOLD_INGOT, 24)),
			new Trade(new ItemStack(Material.BLAZE_POWDER, 5)).need(new ItemStack(Material.BREAD, 64))
		),
		new TradeLevel(
			new Trade(new ItemStack(Material.GLASS_BOTTLE, 32)).need(new ItemStack(Material.BREAD, 32)),
			new Trade(new ItemStack(Material.DRAGONS_BREATH)).need(new ItemStack(Material.EXP_BOTTLE, 24)).need(new ItemStack(Material.GOLD_INGOT, 16))
		),
		new TradeLevel(
			new Trade(new ItemStack(Material.WATCH)).need(new ItemStack(Material.GOLD_INGOT, 32)).need(new ItemStack(Material.EXP_BOTTLE, 16)),
			new Trade(new ItemStack(Material.EXP_BOTTLE, 64)).need(new ItemStack(Material.GLASS_BOTTLE, 64)).need(new ItemStack(Material.GOLD_INGOT, 8))
		),
		new TradeLevel(
			new Trade(new ItemStack(Material.TOTEM)).need(new ItemStack(Material.EMERALD, 24)),
			new Trade(new ItemStack(Material.EXP_BOTTLE, 64)).need(new ItemStack(Material.GLASS_BOTTLE, 64)).need(new ItemStack(Material.LAPIS_ORE, 8))
		)
	),
	B(1, Profession.FARMER, 0, 1, 2),
	C(2, Profession.FARMER, 0, 1, 2),
	D(3, Profession.FARMER, 0, 1, 2),
	E(4, Profession.FARMER, 0, 1, 2),
	F(5, Profession.FARMER, 0, 1, 2),
	DEFAULT(6, Profession.LIBRARIAN, 0, 1, 2),
	;

	public static Randomiser r = new Randomiser(0, false);

	static{
		for(Villagers v : values())r.max+=v.chance;
	}

	public int id; // custom career
	public Profession p;
	public int chance;
	public TradeLevel[] levels;

	Villagers(int id, Profession p, int chance, int dmin, int drandom, TradeLevel... levels){
		this.id = id;
		this.p = p;
		this.chance = chance;
		for(TradeLevel tl : levels){
			if(tl.min==0&&tl.random==0){
				tl.min = dmin;
				tl.random = drandom;
			}
		}
		this.levels = levels;
	}

	public static Villagers getByID(int id){
		for(Villagers vi : values()){
			if(vi.id==id)return vi;
		}
		return null;
	}

	public static Villagers getType(Villager v){
		for(Villagers vi : values()){
			if(v.getScoreboardTags().contains("npctype-"+vi.id)){
				return vi;
			}
		}
		return null;
	}

	public static Villagers getOne(){
		r.regen();
		for(Villagers v : values()){
			if(r.isInNext(v.chance))return v;
		}
		return A;
	}

	public void apply(Villager v){
		List<MerchantRecipe> list = new ArrayList<>();
		addToList(list, 1);
		v.setRecipes(list);
		v.setProfession(p);
//		EntityNBT.addNBT(v, new NBTComponent(String.format("{Career:%s}", id))); // est override par le jeu quand upgrade
		v.addScoreboardTag("npctype-"+id);
	}

	public void addToList(List<MerchantRecipe> list, int current){ // current 1-5 (max=5)
		TradeLevel lvl = levels[current-1];
		int max = lvl.min + Main.r.nextInt(lvl.random +1);
		ArrayList<Trade> tempTrades = new ArrayList<>(Arrays.asList(lvl.trades));
		for(int i=0;i<max;i++){
			if(tempTrades.size()==0)break;
			Trade t = tempTrades.remove(Main.r.nextInt(tempTrades.size()));
			list.add(t);
		}
	}

// EN CAS DE % DE CHANCE DANS LES TRADES DANS EUX MÊMES
//	private void addToList(List<MerchantRecipe> list, int current){
//		int max = levels[current].min + Main.r.nextInt(levels[current].random +1);
//		Randomiser r = levels[current].getRandomiser();
//		Trade[] trades = Arrays.copyOf(levels[current].trades, levels[current].trades.length);
//		for(int i=0;i<max;i++){
//			r.regen();
//			for(int j=0;j<trades.length;j++){
//				if(trades[j]!=null&&r.isInNext(trades[j].percent)){
//					r.max-=trades[j].percent;
//					trades[j] = null;
//					list.add(trades[j]);
//					break;
//				}
//			}
//			// ne devrait pas être atteint
//			ServerUtils.permMsg("log.upgradenpc","§cErreur lors du chois des nouveaux tades d'un PNJ ! Infos : "+name()+" "+current);
//		}
//	}
}
