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

public class TpacceptCmd implements CommandExecutor {
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
				Iterator<TPRequest> ite = tool.tpRequests.iterator();
				TPRequest tp;
				while(ite.hasNext()){
					tp = ite.next();
					if (asker == tp.source){
						tp.accept();
						ite.remove();
						return true;
					}
				}
				p.sendMessage("§cTu n'as pas de demande de téléportation de §4"+asker.getName()+"§c !");
			}else{
				ToolPlayer tool = Utils.getToolPlayer(p);
				Iterator<TPRequest> ite = tool.tpRequests.iterator();
				TPRequest tp;

				if(tool.tpRequests.size()==0){
					p.sendMessage("§cTu n'as reçu aucune demande de téléportation !");
				}else if(tool.tpRequests.size()==1){
					tp = tool.tpRequests.get(0);
					tp.accept();
					tool.tpRequests.clear();
				}else{
					while(ite.hasNext()) {
						tp = ite.next();
						if (tp.isValid()) {
							tp.sendMsg();
						} else {
							ite.remove();
						}
					}
				}
			}
		}
		return true;
	}
}
