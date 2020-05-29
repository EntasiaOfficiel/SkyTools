package fr.entasia.skytools.commands;

import fr.entasia.skytools.Main;
import fr.entasia.skytools.objs.Warp;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class SetWarpCmd implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player))return false;
		if(sender.hasPermission("entasia.setwarp")){
			if (args.length==0) sender.sendMessage("§cMet un nom de warp !");
			else {
				Player p = (Player) sender;
				String wname = args[0].toLowerCase();
				Warp w = Warp.getWarp(wname);
				boolean moved = true;
				if (w == null) {
					moved = false;
					w = new Warp();
					w.name = wname;
					w.desc = Collections.singletonList("§6Aucune description actuellement");
					Warp.warps.put(wname, w);
					Main.main.getConfig().set("warps." + wname + ".desc", w.desc);
				} else w = Warp.warps.get(wname);

				w.loc = new Location(p.getWorld(), p.getLocation().getBlockX() + 0.5,
						p.getLocation().getBlockY() + 0.1, p.getLocation().getBlockZ() + 0.5,
						(int) p.getLocation().getYaw(), (int) p.getLocation().getPitch());

				Main.main.getConfig().set("warps." + wname + ".world", w.loc.getWorld().getName());
				Main.main.getConfig().set("warps." + wname + ".x", w.loc.getBlockX());
				Main.main.getConfig().set("warps." + wname + ".y", w.loc.getBlockY());
				Main.main.getConfig().set("warps." + wname + ".z", w.loc.getBlockZ());
				Main.main.getConfig().set("warps." + wname + ".yaw", (int) w.loc.getYaw());
				Main.main.getConfig().set("warps." + wname + ".pitch", (int) w.loc.getPitch());
				Main.main.saveConfig();

				if (moved) p.sendMessage("§aWarp " + wname + " déplacé avec succès !");
				else p.sendMessage("§aWarp " + wname + " créé !");
			}
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
		return true;
	}
}
