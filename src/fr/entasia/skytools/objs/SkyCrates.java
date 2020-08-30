package fr.entasia.skytools.objs;

import fr.entasia.apis.other.ItemBuilder;
import fr.entasia.cosmetiques.utils.pets.PetsUtils;
import fr.entasia.crates.CratesAPI;
import fr.entasia.crates.utils.CrateLoot;
import fr.entasia.crates.utils.CrateType;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class SkyCrates {

	public static void registerCrates() throws Error {

		CrateType ct = new CrateType();
		ct.name = "Agriculture";
		ct.key = new ItemBuilder(Material.SEEDS).fakeEnchant().name("§eClé de crate").lore("§6Crate §aAgriculture").build();

		CrateLoot loot = new CrateLoot(10, "32 graines", new ItemStack(Material.SEEDS, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "32 graines de citrouille", new ItemStack(Material.PUMPKIN_SEEDS, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "32 graines de pastèque", new ItemStack(Material.MELON_SEEDS, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "32 carrotes", new ItemStack(Material.CARROT_ITEM, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "32 pommes de terre", new ItemStack(Material.POTATO_ITEM, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "32 nether wart", new ItemStack(Material.NETHER_STALK, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "une houe en diamant u3",
				new ItemBuilder(Material.DIAMOND_HOE).enchant(Enchantment.DURABILITY, 3).build());
		ct.addLoot(loot);

		ItemStack item = new ItemStack(Material.IRON_HOE);
		CustomEnchants.TONDEUSE.enchant(item, Main.r.nextInt(2)+1);
		loot = new CrateLoot(10, "une houe en fer §7Tondeuse", item);
		ct.addLoot(loot);

		CratesAPI.registerCrateType(ct);


		CrateType ct2 = new CrateType();
		ct2.name = "Minage";
		ct2.key = new ItemBuilder(Material.COAL).fakeEnchant().name("§eClé de crate").lore("§6Crate §8Minage").build();

		loot = new CrateLoot(10,"1 diamant", new ItemStack(Material.DIAMOND));
		ct2.addLoot(loot);

		loot = new CrateLoot(15,"1 lingot d'or", new ItemStack(Material.GOLD_INGOT));
		ct2.addLoot(loot);

		loot = new CrateLoot(15,"16 redstone", new ItemStack(Material.REDSTONE,16));
		ct2.addLoot(loot);

		loot = new CrateLoot(20,"8 fer", new ItemStack(Material.IRON_INGOT,8));
		ct2.addLoot(loot);

		loot = new CrateLoot(20,"16 lapis lazuli", new ItemStack(Material.INK_SACK, 16, (short) 4));
		ct2.addLoot(loot);


		ItemStack iteme = new ItemStack(Material.MONSTER_EGG,1,(short) 1);
		ItemMeta meta = iteme.getItemMeta();
		meta.setDisplayName("Gnobo's stone");
		item.setItemMeta(meta);
		loot = new CrateLoot(20,"Gnobo's stone", iteme);
		ct2.addLoot(loot);

		loot = new CrateLoot(20,"1 cobblestone", new ItemStack(Material.COBBLESTONE));
		ct2.addLoot(loot);

		loot = new CrateLoot(10,"Pioche en bois", new ItemStack(Material.WOOD_PICKAXE));
		ct2.addLoot(loot);



		loot = new CrateLoot(10,"Sainte pioche", new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED,5).enchant(Enchantment.DURABILITY,5).enchant(Enchantment.LOOT_BONUS_BLOCKS,5).damage(5).build());
		ct2.addLoot(loot);

		CratesAPI.registerCrateType(ct2);


		CrateType ct3 = new CrateType();
		ct3.name = "Beta";
		ct3.key = new ItemBuilder(Material.COAL).fakeEnchant().name("§eClé de crate").lore("§6Crate §cBeta").build();

		item = PetsUtils.getSkull(new ItemStack(Material.SKULL_ITEM, 1, (short)3),"skull:panda", "d8cdd4f285632c25d762ece25f4193b966c2641b15d9bdbc0a113023de76ab");
		meta = item.getItemMeta();
		meta.setDisplayName("Rip flying Bobby");
		item.setItemMeta(meta);
		meta.addEnchant(Enchantment.LURE, 1, true);
		meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
		loot = new CrateLoot(1,"Rip flying Bobby", item);
		ct3.addLoot(loot);

		item = new ItemStack(Material.LAVA_BUCKET);
		meta = item.getItemMeta();
		meta.setDisplayName("Restes du pnj de Narcos");
		meta.addEnchant(Enchantment.LURE, 1, true);
		meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
		item.setItemMeta(meta);
		loot = new CrateLoot(1,"Restes du pnj de Narcos", item);
		ct3.addLoot(loot);

		item = new ItemStack(Material.SKULL_ITEM,1,(short)3);
		SkullMeta sm = (SkullMeta) item.getItemMeta();
		sm.setOwningPlayer(Bukkit.getOfflinePlayer("ImRobot"));
		sm.setDisplayName("Tête du dieu du dev");
		sm.addEnchant(Enchantment.LURE, 1, true);
		sm.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
		item.setItemMeta(sm);
		loot = new CrateLoot(1,"Tête du dieu du dev", item);
		ct3.addLoot(loot);

		item = new ItemStack(Material.WATER_BUCKET);
		meta = item.getItemMeta();
		meta.setDisplayName("La sueur d'itrooz");
		meta.addEnchant(Enchantment.LURE, 1, true);
		meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
		item.setItemMeta(meta);
		loot = new CrateLoot(1,"La sueur d'itrooz", item);
		ct3.addLoot(loot);

		item = new ItemStack(Material.PORK);
		meta = item.getItemMeta();
		meta.setDisplayName("Restes de Poporc");
		meta.addEnchant(Enchantment.LURE, 1, true);
		meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
		item.setItemMeta(meta);
		loot = new CrateLoot(1,"Restes de Poporc", item);
		ct3.addLoot(loot);

		item = PetsUtils.getSkull(new ItemStack(Material.SKULL_ITEM, 1, (short)3),"skull_toad", "10a2ed5bb46c24e8ed94c279c5769bb86556a48469a3745b03f2abfb3fdb75");
		meta = item.getItemMeta();
		meta.setDisplayName("Joyeux Aujourd'hui !");
		meta.addEnchant(Enchantment.LURE, 1, true);
		meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
		item.setItemMeta(meta);
		loot = new CrateLoot(1,"Joyeux Aujourd'hui !", item);
		ct3.addLoot(loot);

		item = new ItemStack(Material.MONSTER_EGGS,1,(short) 1);
		meta = item.getItemMeta();
		meta.setDisplayName("cheh");
		meta.addEnchant(Enchantment.LURE, 1, true);
		meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
		item.setItemMeta(meta);
		loot = new CrateLoot(1,"cheh", item);
		ct3.addLoot(loot);


		ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
		bookMeta.setTitle("DLC Chapter 4");
		bookMeta.setAuthor("Gnobeo");
		List<String> pages = new ArrayList<String>();
		pages.add("Nop , on est pas sur Antimatter chemistry , donc \\\"oN fé Pa dRakOnik\\\" \\n "); // Page 1
		pages.add("Ps : Vous avez presque eu un ore excavation dans ce livre , dites merci au temps qui manquait"); // Page 2
		bookMeta.setPages(pages);
		writtenBook.setItemMeta(bookMeta);
		loot = new CrateLoot(1,"DLC Chapter 4", writtenBook);
		ct3.addLoot(loot);

		ItemStack writtenBook2 = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookMeta2 = (BookMeta) writtenBook.getItemMeta();
		bookMeta2.setTitle("La légende du roi-dieu");
		bookMeta2.setAuthor("§kRohetan§0");
		List<String> pages2 = new ArrayList<String>();
		pages2.add("Il était une fois, dans un très lointain pays d'antan, un être dont les capacitées pouvait transcender la réalitée êlle même.Cet être modifia alors la réalité et ses capacitées afin de la modeler à son image.Il voyagea, explora et vécut alors dans son §0");
		pages2.add("pays avec comme objectif de corriger les défauts de celui-ci.Mais un jour, il se rendit compte que son vaste pays était vide, qu'il était seul et nombre défauts fallait il encore éliminer.Il décida alors de créer des Invoqués.§0");
		pages2.add("Les Invoqués aidait l'être, et, dans le besoin de nom, ce dernier accepta celui donné par ses Invoqués.\n" +
				"\n" +
				"Le Roi-Dieu désormais vénéré, corrigea, modifia, réassembla ce pays jusqu'a la dernière once de son pouvoir.§0");
		pages2.add("Épuisé, il perdit son apparance, mais les Invoqués continuait de croire en lui. Il devint alors un être de même forme que ses Invoqués, et la légende dit qu'il reigne toujours...\n");

		bookMeta2.setPages(pages2);
		writtenBook2.setItemMeta(bookMeta2);
		loot = new CrateLoot(1,"La légende du roi-dieu", writtenBook2);
		ct3.addLoot(loot);

		ItemStack writtenBook3 = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookMeta3 = (BookMeta) writtenBook3.getItemMeta();
		bookMeta3.setTitle("Historique des commits");
		bookMeta3.setAuthor("iTrooz_");
		List<String> pages3 = new ArrayList<String>();
		pages3.add("commit automatique 7aa58e\n");
		pages3.add("transfert vers github a8639ec\n");
		pages3.add("update fb9ac30\n");
		pages3.add(" 8c6931e\n" +
				"\n" +
				"\n");
		pages3.add("test f9a291a\n");
		pages3.add("NullCommitException 6a8afa5\n");
		pages3.add("\nWEEEEE ARE THE CHAMPIONS 8cc06fb\n");
		pages3.add("bugfixes  93550de\n");
		pages3.add("create LICENSE 084107b\n");
		pages3.add("Creation 304e36c\n");
		pages3.add("VOUUUUUUUUUS NE PASSEZ PAAAAA ef96352\n");
		pages3.add("La terre est plate\n" +
				"4f5c0de\n" +
				"\n" +
				"Non, la terre est ronde ! 33683ab\n" +
				"\n" +
				"Bande d'idiots, la terre est héxagonale !! 401b545\n" +
				"\n" +
				"La terre est une invention du gouvernement pour avoir du pouvoir. 567dc61");
		pages3.add(" 1a98992\n");
		pages3.add("72db97b temp\n");
		pages3.add("c + bo c6a06fa\n");
		pages3.add("F F GO BACK d615727\n");
		pages3.add("Hi, I'm a commit ! 09e8400\n");
		pages3.add("fixes d7a6da2\n");
		pages3.add("oops 86f4e12\n");
		pages3.add("I see no god up there... other than ME  7a49f80\n");
		pages3.add("It worked senpai UwU 4be7370\n");
		pages3.add("adds fd45700\n");
		pages3.add("Les bots vont mourir (et moi aussi) 052284e\n");
		pages3.add("they called me a madman f7d38d8\n");
		pages3.add("we're no strangers to love bad bad2129\n");
		pages3.add("many MANY things 0880330\n");
		pages3.add("toujours plus de cooooode 50cc76d\n");
		pages3.add("wtf final ? 33dc879\n");
		pages3.add("#SauvezOlaf 11540e9\n");
		pages3.add("fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"fix\n" +
				"finish line ?\n" +
				"finish line\n" +
				"\n" +
				"Projet suivant !");
		bookMeta3.setPages(pages3);
		writtenBook3.setItemMeta(bookMeta3);
		loot = new CrateLoot(1,"Historique des commits", writtenBook3);
		ct3.addLoot(loot);

		CratesAPI.registerCrateType(ct3);



	}

}
