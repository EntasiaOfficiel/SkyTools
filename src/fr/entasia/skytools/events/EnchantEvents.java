package fr.entasia.skytools.events;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import fr.entasia.skytools.tasks.ConstantTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class EnchantEvents implements Listener {

	@EventHandler
	public void a(PlayerItemDamageEvent e) {
		if(ConstantTask.isLava(e.getPlayer().getLocation().getBlock().getType())){
			if(CustomEnchants.LAVA_EATER.hasEnchant(e.getItem())) e.setCancelled(true);
		}
	}

	public static final int PERCENT = 10;

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			int l = CustomEnchants.VAMPIRE.getLevel(p.getInventory().getItemInMainHand());
			if(l!=0){
				int r = Main.random.nextInt(100);
				if(r<PERCENT*l){
					double heal = p.getHealth();
					heal+=e.getDamage()/2f;
					if(heal>p.getMaxHealth())heal = p.getMaxHealth();
					p.setHealth(heal);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void a(PlayerArmorChangeEvent e) {
		Utils.checkEffects(e.getPlayer());
	}
}
