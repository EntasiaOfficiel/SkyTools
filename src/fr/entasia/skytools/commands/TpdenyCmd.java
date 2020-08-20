package fr.entasia.skytools.commands;

import fr.entasia.skytools.Utils;
import fr.entasia.skytools.objs.TPRequest;
import fr.entasia.skytools.objs.ToolPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class TpdenyCmd implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] arg) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (arg.length == 1) {

				Player asker = Bukkit.getPlayer(arg[0]);
				if(asker==null){
					p.sendMessage("§cCe joueur n'est pas connecté !");
					return true;
				}

				ToolPlayer tool = Utils.getToolPlayer(p);
				Iterator<TPRequest> it = tool.tpRequests.iterator();
				TPRequest tpa;
				while(it.hasNext()) {
					tpa = it.next();
					if (asker == tpa.source) {
						if (tpa.isValid()) {
							tpa.target.sendMessage("§cTu as refusé la demande de téléportation");
							tpa.source.sendMessage("§cTa demande de téléportation a été refusé");
						} else {
							tpa.target.sendMessage("§cLa demande de tp a expiré !");
						}
						it.remove();
						return true;
					}
				}
				p.sendMessage("§cTu n'as pas de demande de téléportation de §4"+asker.getName()+"§c !");

			}
		}
		return true;
	}
}
