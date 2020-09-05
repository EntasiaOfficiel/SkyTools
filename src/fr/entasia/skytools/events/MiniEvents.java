package fr.entasia.skytools.events;

import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;

public class MiniEvents implements Listener {

	@EventHandler
	public void furnaceBurn(FurnaceBurnEvent e) {
		Furnace furnace = (Furnace) e.getBlock().getState();
		furnace.setCookTime((short) 120);
	}

	@EventHandler
	public void onFade(BlockFadeEvent e){
		if(e.getBlock().getType()== Material.FARMLAND)e.setCancelled(true);
	}

}
