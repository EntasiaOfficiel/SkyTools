package fr.entasia.skytools.events;

import fr.entasia.skytools.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class ChatEvents implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		if(Main.guessWord!=null&&e.getMessage().equals(Main.guessWord)) {
			e.setCancelled(true);
			Bukkit.broadcastMessage("§eSpeedWriter §7» §e" + e.getPlayer().getDisplayName() + "§6 à gagné !");
			Main.guessWord = null;
			switch (Main.r.nextInt(6)) {
				case 0:
					e.getPlayer().getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, 32));
					break;
				case 1:
					e.getPlayer().getInventory().addItem(new ItemStack(Material.DIAMOND));
					break;
				case 2:
					e.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_INGOT, 8));
					break;
				case 3:
					e.getPlayer().getInventory().addItem(new ItemStack(Material.POISONOUS_POTATO, 4));
					break;
				case 4:
					e.getPlayer().getInventory().addItem(new ItemStack(Material.CAKE, 1));
					break;
				case 5:
					e.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
					break;
				default:
					e.getPlayer().sendMessage("§cHeu... Oops , je n'arrive pas à te donner la récompense, tu peux contacter un membre du staff ? ^^");

			}
		}
	}
}
