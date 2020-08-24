package fr.entasia.skytools.events.cenchants;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import fr.entasia.apis.nbt.ItemNBT;
import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.apis.nbt.NBTTypes;
import fr.entasia.apis.other.ItemBuilder;
import fr.entasia.apis.other.ItemCategory;
import fr.entasia.apis.other.Mutable;
import fr.entasia.apis.other.Pair;
import fr.entasia.apis.utils.ServerUtils;
import fr.entasia.apis.utils.TextUtils;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import fr.entasia.skytools.objs.custom.RomanUtils;
import fr.entasia.skytools.tasks.LavaTask;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EnchantEvents implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(PlayerItemDamageEvent e) {
		if(LavaTask.isLava(e.getPlayer().getLocation().getBlock().getType())){
			if(CustomEnchants.LAVA_EATER.hasEnchant(e.getItem())) e.setCancelled(true);
		}
	}

	public static final int PERCENT = 10;

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			ItemStack item = p.getInventory().getItemInMainHand();
			int l = CustomEnchants.VAMPIRE.getLevel(item);
			if(l!=0){
				int r = Main.r.nextInt(100);
				if(r<PERCENT*l){
					double heal = p.getHealth();
					heal+=e.getDamage()/2f;
					if(heal>p.getMaxHealth())heal = p.getMaxHealth();
					p.setHealth(heal);
				}
			}
			l = CustomEnchants.WITHER.getLevel(item);
			if(l!=0){
				((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, l-1), true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(PlayerArmorChangeEvent e) {
		Utils.updateEffects(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(BlockBreakEvent e) {
		e.setExpToDrop(0);
		e.setDropItems(false);
		if(e.getBlock().getType()==Material.MOB_SPAWNER){
			CreatureSpawner cs = (CreatureSpawner) e.getBlock().getState();
			StringBuilder sb = new StringBuilder();
			for(String s : cs.getSpawnedType().name().split("_")){
				sb.append(TextUtils.firstLetterUpper(s)).append(" ");
			}

			NBTComponent nbt = new NBTComponent();
			nbt.setValue(NBTTypes.String, "Entity", cs.getSpawnedType().name());
			ItemStack item = new ItemBuilder(Material.MOB_SPAWNER).nbt(nbt).lore("§7Mob : "+sb).build();

			e.getBlock().setType(Material.AIR);
			new BukkitRunnable() {
				@Override
				public void run() {
					e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), item);
				}
			}.runTask(Main.main);
		}
	}

	@EventHandler
	public void a(BlockPlaceEvent e) {
		if(e.getItemInHand().getType() != Material.MOB_SPAWNER)return;
		NBTComponent nbt = ItemNBT.getNBTSafe(e.getItemInHand());
		String s = (String) nbt.getValue(NBTTypes.String, "Entity");
		if(s.equals(""))return;
		EntityType ent;
		try{
			ent = EntityType.valueOf(s);
		}catch(IllegalArgumentException e2){
			e2.printStackTrace();
			e.getPlayer().sendMessage("§cUne erreur est survenue ! Préviens un membre du Staff");
			return;
		}
		CreatureSpawner cs = (CreatureSpawner) e.getBlockPlaced().getState();
		cs.setSpawnedType(ent);
		cs.update();
	}

	public static Pair<String, String> parseEnchant(String s){
		Pair<String, String> pair = new Pair<>();
		String[] split = s.split(" ");
		pair.key = String.join(" ", Arrays.copyOfRange(split, 0, split.length-1));
		pair.value = split[split.length-1];
		return pair;
	}

	private boolean a(ItemStack a){
		return a == null || a.getType() == Material.AIR;
	}

	@EventHandler
	public void a(PrepareAnvilEvent e) { // gngn ca s'éxecute 3 fois
		ItemStack item1 = e.getInventory().getItem(0);
		if (a(item1)) return;
		ItemStack item2 = e.getInventory().getItem(1);
		if (a(item2)) return;
		ItemStack result = e.getResult();
		if (a(result)) return;

		List<String> lore = new ArrayList<>();
		Map<String, Mutable<String>> ench1 = new HashMap<>();

		List<String> temp = item1.getLore();
		Pair<String, String> pair;
		if (temp != null) {
			boolean pass = false;
			for (String line : temp) {
				if(pass)lore.add(line);
				else if(line.startsWith("§6§r§7")){
					pair = parseEnchant(line);
					ench1.put(pair.key, new Mutable<>(pair.value));
				}else{
					pass = true;
					lore.add(line);
				}
			}
		}


		temp = item2.getLore();
		Mutable<String> lvl;
		if (temp == null)temp = new ArrayList<>();
		else{
			for (String line : temp) {
				if (line.startsWith("§6§r§7")){
					pair = parseEnchant(line);
					lvl = ench1.get(pair.key);
					if (lvl == null) ench1.put(pair.key, new Mutable<>(pair.value));
					else {
						if (lvl.value.equals(pair.value)){
							CustomEnchants ench = CustomEnchants.get(pair.key);
							if(ench==null){
								ServerUtils.permMsg("log.skytools","Enchantement invalide : "+pair.key.replace("§", "&")+" (lore ?)");
								continue; // poof il disparait (pas sensé se produire)
							}
							int rlvl = RomanUtils.toInt(lvl.value)+1;
							lvl.value = RomanUtils.toRoman(Math.min(rlvl, ench.maxlvl));
						}else{
							lvl.value = RomanUtils.max(lvl.value, pair.value);
						}
					}
				}else break;
			}
			temp.clear();
		}
		for (Map.Entry<String, Mutable<String>> entry : ench1.entrySet()) {
			temp.add(entry.getKey() + " " + entry.getValue().value);
		}
		temp.addAll(lore);
		result.setLore(temp);
	}


	@EventHandler
	public void a(EnchantItemEvent e){
		for(EnchantEntry entry : entries){
			if(e.getExpLevelCost()<entry.neededLvl)continue;
			if(!entry.items.contains(e.getItem().getType()))continue;
			int lvl = 0;
			for(int i=0;i<entry.maxlvl;i++){
				if(entry.percent==100||Main.r.nextInt(100)<entry.percent){
					lvl++;
				}else break;
			}
			if(lvl==0)continue;
			entry.enchant.enchant(e.getItem(), lvl);
		}
	}

	public static ArrayList<EnchantEntry> entries = new ArrayList<>();

	static{
		entries.add(new EnchantEntry(CustomEnchants.VISION, 13, 35, 2, ItemCategory.HELMETS));
		entries.add(new EnchantEntry(CustomEnchants.SPEED, 13, 35, 2, ItemCategory.BOOTS));
		entries.add(new EnchantEntry(CustomEnchants.JUMP, 13, 35, 2, ItemCategory.BOOTS));
		entries.add(new EnchantEntry(CustomEnchants.LAVA_EATER, 15, 30, 1, ItemCategory.ARMORS));

		entries.add(new EnchantEntry(CustomEnchants.WITHER, 18, 30, 1, ItemCategory.SWORDS));

		entries.add(new EnchantEntry(CustomEnchants.SKY_FISHER, 30, 50, 1, Material.FISHING_ROD));
	}
	
	public static class EnchantEntry{
		public CustomEnchants enchant;
		public int neededLvl;
		public int percent;
		public int maxlvl;
		public HashSet<Material> items;

		public EnchantEntry(CustomEnchants enchant, int neededLvl, int percent, int maxlvl, ItemCategory cat){
			this.enchant = enchant;
			this.neededLvl = neededLvl;
			this.percent = percent;
			this.maxlvl = maxlvl;
			this.items = cat.content;
		}

		public EnchantEntry(CustomEnchants enchant, int neededLvl, int percent, int maxlvl, Material... items){
			this.enchant = enchant;
			this.neededLvl = neededLvl;
			this.percent = percent;
			this.maxlvl = maxlvl;
			this.items = new HashSet<>(Arrays.asList(items));
		}
	}
	
}
