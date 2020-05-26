package fr.entasia.skytools.events;

import fr.entasia.apis.nbt.EntityNBT;
import fr.entasia.apis.nbt.ItemNBT;
import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.apis.other.InstantFirework;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;


public class FireworksEvents implements Listener {


	public static HashMap<Entity, NBTComponent> fireworks = new HashMap<>();
	public static HashMap<ProjectileSource, Long> cooldown = new HashMap<>();

	@EventHandler
	public void a(CraftItemEvent e) {
		ItemStack item = e.getRecipe().getResult();
		if (item.getType() == Material.SNOW_BALL) {
			char type='0';
			NBTComponent nbt = null;
			for (ItemStack i : e.getInventory().getContents()) {
				if (i.getType() == Material.FIREWORK_CHARGE) {
					nbt = new NBTComponent("{Fireworks:{Explosions:[" + ItemNBT.getNBT(i).getComponent("Explosion") + "]}}");
					type = '1';
					break;
				} else if (i.getType() == Material.FIREWORK) {
					nbt = ItemNBT.getNBT(i);
					type = '0';
					break;
				}
			}
			if (nbt != null) {
				NBTComponent injected = new NBTComponent();
				injected.setKeyString("type", Character.toString(type));
				injected.setKeyString("id", "minecraft:fireworks");
				injected.fusion(new NBTComponent("{Count:1b,Damage:0s}"));
				injected.setComponent("tag", nbt);

				nbt = ItemNBT.getNBT(item);
				nbt.setComponent("entasia", injected);

				e.setCurrentItem(ItemNBT.setNBT(item, nbt));
			}
		}
	}

	@EventHandler
	public void a(ProjectileLaunchEvent e) {
		Projectile pr = e.getEntity();
		if (pr.getType() == EntityType.SNOWBALL&&pr.getShooter() instanceof Player) {
			ItemStack item = ((Player) pr.getShooter()).getInventory().getItemInMainHand();
			if(item.getType()==Material.SNOW_BALL){
				if(System.currentTimeMillis()-cooldown.getOrDefault(pr.getShooter(), 2L)>500){
					cooldown.put(pr.getShooter(), System.currentTimeMillis());
					fireworks.put(pr, ItemNBT.getNBT(item).getComponent("entasia"));
				}else{
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void b(ProjectileHitEvent e) {
		Projectile pr = e.getEntity();
		if (pr.getType() == EntityType.SNOWBALL&&pr.getShooter() instanceof Player) {
			NBTComponent inject = fireworks.remove(pr);
			if(inject!=null){
				Firework fw = (Firework) pr.getWorld().spawnEntity(pr.getLocation(), EntityType.FIREWORK);
				NBTComponent nbt = EntityNBT.getNBT(fw);
				nbt.setComponent("FireworksItem", inject);
				EntityNBT.setNBT(fw, nbt);
				if(inject.getKeyString("type").equals("1")){
					InstantFirework.explode(fw);
				}
			}
		}
	}
}