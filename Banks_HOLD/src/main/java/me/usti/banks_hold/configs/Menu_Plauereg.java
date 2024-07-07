package me.usti.banks_hold.configs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class Menu_Plauereg implements InventoryHolder {
    private  Inventory inventory;
    private Player player;

    public Menu_Plauereg()  {
        inventory = Bukkit.createInventory(this, 54, "Игроки");
        item();
    }
    private void item() {
        int i = 0;
        for (int s = 0; s < 54; s++){
            inventory.setItem(s, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        for (Player player: Bukkit.getOnlinePlayers()) {
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setDisplayName( player.getName());
            meta.setOwner(player.getName());
            itemStack.setItemMeta(meta);
            if (DataUtils.hasBank(player.getName(), "Sbeebank")){
                if (DataUtils.hasBank(player.getName(), "beenkoof")){
                    if (DataUtils.hasBank(player.getName(), "beebusiness")){
                    }else {
                        inventory.setItem(i, itemStack);
                        i = i + 1;
                    }
                }else {
                    inventory.setItem(i, itemStack);
                    i = i + 1;
                }
            }else {
                inventory.setItem(i, itemStack);
                i = i + 1;
            }


        }
    }
    @Override
    public @NotNull Inventory getInventory(Player player) {
        return inventory;
    }
}
