package fr.entasia.skytools.objs.villagers;

import fr.entasia.apis.other.ItemBuilder;
import fr.entasia.skytools.Main;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class TradeItem extends ItemBuilder {

	public boolean rdAmount = true;
	public boolean rdDamage = false;

	public TradeItem(Material m) {
		super(m);
	}

	public TradeItem(Material m, int amount) {
		super(m, amount);
	}

	public TradeItem(Material m, int amount, boolean rdAmount) {
		super(m, amount);
		this.rdAmount = rdAmount;
	}

	public TradeItem(Material m, int amount, boolean rdAmount, ItemMeta meta) {
		super(m, amount, meta);
		this.rdAmount = rdAmount;
	}

	public TradeItem rdDamage(){
		rdAmount = true;
		return this;
	}

	public TradeItem effect(PotionType type){
		PotionMeta pmeta = (PotionMeta) meta;
		pmeta.setBasePotionData(new PotionData(type));
		return this;
	}

	@Override
	public TradeItem damage(int damage) {
		return (TradeItem) super.damage(damage);
	}

	@Override
	public TradeItem name(String name) {
		return (TradeItem) super.name(name);
	}

	@Override
	public TradeItem lore(String lore) {
		return (TradeItem) super.lore(lore);
	}

	@Override
	public TradeItem enchant(Enchantment ench) {
		return (TradeItem) super.enchant(ench);
	}

	@Override
	public TradeItem enchant(Enchantment ench, int lvl) {
		return (TradeItem) super.enchant(ench, lvl);
	}

	@Override
	public ItemStack build(){
		ItemStack item = super.build();
		if(rdAmount){
			if(item.getAmount()>8){
				item.setAmount(Trade.applyRandom(item.getAmount()));
			}
		}
		if(rdDamage)item.setDurability((short) Main.r.nextInt(16));
		return item;
	}

}
