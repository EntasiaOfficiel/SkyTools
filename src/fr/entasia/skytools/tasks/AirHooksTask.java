package fr.entasia.skytools.tasks;

import fr.entasia.apis.other.Randomiser;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.Color;
import fr.entasia.skytools.objs.custom.CustomArrows;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
	public Location corner;
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

		counter = Main.r.nextInt(15*10)+4*10; // 10 pas 20 car periode de 2 ticks

		corner = armorStand.getLocation().subtract(baserVector);

		points = new Vector[Main.r.nextInt(2)+4];
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
					w.spawnParticle(background, corner.clone().add(randomVector()), 0, backgroundColor.r, backgroundColor.g, backgroundColor.b, 1);
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
					if (counter == 8) stop();
					a = baserVector;
				}
				w.spawnParticle(fish, corner.clone().add(a), 10, 0, 0, 0, 0);
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
		hook.remove();
		armorStand.remove();
		Utils.airHookTasks.remove(this);
		cancel();
	}


	public Location centerLoc(){
		return corner.clone().add(baserVector);
	}

	// Merci Narcos/Samourai_Mouton pour les loots !

	private int rand(int max){
		return Main.r.nextInt(max)+1;
	}

	public Entity owLoot(){
		Randomiser r = new Randomiser(100);
		if(r.isInNext(15)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.RAW_FISH, rand(4) + 2));
		}else if(r.isInNext(10)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.RAW_FISH, rand(2) + 2, (short)1));
		}else if(r.isInNext(10)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.RAW_FISH, rand(2), (short)2));

		}else if(r.isInNext(7)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.ARROW, rand(8) + 12));
		}else if(r.isInNext(5)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.FEATHER, rand(2)));
		}else if(r.isInNext(5)) {
			ItemStack item = new ItemStack(Material.ELYTRA);
			item.setDurability((short) (Main.r.nextInt(300) + 75));
			return w.dropItem(centerLoc(), item);
		}

		if(r.isInNext(10)) {
			return w.spawnEntity(centerLoc(), EntityType.PARROT);
		}else if(r.isInNext(7)) {
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
			ItemStack item = new ItemStack(Material.ARROW, rand(8)+8);
			CustomArrows.EXPLOSION.enchant(item);
			return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(10)) {
			ItemStack item = new ItemStack(Material.ARROW, rand(8)+8);
			CustomArrows.FIREWORKS.enchant(item);
			return w.dropItem(centerLoc(), item);
		}else{
			ItemStack item = new ItemStack(Material.FIREWORK, rand(5)+5);
			FireworkMeta meta = (FireworkMeta) item.getItemMeta();
			meta.addEffect(FireworkEffect.builder().withColor(org.bukkit.Color.RED, org.bukkit.Color.PURPLE)
					.build());
			meta.setPower(2);
			item.setItemMeta(meta);
			return w.dropItem(centerLoc(), item);
		}
	}

	public Entity netherLoot(){
		Randomiser r = new Randomiser(100);

		if(r.isInNext(10)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.GLOWSTONE_DUST, rand(16)+16));
		}else if(r.isInNext(10)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.GLOWSTONE, rand(8)+8));
		}else if(r.isInNext(10)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.SOUL_SAND, rand(6)+4));
		}else if(r.isInNext(5)){
			return w.dropItem(centerLoc(), new ItemStack(Material.BLAZE_ROD, rand(4)+4));
		}else if(r.isInNext(5)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.FIREBALL));
		}else if(r.isInNext(5)) {
			ItemStack item = new ItemStack(Material.ELYTRA);
			item.setDurability((short) (rand(300)+75));
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
			ItemStack item = new ItemStack(Material.ARROW, rand(8)+8);
			CustomArrows.FIRE.enchant(item);
			return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(5)){
			ItemStack item = new ItemStack(Material.STONE_SWORD);
			item.setDurability((short) (rand(60)+40));
			CustomEnchants.WITHER.enchant(item, 1);
			return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(2)){
			ItemStack item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
			item.setDurability((short) (rand(100)+50));
			CustomEnchants.LAVA_EATER.enchant(item, 1);
			return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(2)){
			ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
			item.setDurability((short) (rand(100)+50));
			CustomEnchants.LAVA_EATER.enchant(item, 1);
			return w.dropItem(centerLoc(), item);
		}else{
			return w.dropItem(centerLoc(), new ItemStack(Material.NETHERRACK));
		}
	}

	public Entity endLoot(){
		Randomiser r = new Randomiser(100);


		if(r.isInNext(15)){
			return w.dropItem(centerLoc(), new ItemStack(Material.CHORUS_FRUIT, rand(6)+4));
		}else if(r.isInNext(15)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.CHORUS_FRUIT_POPPED, rand(8) + 8));
		}else if(r.isInNext(14)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.ENDER_PEARL, rand(4)+4));
		}else if(r.isInNext(5)) {
			ItemStack item = new ItemStack(Material.ELYTRA);
			item.setDurability((short) (rand(300)+75));
			return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(5)) {
			return w.dropItem(centerLoc(), new ItemStack(Material.DRAGONS_BREATH, rand(4)+4));
		}

		if(r.isInNext(5)) {
			LingeringPotion lp = (LingeringPotion)w.spawnEntity(centerLoc(), EntityType.LINGERING_POTION);

			ItemStack item = new ItemStack(Material.LINGERING_POTION);
			PotionMeta meta = (PotionMeta) item.getItemMeta();
			meta.addCustomEffect(new PotionEffect(PotionEffectType.HARM,1, 1), true);
			item.setItemMeta(meta);

			lp.setItem(item);

			return lp;
		}else if(r.isInNext(8)){
			return w.spawnEntity(centerLoc(), EntityType.ENDERMITE);
		}else if(r.isInNext(6)){
			return w.spawnEntity(centerLoc(), EntityType.ENDERMAN);
		}else if(r.isInNext(6)){
			return w.spawnEntity(centerLoc(), EntityType.SHULKER_BULLET);
		}else if(r.isInNext(1)) {
			return w.spawnEntity(centerLoc(), EntityType.ENDER_DRAGON); // TODO CHECK S'IL EST DEJA LA
		}

		if(r.isInNext(10)){
			ItemStack item = new ItemStack(Material.ARROW, rand(8)+8);
			CustomArrows.DRAGON.enchant(item);
			return w.dropItem(centerLoc(), item);
		}else if(r.isInNext(5)){
			ItemStack item = new ItemStack(Material.IRON_SWORD);
			CustomEnchants.VAMPIRE.enchant(item, rand(2));
			return w.dropItem(centerLoc(), item);
		}else{
			return w.dropItem(centerLoc(), new ItemStack(Material.ENDER_STONE, rand(4)+4));
		}
	}
}
