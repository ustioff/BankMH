package me.usti.banks_hold.configs;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Menu_Plauer implements InventoryHolder {
    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4"); // We reuse the same "random" UUID all the time
    private static PlayerProfile getProfile(String url) {
        PlayerProfile profile = (PlayerProfile) Bukkit.createPlayerProfile(RANDOM_UUID); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url); // The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        return profile;
    }
    private  Inventory inventory;

    public Menu_Plauer()  {
        inventory = Bukkit.createInventory(this, 54, "Игроки");
        item();
    }
    private void item()  {
        int i = 0;
        for (int s = 0; s < 52; s++){
            inventory.setItem(s, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        for (OfflinePlayer player: Bukkit.getOfflinePlayers()) {

            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setDisplayName(player.getName());
            List list = new ArrayList<>();
            meta.getPersistentDataContainer().set(NamespacedKey.fromString("num"), PersistentDataType.INTEGER, i + 1);
            list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Был в сети:");
            list.add(ChatColor.RESET + "" + ChatColor.YELLOW + PlaceholderAPI.setPlaceholders(player, "%player_last_played_formatted%"));
            meta.setLore(list);

            meta.setOwner(player.getName());
            itemStack.setItemMeta(meta);
            if (i <= 52){
                if (DataUtils.hasBank(player.getName(), "beenkoof")) {
                    inventory.setItem(i, itemStack);
                    i = i + 1;
                } else if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                    inventory.setItem(i, itemStack);
                    i = i + 1;
                } else if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                    inventory.setItem(i, itemStack);
                    i = i + 1;
                }
            }
        }
        ItemStack down = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta downItemMeta = (SkullMeta) down.getItemMeta();
        downItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/b72d10e410df8d515abf35b76666f11b74639500cf0eeb6e70d45f38bd4bba3a"));
        downItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Предыдущая страница");
        down.setItemMeta(downItemMeta);
        inventory.setItem(52, down);
        ItemStack up = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta upItemMeta = (SkullMeta) up.getItemMeta();
        upItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Следущая страница");
        upItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/8003f2c9f01d2ed58b900314d72fb25156ca9bf13b7fae3093104ce8fe964e9f"));
        up.setItemMeta(upItemMeta);
        inventory.setItem(53, up);
    }
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
