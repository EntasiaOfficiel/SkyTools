package fr.entasia.skytools;

import fr.entasia.skytools.commands.SWCmd;
import fr.entasia.skytools.commands.SkyToolsCmd;
import fr.entasia.skytools.commands.WarpCmd;
import fr.entasia.skytools.commands.custom.CustomArrowCmd;
import fr.entasia.skytools.commands.custom.CustomEnchantCmd;
import fr.entasia.skytools.events.*;
import fr.entasia.skytools.events.cenchants.ArrowEvents;
import fr.entasia.skytools.events.cenchants.EnchantEvents;
import fr.entasia.skytools.events.cenchants.EnchantEvents2;
import fr.entasia.skytools.events.cenchants.SkyFisherEvents;
import fr.entasia.skytools.objs.Warp;
import fr.entasia.skytools.objs.custom.CustomArrows;
import fr.entasia.skytools.tasks.CleanUpTask;
import fr.entasia.skytools.tasks.LavaTask;
import fr.entasia.skytools.tasks.SWTask;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.Random;

public class Main extends JavaPlugin {

	/*
	Stratégie actuelle :

	- Charger toutes les iles au démarrage
	- Charger tout les joueurs au démarragze
	 */

	public static Main main;

	public static Random random = new Random();

	public static File warpsfile;
	public static FileConfiguration warpsconfig;

	public static String guessWord = null;
	public static boolean enableDev;


	@Override
	public void onEnable(){
		try{
			main = this;
			getLogger().info("Activation du plugin..");
			saveDefaultConfig();
			enableDev = main.getConfig().getBoolean("dev", false);

			warpsfile = new File(getDataFolder()+"/warps.yml");
			warpsfile.createNewFile();
			warpsconfig = YamlConfiguration.loadConfiguration(warpsfile);

			loadConfigs();

			registerRecipes();
			registerFW();

			getServer().getPluginManager().registerEvents(new ChatEvents(), this);
			getServer().getPluginManager().registerEvents(new MiniEvents(), this);
			getServer().getPluginManager().registerEvents(new OthersEvents(), this);

			getServer().getPluginManager().registerEvents(new FireworksEvents(), this);
			getServer().getPluginManager().registerEvents(new SkullEvents(), this);
			getServer().getPluginManager().registerEvents(new EnchantEvents(), this);
			getServer().getPluginManager().registerEvents(new EnchantEvents2(), this);
			getServer().getPluginManager().registerEvents(new SkyFisherEvents(), this);
			getServer().getPluginManager().registerEvents(new ArrowEvents(), this);

			getCommand("skytools").setExecutor(new SkyToolsCmd());
			getCommand("speedwriter").setExecutor(new SWCmd());
			getCommand("warp").setExecutor(new WarpCmd());

			getCommand("customarrow").setExecutor(new CustomArrowCmd());
			getCommand("customenchant").setExecutor(new CustomEnchantCmd());

			new SWTask().runTaskTimerAsynchronously(this, 0, 10*60*20);
			new CleanUpTask().runTaskTimerAsynchronously(this, 0, 5*60*20);
			// a voir pour mettre tout ca plus bas par joueur ?
			new LavaTask().runTaskTimerAsynchronously(this, 0, 20);

			getLogger().info("Plugin activé !");
		}catch(Throwable e){
			e.printStackTrace();
			if(!enableDev){
				getLogger().severe("Erreur lors du chargement du plugin ! ARRET DU SERVEUR");
				getServer().shutdown();
			}
		}
	}

	public static void loadConfigs() throws Exception {
		main.saveDefaultConfig();
		main.reloadConfig();

		Warp.warps.clear();
		ConfigurationSection cs;
		for (String i : warpsconfig.getKeys(false)) {
			cs = warpsconfig.getConfigurationSection(i);
			Warp a = new Warp();
			a.loc = new Location(Bukkit.getWorld(cs.getString("world")), cs.getInt("x") + 0.5, cs.getInt("y") + 0.2,
					cs.getInt("z") + 0.5, cs.getInt("yaw"), cs.getInt("pitch"));
			if (a.loc.getWorld() == null) main.getLogger().warning("MONDE INVALIDE POUR LE WARP " + i + " !");
			else{
				a.name = i;
				for (String j : cs.getStringList("desc")) {
					j = j.replace("&", "§");
					if (j.startsWith("§")) a.desc.add(j);
					else a.desc.add("§6" + j);
				}
				a.aliases = cs.getStringList("aliases").toArray(new String[0]);
				Warp.warps.put(i, a);
			}
		}

		Warp.spawnWarp = Warp.getWarp("spawn");
	}

