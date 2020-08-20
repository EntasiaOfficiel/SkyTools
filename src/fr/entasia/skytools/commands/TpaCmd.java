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

public class TpaCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] arg) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (arg.length > 0) {
				Player target = Bukkit.getServer().getPlayer(arg[0]);
				if (target == null) {
					p.sendMessage("§cMerci de mettre un joueur en ligne !");
				} else if (target == p) {
					p.sendMessage("§cTu ne peux pas te téléporter à toi même !");
				} else {
					ToolPlayer toolp = Utils.getToolPlayer(target);
					Iterator<TPRequest> ite = toolp.tpRequests.iterator();
					TPRequest tpa;
					while(ite.hasNext()){
						tpa = ite.next();
						if(tpa.isValid()){
							if(tpa.source == p){
								p.sendMessage("§cTu as déjà une demande de téléportation en cours !");
								return true;
							}
						}else{
							ite.remove();
						}
					}
					TPRequest tp = new TPRequest();
					p.sendMessage("§aTa demande de tp a bien été envoyé !");
					tp.source = p;
					tp.target = target;
					tp.when = System.currentTimeMillis();
					ToolPlayer tool = Utils.getToolPlayer(target);
					tool.tpRequests.add(tp);
					tp.sendMsg();
				}
			}
		}
		return true;
	}
}
