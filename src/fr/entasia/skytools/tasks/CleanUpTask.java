package fr.entasia.skytools.tasks;

import fr.entasia.skytools.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CleanUpTask extends BukkitRunnable {
	@Override
	public void run() {
		Utils.fireworks.entrySet().removeIf(e->!e.getKey().isValid()||e.getKey().getTicksLived()>500);
		Utils.airHookTasks.removeIf(ah -> { // boucle
			if (!ah.hook.isValid() || ah.hook.getTicksLived() > 5000 ) {
				ah.hook.remove();
				ah.armorStand.remove();
				return true;
			} else return false;
		});

		for(Player p : Bukkit.getOnlinePlayers()){
			Utils.updateEffects(p);
		}
	}
}
