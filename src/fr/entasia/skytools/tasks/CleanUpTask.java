package fr.entasia.skytools.tasks;

import fr.entasia.skytools.Utils;
import fr.entasia.skytools.events.EnchantEvents2;
import fr.entasia.skytools.events.FireworksEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CleanUpTask extends BukkitRunnable {
	@Override
	public void run() {
		FireworksEvents.fireworks.entrySet().removeIf(e->!e.getKey().isValid()||e.getKey().getTicksLived()>500);
		EnchantEvents2.explosions.removeIf(e->!e.isValid()||e.getTicksLived()>500);

		for(Player p : Bukkit.getOnlinePlayers()){
			Utils.checkEffects(p);
		}
	}
}
