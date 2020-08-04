package fr.entasia.skytools.events.cenchants;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import fr.entasia.apis.other.Mutable;
import fr.entasia.apis.other.Pair;
import fr.entasia.skycore.apis.InternalAPI;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import fr.entasia.skytools.objs.custom.RomanUtils;
import fr.entasia.skytools.tasks.LavaTask;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class EnchantEvents implements Listener {

	@EventHandler
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
								InternalAPI.warn("Enchantement invalide : "+pair.key.replace("§", "&")+" (lore ?)", false);
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


//	public void a(EnchantItemEvent e){
//		e.getExpLevelCost()
//	}
}
