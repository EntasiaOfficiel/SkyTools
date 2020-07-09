package fr.entasia.skytools.objs.villagers;

import com.sun.deploy.util.SyncFileAccess;
import fr.entasia.apis.nbt.EntityNBT;
import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.apis.nbt.NBTTypes;
import fr.entasia.apis.other.ItemBuilder;
import fr.entasia.apis.other.Randomiser;
import fr.entasia.apis.utils.ItemUtils;
import fr.entasia.apis.utils.ServerUtils;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.objs.custom.CustomArrows;
import net.minecraft.server.v1_12_R1.IMerchant;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftMerchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.entity.Villager.Profession;

public enum Villagers {

	PRETRE(0, Profession.PRIEST, 25, 1, 2,
		new TradeLevel(
			new Trade(new ItemBuilder(Material.BREAD).name("§7Pain divin")).need(new ItemBuilder(Material.EMERALD)),
			new Trade(new ItemBuilder(Material.EXP_BOTTLE, 5)).need(new ItemBuilder(Material.GOLD_INGOT, 2))
		),
		new TradeLevel(
			new Trade(new ItemBuilder(Material.BREWING_STAND_ITEM)).need(new ItemBuilder(Material.GLASS_BOTTLE, 64)).need(new ItemBuilder(Material.GOLD_INGOT, 24)),
			new Trade(new ItemBuilder(Material.BLAZE_POWDER, 5)).need(new ItemBuilder(Material.BREAD, 64))
		),
		new TradeLevel(
			new Trade(new ItemBuilder(Material.GLASS_BOTTLE, 32)).need(new ItemBuilder(Material.BREAD, 32)),
			new Trade(new ItemBuilder(Material.DRAGONS_BREATH)).need(new ItemBuilder(Material.EXP_BOTTLE, 24)).need(new ItemBuilder(Material.GOLD_INGOT, 16))
		),
		new TradeLevel(
			new Trade(new ItemBuilder(Material.WATCH)).need(new ItemBuilder(Material.GOLD_INGOT, 32)).need(new ItemBuilder(Material.EXP_BOTTLE, 16)),
			new Trade(new ItemBuilder(Material.EXP_BOTTLE, 64)).need(new ItemBuilder(Material.GLASS_BOTTLE, 64)).need(new ItemBuilder(Material.GOLD_INGOT, 8)),
			new Trade(new ItemBuilder(Material.INK_SACK).damage(2).name("§cCoeur")).need(new ItemBuilder(Material.ROTTEN_FLESH, 64)).need(new ItemBuilder(Material.GOLDEN_APPLE))
		),
		new TradeLevel(
			new Trade(new ItemBuilder(Material.TOTEM)).need(new ItemBuilder(Material.EMERALD, 24)),
			new Trade(new ItemBuilder(Material.EXP_BOTTLE, 64)).need(new ItemBuilder(Material.GLASS_BOTTLE, 64)).need(new ItemBuilder(Material.LAPIS_ORE, 8))
		)
	),
	FORGERON_OUTILS(1, Profession.BLACKSMITH, 25, 1, 2,
		new TradeLevel(
			new Trade(new ItemBuilder(Material.IRON_PICKAXE)).need(new ItemBuilder(Material.COBBLESTONE, 64)).need(new ItemBuilder(Material.IRON_INGOT)),
			new Trade(new ItemBuilder(Material.DIAMOND_PICKAXE)).need(new ItemBuilder(Material.EMERALD, 2)),
			new Trade(new ItemBuilder(Material.IRON_SPADE)).need(new ItemBuilder(Material.COBBLESTONE, 64)),
			new Trade(new ItemBuilder(Material.DIAMOND_SPADE)).need(new ItemBuilder(Material.EMERALD, 2))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.DIAMOND_PICKAXE)).need(new ItemBuilder(Material.IRON_INGOT, 16)),
				new Trade(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.DURABILITY, 1)).need(new ItemBuilder(Material.IRON_INGOT, 24)),
				new Trade(new ItemBuilder(Material.DIAMOND_SPADE)).need(new ItemBuilder(Material.IRON_INGOT, 16)),
				new Trade(new ItemBuilder(Material.DIAMOND_SPADE).enchant(Enchantment.DURABILITY, 1)).need(new ItemBuilder(Material.IRON_INGOT, 24))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.IRON_PICKAXE).enchant(Enchantment.DIG_SPEED, 3)).need(new ItemBuilder(Material.IRON_INGOT, 16)),
				new Trade(new ItemBuilder(Material.IRON_PICKAXE).enchant(Enchantment.DURABILITY, 2)).need(new ItemBuilder(Material.IRON_INGOT, 16)),
				new Trade(new ItemBuilder(Material.DIAMOND_SPADE).enchant(Enchantment.DURABILITY, 2)).need(new ItemBuilder(Material.IRON_INGOT, 24)),
				new Trade(new ItemBuilder(Material.IRON_SPADE).enchant(Enchantment.DIG_SPEED, 3)).need(new ItemBuilder(Material.IRON_INGOT, 16)),
				new Trade(new ItemBuilder(Material.IRON_SPADE).enchant(Enchantment.DURABILITY, 2)).need(new ItemBuilder(Material.IRON_INGOT, 16)),
				new Trade(new ItemBuilder(Material.DIAMOND_SPADE).enchant(Enchantment.DIG_SPEED, 2)).need(new ItemBuilder(Material.IRON_INGOT, 24))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 2)).need(new ItemBuilder(Material.EMERALD)),
				new Trade(new ItemBuilder(Material.DIAMOND_SPADE).enchant(Enchantment.DIG_SPEED, 1)).need(new ItemBuilder(Material.EMERALD)),
				new Trade(new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 2).enchant(Enchantment.DURABILITY, 1)).need(new ItemBuilder(Material.EMERALD, 3)),
				new Trade(new ItemBuilder(Material.DIAMOND_SPADE).enchant(Enchantment.DIG_SPEED, 2).enchant(Enchantment.DURABILITY, 1)).need(new ItemBuilder(Material.EMERALD, 2))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.EMERALD)).need(new ItemBuilder(Material.DIAMOND_PICKAXE)),
				new Trade(new ItemBuilder(Material.EMERALD)).need(new ItemBuilder(Material.DIAMOND_SPADE)),
				new Trade(new ItemBuilder(Material.IRON_INGOT, 24)).need(new ItemBuilder(Material.DIAMOND_PICKAXE)),
				new Trade(new ItemBuilder(Material.IRON_INGOT, 24)).need(new ItemBuilder(Material.DIAMOND_SPADE))
		)
	),




	GOOGLE_MAP(2, Profession.LIBRARIAN, 25, 1, 2,
		new TradeLevel(
				new Trade(new ItemBuilder(Material.PAPER, 24)).need(new ItemBuilder(Material.GOLD_INGOT)),
				new Trade(new ItemBuilder(Material.PAPER, 24)).need(new ItemBuilder(Material.IRON_INGOT, 2))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.PAPER, 16)).need(new ItemBuilder(Material.IRON_INGOT)),
				new Trade(new ItemBuilder(Material.PAPER, 64)).need(new ItemBuilder(Material.EMERALD, 2)),
				new Trade(new ItemBuilder(Material.EMPTY_MAP)).need(new ItemBuilder(Material.IRON_INGOT))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.IRON_INGOT)).need(new ItemBuilder(Material.EMPTY_MAP, 2)),
				new Trade(new ItemBuilder(Material.EMPTY_MAP, 4)).need(new ItemBuilder(Material.IRON_INGOT, 2))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.EMERALD)).need(new ItemBuilder(Material.EMPTY_MAP, 16)),
				new Trade(new ItemBuilder(Material.IRON_INGOT, 8)).need(new ItemBuilder(Material.EMPTY_MAP, 16))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.EMERALD, 2)).need(new ItemBuilder(Material.PAPER, 64)).need(new ItemBuilder(Material.EMPTY_MAP, 32)),
				new Trade(new ItemBuilder(Material.EMERALD, 4)).need(new ItemBuilder(Material.EMPTY_MAP, 64))
		)
	),
	FISHEMAN(3, Profession.FARMER, 25, 1, 2,
		new TradeLevel(
				new Trade(new ItemBuilder(Material.RAW_FISH).damage(3)).need(new ItemBuilder(Material.EMERALD)),
				new Trade(new ItemBuilder(Material.RAW_FISH).damage(2)).need(new ItemBuilder(Material.EMERALD, 2)),
				new Trade(new ItemBuilder(Material.RAW_FISH, 8)).need(new ItemBuilder(Material.EMERALD, 2)),
				new Trade(new ItemBuilder(Material.RAW_FISH, 8).damage(1)).need(new ItemBuilder(Material.EMERALD, 2))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.RAW_FISH, 5).damage(3)).need(new ItemBuilder(Material.EMERALD, 2)),
				new Trade(new ItemBuilder(Material.RAW_FISH, 10).damage(2)).need(new ItemBuilder(Material.EMERALD, 6)),
				new Trade(new ItemBuilder(Material.RAW_FISH, 32)).need(new ItemBuilder(Material.EMERALD, 5)),
				new Trade(new ItemBuilder(Material.RAW_FISH, 32).damage(1)).need(new ItemBuilder(Material.EMERALD, 5))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.IRON_INGOT, 4)).need(new ItemBuilder(Material.RAW_FISH, 64)),
				new Trade(new ItemBuilder(Material.IRON_INGOT, 4)).need(new ItemBuilder(Material.RAW_FISH, 64).damage(1)),
				new Trade(new ItemBuilder(Material.IRON_INGOT, 5)).need(new ItemBuilder(Material.COOKED_FISH, 48).damage(1)),
				new Trade(new ItemBuilder(Material.IRON_INGOT, 5)).need(new ItemBuilder(Material.COOKED_FISH, 48))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.IRON_INGOT, 6)).need(new ItemBuilder(Material.RAW_FISH, 64)),
				new Trade(new ItemBuilder(Material.IRON_INGOT, 6)).need(new ItemBuilder(Material.RAW_FISH, 64).damage(1)),
				new Trade(new ItemBuilder(Material.IRON_INGOT, 6)).need(new ItemBuilder(Material.COOKED_FISH, 16)).need(new ItemBuilder(Material.COOKED_FISH).damage(1))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.EMERALD, 2)).need(new ItemBuilder(Material.RAW_FISH, 64).damage(3)),
				new Trade(new ItemBuilder(Material.EMERALD, 2)).need(new ItemBuilder(Material.RAW_FISH, 64).damage(2)),
				new Trade(new ItemBuilder(Material.EMERALD, 5)).need(new ItemBuilder(Material.RAW_FISH, 64).damage(3)).need(new ItemBuilder(Material.RAW_FISH, 64).damage(2))
		)
	),
	CHASSEUR(4, Profession.BUTCHER, 0, 1, 2,
		new TradeLevel(
				new Trade(new ItemBuilder(Material.LEATHER, 16)).need(new ItemBuilder(Material.EMERALD)),
				new Trade(new ItemBuilder(Material.LEATHER, 16)).need(new ItemBuilder(Material.IRON_INGOT, 6)),
				new Trade(new ItemBuilder(Material.RABBIT_HIDE, 32)).need(new ItemBuilder(Material.EMERALD)),
				new Trade(new ItemBuilder(Material.RABBIT_HIDE, 32)).need(new ItemBuilder(Material.IRON_INGOT, 6))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.LEATHER, 24)).need(new ItemBuilder(Material.EMERALD)),
				new Trade(new ItemBuilder(Material.LEATHER, 24)).need(new ItemBuilder(Material.IRON_INGOT, 6)),
				new Trade(new ItemBuilder(Material.RABBIT_HIDE, 48)).need(new ItemBuilder(Material.EMERALD)),
				new Trade(new ItemBuilder(Material.RABBIT_HIDE, 48)).need(new ItemBuilder(Material.IRON_INGOT, 6))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.RABBIT_FOOT)).need(new ItemBuilder(Material.EMERALD, 6)),
				new Trade(new ItemBuilder(Material.LEATHER_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new ItemBuilder(Material.EMERALD, 10)),
				new Trade(new ItemBuilder(Material.LEATHER_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new ItemBuilder(Material.EMERALD, 10)),
				new Trade(new ItemBuilder(Material.LEATHER_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new ItemBuilder(Material.EMERALD, 10)),
				new Trade(new ItemBuilder(Material.LEATHER_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new ItemBuilder(Material.EMERALD, 10))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.EMERALD, 2)).need(new ItemBuilder(Material.RABBIT_FOOT)),
				new Trade(new ItemBuilder(Material.IRON_INGOT, 4)).need(new ItemBuilder(Material.LEATHER, 64)),
				new Trade(new ItemBuilder(Material.IRON_INGOT, 2)).need(new ItemBuilder(Material.RABBIT_HIDE, 64))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.LEATHER_HELMET).enchant(Enchantment.OXYGEN, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new ItemBuilder(Material.EMERALD, 15)),
				new Trade(new ItemBuilder(Material.LEATHER_CHESTPLATE).enchant(Enchantment.THORNS, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new ItemBuilder(Material.EMERALD, 15)),
				new Trade(new ItemBuilder(Material.LEATHER_LEGGINGS).enchant(Enchantment.THORNS, 3).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new ItemBuilder(Material.EMERALD, 15)),
				new Trade(new ItemBuilder(Material.LEATHER_BOOTS).enchant(Enchantment.WATER_WORKER, 4).enchant(Enchantment.PROTECTION_FALL, 4).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).enchant(Enchantment.DURABILITY, 3).enchant(Enchantment.MENDING, 1)).need(new ItemBuilder(Material.EMERALD, 15)),
				new Trade(new ItemBuilder(Material.RABBIT_FOOT, 32)).need(new ItemBuilder(Material.EMERALD, 16))
		)
	),
	CHARCUTIER(5, Profession.BUTCHER, 0, 1, 2,
		new TradeLevel(
				new Trade(new ItemBuilder(Material.COOKED_CHICKEN, 4)).need(new ItemBuilder(Material.RAW_CHICKEN, 8)),
				new Trade(new ItemBuilder(Material.COOKED_RABBIT, 4)).need(new ItemBuilder(Material.RABBIT, 8)),
				new Trade(new ItemBuilder(Material.COOKED_MUTTON, 4)).need(new ItemBuilder(Material.MUTTON, 8))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.COOKED_BEEF, 4)).need(new ItemBuilder(Material.RAW_BEEF, 8)),
				new Trade(new ItemBuilder(Material.GRILLED_PORK, 4)).need(new ItemBuilder(Material.PORK, 8))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.RAW_CHICKEN)).need(new ItemBuilder(Material.ROTTEN_FLESH, 32)),
				new Trade(new ItemBuilder(Material.RAW_BEEF)).need(new ItemBuilder(Material.ROTTEN_FLESH, 32)),
				new Trade(new ItemBuilder(Material.RABBIT)).need(new ItemBuilder(Material.ROTTEN_FLESH, 32)),
				new Trade(new ItemBuilder(Material.PORK)).need(new ItemBuilder(Material.ROTTEN_FLESH, 32)),
				new Trade(new ItemBuilder(Material.MUTTON)).need(new ItemBuilder(Material.ROTTEN_FLESH, 32))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.COOKED_CHICKEN)).need(new ItemBuilder(Material.ROTTEN_FLESH, 64)),
				new Trade(new ItemBuilder(Material.COOKED_BEEF)).need(new ItemBuilder(Material.ROTTEN_FLESH, 64)),
				new Trade(new ItemBuilder(Material.COOKED_RABBIT)).need(new ItemBuilder(Material.ROTTEN_FLESH, 64)),
				new Trade(new ItemBuilder(Material.GRILLED_PORK)).need(new ItemBuilder(Material.ROTTEN_FLESH, 64)),
				new Trade(new ItemBuilder(Material.COOKED_MUTTON)).need(new ItemBuilder(Material.ROTTEN_FLESH, 64))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.COOKED_CHICKEN).name("§6KFC")).need(new ItemBuilder(Material.IRON_INGOT, 6)),
				new Trade(new ItemBuilder(Material.BAKED_POTATO).name("§6Frites McDo")).need(new ItemBuilder(Material.IRON_INGOT, 6))
		)
	),
	FLECHIER(6, Profession.FARMER, 0, 1, 2,
		new TradeLevel(
				new Trade(new ItemBuilder(Material.BOW)).need(new ItemBuilder(Material.IRON_INGOT, 2)),
				new Trade(new ItemBuilder(Material.ARROW, 32)).need(new ItemBuilder(Material.IRON_INGOT, 2))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.IRON_INGOT)).need(new ItemBuilder(Material.ARROW, 64)),
				new Trade(new ItemBuilder(Material.BOW)).need(new ItemBuilder(Material.ARROW, 48))
		),
		new TradeLevel(
				new Trade(new TippedArrow().effect(PotionType.INSTANT_DAMAGE)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.STRENGTH)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.WEAKNESS)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.INSTANT_HEAL)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.JUMP)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.SPEED)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.SLOWNESS)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.SPECTRAL_ARROW)).need(new ItemBuilder(Material.GOLD_INGOT, 8)).need(new ItemBuilder(Material.ARROW, 64)),
				new Trade(new ItemBuilder(Material.BOW)).need(new ItemBuilder(Material.ARROW, 48)),
				new Trade(new TippedArrow().effect(PotionType.NIGHT_VISION)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.INVISIBILITY)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.FIRE_RESISTANCE)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.POISON)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.REGEN)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.LUCK)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4)),
				new Trade(new TippedArrow().effect(PotionType.WATER_BREATHING)).need(new ItemBuilder(Material.ARROW, 8)).need(new ItemBuilder(Material.IRON_INGOT, 4))
		),
		new TradeLevel(
				new Trade(new ItemBuilder(Material.ARROW, 16).lore(CustomArrows.FIREWORKS.a())).need(new ItemBuilder(Material.IRON_INGOT, 2)),
				new Trade(new ItemBuilder(Material.ARROW, 4).lore(CustomArrows.EXPLOSION.a())).need(new ItemBuilder(Material.GOLD_INGOT, 1)),
				new Trade(new ItemBuilder(Material.ARROW, 5).lore(CustomArrows.FIRE.a())).need(new ItemBuilder(Material.EMERALD, 1)),
				new Trade(new ItemBuilder(Material.ARROW, 5).lore(CustomArrows.DRAGON.a())).need(new ItemBuilder(Material.EMERALD, 1))
		)
	),

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
		return null;
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