	public static void registerRecipes(){
		ItemStack item = new ItemStack(Material.HUGE_MUSHROOM_1, 2);
		ItemMeta meta;

		ShapelessRecipe slrecipe = new ShapelessRecipe(new NamespacedKey(main, "brown_mushoom_block"), item);
		slrecipe.addIngredient(4, Material.BROWN_MUSHROOM);
		Bukkit.addRecipe(slrecipe);

		item = new ItemStack(Material.HUGE_MUSHROOM_2, 2);
		slrecipe = new ShapelessRecipe(new NamespacedKey(main, "red_mushoom_block"), item);
		slrecipe.addIngredient(4, Material.RED_MUSHROOM);
		Bukkit.addRecipe(slrecipe);

		item = new ItemStack(Material.POISONOUS_POTATO);
		slrecipe = new ShapelessRecipe(new NamespacedKey(main, "poison_potato_a"), item);
		slrecipe.addIngredient(1, Material.POTATO);
		slrecipe.addIngredient(1, Material.ROTTEN_FLESH);
		Bukkit.addRecipe(slrecipe);

		item.setAmount(3);
		slrecipe = new ShapelessRecipe(new NamespacedKey(main, "poison_potato_b"), item);
		slrecipe.addIngredient(1, Material.POTATO);
		slrecipe.addIngredient(1, Material.SPIDER_EYE);
		Bukkit.addRecipe(slrecipe);

		item.setAmount(5);
		slrecipe = new ShapelessRecipe(new NamespacedKey(main, "poison_potato_c"), item);
		slrecipe.addIngredient(1, Material.POTATO);
		slrecipe.addIngredient(1, Material.FERMENTED_SPIDER_EYE);
		Bukkit.addRecipe(slrecipe);

		item = new ItemStack(Material.DIRT);
		meta = item.getItemMeta();
		meta.setDisplayName("§6I am a flower pot");
		item.setItemMeta(meta);
		slrecipe = new ShapelessRecipe(new NamespacedKey(main, "dirt_lol"), item);
		slrecipe.addIngredient(1, Material.DIRT);
		Bukkit.addRecipe(slrecipe);

		item = new ItemStack(Material.TIPPED_ARROW);
		PotionMeta pmeta = (PotionMeta) item.getItemMeta();
		pmeta.setMainEffect(PotionEffectType.NIGHT_VISION);
		pmeta.setColor(Color.fromRGB(255, 153, 255));
		pmeta.setDisplayName("§f"+CustomArrows.FIREWORKS.name);
		item.setItemMeta(pmeta);
		slrecipe = new ShapelessRecipe(new NamespacedKey(main, "arrow_fireworks"), item);
		slrecipe.addIngredient(1, Material.ARROW);
		slrecipe.addIngredient(4, Material.FIREWORK);
		Bukkit.addRecipe(slrecipe);

		item = new ItemStack(Material.TIPPED_ARROW);
		pmeta = (PotionMeta) item.getItemMeta();
		pmeta.setColor(Color.fromRGB(3, 3, 4));
		pmeta.setDisplayName("§f"+CustomArrows.EXPLOSION.name);
		item.setItemMeta(pmeta);
		slrecipe = new ShapelessRecipe(new NamespacedKey(main, "arrow_explosion"), item);
		slrecipe.addIngredient(1, Material.ARROW);
		slrecipe.addIngredient(1, Material.SULPHUR);
		Bukkit.addRecipe(slrecipe);
	}

	public static void registerFW(){

		FireworkMeta base = (FireworkMeta) Bukkit.getItemFactory().getItemMeta(Material.FIREWORK);


		Utils.metas = new FireworkMeta[3];

		Utils.metas[0] = base.clone();
		Utils.metas[0].addEffect(FireworkEffect.builder().withColor(Color.fromRGB(255, 102, 163), Color.ORANGE, Color.MAROON).build());

		Utils.metas[1] = base.clone();
		Utils.metas[1].addEffect(FireworkEffect.builder().withColor(Color.GREEN, Color.BLUE).build());

		Utils.metas[2] = base.clone();
		Utils.metas[2].addEffect(FireworkEffect.builder().withColor(Color.fromRGB(255, 0, 102), Color.PURPLE, Color.GRAY).build());

//		Utils.metas[3] = base.clone();
//		Utils.metas[3].addEffect(FireworkEffect.builder().withColor(Color.BLUE).build());
//
//		Utils.metas[4] = base.clone();
//		Utils.metas[4].addEffect(FireworkEffect.builder().withColor(Color.BLUE).build());
//
//		Utils.metas[5] = base.clone();
//		Utils.metas[5].addEffect(FireworkEffect.builder().withColor(Color.BLUE).withFade(Color.MAROON).build());
	}

}
