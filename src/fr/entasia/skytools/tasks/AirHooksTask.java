package fr.entasia.skytools.tasks;

import fr.entasia.apis.other.Randomiser;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.Color;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AirHooksTask extends BukkitRunnable {

	// constantes
	public static final float iterations = 120;
	public static final int radius = 20;
	public static final Vector baserVector = new Vector(radius, radius, radius);


	// base
	public FishHook hook;
	public ArmorStand armorStand;
	public int counter = 0;


	// pour bézier
	public Location base;
	public World w;
	public Vector[] points;
	public byte state = 0;

	// particules
	public Particle background=null;
	public Color backgroundColor=null;
	public Particle fish=null;
	public Color fishColor=null;

	// fonctions
	public static Vector randomVector(){
		return Vector.getRandom().multiply(radius*2);
	}

	public static double color(int color){
		return color/255f;
	}


	public void init(){

		counter = Main.random.nextInt(15*10)+4*10; // 10 pas 20 car periode de 2 ticks

		base = armorStand.getLocation().subtract(baserVector);

		points = new Vector[Main.random.nextInt(2)+4];
		for(int i=0;i<points.length-1;i++){
			points[i] = randomVector();
		}

		points[points.length-1] = baserVector;


		runTaskTimer(Main.main, 0, 2);
	}

	public boolean isTrapped(){
		if(state==1)return getVector().distance(baserVector) < 1;
		else return state == 2;
	}


	public void run() {
		if (hook.isValid()) {

			if(background!=null){
				for (int j = 0; j < 50; j++) {
					w.spawnParticle(background, base.clone().add(randomVector()), 0, backgroundColor.r, backgroundColor.g, backgroundColor.b, 1);
				}
			}

			if(state==0) {
				counter--;
				if (counter == -1) {
					state = 1;
				}
			}else {
				Vector a=null;
				if (state == 1) {
					counter++;
					a = getVector();
					if (counter == iterations) {
						state = 2;
						counter = 0;
					}
				} else if (state == 2){
					counter++;
					if (counter == 10) stop();
					a = baserVector;
				}
				w.spawnParticle(fish, base.clone().add(a), 10, fishColor.r, fishColor.g, fishColor.b, 0);
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


	private Location centerLoc(){
		return base.clone().add(baserVector);
	}

	private Entity entity(ItemStack item){
		return w.dropItem(centerLoc(), item);
	}


	// Merci Narcos pour les loots !
	public Entity owLoot(){
		Randomiser r = new Randomiser();
		System.out.println(r.getCPercent());

		return null;
	}

	public Entity netherLoot(){
		Randomiser r = new Randomiser();
		System.out.println(r.getCPercent());
		if(r.isInNext(10)) {
			return entity(new ItemStack(Material.SOUL_SAND));
		}else if(r.isInNext(10)){
			return entity(new ItemStack(Material.BLAZE_ROD));
		}else if(r.isInNext(15)) {
			return entity(new ItemStack(Material.FIREBALL));
		}


		if(r.isInNext(5)) {
			return w.spawnEntity(centerLoc(), EntityType.SMALL_FIREBALL);
		}else if(r.isInNext(10)){
			return w.spawnEntity(centerLoc(), EntityType.BLAZE);
		}else if(r.isInNext(5)){
			return w.spawnEntity(centerLoc(), EntityType.GHAST);
		}else if(r.isInNext(0.5)) {
			return w.spawnEntity(centerLoc(), EntityType.WITHER);
		}

		if(r.isInNext(4)){
		    ItemStack item = new ItemStack(Material.STONE_SWORD);
			item.setDurability((short) (Randomiser.random.nextInt(60)+40));
		    ItemMeta meta = item.getItemMeta();
		    CustomEnchants.WITHER.enchant(meta, 1);
		    item.setItemMeta(meta);
		    return entity(item);
		}else if(r.isInNext(2)){
		    ItemStack item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		    item.setDurability((short) (Randomiser.random.nextInt(100)+50));
		    ItemMeta meta = item.getItemMeta();
		    CustomEnchants.LAVA_EATER.enchant(meta, 1);
		    item.setItemMeta(meta);
		    return entity(item);
		}else if(r.isInNext(1)){
		    ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
			item.setDurability((short) (Randomiser.random.nextInt(100)+50));
		    ItemMeta meta = item.getItemMeta();
		    CustomEnchants.LAVA_EATER.enchant(meta, 1);
		    item.setItemMeta(meta);
		    return entity(item);
		}else{
			if(r.getNumber()%2==0) return entity(new ItemStack(Material.GLOWSTONE));
			else return entity(new ItemStack(Material.NETHERRACK));
		}
	}

	public Entity endLoot(){
		Randomiser r = new Randomiser();
		System.out.println(r.getCPercent());
		if(r.isInNext(15)){
			return entity(new ItemStack(Material.CHORUS_FRUIT));
		}


		if(r.isInNext(5)) {
			LingeringPotion lp = (LingeringPotion)w.spawnEntity(centerLoc(), EntityType.LINGERING_POTION);
//			lp.set
			return w.spawnEntity(centerLoc(), EntityType.LINGERING_POTION);
		}else if(r.isInNext(2)){
			return w.spawnEntity(centerLoc(), EntityType.ENDERMITE);
		}else if(r.isInNext(1)){
			return w.spawnEntity(centerLoc(), EntityType.ENDERMAN);
		}else if(r.isInNext(0.5)) {
			return w.spawnEntity(centerLoc(), EntityType.ENDER_DRAGON);
		}


		return null;
	}


}
