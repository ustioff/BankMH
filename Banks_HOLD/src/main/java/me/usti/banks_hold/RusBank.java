// 
// Decompiled by Procyon v0.5.36
// 

package me.usti.banks_hold;

import java.security.Security;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.text.DecimalFormat;
import me.usti.banks_hold.utils.ChatUtils;
import me.usti.banks_hold.configs.DataUtils;
import org.bukkit.entity.Player;
import me.usti.banks_hold.utils.Expansions;
import me.usti.banks_hold.commands.BankCommand;

import org.bukkit.plugin.Plugin;
import me.usti.banks_hold.configs.Banks;
import me.usti.banks_hold.configs.Data;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RusBank extends JavaPlugin {
    public Team team;
    private final int delay;


    public RusBank() {
        this.delay = this.getConfig().getInt("percent-time") * 20 * 60;
    }

    private void msg(final String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static RusBank inst;
    private Database database;

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Event(), this);
        inst = this;
        this.team = new Team();
        this.saveDefaultConfig();
        final Data data = new Data();
        final Banks banks = new Banks();
        data.loadYamlFile((Plugin) this);
        banks.loadYamlFile((Plugin) this);
        new BankCommand(this);
        final String prefix = "&6&lRUSBANK &7| &f";
        this.msg("");
        this.msg(prefix + "&a\u041f\u043b\u0430\u0433\u0438\u043d \u0437\u0430\u043f\u0443\u0449\u0435\u043d. &f\u0412\u0435\u0440\u0441\u0438\u044f: &ev" + this.getDescription().getVersion());
        this.msg(prefix + "&f\u0420\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a: &ahttps://vk.com/emptycsgo");
        this.msg("");
        try {
            this.database = new Database();
            database.Tablecreate();
        } catch (SQLException e) {
            System.out.println("stop");
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Expansions().register();
        }
    }

    public void onDisable() {
    }

    public Database getDatabase() {
        return database;
    }
}

