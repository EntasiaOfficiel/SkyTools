package fr.entasia.skytools.objs;

import fr.entasia.apis.other.ChatComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;

public class TPRequest {

	public long when;
	public Player source;
	public Player target;


	public boolean isValid(){
		if(System.currentTimeMillis() - when > 30000){
			return false;
		}else return true;
	}

	public void accept(){
		if(isValid()){
			source.teleport(target);
			source.sendMessage("§aTu as été téléporté à " + target.getName());
			target.sendMessage("§a" + source.getName() + " a été téléporté à toi !");
		}else{
			source.sendMessage("§cLa demande a expiré !");
		}
	}

	public void sendMsg(){
		target.sendMessage("§cTu as reçu une demande de téléportation de " + source.getName() + " !");
		ChatComponent accept = new ChatComponent("§2[§aAccepter§2]");
		accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + source.getName()));
		accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponent.create("§aClique pour accepter la demande !")));
		ChatComponent deny = new ChatComponent("§4[§cRefuser§4]");
		deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny " + source.getName()));
		deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponent.create("§cClique pour refuser la demande !")));
		target.sendMessage(accept.append(" ").append(deny).create());
	}
}