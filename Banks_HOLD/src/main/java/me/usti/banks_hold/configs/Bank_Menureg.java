package me.usti.banks_hold.configs;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.usti.banks_hold.RusBank;
import me.usti.banks_hold.commands.BankCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.UUID;

public class Bank_Menureg implements InventoryHolder{
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

    public Bank_Menureg()  {
    inventory = Bukkit.createInventory(this, 27, "Не зарегистрированные банки");
    item();
    }

    private void item()  {
        ItemStack tin = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metatin = (SkullMeta) tin.getItemMeta();
        PlayerProfile profile = getProfile("http://textures.minecraft.net/texture/8433b7964a05576ea1fd665e52edffd74311ae7f7aedaae6403bced329f4fdca");
        metatin.setOwnerProfile(profile);
        metatin.setDisplayName("Бинькофф");
        tin.setItemMeta(metatin);
        ItemStack sbe = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metasbe = (SkullMeta) sbe.getItemMeta();
        PlayerProfile profilesbe = getProfile("http://textures.minecraft.net/texture/5fb4b94f1a3f6f207632da797eddb11ddb3c0dffea8d67a6f2e420f1094cf198");
        metasbe.setOwnerProfile(profilesbe);
        metasbe.setDisplayName("Сби-банк");
        sbe.setItemMeta(metasbe);
        ItemStack besbe = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metabesbe = (SkullMeta) besbe.getItemMeta();
        PlayerProfile profilebe = getProfile("http://textures.minecraft.net/texture/4a7e0d55ea8051569955da138697174a0bd76190b2d58b997509737dce5fb61f");
        metabesbe.setOwnerProfile(profilebe);
        metabesbe.setDisplayName("Би-бизнес");
        besbe.setItemMeta(metabesbe);
        BankCommand command = new BankCommand(RusBank.inst);
        Player player = command.getReg().get("reg");
        for (int i = 0; i < 27; i++){
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        ItemStack exit = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mexit = (SkullMeta) exit.getItemMeta();
        mexit.setDisplayName(ChatColor.RED + "" + ChatColor.RESET + ChatColor.RED +   "Закрыть");
        mexit.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/548d7d1e03e1af145b0125ab841285672b421265da2ab915015f9058438ba2d8"));
        exit.setItemMeta(mexit);
        inventory.setItem( 8,exit);
        if (DataUtils.hasBank(player.getName(), "beenkoof")){
            if (DataUtils.hasBank(player.getName(), "Sbeebank")){
                if (DataUtils.hasBank(player.getName(), "beebusiness")){

                }else {
                 inventory.setItem(13, besbe);
                }
            }
            else if (DataUtils.hasBank(player.getName(), "beebusiness")){

                inventory.setItem(13, sbe);

            }
            else {
                inventory.setItem(12, sbe);
                inventory.setItem(14, besbe);
            }
        }else if (DataUtils.hasBank(player.getName(), "Sbeebank")){
            if (DataUtils.hasBank(player.getName(), "beebusiness")){
                inventory.setItem(13, tin);
            }
            else {
                inventory.setItem(12, tin);
                inventory.setItem(14, besbe);
            }
        }
        else if (DataUtils.hasBank(player.getName(), "beebusiness")){
            inventory.setItem(12, tin);
            inventory.setItem(14, sbe);
        }else {
            inventory.setItem(11, tin);
            inventory.setItem(13, sbe);
            inventory.setItem(15, besbe);
        }
    }
    @Override
    public Inventory getInventory() {

        return inventory;
    }
}
