package fr.entasia.skytools.tasks;

import fr.entasia.skycore.others.enums.Dimensions;
import fr.entasia.skytools.Main;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class SquidTask extends BukkitRunnable {

	public static int offset = 1;
	public static ArrayList<Squid> tracker = new ArrayList<>();
	public static BukkitTask task;

	public static void start(){
		task = new SquidTask().runTaskTimer(Main.main, 100, 20);
	}

	public static void stop(){
		if(task==null)return;
		task.cancel();
		for(Entity ent : SquidTask.tracker){
			ent.remove();
		}
		task = null;
	}

	@Override
	public void run() {
		int i = 0;
		Location l = new Location(Dimensions.OVERWORLD.world, 0, 255, 0);

		for (Chunk c : Dimensions.OVERWORLD.world.getLoadedChunks()) {
			i++;
			if (i % 10 != offset) continue;
			int r = Main.r.nextInt(256);
			l.setX((c.getX() * 16 + r / 16) + 0.5); // tkt
			l.setZ((c.getZ() * 16 + r % 16) + 0.5);
			Squid ent = (Squid) Dimensions.OVERWORLD.world.spawnEntity(l, EntityType.SQUID);
			ent.setRemoveWhenFarAway(false);
			ent.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 99999, 1));
		}
		offset++;
		if (offset == 10) offset = 0;
	}
}
