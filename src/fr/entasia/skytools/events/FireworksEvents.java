package fr.entasia.skytools.events;

import fr.entasia.apis.nbt.EntityNBT;
import fr.entasia.apis.nbt.ItemNBT;
import fr.entasia.apis.nbt.NBTComponent;
import fr.entasia.apis.other.InstantFirework;
import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.ToolPlayer;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;


public class FireworksEvents implements Listener {


	@EventHandler
	public void a(PrepareItemCraftEvent e) {
		Recipe r = e.getRecipe();
		if(r==null)return;
		ItemStack item = r.getResult();
		if (item.getType() == Material.SNOW_BALL) {
			char type = '0';
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

				item = ItemNBT.setNBT(item, nbt);
				e.getInventory().setResult(item);
			}
		}
	}

	@EventHandler
	public void a(ProjectileLaunchEvent e) {
		Projectile pr = e.getEntity();
		if (pr.getType() == EntityType.SNOWBALL&&pr.getShooter() instanceof Player) {
			ItemStack item = ((Player) pr.getShooter()).getInventory().getItemInMainHand();
			if(item.getType()==Material.SNOW_BALL){
				ToolPlayer tp = Utils.getPlayer((Player)pr.getShooter());
				if(System.currentTimeMillis()-tp.cdFirework>500){
					tp.cdFirework = System.currentTimeMillis();
					Utils.fireworks.put(pr, ItemNBT.getNBT(item).getComponent("entasia"));
				}else{
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void a(ProjectileHitEvent e) {
		Projectile pr = e.getEntity();
		if (pr.getType() == EntityType.SNOWBALL&&pr.getShooter() instanceof Player) {
			NBTComponent inject = Utils.fireworks.remove(pr);
			if(inject!=null){
				Firework fw = (Firework) pr.getWorld().spawnEntity(pr.getLocation(), EntityType.FIREWORK);
				NBTComponent nbt = EntityNBT.getNBT(fw);
				nbt.setComponent("FireworksItem", inject);
				EntityNBT.setNBT(fw, nbt);
				if("1".equals(inject.getKeyString("type"))){
					InstantFirework.explode(fw);
				}
			}
		}
	}
}