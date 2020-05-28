package fr.entasia.skytools.tasks;

import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.FishHook;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AirHooksTask extends BukkitRunnable {


	public static final float iterations = 150;
	public static final int radius = 20;
	public static final Vector baserVector = new Vector(radius, radius, radius);

	public static Vector randomVector(){
		return Vector.getRandom().multiply(radius*2);
	}

	public FishHook hook;
	public ArmorStand armorStand;
	public int counter = 0;

	// pour bézier
	public Location base;
	public World w;
	public Vector[] points;
	public boolean spawned = false;

	public void init(){

		counter = Main.random.nextInt(15*20)+4*20;

		base = armorStand.getLocation().subtract(baserVector);
		w = base.getWorld();

		points = new Vector[Main.random.nextInt(2)+4];
		for(int i=0;i<points.length-1;i++){
			points[i] = randomVector();
		}

		points[points.length-1] = baserVector;

		runTaskTimer(Main.main, 0, 2);
	}

	public boolean isTrapped(){
		Vector v = getVector();
		return v.distance(baserVector) < 1;
	}


	public void run() {
		if (hook.isValid()) {
			System.out.println(counter);
			for (int j = 0; j < 20; j++) {
				w.spawnParticle(
						Particle.CLOUD,
						base.clone().add(randomVector())
						, 1, 0, 0, 0, 0);
			}

			if (spawned) {
				counter += 1;
				if (counter == iterations) stop();
				Vector a = getVector();
				w.spawnParticle(Particle.END_ROD, base.clone().add(a), 10, 0, 0, 0, 0);
			} else {
				counter--;
				if (counter == -1) spawned = true;
			}
		}else stop();
	}

	public Vector getVector(){
		float percent = counter/iterations;
		Vector[] a = points;

		while(a.length > 1){
			Vector[] b = new Vector[a.length-1];
			for(int j=0;j<b.length;j++){
				double temp_x = (a[j + 1].getX() - a[j].getX()) * percent;
				double temp_y = (a[j + 1].getY() - a[j].getY()) * percent;
				double temp_z = (a[j + 1].getZ() - a[j].getZ()) * percent;
				b[j] = new Vector(temp_x +a[j].getX(), temp_y +a[j].getY(), temp_z +a[j].getZ());
			}
			a = b;
		}
		return a[0];
	}

	public void stop() {
		Bukkit.broadcastMessage("arret");
		hook.remove();
		armorStand.remove();
		Utils.airHookTasks.remove(this);
		cancel();
	}
}
