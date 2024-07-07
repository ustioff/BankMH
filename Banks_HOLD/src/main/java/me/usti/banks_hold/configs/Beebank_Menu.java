package me.usti.banks_hold.configs;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.usti.banks_hold.RusBank;
import me.usti.banks_hold.commands.BankCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import me.clip.placeholderapi.PlaceholderAPI;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Beebank_Menu implements InventoryHolder {
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

    public Beebank_Menu(){
        inventory = Bukkit.createInventory(this, 27, "Банк");
        item();
    }
    private void item(){
        for (int i = 0; i < 27; i++){
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        BankCommand command = new BankCommand(RusBank.inst);
        ItemStack player = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) player.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Личный кабинет");
        List list = new ArrayList<>();
        list.add(ChatColor.WHITE + "" + ChatColor.RESET + ChatColor.WHITE +   "Имя: " + ChatColor.GREEN + command.getPlayerMap().get("player").getName());
        list.add(ChatColor.WHITE + "" + ChatColor.RESET + ChatColor.WHITE + "Наиграно: " + ChatColor.GREEN + PlaceholderAPI.setPlaceholders(command.getPlayerMap().get("player"), "%statistic_time_played:days%д %statistic_time_played:hours%ч %statistic_time_played:minutes%м"));
        meta.setOwner(command.getPlayerMap().get("player").getName());
        meta.setLore(list);
        player.setItemMeta(meta);
        inventory.setItem(4, player);
        ItemStack exit = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mexit = (SkullMeta) exit.getItemMeta();
        mexit.setDisplayName(ChatColor.RED + "" + ChatColor.RESET + ChatColor.RED +   "Закрыть");
        mexit.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/548d7d1e03e1af145b0125ab841285672b421265da2ab915015f9058438ba2d8"));
        mexit.setLore(null);
        exit.setItemMeta(mexit);
        inventory.setItem( 8,exit);
        ItemStack pay = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mpay = (SkullMeta) pay.getItemMeta();
        mpay.setDisplayName(ChatColor.YELLOW + "" + ChatColor.RESET + "Баланс");
        mpay.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/95fd67d56ffc53fb360a17879d9b5338d7332d8f129491a5e17e8d6e8aea6c3a"));
        mpay.setLore(null);
        pay.setItemMeta(mpay);
        inventory.setItem(21,pay);
        ItemStack shtr = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mshtr = (SkullMeta) shtr.getItemMeta();
        mshtr.setDisplayName(ChatColor.YELLOW + "" + ChatColor.RESET + "Баланс");
        mshtr.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/95fd67d56ffc53fb360a17879d9b5338d7332d8f129491a5e17e8d6e8aea6c3a"));
        mshtr.setLore(null);
        shtr.setItemMeta(mshtr);
        ItemStack take = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mtake = (SkullMeta) take.getItemMeta();
        mtake.setDisplayName(ChatColor.YELLOW + "" + ChatColor.RESET + "Переводы");
        mtake.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/c4c01642bf488b1c747f8f09139cb6d069cc85fb0becabe0429c3b7b8cb8fbc9"));
        mtake.setLore(null);
        take.setItemMeta(mtake);
        inventory.setItem(23,take);
    }
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
