package fr.entasia.skytools.objs;

import fr.entasia.apis.other.ItemBuilder;
import fr.entasia.crates.CratesAPI;
import fr.entasia.crates.utils.CrateLoot;
import fr.entasia.crates.utils.CrateType;
import fr.entasia.skytools.Main;
import fr.entasia.skytools.objs.custom.CustomEnchants;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class SkyCrates {

	public static void registerCrates() throws Throwable {

		CrateType ct = new CrateType();
		ct.name = "Agriculture";
		ct.key = new ItemBuilder(Material.SEEDS).fakeEnchant().name("§eClé de crate").lore("§Crate §aAgriculture").build();

		CrateLoot loot = new CrateLoot(10, new ItemStack(Material.SEEDS, 32));
		loot.name= "32 graines";
		ct.loots.add(loot);

		loot = new CrateLoot(10, new ItemStack(Material.PUMPKIN_SEEDS, 32));
		loot.name= "32 graines de citrouille";
		ct.loots.add(loot);

		loot = new CrateLoot(10, new ItemStack(Material.MELON_SEEDS, 32));
		loot.name= "32 graines de pastèque";
		ct.loots.add(loot);

		loot = new CrateLoot(10, new ItemStack(Material.CARROT_ITEM, 32));
		loot.name= "32 carrotes";
		ct.loots.add(loot);

		loot = new CrateLoot(10, new ItemStack(Material.POTATO_ITEM, 32));
		loot.name= "32 pommes de terre";
		ct.loots.add(loot);

		loot = new CrateLoot(10, new ItemStack(Material.NETHER_STALK, 32));
		loot.name= "32 nether wart";
		ct.loots.add(loot);

		loot = new CrateLoot(10, new ItemStack(Material.DIAMOND_HOE));
		loot.name= "une houe en diamant u3";
		ct.loots.add(loot);

		ItemStack item = new ItemStack(Material.IRON_HOE);
		CustomEnchants.TONDEUSE.enchant(item, Main.r.nextInt(2)+1);
		loot = new CrateLoot(10, new ItemBuilder(Material.IRON_HOE).enchant(Enchantment.DURABILITY, 3).build());
		loot.name= "une houe en fer §7Tondeuse";
		ct.loots.add(loot);

		CratesAPI.registerCrate(ct);


	}

}
