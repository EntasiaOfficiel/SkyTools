package fr.entasia.skytools.events;

import fr.entasia.skycore.apis.OthersAPI;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

public class LockEvents implements Listener {

	private static boolean isChest(Material m){
		return m==Material.CHEST||m==Material.TRAPPED_CHEST;
	}

	@EventHandler(ignoreCancelled = true)
	public void a(InventoryMoveItemEvent e){
		InventoryHolder source = e.getSource().getHolder();
		if(source instanceof DoubleChest){
			String s = isLockChest(((DoubleChest) source).getLocation().getBlock(), true);
			if(s!=null)e.setCancelled(true);
		}else if(source instanceof Block){
			if(isChest(((Block) source).getType())) {
				String s = isLockChest((Block) source, false);
				if (s != null) e.setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void a(PlayerInteractEvent e){
		if(!e.hasBlock())return;
		Player p = e.getPlayer();
		if(OthersAPI.isMasterEdit(p))return;

		String s = null;
		if(isChest(e.getClickedBlock().getType())) {
			s = isLockChest(e.getClickedBlock(), true);
		}
		if (s==null) {
			if(Tag.SIGNS.isTagged(e.getClickedBlock().getType())){
				s = isLockSign(e.getClickedBlock());
			}
			if (s == null) return;
		}
		if (!p.getName().equals(s)) {
			e.setCancelled(true);
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
				p.sendMessage("§cTu ne peux pas casser ce coffre !");
			} else {
				p.sendMessage("§cTu ne peux pas ouvrir ce coffre !");
			}
		}
	}

	private static final BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH};

	private static String isLockChest(Block base, boolean checkDouble){ // assuming base is sign
		Block b;
		String s;
		for(BlockFace bf : faces){
			b = base.getRelative(bf);
			if(Tag.WALL_SIGNS.isTagged(b.getType())){
				s = isLockSign(b);
				if(s!=null)return s;
			}else if(b.getType()==base.getType()&&checkDouble){
				s = isLockChest(b, false);
				if(s!=null)return s;
			}
		}
		b = base.getRelative(BlockFace.UP);
		if(Tag.STANDING_SIGNS.isTagged(b.getType())){
			return isLockSign(b);
		}else return null;
	}

	private static String isLockSign(Block base){ // assuming base is sign
		Sign s = (Sign)base.getState();
		if(s.getLine(0).equals("§9[§3Protection§9]")){
			return s.getLine(1);
		}else return null;
	}
}
