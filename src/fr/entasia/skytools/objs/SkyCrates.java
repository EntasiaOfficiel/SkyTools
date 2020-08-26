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

	public static void registerCrates() throws Error {

		CrateType ct = new CrateType();
		ct.name = "Agriculture";
		ct.key = new ItemBuilder(Material.SEEDS).fakeEnchant().name("§eClé de crate").lore("§6Crate §aAgriculture").build();

		CrateLoot loot = new CrateLoot(10, "32 graines", new ItemStack(Material.SEEDS, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "32 graines de citrouille", new ItemStack(Material.PUMPKIN_SEEDS, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "32 graines de pastèque", new ItemStack(Material.MELON_SEEDS, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "32 carrotes", new ItemStack(Material.CARROT_ITEM, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "32 pommes de terre", new ItemStack(Material.POTATO_ITEM, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "32 nether wart", new ItemStack(Material.NETHER_STALK, 32));
		ct.addLoot(loot);

		loot = new CrateLoot(10, "une houe en diamant u3",
				new ItemBuilder(Material.DIAMOND_HOE).enchant(Enchantment.DURABILITY, 3).build());
		ct.addLoot(loot);

		ItemStack item = new ItemStack(Material.IRON_HOE);
		CustomEnchants.TONDEUSE.enchant(item, Main.r.nextInt(2)+1);
		loot = new CrateLoot(10, "une houe en fer §7Tondeuse", item);
		ct.addLoot(loot);

		CratesAPI.registerCrateType(ct);


	}

}
