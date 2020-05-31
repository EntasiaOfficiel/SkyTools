package fr.entasia.skytools.tasks;

import fr.entasia.apis.other.Randomiser;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.Color;
import fr.entasia.skytools.objs.custom.CustomArrows;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import javafx.scene.layout.BorderWidths;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collections;

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

	// fonctions
	public static Vector randomVector(){
		return Vector.getRandom().multiply(radius*2);
	}


	public void initColors (){
		backgroundColor = new Color(0, 0, 0);
		switch(w.getEnvironment()){
			case NORMAL: {
				if(w.getTime()>13000){
					fish = Particle.SMOKE_NORMAL;

					background = Particle.REDSTONE;
					backgroundColor = new Color(128, 128, 128);
				}else{
					fish = Particle.END_ROD;

					background = Particle.REDSTONE;
					backgroundColor = new Color(210, 240, 255);
				}
				if(w.hasStorm())background = null;
				break;
			}
			case NETHER:{
				fish = Particle.FLAME;

				background = Particle.REDSTONE;
				backgroundColor = new Color(255, 153, 0);
				break;
			}
			case THE_END:{
				fish = Particle.DRAGON_BREATH;

				background = Particle.REDSTONE;
				backgroundColor = new Color(160, 160, 160);
				break;
			}
		}

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
				w.spawnParticle(fish, base.clone().add(a), 10, 0, 0, 0, 0);
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

	// Merci Narcos pour les loots !

	public Entity owLoot(){
		Randomiser r = new Randomiser();
		if(r.isInNext(30)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.FEATHER));
		}else if(r.isInNext(20)) {
			ItemStack item = new ItemStack(Material.BOW);
			item.setDurability((short) (Randomiser.random.nextInt(300)+25));
			return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(5)) {
			ItemStack item = new ItemStack(Material.ELYTRA);
			item.setDurability((short) (Randomiser.random.nextInt(300)+75));
			return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(10)) {
			ItemStack item = new ItemStack(Material.ARROW);
			item.setDurability((short) (Randomiser.random.nextInt(300)+75));
			return w.dropItem(centerLoc(), item);
		}

		if(r.isInNext(8)) {
			return w.spawnEntity(centerLoc(), EntityType.PARROT);
		}else if(r.isInNext(3)) {
			return w.spawnEntity(centerLoc(), EntityType.CHICKEN);
		}else if(r.isInNext(1)) {
			if(w.hasStorm()){
				return w.spawnEntity(centerLoc(), EntityType.LIGHTNING);
			}else{
				ItemStack item = new ItemStack(Material.DIAMOND);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("§bEnta§7sia");
				meta.setLore(Collections.singletonList("§eThanks you for playing my game :)"));
				item.setItemMeta(meta);
				return w.dropItem(centerLoc(), item);
			}
		}
		if(r.isInNext(10)) {
			ItemStack item = new ItemStack(Material.ARROW);
			CustomArrows.EXPLOSION.enchant(item);
			return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(10)) {
			ItemStack item = new ItemStack(Material.ARROW);
			CustomArrows.FIREWORKS.enchant(item);
			return w.dropItem(centerLoc(), item);
		}else{
			if(r.getNumber()%2==0) return w.dropItem(centerLoc(), new ItemStack(Material.ARROW));
			else{
				ItemStack item = new ItemStack(Material.FIREWORK);
				FireworkMeta meta = (FireworkMeta) item.getItemMeta();
				meta.addEffect(FireworkEffect.builder().withColor(org.bukkit.Color.RED, org.bukkit.Color.PURPLE).build());
				meta.setPower(2);
				return w.dropItem(centerLoc(), new ItemStack(Material.FIREWORK));
			}
		}
	}

	public Entity netherLoot(){
		Randomiser r = new Randomiser();

		if(r.isInNext(20)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.SOUL_SAND));
		}else if(r.isInNext(15)){
			return w.dropItem(centerLoc(), new ItemStack(Material.BLAZE_ROD));
		}else if(r.isInNext(15)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.FIREBALL));
		}else if(r.isInNext(10)) {
			ItemStack item = new ItemStack(Material.ARROW);
			CustomArrows.FIREWORKS.enchant(item);
			return w.dropItem(centerLoc(), item);
		}

		if(r.isInNext(5)) {
			return w.spawnEntity(centerLoc(), EntityType.SMALL_FIREBALL);
		}else if(r.isInNext(4)) {
			return w.spawnEntity(centerLoc(), EntityType.FIREBALL);
		}else if(r.isInNext(10)){
			return w.spawnEntity(centerLoc(), EntityType.BLAZE);
		}else if(r.isInNext(5)){
			return w.spawnEntity(centerLoc(), EntityType.GHAST);
		}else if(r.isInNext(1)) {
			return w.spawnEntity(centerLoc(), EntityType.WITHER);
		}

		if(r.isInNext(10)) {
			ItemStack item = new ItemStack(Material.ARROW);
			CustomArrows.FIRE.enchant(item);
			return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(5)){
		    ItemStack item = new ItemStack(Material.STONE_SWORD);
			item.setDurability((short) (Randomiser.random.nextInt(60)+40));
		    CustomEnchants.WITHER.enchant(item, 1);
		    return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(2)){
		    ItemStack item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		    item.setDurability((short) (Randomiser.random.nextInt(100)+50));
		    CustomEnchants.LAVA_EATER.enchant(item, 1);
		    return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(1)){
		    ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
			item.setDurability((short) (Randomiser.random.nextInt(100)+50));
		    CustomEnchants.LAVA_EATER.enchant(item, 1);
		    return w.dropItem(centerLoc(), item);
		}else{
			if(r.getNumber()%2==0) return w.dropItem(centerLoc(), new ItemStack(Material.GLOWSTONE));
			else return w.dropItem(centerLoc(), new ItemStack(Material.NETHERRACK));
		}
	}

	public Entity endLoot(){
		Randomiser r = new Randomiser();

		if(r.isInNext(15)){
			return w.dropItem(centerLoc(), new ItemStack(Material.CHORUS_FRUIT));
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
