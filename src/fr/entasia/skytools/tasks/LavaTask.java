package fr.entasia.skytools.tasks;

import fr.entasia.skycore.others.enums.Dimensions;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class LavaTask extends BukkitRunnable { // 10 ticks actuellement

	public static class LavaItem{
		public ItemStack item;
		public int i;

		public LavaItem(ItemStack item, int i){
			this.item = item;
			this.i = i;

		}
	}

	public static boolean isLava(Material m){
		return m==Material.LAVA||m==Material.STATIONARY_LAVA;

	}

	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()){
			if(Dimensions.isIslandWorld(p.getWorld())){
				Material m = p.getLocation().getBlock().getType();
				if(isLava(m)){
					ArrayList<LavaItem> list = new ArrayList<>();
					int l;
					for(ItemStack item : p.getInventory().getContents()){
						if(item==null||item.getType()==Material.AIR||item.getDurability()==0||item.getType().getMaxDurability()==0)continue;
						l = CustomEnchants.LAVA_EATER.getLevel(item);
						if(l!=0)list.add(new LavaItem(item, l));

					}
					if(list.size()!=0){
						LavaItem litem = list.get(Main.r.nextInt(list.size()));
						short n = (short) (litem.item.getDurability()-litem.i);
						if(n<0)n=0;
						litem.item.setDurability(n);
					}
				}
			}
		}
	}
}
