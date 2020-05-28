package fr.entasia.skytools.tasks;

import fr.entasia.apis.other.InstantFirework;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class FWArrowTask extends BukkitRunnable {

	public Entity ent;

	public static void start(Entity ent){
		FWArrowTask ftask = new FWArrowTask(ent);
		ftask.runTaskTimer(Main.main, 8, 10);
	}

	private FWArrowTask(Entity ent){
		this.ent = ent;
	}

	public void run(){
		if(ent.isValid()&&!ent.isOnGround()&&ent.getTicksLived()<500){
			InstantFirework.explode(ent.getLocation(), Utils.metas[Main.random.nextInt(Utils.metas.length)]);
		}else cancel();
	}
}
