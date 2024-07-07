package me.usti.banks_hold.configs;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.clip.placeholderapi.PlaceholderAPI;
import me.usti.banks_hold.Database;
import me.usti.banks_hold.RusBank;
import me.usti.banks_hold.commands.BankCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class History_Menu implements InventoryHolder {
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

    private Inventory inventory;

    public History_Menu() {
        inventory = Bukkit.createInventory(this, 27, "Ваши переводы");
        item();
    }

    private void item() {
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        BankCommand command = new BankCommand(RusBank.inst);
        ItemStack add = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta addItemMeta = (SkullMeta) add.getItemMeta();
        addItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/69ea1d86247f4af351ed1866bca6a3040a06c68177c78e42316a1098e60fb7d3"));
        addItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Вам перевели");
        add.setItemMeta(addItemMeta);
        inventory.setItem(5, add);
        ItemStack down = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta downItemMeta = (SkullMeta) down.getItemMeta();
        downItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/b72d10e410df8d515abf35b76666f11b74639500cf0eeb6e70d45f38bd4bba3a"));
        downItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Предыдущая страница");
        down.setItemMeta(downItemMeta);
        inventory.setItem(0, down);
        ItemStack up = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta upItemMeta = (SkullMeta) up.getItemMeta();
        upItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Следущая страница");
        upItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/8003f2c9f01d2ed58b900314d72fb25156ca9bf13b7fae3093104ce8fe964e9f"));
        up.setItemMeta(upItemMeta);
        inventory.setItem(1, up);

        ItemStack player = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) player.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Перевести");
        meta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/8ae7bf4522b03dfcc866513363eaa9046fddfd4aa6f1f0889f03c1e6216e0ea0"));
        player.setItemMeta(meta);
        inventory.setItem(3, player);
        ItemStack exit = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mexit = (SkullMeta) player.getItemMeta();
        mexit.setDisplayName(ChatColor.RED + "" + ChatColor.RESET + ChatColor.RED + "Закрыть");
        mexit.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/548d7d1e03e1af145b0125ab841285672b421265da2ab915015f9058438ba2d8"));
        exit.setItemMeta(mexit);
        inventory.setItem(8, exit);
        Database database = RusBank.inst.getDatabase();
        Statement countstatement = null;
        int count = 0;
        try {
            countstatement = database.getConnection().createStatement();
            String sql = "SELECT * FROM pay WHERE Users = '" + command.getPlayerMap().get("player").getName() + "' AND action = 'take'";
            ResultSet resultSet = countstatement.executeQuery(sql);
            count = database.resultSetCount(resultSet);
        } catch (SQLException e) {
            count = 0;
        }
        int i = count;
        for (int a = 9; a < 27; a++) {
            int sum = 0;
            String target = null;
            String bank = null;
            String messeg = null;
            String targetbank = null;
            int num = 0;
            try {
                Statement statement = database.getConnection().createStatement();
                String sql = "SELECT * FROM pay WHERE Users = '" + command.getPlayerMap().get("player").getName() + "' AND action = 'take' AND num = '" + i + "'";
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    sum = resultSet.getInt("sum");
                    target = resultSet.getString("target");
                    bank = resultSet.getString("bank");
                    messeg = resultSet.getString("messeg");
                    targetbank = resultSet.getString("targetbank");
                    num = resultSet.getInt("num");
                    statement.close();
                    ItemStack histor = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta hist = (SkullMeta) histor.getItemMeta();
                    List list = new ArrayList<>();
                    hist.setOwner(target);
                    hist.getPersistentDataContainer().set(NamespacedKey.fromString("num"), PersistentDataType.INTEGER, num);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сумма: " + ChatColor.YELLOW + sum);
                    hist.setDisplayName(target);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сообщение: " + ChatColor.YELLOW + messeg);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "В банк: " + ChatColor.YELLOW + targetbank);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Из банка: " + ChatColor.YELLOW + bank);
                    hist.setLore(list);
                    histor.setItemMeta(hist);
                    inventory.setItem(a, histor);

                }else {
                    inventory.setItem(a, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                    statement.close();
                }
                statement.close();
            } catch (SQLException e) {
            }

        i--;

        }

    }

    @Override
    public Inventory getInventory() {

        return inventory;
    }
}
