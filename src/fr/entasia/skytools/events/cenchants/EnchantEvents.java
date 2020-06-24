package fr.entasia.skytools.events.cenchants;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.Mutable;
import fr.entasia.skytools.objs.Pair;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import fr.entasia.skytools.objs.custom.RomanUtils;
import fr.entasia.skytools.tasks.LavaTask;
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
				int r = Main.random.nextInt(100);
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

	public static Pair<String, Integer> parseEnchant(String s){
		Pair<String, Integer> pair = new Pair<>();
		String[] split = s.split(" ");
		pair.a = String.join(" ", Arrays.copyOfRange(split, 0, split.length-1));
		pair.b = RomanUtils.toInt(split[split.length-1]);
		return pair;
	}

	@EventHandler
	public void a(PrepareAnvilEvent e) {
		ItemStack item1 = e.getInventory().getItem(0);
		if (item1 == null) return;
		ItemStack item2 = e.getInventory().getItem(1);
		if (item2 == null) return;
		List<String> lore = new ArrayList<>();
		Map<String, Mutable<Integer>> ench1 = new HashMap<>();

		List<String> temp = item1.getLore();
		Pair<String, Integer> pair;
		if (temp != null) {
			boolean pass = false;
			for (String line : temp) {
				if(pass)lore.add(line);
				else if(line.startsWith("§6§r§7")){
					pair = parseEnchant(line);
					ench1.put(pair.a, new Mutable<>(pair.b));
				}else{
					pass = true;
					lore.add(line);
				}
			}
		}

		temp = item2.getLore();
		Mutable<Integer> lvl;
		if (temp == null)temp = new ArrayList<>();
		else{
			for (String line : temp) {
				if (line.startsWith("§6§r§7")){
					pair = parseEnchant(line);
					lvl = ench1.get(pair.a);
					if (lvl == null) ench1.put(pair.a, new Mutable<>(pair.b));
					else {
						if (lvl.value == pair.b.intValue()) lvl.value++;
						else lvl.value = Math.max(lvl.value, pair.b);
					}
				}else break;
			}
			temp.clear();
		}
		for (Map.Entry<String, Mutable<Integer>> entry : ench1.entrySet()) {
			temp.add(entry.getKey() + " " + entry.getValue());
		}
		temp.addAll(lore);
		e.getResult().setLore(temp);
	}
}
