package fr.entasia.skytools.commands;

import fr.entasia.skytools.invs.BaseMenus;
import fr.entasia.skytools.objs.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player))return false;
		if(args.length==0) BaseMenus.WarpsOpen((Player)sender);
		else{
			Warp w = Warp.getWarp(args[0]);
			if(w==null)sender.sendMessage("§cCe warp n'existe pas !");
			else w.teleport((Player)sender, true);
		}
		return true;
	}
}
